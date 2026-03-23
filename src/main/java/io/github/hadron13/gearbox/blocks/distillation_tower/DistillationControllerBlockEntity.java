package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllIcons;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlockEntity;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import io.github.hadron13.gearbox.register.GearboxFluids;
import io.github.hadron13.gearbox.register.GearboxIcons;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;


public class DistillationControllerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public ScrollOptionBehaviour<DistilMode> distilMode;
    public SmartFluidTankBehaviour inputTank;
    public SmartFluidTankBehaviour outputTank;
    public DistillingRecipe currentRecipe;
    public BlockPos tankController;
    public int requiredOutputs = 0;
    public Map<Integer, BlockPos> outputs = new HashMap<>();
    public int timer;
    public LerpedFloat gaugeLevel = LerpedFloat.linear();

    public boolean contentsChanged;

    protected LazyOptional<IFluidHandler> fluidCapability;

    public DistillationControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        gaugeLevel.chase(0, 1/16f, LerpedFloat.Chaser.EXP);
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        distilMode = new ScrollOptionBehaviour<>(DistilMode.class,
                GearboxLang.translate("gui.distil_mode").component(), this, new DistilModeBoxTransform());
        distilMode.withCallback(i -> {
            switch (DistilMode.class.getEnumConstants()[i]){
                case DISTIL_VACUUM -> {
                    outputTank.getPrimaryHandler().setFluid(new FluidStack(GearboxFluids.AIR.get(), 8000));
                }
                case DISTIL_FLASH, DISTIL_ATMOSPHERIC -> {
                    outputTank.getPrimaryHandler().setFluid(FluidStack.EMPTY);
                }
            }
        });
        behaviours.add(distilMode);


        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 4000, true)
                .whenFluidUpdates(this::sendData);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 1, 8000, true)
                .whenFluidUpdates(this::sendData)
                .forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = inputTank.getCapability();
            LazyOptional<? extends IFluidHandler> outputCap = outputTank.getCapability();
            return new CombinedTankWrapper(outputCap.orElse(null), inputCap.orElse(null));
        });
    }

    public Optional<SteelTankBlockEntity> getTankControllerBE(){
        if(tankController == null)
            return Optional.empty();
        BlockEntity be = level.getBlockEntity(tankController);
        if(be instanceof SteelTankBlockEntity tank){
            SteelTankBlockEntity controller = tank.getControllerBE();
            if(controller != null)
                return Optional.of(controller);
        }
        return Optional.empty();
    }

    public boolean addOutput(int level, BlockPos pos){
        if(outputs.containsKey(level)) {
            return outputs.get(level) != pos;
        }
        outputs.put(level, pos);
        if(currentRecipe != null)
            requiredOutputs = currentRecipe.getFluidResults().size() - outputs.size();
        sendData();
        return false;
    }

    public void removeOutput(int level){
        SteelTankBlockEntity tankController = getTankControllerBE().orElse(null);
        outputs.remove(level);
        if(this.level.isClientSide)
            return;
        if(tankController != null)
            requiredOutputs = (tankController.getHeight()/2)+2 - outputs.size();
        sendData();
    }

    public int getSteam(){
        IFluidHandler availableFluids = getCapability(ForgeCapabilities.FLUID_HANDLER)
                .orElse(null);

        for(int i = 0; i < availableFluids.getTanks();i++){
            FluidStack fluid = availableFluids.getFluidInTank(i);
            if(fluid.getFluid() == GearboxFluids.STEAM.get().getSource()){
                return fluid.getAmount();
            }
        }
        return 0;
    }

    public int getAir(){
        return outputTank.getPrimaryHandler().getFluidInTank(0).getAmount();
    }

    public boolean hasSteam(){
        return getSteam() >= 1000;
    }

    public boolean hasVacuum(){
        return  getAir() < 500;
    }

    public boolean canProcess(){

        if(currentRecipe == null)
            return false;

        if(outputs.size() < currentRecipe.getFluidResults().size())
            return false;

        if(inputTank.isEmpty())
            return false;

        SteelTankBlockEntity tankController = getTankControllerBE().orElse(null);
        if(tankController == null)
            return false;

        int width = tankController.getWidth();
        if(width < 2) return false;

        return switch (distilMode.get()){
            case DISTIL_FLASH -> hasSteam();
            case DISTIL_ATMOSPHERIC -> width == 3 && tankController.heat > 1;
            case DISTIL_VACUUM -> hasVacuum() && tankController.heat > 1;
        };
    }

    public float getGaugeTarget(){
        return switch (distilMode.get()){
            case DISTIL_FLASH -> getSteam()/4000f;
            case DISTIL_ATMOSPHERIC -> getTankControllerBE().map(tank-> (float)Math.min(1.0, tank.heat / 6f) ).orElse(0F);
            case DISTIL_VACUUM -> 1.0f - (getAir() / 8000f);
        };
    }

    @Override
    public void tick() {
        super.tick();

        Optional<SteelTankBlockEntity> tankControllerOptional = getTankControllerBE();

        if(tankControllerOptional.isEmpty())
            return;

        SteelTankBlockEntity tankController = tankControllerOptional.get();

        if(level.isClientSide){
            gaugeLevel.updateChaseTarget(getGaugeTarget());
            gaugeLevel.tickChaser();
            return;
        }

        if(distilMode.get() == DistilMode.DISTIL_VACUUM){
            if(outputTank.getPrimaryHandler().getFluidInTank(0).getAmount() < 8000)
                sendData();
            outputTank.getPrimaryHandler().fill(new FluidStack(GearboxFluids.AIR.get(), tankController.getHeight()*15), IFluidHandler.FluidAction.EXECUTE);
        }

        if(!DistillingRecipe.match(this, currentRecipe)){
            currentRecipe = null;
        }

        if(!canProcess())
            return;

        if(timer > 0) {
            timer -= distilMode.get() == DistilMode.DISTIL_FLASH? 2 : (int)tankController.heat;
        }else {
            //apply time!

            for(boolean simulate : Iterate.trueAndFalse) {
                int output = 0;
                for (FluidStack result : currentRecipe.getFluidResults()) {
                    output++;

                    BlockPos pos = outputs.get(output);
                    if (pos == null)
                        continue;
                    BlockEntity be = level.getBlockEntity(pos);
                    if (!(be instanceof DistillationOutputBlockEntity))
                        continue;

                    DistillationOutputBlockEntity outBE = (DistillationOutputBlockEntity) be;

                    outBE.tankInventory.allowInsertion();
                    int filled = outBE.tankInventory.getPrimaryHandler().fill(result, simulate? SIMULATE : EXECUTE);

                    if(simulate && filled < result.getAmount()){
                        timer += 50;
                        return;
                    }
                    outBE.tankInventory.forbidInsertion();
                }
            }


            FluidIngredient fluidIngredient = currentRecipe.getFluidIngredients().get(0);
            int amountRequired = fluidIngredient.getRequiredAmount();

            IFluidHandler availableFluids = getCapability(ForgeCapabilities.FLUID_HANDLER)
                    .orElse(null);

            for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                if(distilMode.get() == DistilMode.DISTIL_FLASH){
                    if(fluidStack.getFluid() == GearboxFluids.STEAM.getSource()){
                        int drainedAmount = Math.min(500, fluidStack.getAmount());
                        fluidStack.shrink(drainedAmount);
                    }
                }
                if (!fluidIngredient.test(fluidStack))
                    continue;
                int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                fluidStack.shrink(drainedAmount);
            }



            if(DistillingRecipe.match(this, currentRecipe)){
                timer = currentRecipe.getProcessingDuration();
            }else{
                currentRecipe = null;
            }
            sendData();
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if(tankController == null){
            for(Direction d : Iterate.directions){
                BlockEntity neighbour = getLevel().getBlockEntity(worldPosition.relative(d));
                if(neighbour instanceof SteelTankBlockEntity tank){
                    tankController = tank.getController();
                    break;
                }
            }
        }else{
            if(!getLevel().getBlockState(tankController).is(GearboxBlocks.STEEL_FLUID_TANK.get()))
                tankController = null;
        }
        if(tankController == null)
            return;

        BlockEntity be = level.getBlockEntity(tankController);
        if(be instanceof SteelTankBlockEntity tank){
            tank.distillationController = worldPosition;
            tank.setDistillationMode(true);
        }

        if(level.isClientSide)
            return;
        if(currentRecipe == null) {
            Optional<DistillingRecipe> recipeOptional = GearboxRecipeTypes.DISTILLING.find(this, level);

            if (recipeOptional.isEmpty())
                return;

            currentRecipe = recipeOptional.get();
            timer = currentRecipe.getProcessingDuration();
            requiredOutputs = currentRecipe.getFluidResults().size() - outputs.size();
            sendData();
        }
    }

    @Override
    public void remove() {
        super.remove();
        if(tankController == null)
            return;

        BlockEntity be = level.getBlockEntity(tankController);
        if(be instanceof SteelTankBlockEntity tank){
            tank.distillationController = null;
            tank.setDistillationMode(false);
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putInt("required_outputs", requiredOutputs);
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        requiredOutputs = tag.getInt("required_outputs");
        super.read(tag, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        GearboxLang.translate("gui.distil_mode").text(":").forGoggles(tooltip);
        GearboxLang.translate(distilMode.get().getRawTranslationKey())
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, 1);

        containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));


        if(distilMode.get() == DistilMode.DISTIL_VACUUM && !hasVacuum()){
            GearboxLang.text("")
                    .forGoggles(tooltip);
            GearboxLang.addHint(tooltip, "hint.distil.vacuum");
        }
        if(distilMode.get() == DistilMode.DISTIL_FLASH && !hasSteam()){
            GearboxLang.text("")
                    .forGoggles(tooltip);
            GearboxLang.addHint(tooltip, "hint.distil.flash");
        }

        if(requiredOutputs > 0){
            GearboxLang.text("")
                    .forGoggles(tooltip);
            GearboxLang.addHint(tooltip, "hint.distil.missing");
        }

        SteelTankBlockEntity tank= getTankControllerBE().orElse(null);
        if(tank == null)
            return true;

        if(distilMode.get() != DistilMode.DISTIL_FLASH && tank.heat < 2) {
            GearboxLang.text("")
                    .forGoggles(tooltip);
            GearboxLang.addHint(tooltip, "hint.distil.heat");
        }

        if(tank.getWidth() < 2 || ( distilMode.get() == DistilMode.DISTIL_ATMOSPHERIC && tank.getWidth() < 3)){
            GearboxLang.text("")
                    .forGoggles(tooltip);
            GearboxLang.addHint(tooltip, "hint.distil.width");
        }

        return true;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER && (side == null || side.getAxis() == DistillationControllerBlock.getAxis(getBlockState()) )){
            return fluidCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    private class DistilModeBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8f, 8f, 16f);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return DistillationControllerBlock.shouldRenderHeadOnFaceStatic(state, direction);
        }
    }



    public static enum DistilMode implements INamedIconOptions {
        DISTIL_FLASH(GearboxIcons.DISTIL_FLASH),
        DISTIL_ATMOSPHERIC(GearboxIcons.DISTIL_ATMOSPHERIC),
        DISTIL_VACUUM(GearboxIcons.DISTIL_VACUUM),
        ;

        private String translationKey;
        private GearboxIcons icon;

        private DistilMode(GearboxIcons icon) {
            this.icon = icon;
            translationKey = "gui.distil_mode." + Lang.asId(name());
        }

        @Override
        public AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return "gearbox." + translationKey;
        }

        public String getRawTranslationKey(){
            return translationKey;
        }
    }
}

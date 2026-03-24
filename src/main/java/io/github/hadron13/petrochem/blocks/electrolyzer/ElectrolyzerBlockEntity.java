package io.github.hadron13.petrochem.blocks.electrolyzer;

import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import io.github.hadron13.petrochem.PetrochemLang;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.github.hadron13.petrochem.blocks.electrolyzer.ElectrolyzerBlock.HORIZONTAL_FACING;


public class ElectrolyzerBlockEntity extends MechanicalMixerBlockEntity {

    public static final Object electrolyzingRecipeKey = new Object();

    public final InternalEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyEnergy;

    public ScrollValueBehaviour speed;

    public int energy_consumption = 0;



    public ElectrolyzerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyStorage = new InternalEnergyStorage(8192, 8192, 0);
        lazyEnergy = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        speed = new ScrollValueBehaviour(PetrochemLang.translateDirect("gui.electrolyzer.speed"), this, new SpeedValueBoxTransform())
                .between(0, 100);
        speed.setValue(50);

        behaviours.add(speed);
    }

    private class SpeedValueBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 16.0f);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return state.getValue(HORIZONTAL_FACING) == direction;
        }
    }
    @Override
    public void tick(){
        if(level != null && !level.isClientSide) {
            if(speed.value == 0){
                runningTicks++;
            }
            if (currentRecipe != null && currentRecipe instanceof ElectrolyzingRecipe electrolyzingRecipe) {
                energy_consumption = (int)(electrolyzingRecipe.requiredEnergy * Math.max((speed.value*speed.value)/2500f, 0.5f));
                if (energyStorage.internalConsumeEnergy(energy_consumption) < energy_consumption){
                    runningTicks++;
                }
            }
            if(!isRunning()){
                energy_consumption = 0;
                currentRecipe = null;
            }
            sendData();
        }
        super.tick();
    }

    @Override
    public float getSpeed(){
        if(energyStorage.getEnergyStored() == 0)
            return 0;
        return 128f * (speed.value/100f);
    }

    @Override
    public float getRenderedHeadRotationSpeed(float partialTicks) {
        return running? getSpeed()/2 : getSpeed()/4;
    }

    @Override
    public void renderParticles() {

        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent() || level == null)
            return;

        spillParticle(ParticleTypes.BUBBLE.getType());
        spillParticle(ParticleTypes.BUBBLE.getType());

        if(level.random.nextInt(4) != 1)
            return;

        for (SmartInventory inv : basin.get()
                .getInvs()) {
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getItem(slot);
                if (stackInSlot.isEmpty())
                    continue;
                ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                spillParticle(data);
            }
        }

        for (SmartFluidTankBehaviour behaviour : basin.get()
                .getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                spillParticle(FluidFX.getFluidParticle(tankSegment.getRenderedFluid()));
            }
        }
    }
    @Override
    protected List<Recipe<?>> getMatchingRecipes() {
        if (getBasin().map(BasinBlockEntity::isEmpty)
                .orElse(true))
            return new ArrayList<>();

        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);
        return list.stream()
                .filter(this::matchBasinRecipe)
                .sorted((r1, r2) -> r2.getIngredients()
                        .size()
                        - r1.getIngredients()
                        .size())
                .collect(Collectors.toList());
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        energyStorage.storedEnergyTooltip(tooltip);
        if(energy_consumption > 0)
            InternalEnergyStorage.energyConsumptionTooltip(tooltip, energy_consumption);
        return true;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if(clientPacket) {
            energy_consumption = compound.getInt("consumption");
            energyStorage.read(compound);
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("consumption", energy_consumption);
        energyStorage.write(compound);
    }

    @Override
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger(){
        return Optional.empty();
    }
    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r instanceof ElectrolyzingRecipe;
    }

    @Override
    public Object getRecipeCacheKey(){
        return electrolyzingRecipeKey;
    }


    @Override
    public void remove() {
        super.remove();
        lazyEnergy.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY && (side == null || (side.getAxis().isHorizontal() && side != getBlockState().getValue(HORIZONTAL_FACING))) )// && !level.isClientSide
            return lazyEnergy.cast();
        return LazyOptional.empty();
    }

}

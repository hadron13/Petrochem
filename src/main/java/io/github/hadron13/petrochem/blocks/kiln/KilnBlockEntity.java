package io.github.hadron13.petrochem.blocks.kiln;


import java.util.List;
import java.util.Optional;


import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.sound.SoundScapes.AmbienceGroup;

import io.github.hadron13.petrochem.PetrochemLang;
import io.github.hadron13.petrochem.register.PetrochemRecipeTypes;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class KilnBlockEntity extends KineticBlockEntity implements IHaveHoveringInformation {

    public ItemStackHandler inputInv;
    public ItemStackHandler outputInv;
    public LazyOptional<IItemHandler> capability;
    public int timer;
    private PyroprocessingRecipe lastRecipe;

    public KilnBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInv = new ItemStackHandler(1);
        outputInv = new ItemStackHandler(9);
        capability = LazyOptional.of(KilnInventoryHandler::new);
    }

    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return true;
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));

        super.addBehaviours(behaviours);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        if (getSpeed() == 0)
            return;
        if(level.getRandom().nextInt(4) != 0)
            return;
        if (inputInv.getStackInSlot(0)
                .isEmpty())
            return;

        float pitch = Mth.clamp((Math.abs(getSpeed()) / 256f) + .45f, .85f, 1f);
        SoundScapes.play(AmbienceGroup.MILLING, worldPosition, pitch);


        BlockPos pos = getBlockPos();
        getLevel().playLocalSound(pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.AMBIENT, 0.5f, pitch, false);
    }

    @Override
    public void tick() {

        super.tick();

        if (getSpeed() == 0)
            return;
        if(inputInv.getStackInSlot(0).isEmpty()) {
            level.setBlock(worldPosition, getBlockState().setValue(KilnBlock.POWERED, false), 3);
        }
        for (int i = 0; i < outputInv.getSlots(); i++)
            if (outputInv.getStackInSlot(i)
                    .getCount() == outputInv.getSlotLimit(i))
                return;

        if (timer > 0) {
            timer -= getProcessingSpeed();

            ItemStack stackInSlot = inputInv.getStackInSlot(0);
            if (!stackInSlot.isEmpty()) {
                level.setBlock(worldPosition, getBlockState().setValue(KilnBlock.POWERED, true), 3);
                sendData();
                if (level.isClientSide) {
                    spawnParticles();
                    return;
                }
            }

            if (timer <= 0) {
                process();
            }
            return;
        }

        if (inputInv.getStackInSlot(0)
                .isEmpty())
            return;

        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<PyroprocessingRecipe> recipe = PetrochemRecipeTypes.PYROPROCESSING.find(inventoryIn, level);
            if (!recipe.isPresent()) {
                timer = 100;
                sendData();
            } else {
                lastRecipe = recipe.get();
                timer = lastRecipe.getProcessingDuration();
                sendData();
            }
            return;
        }

        timer = lastRecipe.getProcessingDuration();
        sendData();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        capability.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInv);
        ItemHelper.dropContents(level, worldPosition, outputInv);
    }

    private void process() {
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);

        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<PyroprocessingRecipe> recipe = PetrochemRecipeTypes.PYROPROCESSING.find(inventoryIn, level);
            if (!recipe.isPresent())
                return;
            lastRecipe = recipe.get();
        }

        ItemStack stackInSlot = inputInv.getStackInSlot(0);
        stackInSlot.shrink(1);

        lastRecipe.rollResults()
                    .forEach(stack -> ItemHandlerHelper.insertItemStacked(outputInv, stack, false));
        inputInv.setStackInSlot(0, stackInSlot);

        sendData();
        setChanged();
    }

    public void spawnParticles() {
        if(level.random.nextInt(8) != 1)
            return;

        Vec3 center = VecHelper.getCenterOf(worldPosition);
        level.addParticle(ParticleTypes.LARGE_SMOKE, center.x, center.y + .4f, center.z, 0, 1 /32f, 0);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Timer", timer);
        compound.put("InputInventory", inputInv.serializeNBT());
        compound.put("OutputInventory", outputInv.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        timer = compound.getInt("Timer");
        inputInv.deserializeNBT(compound.getCompound("InputInventory"));
        outputInv.deserializeNBT(compound.getCompound("OutputInventory"));
        super.read(compound, clientPacket);
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isItemHandlerCap(cap))
            return capability.cast();
        return super.getCapability(cap, side);
    }

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tester = new ItemStackHandler(1);
        tester.setStackInSlot(0, stack);
        RecipeWrapper inventoryIn = new RecipeWrapper(tester);

        if (lastRecipe != null && lastRecipe.matches(inventoryIn, level))
            return true;
        return PetrochemRecipeTypes.PYROPROCESSING.find(inventoryIn, level)
                .isPresent();
    }

    private class KilnInventoryHandler extends CombinedInvWrapper {

        public KilnInventoryHandler() {
            super(inputInv, outputInv);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return false;
            return canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return stack;
            if (!isItemValid(slot, stack))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (inputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }

    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean kinetic_tooltip = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        boolean item_tooltip = false;
        for (int i = 0; i < inputInv.getSlots(); i++) {
            item_tooltip = true;
            ItemStack stackInSlot = inputInv.getStackInSlot(i);
            if (stackInSlot.isEmpty())
                continue;
            PetrochemLang.text("")
                    .add(Component.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(PetrochemLang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
        }
        for (int i = 0; i < outputInv.getSlots(); i++) {
            item_tooltip = true;
            ItemStack stackInSlot = outputInv.getStackInSlot(i);
            if (stackInSlot.isEmpty())
                continue;
            PetrochemLang.text("")
                    .add(Component.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(PetrochemLang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
        }
        return kinetic_tooltip || item_tooltip;
    }

}
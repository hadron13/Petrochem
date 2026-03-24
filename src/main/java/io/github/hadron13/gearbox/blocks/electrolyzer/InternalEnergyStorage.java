package io.github.hadron13.gearbox.blocks.electrolyzer;

import io.github.hadron13.gearbox.PetrochemLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;


//carefully stolen from https://github.com/mrh0/createaddition/blob/1.18/src/main/java/com/mrh0/createaddition/energy/InternalEnergyStorage.java
public class InternalEnergyStorage extends EnergyStorage {

    public InternalEnergyStorage(int capacity) {
        super(capacity, capacity, capacity, 0);
    }

    public InternalEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public CompoundTag write(CompoundTag nbt) {
        nbt.putInt("energy", energy);
        return nbt;
    }

    public void read(CompoundTag nbt) {
        setEnergy(nbt.getInt("energy"));
    }

    public CompoundTag write(CompoundTag nbt, String name) {
        nbt.putInt("energy_"+name, energy);
        return nbt;
    }

    public void read(CompoundTag nbt, String name) {
        setEnergy(nbt.getInt("energy_"+name));
    }

    public int getSpace() {
        return Math.max(getMaxEnergyStored() - getEnergyStored(), 0);
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    public int internalConsumeEnergy(int consume) {
        int oenergy = energy;
        energy = Math.max(0, energy - consume);
        return oenergy - energy;
    }

    public int internalProduceEnergy(int produce) {
        int oenergy = energy;
        energy = Math.min(capacity, energy + produce);
        return oenergy - energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void storedEnergyTooltip(List<Component> tooltip){
        PetrochemLang.translate("gui.goggles.energy_stats").forGoggles(tooltip);

        PetrochemLang.number(this.getEnergyStored())
                .add(PetrochemLang.text("/"))
                .add(PetrochemLang.number(this.getMaxEnergyStored()))
                .add(PetrochemLang.text(" FE"))
                .style(ChatFormatting.AQUA)
                .space()
                .add(PetrochemLang.translate("gui.goggles.energy_stored")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

    }

    public static void energyConsumptionTooltip(List<Component> tooltip, int consumption){
        PetrochemLang.number(consumption)
                .add(PetrochemLang.text(" FE/tick"))
                .style(ChatFormatting.AQUA)
                .space()
                .add(PetrochemLang.translate("gui.goggles.energy_consumption")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
    }


}

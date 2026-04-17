package io.github.hadron13.petrochem.blocks.small_engine;

import dev.architectury.utils.value.FloatSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.lang.ref.WeakReference;

public class EngineSoundInstance extends AbstractTickableSoundInstance {

    public final WeakReference<BlockEntity> be;
    public final FloatSupplier pitchSupplier;

    public EngineSoundInstance(SoundEvent soundEvent, RandomSource random, BlockEntity be, FloatSupplier pitchSupplier) {
        super(soundEvent, SoundSource.AMBIENT, random);
        this.be = new WeakReference<>(be);
        this.pitchSupplier = pitchSupplier;
        this.looping = true;
        this.attenuation = Attenuation.LINEAR;
    }

    public void cease(){
        stop();
    }

    @Override
    public void tick() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || be.get() == null || !level.isLoaded(be.get().getBlockPos())) {
            this.stop();
        }

        pitch = pitchSupplier.getAsFloat();
    }

}

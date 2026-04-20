package io.github.hadron13.petrochem.blocks.small_engine;

import dev.architectury.utils.value.FloatSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.BiomeAmbientSoundsHandler;
import net.minecraft.client.resources.sounds.SoundInstance;
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

    public EngineSoundInstance(SoundEvent soundEvent, BlockEntity be, FloatSupplier pitchSupplier) {
        super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.be = new WeakReference<>(be);
        this.pitchSupplier = pitchSupplier;
        this.looping = true;
        this.attenuation = Attenuation.LINEAR;
        volume = 0.2f;
        pitch = 1.0f;
        this.delay = 0;

        BlockPos pos = be.getBlockPos();
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public void cease(){
        stop();
    }

    @Override
    public void tick() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null  || be.get() == null || level.getBlockEntity(be.get().getBlockPos()) == null || !level.isLoaded(be.get().getBlockPos())) {
            this.stop();
        }

        pitch = pitchSupplier.getAsFloat();
    }

}

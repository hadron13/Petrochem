package io.github.hadron13.petrochem.blocks.small_engine;

import dev.architectury.utils.value.FloatSupplier;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.animation.LerpedFloat;
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

    public WeakReference<BlockEntity> be;
    public final LerpedFloat lerpedPitch = LerpedFloat.linear();
    public final LerpedFloat lerpedVolume = LerpedFloat.linear();

    public EngineSoundInstance(SoundEvent soundEvent, BlockEntity be) {
        super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.attenuation = Attenuation.LINEAR;
        volume = 0.2f;
        pitch = 1.0f;
        lerpedPitch.chase(1.0, 1 / 20f, LerpedFloat.Chaser.EXP);
        lerpedVolume.chase(0.2, 1 / 5f, LerpedFloat.Chaser.EXP);

        this.be = new WeakReference<>(be);
        BlockPos position = be.getBlockPos();

        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
    }

    public void setVolume(float volume){
        lerpedVolume.updateChaseTarget(volume);
    }

    public void setPitch(float pitch){
        lerpedPitch.updateChaseTarget(pitch);
    }

    public void cease(){
        lerpedVolume.updateChaseTarget(0);
    }

    @Override
    public void tick() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || be.get() == null || be.get().isRemoved() ) {
            cease();
        }

        lerpedPitch.tickChaser();
        lerpedVolume.tickChaser();

        pitch = lerpedPitch.getValue();
        volume = lerpedVolume.getValue();

        if(volume == 0){
            stop();
        }
    }

}

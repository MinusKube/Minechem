package minechem.sound;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class LoopingSound
{

    private SoundEvent sound;
    private SoundCategory soundCategory;
    private int soundLength;
    private int timer;
    private EntityPlayer entity;
    private float volume;
    private float pitch;

    public LoopingSound(SoundEvent sound, SoundCategory soundCategory, int soundLength)
    {
        this.sound = sound;
        this.soundCategory = soundCategory;
        this.soundLength = soundLength;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.timer = soundLength;
    }

    public void setVolume(float volume)
    {
        this.volume = volume;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public void play(World world, double x, double y, double z)
    {
        if (timer == soundLength)
        {
            timer = 0;
            if (this.entity == null)
            {
                world.playSound(x, y, z, this.sound, this.soundCategory, this.volume, this.pitch, false);
            } else
            {
                world.playSound(this.entity, this.entity.getPosition(), this.sound, this.soundCategory, this.volume, this.pitch);
            }
        }
        timer++;
    }

}

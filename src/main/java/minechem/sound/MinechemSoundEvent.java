package minechem.sound;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MinechemSoundEvent
{

    @SubscribeEvent
    public void onSound(SoundLoadEvent event)
    {
        //TODO:Sound event
        //event.manager.soundPoolSounds.addSound(Reference.TEXTURE_MOD_ID + "assets/minechem/sound/minechem/projector.ogg");
    }

}

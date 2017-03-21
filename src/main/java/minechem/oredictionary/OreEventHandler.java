package minechem.oredictionary;

import minechem.MinechemRecipes;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class OreEventHandler
{

    @SubscribeEvent
    public void onOreEvent(OreDictionary.OreRegisterEvent event)
    {
        String oreName = event.getName();
        for (OreDictionaryHandler handler : MinechemRecipes.getOreDictionaryHandlers())
        {
            if (handler.canHandle(oreName))
            {
                handler.handle(oreName);
                return;
            }
        }
    }

}

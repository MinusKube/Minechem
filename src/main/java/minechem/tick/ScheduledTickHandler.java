package minechem.tick;

import minechem.Settings;
import minechem.item.molecule.MoleculeEnum;
import minechem.potion.PharmacologyEffectRegistry;
import minechem.radiation.RadiationHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ScheduledTickHandler
{

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event)
    {
        if (event.side == Side.SERVER && event.phase == TickEvent.Phase.START)
        {
            EntityPlayer player = event.player;
            RadiationHandler.getInstance().update(player);
        }
    }

    @SubscribeEvent
    public void checkForPoison(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getItem() != null && event.getItem().getTagCompound() != null && Settings.FoodSpiking)
        {
            NBTTagCompound stackTag = event.getItem().getTagCompound();
            boolean isPoisoned = stackTag.getBoolean("minechem.isPoisoned");
            int[] effectTypes = stackTag.getIntArray("minechem.effectTypes");
            if (isPoisoned)
            {
                for (int effectType : effectTypes)
                {
                    MoleculeEnum molecule = MoleculeEnum.getById(effectType);
                    PharmacologyEffectRegistry.applyEffect(molecule, event.getEntityLiving());
                }
            }
        }
    }

}

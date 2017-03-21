package minechem.item.polytool.types;

import minechem.item.element.ElementEnum;
import minechem.item.polytool.PolytoolUpgradeType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;

public class PolytoolTypePlatnium extends PolytoolUpgradeType
{
    @Override
    public void hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
    {
        if (!target.world.isRemote)
        {
            if (target.world.rand.nextInt(50) < power + 1)
            {
                player.world.playEvent(2002, player.getPosition(), 0);
                int i = (int)(power + player.world.rand.nextInt(5) + player.world.rand.nextInt(5));

                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    player.world.spawnEntity(new EntityXPOrb(player.world, player.posX, player.posY, player.posZ, j));
                }
            }
        }
    }

    @Override
    public ElementEnum getElement()
    {
        return ElementEnum.Pt;
    }

    @Override
    public String getDescription()
    {
        return "Bonus XP";
    }

}

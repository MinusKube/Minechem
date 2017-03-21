package minechem.item.polytool.types;

import minechem.item.element.ElementEnum;
import minechem.item.polytool.PolytoolUpgradeType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class PolytoolTypeArgon extends PolytoolUpgradeType
{
    @Override
    public void hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
    {
        if (!target.world.canSeeSky(target.getPosition()))
        {
            target.attackEntityFrom(DamageSource.inWall, power);
        }
    }

    @Override
    public ElementEnum getElement()
    {
        return ElementEnum.Ar;
    }

    @Override
    public String getDescription()
    {
        return "Does extra suffocation damage when in a cave";
    }

}

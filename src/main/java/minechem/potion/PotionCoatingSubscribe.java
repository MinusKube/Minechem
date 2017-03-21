package minechem.potion;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionCoatingSubscribe
{

    @SubscribeEvent
    public void entityAttacked(LivingAttackEvent event)
    {
        if (event.getSource().getSourceOfDamage() instanceof EntityLivingBase)
        {
            EntityLivingBase entity = (EntityLivingBase) event.getSource().getSourceOfDamage();
            ItemStack weapon = entity.getActiveItemStack();
            if (weapon == null)
            {
                return;
            }
            NBTTagList list = weapon.getEnchantmentTagList();
            if (list == null)
            {
                return;
            }
            for (int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound enchantmentTag = list.getCompoundTagAt(i);
                Enchantment enchant = Enchantment.getEnchantmentByID(enchantmentTag.getShort("id"));
                if (enchant instanceof PotionEnchantmentCoated)
                {
                    ((PotionEnchantmentCoated) enchant).applyEffect(event.getEntityLiving());
                }
            }
        }
    }
}

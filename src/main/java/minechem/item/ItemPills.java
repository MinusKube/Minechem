package minechem.item;

import minechem.gui.CreativeTabMinechem;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class ItemPills extends ItemFood
{
    public ItemPills(int id, int heal)
    {
        super(heal, 0.4F, false);
        setMaxDamage(0);
        setMaxStackSize(32);
        this.setUnlocalizedName("itempill");
        this.setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        this.setAlwaysEdible();
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 15;
    }

}

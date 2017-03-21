package minechem.item;


import minechem.gui.CreativeTabMinechem;
import minechem.utils.MinechemUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class OpticalMicroscopeLens extends Item
{
    static final String[] descriptiveNames =
    {
        "item.name.concaveLens", "item.name.convexLens", "item.name.microscopeLens", "item.name.projectorLens"
    };

    public OpticalMicroscopeLens()
    {
        super();
        setUnlocalizedName("opticalMicroscopeLens");
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
        int metadata = itemStack.getItemDamage();
        return MinechemUtil.getLocalString(descriptiveNames[metadata], true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(item, 1, 0));
        par3List.add(new ItemStack(item, 1, 1));
        par3List.add(new ItemStack(item, 1, 2));
        par3List.add(new ItemStack(item, 1, 3));
    }

}

package minechem.item;

import minechem.gui.CreativeTabMinechem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemAtomicManipulator extends Item
{

    public ItemAtomicManipulator()
    {
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        setUnlocalizedName("itemAtomicManipulator");
        setHasSubtypes(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List subItems)
    {
        subItems.add(new ItemStack(item, 1, 0));
    }

}

package minechem.block;

import minechem.gui.CreativeTabMinechem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockUraniumOre extends Block
{
    public BlockUraniumOre()
    {
        super(Material.IRON);
        this.setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        this.setUnlocalizedName("uraniumOre");
        this.setRegistryName("oreUranium");
        this.setHardness(4F);
    }

    /*@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(Textures.IIcon.URANIUM);
    }*/

}

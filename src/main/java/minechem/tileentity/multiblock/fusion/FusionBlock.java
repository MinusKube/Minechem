package minechem.tileentity.multiblock.fusion;

import minechem.Minechem;
import minechem.block.BlockSimpleContainer;
import minechem.gui.CreativeTabMinechem;
import minechem.tileentity.multiblock.MultiBlockTileEntity;
import minechem.tileentity.multiblock.fission.FissionTileEntity;
import minechem.tileentity.prefab.TileEntityProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FusionBlock extends BlockSimpleContainer
{

    public FusionBlock()
    {
        super(Material.IRON);
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        setUnlocalizedName("fusionWall");
        setRegistryName("fusionWall");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    @Nullable ItemStack itemStack, EnumFacing facing, float f8, float f9, float f10) {

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity == null)
        {
            return false;
        }
        if (!world.isRemote)
        {
            player.openGui(Minechem.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList<ItemStack> itemStacks)
    {
        // Should not drop blocks if this is a proxy
        if (tileEntity instanceof MultiBlockTileEntity && tileEntity instanceof IInventory)
        {
            IInventory inv = (IInventory) tileEntity;
            for (int i = 0; i < inv.getSizeInventory(); i++)
            {
                if (inv.getStackInSlot(i) != null)
                {
                    itemStacks.add(inv.getStackInSlot(i));
                }
            }
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int metadata = state.getBlock().getMetaFromState(state);
        if (metadata == 2)
        {
            return new FusionTileEntity();
        }
        if (metadata == 3)
        {
            return new FissionTileEntity();
        } else
        {
            return new TileEntityProxy();
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getBlock().getMetaFromState(state);
    }

    // Do not drop if this is a reactor core
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        return state.getBlock().getMetaFromState(state) < 2 ? 1 : 0;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < 2; i++)
        {
            par3List.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        super.breakBlock(world, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new TileEntityProxy();
    }

}

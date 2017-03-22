package minechem.tileentity.microscope;

import minechem.Minechem;
import minechem.block.BlockSimpleContainer;
import minechem.gui.CreativeTabMinechem;
import minechem.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class MicroscopeBlock extends BlockSimpleContainer
{
    public MicroscopeBlock()
    {
        super(Material.IRON);
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        setUnlocalizedName("opticalMicroscope");
        setRegistryName("opticalMicroscope");
        setLightLevel(0.5F);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase el, ItemStack is)
    {
        super.onBlockPlacedBy(world, pos, state, el, is);

        int facing = MathHelper.floor(el.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        world.setBlockState(pos, state.getBlock().getStateFromMeta(facing), 2);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    @Nullable ItemStack itemStack, EnumFacing facing, float f8, float f9, float f10) {

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }

        player.openGui(Minechem.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int i)
    {
        return new MicroscopeTileEntity();
    }

    @Override
    public void addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList<ItemStack> itemStacks)
    {
        MicroscopeTileEntity decomposer = (MicroscopeTileEntity) tileEntity;
        for (int slot = 0; slot < decomposer.getSizeInventory(); slot++)
        {
            ItemStack itemstack = decomposer.getStackInSlot(slot);
            if (itemstack != null)
            {
                itemStacks.add(itemstack);
            }
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.values()[CommonProxy.RENDER_ID];
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

}

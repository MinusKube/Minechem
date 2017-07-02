package minechem.tileentity.leadedchest;

import minechem.Minechem;
import minechem.gui.CreativeTabMinechem;
import minechem.utils.MinechemUtil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class LeadedChestBlock extends BlockContainer
{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public LeadedChestBlock()
    {
        super(Material.WOOD);
        this.setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setUnlocalizedName("leadChest");
        this.setRegistryName("leadChest");
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        this.dropItems(world, blockPos);
        super.onBlockDestroyedByPlayer(world, blockPos, blockState);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new LeadedChestTileEntity();
    }

    private void dropItems(World world, BlockPos blockPos)
    {

        TileEntity te = world.getTileEntity(blockPos);
        if (te instanceof IInventory)
        {
            IInventory inventory = (IInventory) te;

            int invSize = inventory.getSizeInventory();
            for (int i = 0; i < invSize; i++)
            {
                MinechemUtil.throwItemStack(world, inventory.getStackInSlot(i), blockPos.getX(), blockPos.getY(), blockPos.getZ());
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing facing, float par7, float par8, float par9)
    {
        if (!world.isRemote)
        {
            LeadedChestTileEntity leadedChest = (LeadedChestTileEntity) world.getTileEntity(blockPos);
            if (leadedChest == null || player.isSneaking())
            {
                return false;
            }
            player.openGui(Minechem.INSTANCE, 0, world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.values()[-1];
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase el, ItemStack is)
    {
        EnumFacing facing = null;
        int facingI = MathHelper.floor_double(el.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (facingI == 0)
        {
            facing = EnumFacing.NORTH;
        }

        if (facingI == 1)
        {
            facing = EnumFacing.EAST;
        }

        if (facingI == 2)
        {
            facing = EnumFacing.SOUTH;
        }

        if (facingI == 3)
        {
            facing = EnumFacing.WEST;
        }

        world.setBlockState(blockPos, state.withProperty(FACING, facing), 2);
    }
}

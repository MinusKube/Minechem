package minechem.tileentity.multiblock.ghostblock;

import minechem.MinechemBlocksGeneration;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GhostBlock extends BlockContainer
{

    public GhostBlock()
    {
        super(Material.IRON);
        setRegistryName("ghostBlock");
        setUnlocalizedName("ghostBlock");
        setLightLevel(0.5F);
        setHardness(1000F);
        setResistance(1000F);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    @Nullable ItemStack itemStack, EnumFacing facing, float f8, float f9, float f10) {
        super.onBlockActivated(world, pos, state, player, hand, itemStack, facing, f8, f9, f10);

        if (world.isRemote)
        {
            return true;
        }

        if (player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 64.0D)
        {
            return true;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof GhostBlockTileEntity)
        {
            GhostBlockTileEntity ghostBlock = (GhostBlockTileEntity) tileEntity;
            ItemStack blockAsStack = ghostBlock.getBlockAsItemStack();
            if (playerIsHoldingItem(player, blockAsStack))
            {
                world.setBlockState(pos, MinechemBlocksGeneration.fusion.getStateFromMeta(blockAsStack.getItemDamage()), 3);
                if (!player.capabilities.isCreativeMode)
                {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                }
                return true;
            }
        }
        return false;
    }

    private boolean playerIsHoldingItem(EntityPlayer entityPlayer, ItemStack itemstack)
    {
        ItemStack helditem = entityPlayer.inventory.getCurrentItem();
        if (helditem != null && itemstack != null)
        {
            if (helditem.getItem() == itemstack.getItem())
            {
                if (helditem.getItemDamage() == itemstack.getItemDamage() || itemstack.getItemDamage() == -1)
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean bool) {
        return true;
    }

    /**
     * Returns whether this block is collideable based on the arguments passed in Args: blockMetaData, unknownFlag
     */

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getBlock().getMetaFromState(state);
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been cleared to be reused)
     */
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return null;
    }


    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given coordinates. Args: blockAccess, x, y, z, side
     */
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing facing) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }


    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    /*@Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }*/

    // XXX: Maybe wrong replacement for getRenderBlockPass()
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }



    @Override
    public TileEntity createNewTileEntity(World var1, int i)
    {
        return new GhostBlockTileEntity();
    }

    /**
     * When player places a ghost block delete it
     */
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack)
    {
        if (entity instanceof EntityPlayer)
        {
            world.setBlockToAir(pos);
        }
    }
}

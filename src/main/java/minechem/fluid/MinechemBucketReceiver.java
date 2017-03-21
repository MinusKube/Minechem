package minechem.fluid;

import minechem.item.bucket.MinechemBucketHandler;
import minechem.item.bucket.MinechemBucketItem;
import minechem.radiation.RadiationEnum;
import minechem.radiation.RadiationFluidTileEntity;
import minechem.radiation.RadiationInfo;
import minechem.utils.MinechemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MinechemBucketReceiver implements IBehaviorDispenseItem
{

    public static void init()
    {
        IBehaviorDispenseItem source = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.BUCKET);
        MinechemBucketReceiver receiver = new MinechemBucketReceiver(source);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BUCKET, receiver);
    }

    public final IBehaviorDispenseItem source;

    public MinechemBucketReceiver(IBehaviorDispenseItem source)
    {
        this.source = source;
    }

    @Override
    public ItemStack dispense(IBlockSource blockSource, ItemStack itemstack)
    {
        IPosition position = BlockDispenser.getDispensePosition(blockSource);
        World world = blockSource.getWorld();
        BlockPos pos = new BlockPos(position.getX(), position.getY(), position.getZ());
        IBlockState front = world.getBlockState(pos);

        if (front.getBlock() instanceof MinechemFluidBlock)
        {
            MinechemBucketItem item = MinechemBucketHandler.getInstance().buckets.get(front.getBlock());
            ItemStack newstack = new ItemStack(item);

            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && item.chemical.radioactivity() != RadiationEnum.stable)
            {
                RadiationInfo.setRadiationInfo(((RadiationFluidTileEntity) tile).info, newstack);
            }

            world.destroyBlock(pos, true);
            itemstack.stackSize--;

            if (itemstack.stackSize <= 0)
            {
                return newstack;
            } else
            {
                TileEntity inventoryTile = blockSource.getBlockTileEntity();
                if (inventoryTile instanceof IInventory)
                {
                    ItemStack stack = MinechemUtil.addItemToInventory((IInventory) blockSource.getBlockTileEntity(), newstack);
                    if (stack != null)
                    {
                        MinechemUtil.throwItemStack(world, stack, pos.getX(), pos.getY(), pos.getZ());
                    }
                } else
                {
                    MinechemUtil.throwItemStack(world, newstack, pos.getX(), pos.getY(), pos.getZ());
                }
            }

            return itemstack;
        }

        return source.dispense(blockSource, itemstack);
    }

}

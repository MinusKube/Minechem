package minechem.item.polytool.types;

import minechem.item.element.ElementEnum;
import minechem.item.polytool.PolytoolUpgradeType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PolytoolTypePhosphorus extends PolytoolUpgradeType
{
    @Override
    public void onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase target)
    {
        if (!target.world.isRemote)
        {
            if (block.isFlammable(world, new BlockPos(x, y, z), EnumFacing.UP))
            {
                for (int i = (int)(x - power); i < x + power; i++)
                {
                    for (int j = (int)(y - power); j < y + power; j++)
                    {
                        for (int k = (int)(z - power); k < z + power; k++)
                        {
                            if (world.getBlockState(new BlockPos(i, j, k)).getBlock() == Blocks.AIR)
                            {
                                world.setBlockState(new BlockPos(i, j, k), Blocks.FIRE.getDefaultState());
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public ElementEnum getElement()
    {
        return ElementEnum.P;
    }

    @Override
    public String getDescription()
    {
        return "Sets nearby blocks on fire";
    }

}

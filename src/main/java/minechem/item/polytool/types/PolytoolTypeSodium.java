package minechem.item.polytool.types;

import minechem.item.element.ElementEnum;
import minechem.item.polytool.PolytoolUpgradeType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PolytoolTypeSodium extends PolytoolUpgradeType
{
    @Override
    public void onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entityLiving)
    {
        x = (int)Math.floor(entityLiving.posX);
        y = (int)Math.floor(entityLiving.posY);
        z = (int)Math.floor(entityLiving.posZ);
        if (!world.isRemote && world.rand.nextInt(35) < power && world.getLight(new BlockPos(x, y, z)) < 12)
        {
            if (Blocks.TORCH.canPlaceBlockAt(world, new BlockPos(x, y, z)))
                world.setBlockState(entityLiving.getPosition(), Blocks.TORCH.getStateFromMeta(0), 3);
        }
    }

    @Override
    public ElementEnum getElement()
    {
        return ElementEnum.Na;
    }

    @Override
    public String getDescription()
    {
        return "Lights up area when mining";
    }

}

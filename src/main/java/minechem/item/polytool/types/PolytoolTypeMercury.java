package minechem.item.polytool.types;

import minechem.item.element.ElementEnum;
import minechem.item.polytool.PolytoolUpgradeType;
import minechem.utils.EnumColour;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PolytoolTypeMercury extends PolytoolUpgradeType
{
    @Override
    public void onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entityLiving)
    {
        int search = (int)(4 * power + 1);
        for (int i = 0; i < search; i++)
        {
            Block found = world.getBlockState(new BlockPos(x, y - i, z)).getBlock();
            if (entityLiving instanceof EntityPlayer && found == Blocks.FLOWING_LAVA || found == Blocks.LAVA)
            {
                entityLiving.sendMessage(new TextComponentString(EnumColour.RED + "WARNING: LAVA UNDERNEATH"));
                break;
            }
        }
    }

    @Override
    public ElementEnum getElement()
    {
        return ElementEnum.Hg;
    }

    @Override
    public String getDescription()
    {
        return "Warns of lava underneath";
    }

}

package minechem.item.polytool.types;

import minechem.item.element.ElementEnum;
import minechem.item.polytool.PolytoolItem;
import minechem.item.polytool.PolytoolUpgradeType;
import minechem.utils.CoordTuple;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PolytoolTypeIron extends PolytoolUpgradeType
{
    private static Map<String, Boolean> ores = new LinkedHashMap<String, Boolean>();

    public static void getOres()
    {
        for (String ore : OreDictionary.getOreNames())
        {
            if (ore.regionMatches(0, "ore", 0, 3))
            {
                for (ItemStack stack : OreDictionary.getOres(ore))
                {
                    ores.put(blockHash(stack), true);
                }
            }
        }
    }

    private static String blockHash(ItemStack stack)
    {
        return blockHash(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
    }

    private static String blockHash(Block block, int meta)
    {
        return block.getUnlocalizedName() + "@" + meta;
    }

    @Override
    public void onBlockDestroyed(ItemStack itemStack, World world, Block id, int x1, int y1, int z1, EntityLivingBase entityLiving)
    {
        if (!world.isRemote && entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entityLiving;
            ArrayList<CoordTuple> queue = new ArrayList<CoordTuple>(100);
            float carbon = 0;
            for (Object upgrade : PolytoolItem.getUpgrades(itemStack))
            {
                if (((PolytoolUpgradeType)upgrade).getElement() == ElementEnum.C)
                {
                    carbon = ((PolytoolUpgradeType)upgrade).power;
                }
            }
            IBlockState state = world.getBlockState(new BlockPos(x1, y1, z1));
            if (ores.containsKey(blockHash(id, state.getBlock().getMetaFromState(state))))
            {
                int toMine = (int)power;
                queue.add(new CoordTuple(x1, y1, z1));
                while (!queue.isEmpty())
                {
                    CoordTuple coord = queue.remove(0);
                    int x = coord.x;
                    int y = coord.y;
                    int z = coord.z;
                    for (EnumFacing dir : EnumFacing.values())
                    {
                        if (world.getBlockState(new BlockPos(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ())).getBlock() == id
                                && world.getBlockState(new BlockPos(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ())) == state)
                        {

                            breakExtraBlock(world, x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ(), player, id, state.getBlock().getMetaFromState(state), carbon);
                            queue.add(new CoordTuple(x + dir.getFrontOffsetX(), y + dir.getFrontOffsetY(), z + dir.getFrontOffsetZ()));
                            toMine--;
                            if (toMine <= 0)
                            {
                                return;
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
        return ElementEnum.Fe;
    }


    @Override
    public String getDescription()
    {
        return "Mines deposits of ores";
    }

    protected void breakExtraBlock(World world, int x, int y, int z, EntityPlayer player, Block block, int meta, float carbon)
    {
        if (player.capabilities.isCreativeMode)
        {
            block.onBlockHarvested(world, new BlockPos(x, y, z), block.getStateFromMeta(meta), player);
            world.setBlockToAir(new BlockPos(x, y, z));
            if (!world.isRemote)
            {
                ((EntityPlayerMP)player).connection.sendPacket(new SPacketBlockChange(world, new BlockPos(x, y, z)));
            }
            return;
        }

        if (!world.isRemote)
        {
            int bonus = (block == Blocks.DIAMOND_ORE || block == Blocks.COAL_ORE) ? (int)(world.rand.nextDouble() * Math.log(carbon)) + 1 : 1;
            block.onBlockHarvested(world, new BlockPos(x, y, z), block.getStateFromMeta(meta), player);

            if (block.removedByPlayer(block.getStateFromMeta(meta), world, new BlockPos(x, y, z), player, true))
            {
                for (int i = 0; i < bonus; i++)
                {
                    block.harvestBlock(world, player, new BlockPos(x, y, z), block.getStateFromMeta(meta), null, null);
                }
            }

            EntityPlayerMP mpPlayer = (EntityPlayerMP)player;
            mpPlayer.connection.sendPacket(new SPacketBlockChange(world, new BlockPos(x, y, z)));
        }
    }

}

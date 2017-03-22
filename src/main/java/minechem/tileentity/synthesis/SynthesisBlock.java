package minechem.tileentity.synthesis;

import minechem.Minechem;
import minechem.Settings;
import minechem.block.BlockSimpleContainer;
import minechem.gui.CreativeTabMinechem;
import minechem.network.MessageHandler;
import minechem.network.message.SynthesisUpdateMessage;
import minechem.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Chemical Synthesizer block. Its associated TileEntitySynthesis's inventory inventory has many specialized slots, including some "ghost" slots whose contents don't really exist and shouldn't be able
 * to be extracted or dumped when the block is broken. See {@link minechem.tileentity.synthesis.SynthesisTileEntity} for details of the inventory slots.
 */
public class SynthesisBlock extends BlockSimpleContainer
{

    public SynthesisBlock()
    {
        super(Material.IRON);
        setRegistryName("chemicalSynthesizer");
        setUnlocalizedName("chemicalSynthesizer");
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
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
        if (!world.isRemote)
        {
            SynthesisUpdateMessage message = new SynthesisUpdateMessage((SynthesisTileEntity)tileEntity);
            if (player instanceof EntityPlayerMP)
            {
                MessageHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
            } else
            {
                MessageHandler.INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), Settings.UpdateRadius));
            }
        }
        player.openGui(Minechem.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new SynthesisTileEntity();
    }

    //TODO:Find replacement
    @Override
    public void addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks)
    {
        SynthesisTileEntity synthesizer = (SynthesisTileEntity) tileEntity;
        for (int slot : SynthesisTileEntity.kRealSlots)
        {
            if (synthesizer.isRealItemSlot(slot))
            {
                ItemStack itemstack = synthesizer.getStackInSlot(slot);
                if (itemstack != null)
                {
                    itemStacks.add(itemstack);
                }
            }
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.values()[CommonProxy.RENDER_ID];
    }

}

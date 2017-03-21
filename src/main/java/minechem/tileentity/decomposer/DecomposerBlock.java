package minechem.tileentity.decomposer;

import minechem.Minechem;
import minechem.Settings;
import minechem.block.BlockSimpleContainer;
import minechem.gui.CreativeTabMinechem;
import minechem.network.MessageHandler;
import minechem.network.message.DecomposerUpdateMessage;
import minechem.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class DecomposerBlock extends BlockSimpleContainer
{
    public DecomposerBlock()
    {
        super(Material.IRON);
        setRegistryName("chemicalDecomposer");
        setUnlocalizedName("chemicalDecomposer");
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, @Nullable ItemStack itemStack, EnumFacing facing, float par8, float par9, float par10) {

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity == null || player.isSneaking())
        {
            return false;
        }
        if (!world.isRemote)
        {
            DecomposerUpdateMessage message = new DecomposerUpdateMessage((DecomposerTileEntity)tileEntity);
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
        return new DecomposerTileEntity();
    }

    //TODO: Find replacement
    @Override
    public void addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList<ItemStack> itemStacks)
    {
        DecomposerTileEntity decomposer = (DecomposerTileEntity) tileEntity;
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
    public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_) {
        return false;
    }

}

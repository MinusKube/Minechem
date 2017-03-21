package minechem.tileentity.blueprintprojector;

import minechem.Minechem;
import minechem.block.BlockSimpleContainer;
import minechem.gui.CreativeTabMinechem;
import minechem.item.blueprint.ItemBlueprint;
import minechem.item.blueprint.MinechemBlueprint;
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

public class BlueprintProjectorBlock extends BlockSimpleContainer
{

    public BlueprintProjectorBlock()
    {
        super(Material.IRON);
        setUnlocalizedName("blueprintProjector");
        setRegistryName("blueprintProjector");
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ITEMS);
        setLightLevel(0.7F);
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
                                    @Nullable ItemStack itemStack, EnumFacing facing, float par8, float par9, float par10) {

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof BlueprintProjectorTileEntity)
        {
            player.openGui(Minechem.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }


    private ItemStack takeBlueprintFromProjector(BlueprintProjectorTileEntity projector)
    {
        MinechemBlueprint blueprint = projector.takeBlueprint();
        return ItemBlueprint.createItemStackFromBlueprint(blueprint);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i)
    {
        return new BlueprintProjectorTileEntity();
    }

    @Override
    public void addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList<ItemStack> itemStacks)
    {
        if (tileEntity instanceof BlueprintProjectorTileEntity)
        {
            BlueprintProjectorTileEntity projector = (BlueprintProjectorTileEntity) tileEntity;
            if (projector.hasBlueprint())
            {
                itemStacks.add(takeBlueprintFromProjector(projector));
            }
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof BlueprintProjectorTileEntity)
        {
            BlueprintProjectorTileEntity projector = (BlueprintProjectorTileEntity) tileEntity;
            projector.destroyProjection();
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {
        return EnumBlockRenderType.values()[CommonProxy.RENDER_ID];
    }

    @Override
    public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_) {
        return false;
    }

}

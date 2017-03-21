package minechem.fluid;

import minechem.Settings;
import minechem.fluid.reaction.ChemicalFluidReactionHandler;
import minechem.item.ChemicalRoomStateEnum;
import minechem.item.MinechemChemicalType;
import minechem.radiation.RadiationEnum;
import minechem.radiation.RadiationFluidTileEntity;
import minechem.utils.MinechemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

import java.util.Random;

public class MinechemFluidBlock extends BlockFluidClassic implements ITileEntityProvider
{
    private final boolean isRadioactivity;
    public static final Material materialFluidBlock = new MaterialLiquid(MapColor.WATER);
    private final boolean solid;

    public MinechemFluidBlock(MinechemFluid fluid, Material material)
    {
        super(fluid, material);
        setQuantaPerBlock(fluid.getQuanta());

        if (fluid instanceof FluidElement)
        {
            isRadioactivity = ((FluidElement) fluid).element.radioactivity() != RadiationEnum.stable;
        } else if (fluid instanceof FluidMolecule)
        {
            isRadioactivity = ((FluidMolecule) fluid).molecule.radioactivity() != RadiationEnum.stable;
        } else
        {
            isRadioactivity = false;
        }

        isBlockContainer = true;
        solid = fluid.getChemical().roomState() == ChemicalRoomStateEnum.SOLID;
    }

    @Override
    public String getUnlocalizedName()
    {
        String fluidUnlocalizedName = getFluid().getUnlocalizedName();
        return fluidUnlocalizedName.substring(0, fluidUnlocalizedName.length() - 5);// Splits off ".name"
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock) {
        super.neighborChanged(state, world, pos, neighborBlock);

        checkStatus(world, pos.getX(), pos.getY(), pos.getZ());
    }

    public void checkStatus(World world, int x, int y, int z)
    {
        if (world.isRemote)
        {
            return;
        }

        if (Settings.reactionFluidMeetFluid)
        {
            for (EnumFacing face : EnumFacing.values())
            {
                if (checkToReact(world, x + face.getFrontOffsetX(), y + face.getFrontOffsetY(), z + face.getFrontOffsetZ(), x, y, z))
                {
                    return;
                }
            }
        }

        checkToExplode(world, x, y, z);
    }

    private boolean checkToReact(World world, int dx, int dy, int dz, int sx, int sy, int sz)
    {
        return ChemicalFluidReactionHandler.checkToReact(this, world.getBlockState(new BlockPos(dx, dy, dz)).getBlock(), world, dx, dy, dz, sx, sy, sz);
    }

    private void checkToExplode(World world, int x, int y, int z)
    {
        MinechemChemicalType type = MinechemUtil.getChemical(this);
        float level = ExplosiveFluidHandler.getInstance().getExplosiveFluid(type);
        if (Float.isNaN(level))
        {
            return;
        }

        boolean flag = false;
        for (EnumFacing face : EnumFacing.values())
        {
            if (ExplosiveFluidHandler.getInstance().existingFireSource(world.getBlockState(new BlockPos(x + face.getFrontOffsetX(), y + face.getFrontOffsetY(), z + face.getFrontOffsetZ())).getBlock()))
            {
                flag = true;
                break;
            }
        }
        if (!flag)
        {
            return;
        }

        world.destroyBlock(new BlockPos(x, y, z), true);
        world.setBlockToAir(new BlockPos(x, y, z));
        world.createExplosion(null, x, y, z, ExplosiveFluidHandler.getInstance().getExplosiveFluid(type), true);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return isRadioactivity && state.getBlock().getMetaFromState(state) == 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return hasTileEntity(getStateFromMeta(i)) ? new RadiationFluidTileEntity() : null;
    }

    @Override
    public boolean eventReceived(IBlockState state, World world, BlockPos pos, int eventID, int eventParameter)
    {
        super.eventReceived(state, world, pos, eventID, eventParameter);

        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null ? tileentity.receiveClientEvent(eventID, eventParameter) : false;
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosion)
    {
        if (world.isRemote)
        {
            return;
        }

        MinechemChemicalType type = MinechemUtil.getChemical(this);
        world.destroyBlock(pos, true);
        world.setBlockToAir(pos);
        world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), ExplosiveFluidHandler.getInstance().getExplosiveFluid(type), true);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (!solid)
        {
            super.updateTick(world, pos, state, rand);
        }
        checkStatus(world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        checkStatus(world, pos.getX(), pos.getY(), pos.getZ());
    }
}

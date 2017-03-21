package minechem.item.bucket;

import minechem.fluid.FluidElement;
import minechem.fluid.FluidMolecule;
import minechem.gui.CreativeTabMinechem;
import minechem.item.MinechemChemicalType;
import minechem.item.molecule.MoleculeEnum;
import minechem.radiation.RadiationEnum;
import minechem.radiation.RadiationFluidTileEntity;
import minechem.radiation.RadiationInfo;
import minechem.utils.Constants;
import minechem.utils.MinechemUtil;
import minechem.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MinechemBucketItem extends ItemBucket
{

    public final Fluid fluid;
    public final Block block;
    public final MinechemChemicalType chemical;

    public MinechemBucketItem(Block block, Fluid fluid, MinechemChemicalType chemical)
    {
        super(block);
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_BUCKETS);
        setContainerItem(Items.BUCKET);
        setUnlocalizedName("minechemBucket");
        this.fluid = fluid;
        this.block = block;
        this.chemical = chemical;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool)
    {
        list.add(Constants.TEXT_MODIFIER + "9" + getFillLocalizedName());
        list.add(Constants.TEXT_MODIFIER + "9" + MinechemUtil.subscriptNumbers(getFormula()));

        String radioactivityColor;
        RadiationEnum radioactivity = RadiationInfo.getRadioactivity(itemstack);
        switch (radioactivity)
        {
            case stable:
                radioactivityColor = Constants.TEXT_MODIFIER + "7";
                break;
            case hardlyRadioactive:
                radioactivityColor = Constants.TEXT_MODIFIER + "a";
                break;
            case slightlyRadioactive:
                radioactivityColor = Constants.TEXT_MODIFIER + "2";
                break;
            case radioactive:
                radioactivityColor = Constants.TEXT_MODIFIER + "e";
                break;
            case highlyRadioactive:
                radioactivityColor = Constants.TEXT_MODIFIER + "6";
                break;
            case extremelyRadioactive:
                radioactivityColor = Constants.TEXT_MODIFIER + "4";
                break;
            default:
                radioactivityColor = "";
                break;
        }

        String radioactiveName = MinechemUtil.getLocalString("element.property." + radioactivity.name(), true);
        String timeLeft = "";
        if (RadiationInfo.getRadioactivity(itemstack) != RadiationEnum.stable && itemstack.getTagCompound() != null)
        {
            long worldTime = player.world.getTotalWorldTime();
            timeLeft = TimeHelper.getTimeFromTicks(RadiationInfo.getRadioactivity(itemstack).getLife() - (worldTime - itemstack.getTagCompound().getLong("decayStart")));
        }
        list.add(radioactivityColor + radioactiveName + (timeLeft.equals("") ? "" : " (" + timeLeft + ")"));
    }

    private String getFillLocalizedName()
    {
        if (fluid instanceof FluidElement)
        {
            return MinechemUtil.getLocalString(((FluidElement) fluid).element.getUnlocalizedName(), true);
        } else if (fluid instanceof FluidMolecule)
        {
            return MinechemUtil.getLocalString(((FluidMolecule) fluid).molecule.getUnlocalizedName(), true);
        }
        return fluid.getLocalizedName(null);
    }

    private String getFormula()
    {
        if (fluid instanceof FluidElement)
        {
            return ((FluidElement) fluid).element.name();
        } else if (fluid instanceof FluidMolecule)
        {
            return ((FluidMolecule) fluid).molecule.getFormula();
        } else if (fluid == FluidRegistry.WATER)
        {
            return MoleculeEnum.water.getFormula();
        }

        return "";
    }

    public boolean placeLiquid(World world, ItemStack itemstack, int x, int y, int z)
    {
        Material material = world.getBlockState(new BlockPos(x, y, z)).getMaterial();
        boolean flag = !material.isSolid();

        if (!world.isAirBlock(new BlockPos(x, y, z)) && !flag)
        {
            return false;
        } else
        {
            if (!world.isRemote && flag && !material.isLiquid())
            {
                world.destroyBlock(new BlockPos(x, y, z), true);
            }

            world.setBlockState(new BlockPos(x, y, z), this.block.getStateFromMeta(0), 3);

            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (chemical.radioactivity() != RadiationEnum.stable && tile instanceof RadiationFluidTileEntity)
            {
                int dimensionID = itemstack.getTagCompound().getInteger("dimensionID");
                long lastUpdate = itemstack.getTagCompound().getLong("lastUpdate");
                long decayStart = itemstack.getTagCompound().getLong("decayStart");
                RadiationInfo radioactivity = new RadiationInfo(itemstack, decayStart, lastUpdate, dimensionID, chemical.radioactivity());

                ((RadiationFluidTileEntity) tile).info = radioactivity;
            }
            return true;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
    {
        RayTraceResult rayTrace = this.rayTrace(world, player, false);

        if (rayTrace == null)
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
        } else
        {
            if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                int x = rayTrace.getBlockPos().getX();
                int y = rayTrace.getBlockPos().getY();
                int z = rayTrace.getBlockPos().getZ();

                if (!world.canMineBlockBody(player, rayTrace.getBlockPos()))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
                }

                switch (rayTrace.sideHit.getIndex())
                {
                    case 0:
                        y--;
                        break;

                    case 1:
                        y++;
                        break;

                    case 2:
                        z--;
                        break;

                    case 3:
                        z++;
                        break;

                    case 4:
                        x--;
                        break;

                    case 5:
                        x++;
                        break;
                }

                if (!player.canPlayerEdit(rayTrace.getBlockPos(), rayTrace.sideHit, itemStack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
                }

                if (this.placeLiquid(world, itemStack, x, y, z) && !player.capabilities.isCreativeMode)
                {
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET));
                }
            }

            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
        }
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player)
    {
        super.onCreated(itemStack, world, player);
        if (RadiationInfo.getRadioactivity(itemStack) != RadiationEnum.stable && itemStack.getTagCompound() == null)
        {
            RadiationInfo.setRadiationInfo(new RadiationInfo(itemStack, world.getTotalWorldTime(), world.getTotalWorldTime(), world.provider.getDimension(), RadiationInfo.getRadioactivity(itemStack)), itemStack);
        }
    }
}

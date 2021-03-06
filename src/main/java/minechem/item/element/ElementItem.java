package minechem.item.element;

import minechem.MinechemItemsRegistration;
import minechem.fluid.FluidElement;
import minechem.fluid.FluidHelper;
import minechem.gui.CreativeTabMinechem;
import minechem.item.ChemicalRoomStateEnum;
import minechem.item.IDescriptiveName;
import minechem.item.MinechemChemicalType;
import minechem.item.molecule.MoleculeEnum;
import minechem.item.polytool.PolytoolHelper;
import minechem.radiation.RadiationEnum;
import minechem.radiation.RadiationFluidTileEntity;
import minechem.radiation.RadiationInfo;
import minechem.utils.Constants;
import minechem.utils.EnumColour;
import minechem.utils.MinechemUtil;
import minechem.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElementItem extends Item
{

    //private final static ElementEnum[] elements = ElementEnum.elements;
    private final Map<IDescriptiveName, Integer> classificationIndexes = new HashMap<IDescriptiveName, Integer>();

    public ElementItem()
    {
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ELEMENTS);
        setUnlocalizedName("itemElement");
        setRegistryName("itemElement");
        setHasSubtypes(true);
        classificationIndexes.put(ElementClassificationEnum.nonmetal, 0);
        classificationIndexes.put(ElementClassificationEnum.halogen, 1);
        classificationIndexes.put(ElementClassificationEnum.inertGas, 2);
        classificationIndexes.put(ElementClassificationEnum.semimetallic, 3);
        classificationIndexes.put(ElementClassificationEnum.otherMetal, 4);
        classificationIndexes.put(ElementClassificationEnum.alkaliMetal, 5);
        classificationIndexes.put(ElementClassificationEnum.alkalineEarthMetal, 6);
        classificationIndexes.put(ElementClassificationEnum.transitionMetal, 7);
        classificationIndexes.put(ElementClassificationEnum.lanthanide, 8);
        classificationIndexes.put(ElementClassificationEnum.actinide, 9);
        classificationIndexes.put(ChemicalRoomStateEnum.GAS, 1);
        classificationIndexes.put(ChemicalRoomStateEnum.SOLID, 17);
        classificationIndexes.put(ChemicalRoomStateEnum.LIQUID, 33);
    }

    public static String getShortName(ItemStack itemstack)
    {
        int atomicNumber = itemstack.getItemDamage();
        return atomicNumber == 0 ? MinechemUtil.getLocalString("element.empty") : ElementEnum.getByID(atomicNumber).name();
    }

    public static String getLongName(ItemStack itemstack)
    {
        int atomicNumber = itemstack.getItemDamage();
        String longName = atomicNumber == 0 ? MinechemUtil.getLocalString("element.empty") : MinechemUtil.getLocalString(ElementEnum.getByID(atomicNumber).getUnlocalizedName(), true);
        if (longName.contains("Element."))
        {
            ElementEnum element = ElementEnum.getByID(atomicNumber);
            if (element != null)
            {
                longName = element.getLongName();
            }
        }
        return longName;
    }

    public static String getClassification(ItemStack itemstack)
    {
        int atomicNumber = itemstack.getItemDamage();
        return atomicNumber != 0 ? ElementEnum.getByID(atomicNumber).classification().descriptiveName() : MinechemUtil.getLocalString("element.empty");
    }

    public static String getRoomState(ItemStack itemstack)
    {
        int atomicNumber = itemstack.getItemDamage();
        return atomicNumber != 0 ? ElementEnum.getByID(atomicNumber).roomState().descriptiveName() : MinechemUtil.getLocalString("element.empty");
    }

    public static ElementEnum getElement(ItemStack itemstack)
    {
        return itemstack.getItemDamage() != 0 ? ElementEnum.getByID(itemstack.getItemDamage()) : null;
    }

    public static void attackEntityWithRadiationDamage(ItemStack itemstack, int damage, Entity entity)
    {
        entity.attackEntityFrom(DamageSource.generic, damage);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return "minechem.itemElement." + getShortName(itemStack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
        return getLongName(itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool)
    {
        if (itemstack.getItemDamage() == 0)
        {
            return;
        }

        list.add(Constants.TEXT_MODIFIER + "9" + getShortName(itemstack) + " (" + (itemstack.getItemDamage()) + ")");

        RadiationEnum radioactivity = RadiationInfo.getRadioactivity(itemstack);
        String radioactivityColor = radioactivity.getColour();

        String radioactiveName = MinechemUtil.getLocalString("element.property." + radioactivity.name(), true);
        String timeLeft = "";
        if (RadiationInfo.getRadioactivity(itemstack) != RadiationEnum.stable && itemstack.getTagCompound() != null)
        {
            long worldTime = player.worldObj.getTotalWorldTime();
            timeLeft = TimeHelper.getTimeFromTicks(RadiationInfo.getRadioactivity(itemstack).getLife() - (worldTime - itemstack.getTagCompound().getLong("decayStart")));
        }
        list.add(radioactivityColor + radioactiveName + (timeLeft.equals("") ? "" : " (" + timeLeft + ")"));
        list.add(getClassification(itemstack));
        list.add(getRoomState(itemstack));

        if (PolytoolHelper.getTypeFromElement(ElementItem.getElement(itemstack), 1) != null)
        {
            // Polytool Detail
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            {
                String polytoolDesc = PolytoolHelper.getTypeFromElement(ElementItem.getElement(itemstack), 1).getDescription();
                String localizedDesc = I18n.format("polytool.description." + ElementItem.getShortName(itemstack));

                if (!I18n.hasKey("polytool.description." + ElementItem.getShortName(itemstack)))
                {
                    localizedDesc = polytoolDesc;
                }

                list.add(EnumColour.AQUA + localizedDesc);

            } else
            {
                list.add(EnumColour.DARK_GREEN + MinechemUtil.getLocalString("polytool.information"));
            }
        }

    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        for (int i = 1; i <= ElementEnum.heaviestMass; i++)
        {
            if (ElementEnum.getByID(i) == null)
            {
                continue;
            }
            list.add(new ItemStack(item, 1, i));
        }
    }

    public static ItemStack createStackOf(ElementEnum element, int amount)
    {
        return new ItemStack(MinechemItemsRegistration.element, amount, element.atomicNumber());
    }

    public static RadiationInfo getRadiationInfo(ItemStack element, World world)
    {
        RadiationEnum radioactivity = RadiationInfo.getRadioactivity(element);
        if (radioactivity == RadiationEnum.stable)
        {
            return new RadiationInfo(element, radioactivity);
        } else
        {
            NBTTagCompound stackTag = element.getTagCompound();
            if (stackTag == null)
            {
                return initiateRadioactivity(element, world);
            } else
            {
                int dimensionID = stackTag.getInteger("dimensionID");
                long lastUpdate = stackTag.getLong("lastUpdate");
                long decayStart = stackTag.getLong("decayStart");
                RadiationInfo info = new RadiationInfo(element, decayStart, lastUpdate, dimensionID, radioactivity);
                return info;
            }
        }
    }

    public static RadiationInfo initiateRadioactivity(ItemStack element, World world)
    {
        RadiationEnum radioactivity = RadiationInfo.getRadioactivity(element);
        int dimensionID = world.provider.getDimension();
        long lastUpdate = world.getTotalWorldTime();
        RadiationInfo info = new RadiationInfo(element, lastUpdate, lastUpdate, dimensionID, radioactivity);
        RadiationInfo.setRadiationInfo(info, element);
        return info;
    }

    public static RadiationInfo decay(ItemStack element, World world)
    {
        int atomicMass = element.getItemDamage();
        element.setItemDamage(atomicMass - 1);
        return initiateRadioactivity(element, world);
    }


    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        TileEntity te = world.getTileEntity(pos);
        boolean result = !world.isRemote;
        if (te != null && te instanceof IFluidHandler && !player.isSneaking() && !(te instanceof IInventory))
        {
            if (stack.getItemDamage() != 0)
            {
                int filled = 0;
                for (int i = 0; i < 6; i++)
                {
                    FluidElement fluid = FluidHelper.elements.get(getElement(stack));
                    if (fluid == null)
                    {
                        return super.onItemUseFirst(stack, player, world, pos, facing, hitX, hitY, hitZ, hand);
                    }
                    filled = ((IFluidHandler) te).fill(EnumFacing.values()[i], new FluidStack(fluid, 125), false);
                    if (filled > 0)
                    {
                        if (result)
                        {
                            ((IFluidHandler) te).fill(EnumFacing.values()[i], new FluidStack(FluidHelper.elements.get(getElement(stack)), 125), true);
                        }
                        if (!player.capabilities.isCreativeMode)
                        {
                            MinechemUtil.incPlayerInventory(stack, -1, player, new ItemStack(MinechemItemsRegistration.element, 1, 0));
                        }
                        return (result || stack.stackSize <= 0) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
                    }
                }
            } else
            {
                FluidStack drained = null;
                Fluid fluid = MinechemUtil.getFluid((IFluidHandler) te);
                ElementEnum element = MinechemUtil.getElement(fluid);
                if (element != null)
                {
                    for (int i = 0; i < 6; i++)
                    {
                        drained = ((IFluidHandler) te).drain(EnumFacing.values()[i], new FluidStack(fluid, 125), false);
                        if (drained != null && drained.amount > 0)
                        {
                            if (result)
                            {
                                ((IFluidHandler) te).drain(EnumFacing.values()[i], new FluidStack(fluid, 125), true);
                            }
                            if (!player.capabilities.isCreativeMode)
                            {
                                MinechemUtil.incPlayerInventory(stack, -1, player, new ItemStack(MinechemItemsRegistration.element, 1, element.atomicNumber()));
                            }
                            return result ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
                        }
                    }
                } else
                {
                    MoleculeEnum molecule = MinechemUtil.getMolecule(fluid);
                    if (fluid == FluidRegistry.WATER)
                    {
                        molecule = MoleculeEnum.water;
                    }
                    if (molecule != null)
                    {
                        for (int i = 0; i < 6; i++)
                        {
                            drained = ((IFluidHandler) te).drain(EnumFacing.values()[i], new FluidStack(fluid, 125), false);
                            if (drained != null && drained.amount > 0)
                            {
                                if (result)
                                {
                                    ((IFluidHandler) te).drain(EnumFacing.values()[i], new FluidStack(fluid, 125), true);
                                }
                                if (!player.capabilities.isCreativeMode)
                                {
                                    MinechemUtil.incPlayerInventory(stack, -1, player, new ItemStack(MinechemItemsRegistration.molecule, 1, molecule.id()));
                                }
                                return result ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
                            }
                        }
                    }
                }
            }
            return result ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
        }
        return super.onItemUseFirst(stack, player, world, pos, facing, hitX, hitY, hitZ, hand);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
    {
        boolean flag = itemStack.getItemDamage() == 0;
        RayTraceResult rayTrace = this.rayTrace(world, player, flag);
        if (rayTrace == null)
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
        }

        if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            int blockX = rayTrace.getBlockPos().getX();
            int blockY = rayTrace.getBlockPos().getY();
            int blockZ = rayTrace.getBlockPos().getZ();

            Block block = world.getBlockState(rayTrace.getBlockPos()).getBlock();

            if (flag)
            {
                MinechemChemicalType chemical = MinechemUtil.getChemical(block);
                if (chemical != null && MinechemUtil.canDrain(world, block, blockX, blockY, blockZ))
                {
                    ItemStack stack = MinechemUtil.createItemStack(chemical, 1);

                    if (stack != null)
                    {
                        stack.stackSize = 8;
                        TileEntity tile = world.getTileEntity(rayTrace.getBlockPos());
                        if (tile instanceof RadiationFluidTileEntity && ((RadiationFluidTileEntity) tile).info != null)
                        {
                            RadiationInfo.setRadiationInfo(((RadiationFluidTileEntity) tile).info, stack);
                        }

                        world.setBlockToAir(rayTrace.getBlockPos());
                        world.removeTileEntity(rayTrace.getBlockPos());

                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, fillTube(itemStack, player, stack));
                    }
                }
            } else
            {
                EnumFacing dir = rayTrace.sideHit;
                blockX += dir.getFrontOffsetX();
                blockY += dir.getFrontOffsetY();
                blockZ += dir.getFrontOffsetZ();
                if (!player.canPlayerEdit(rayTrace.getBlockPos(), rayTrace.sideHit, itemStack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
                }

                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, emptyTube(itemStack, player, world, blockX, blockY, blockZ));
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
    }

    private ItemStack fillTube(ItemStack itemStack, EntityPlayer player, ItemStack block)
    {
        if (player.capabilities.isCreativeMode)
        {
            return itemStack;
        } else
        {
            MinechemUtil.incPlayerInventory(itemStack, -8, player, block);
        }
        return itemStack;
    }

    private ItemStack emptyTube(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);

        if (!world.isAirBlock(pos) && !world.getBlockState(pos).getMaterial().isSolid())
        {
            IBlockState state = world.getBlockState(pos);

            state.getBlock().harvestBlock(world, player, pos, state, null, null);
            state.getBlock().breakBlock(world, pos, state);
            world.setBlockToAir(pos);
        }

        if (world.isAirBlock(pos))
        {
            RadiationInfo radioactivity = getRadiationInfo(itemStack, world);
            long worldtime = world.getTotalWorldTime();
            long leftTime = radioactivity.radioactivity.getLife() - (worldtime - radioactivity.decayStarted);
            Fluid fluid = FluidHelper.elements.get(getElement(itemStack));
            if (fluid == null)
            {
                return itemStack;
            }
            if (!player.capabilities.isCreativeMode)
            {
                if (itemStack.stackSize >= 8)
                {
                    itemStack.stackSize -= 8;
                } else
                {
                    int needs = 8 - itemStack.stackSize;
                    Set<ItemStack> otherItemsStacks = MinechemUtil.findItemStacks(player.inventory, itemStack.getItem(), itemStack.getItemDamage());
                    otherItemsStacks.remove(itemStack);
                    int free = 0;
                    Iterator<ItemStack> it2 = otherItemsStacks.iterator();
                    while (it2.hasNext())
                    {
                        ItemStack stack = it2.next();
                        free += stack.stackSize;
                    }
                    if (free < needs)
                    {
                        return itemStack;
                    }
                    itemStack.stackSize = 0;

                    Iterator<ItemStack> it = otherItemsStacks.iterator();
                    while (it.hasNext())
                    {
                        ItemStack stack = it.next();
                        RadiationInfo anotherRadiation = getRadiationInfo(stack, world);
                        long anotherLeft = anotherRadiation.radioactivity.getLife() - (worldtime - anotherRadiation.decayStarted);
                        if (anotherLeft < leftTime)
                        {
                            radioactivity = anotherRadiation;
                            leftTime = anotherLeft;
                        }

                        if (stack.stackSize >= needs)
                        {
                            stack.stackSize -= needs;
                            needs = 0;
                        } else
                        {
                            needs -= stack.stackSize;
                            stack.stackSize = 0;
                        }

                        if (stack.stackSize <= 0)
                        {
                            MinechemUtil.removeStackInInventory(player.inventory, stack);
                        }

                        if (needs == 0)
                        {
                            break;
                        }
                    }
                }
                ItemStack empties = MinechemUtil.addItemToInventory(player.inventory, new ItemStack(MinechemItemsRegistration.element, 8, 0));
                MinechemUtil.throwItemStack(world, empties, x, y, z);
            }

            Block block = FluidHelper.elementsBlocks.get(fluid);
            world.setBlockState(pos, block.getStateFromMeta(0), 3);
            TileEntity tile = world.getTileEntity(pos);
            if (radioactivity.isRadioactive() && tile instanceof RadiationFluidTileEntity)
            {
                ((RadiationFluidTileEntity) tile).info = radioactivity;
            }
        }
        return itemStack;
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

package minechem.item.molecule;

import minechem.MinechemItemsRegistration;
import minechem.Settings;
import minechem.fluid.FluidHelper;
import minechem.fluid.FluidMolecule;
import minechem.gui.CreativeTabMinechem;
import minechem.item.element.ElementItem;
import minechem.item.polytool.PolytoolHelper;
import minechem.potion.PharmacologyEffect;
import minechem.potion.PharmacologyEffectRegistry;
import minechem.radiation.RadiationEnum;
import minechem.radiation.RadiationFluidTileEntity;
import minechem.radiation.RadiationInfo;
import minechem.utils.EnumColour;
import minechem.utils.MinechemUtil;
import minechem.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MoleculeItem extends Item
{

    public MoleculeItem()
    {
        setCreativeTab(CreativeTabMinechem.CREATIVE_TAB_ELEMENTS);
        setHasSubtypes(true);
        setUnlocalizedName("itemMolecule");
        setRegistryName("itemMolecule");
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
        return MinechemUtil.getLocalString(getMolecule(itemStack).getUnlocalizedName(), true);
    }


    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return getUnlocalizedName() + "." + getMolecule(par1ItemStack).name();
    }

    public String getFormulaWithSubscript(ItemStack itemstack)
    {
        String formula = getMolecule(itemstack).getFormula();
        return MinechemUtil.subscriptNumbers(formula);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool)
    {
        list.add("\u00A79" + getFormulaWithSubscript(itemstack));

        RadiationEnum radioactivity = RadiationInfo.getRadioactivity(itemstack);
        String radioactivityColor = radioactivity.getColour();

        String radioactiveName = MinechemUtil.getLocalString("element.property." + radioactivity.name(), true);
        String timeLeft = "";
        if (RadiationInfo.getRadioactivity(itemstack) != RadiationEnum.stable && itemstack.getTagCompound() != null)
        {
            long worldTime = player.world.getTotalWorldTime();
            timeLeft = TimeHelper.getTimeFromTicks(RadiationInfo.getRadioactivity(itemstack).getLife() - (worldTime - itemstack.getTagCompound().getLong("decayStart")));
        }
        list.add(radioactivityColor + radioactiveName + (timeLeft.equals("") ? "" : " (" + timeLeft + ")"));
        list.add(getRoomState(itemstack));
        MoleculeEnum molecule = MoleculeEnum.getById(itemstack.getItemDamage());
        if (PharmacologyEffectRegistry.hasEffect(molecule) && Settings.displayMoleculeEffects)
        {

            if (PolytoolHelper.getTypeFromElement(ElementItem.getElement(itemstack), 1) != null)
            {
                // Polytool Detail
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
                {
                    for (PharmacologyEffect effect : PharmacologyEffectRegistry.getEffects(molecule))
                    {
                        list.add(effect.getColour() + effect.toString());
                    }

                } else
                {
                    list.add(EnumColour.DARK_GREEN + MinechemUtil.getLocalString("effect.information", true));
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (MoleculeEnum molecule : MoleculeEnum.molecules.values())
        {
            if (molecule != null)
            {
                par3List.add(new ItemStack(item, 1, molecule.id()));
            }
        }
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        TileEntity te = world.getTileEntity(pos);
        boolean result = !world.isRemote;
        if (te != null && te instanceof IFluidHandler && !player.isSneaking() && !(te instanceof IInventory))
        {
            int filled = 0;
            for (int i = 0; i < 6; i++)
            {
                FluidStack fluidStack = new FluidStack(FluidRegistry.WATER, 125);
                if (getMolecule(stack) != MoleculeEnum.water)
                {
                    FluidMolecule fluid = FluidHelper.molecules.get(getMolecule(stack));
                    if (fluid == null)
                    {
                        return super.onItemUseFirst(stack, player, world, pos, facing, hitX, hitY, hitZ, hand);
                    }
                    fluidStack = new FluidStack(fluid, 125);

                }
                filled = ((IFluidHandler) te).fill(EnumFacing.values()[i], fluidStack, false);
                if (filled > 0)
                {
                    if (result)
                    {
                        ((IFluidHandler) te).fill(EnumFacing.values()[i], fluidStack, true);
                    }
                    if (!player.capabilities.isCreativeMode)
                    {
                        MinechemUtil.incPlayerInventory(stack, -1, player, new ItemStack(MinechemItemsRegistration.element, 1, 0));
                    }
                    return (result || stack.stackSize <= 0) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
                }
            }
            return result ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
        }
        return super.onItemUseFirst(stack, player, world, pos, facing, hitX, hitY, hitZ, hand);
    }

    public static MoleculeEnum getMolecule(ItemStack itemstack)
    {
        int itemDamage = itemstack.getItemDamage();
        MoleculeEnum mol = MoleculeEnum.getById(itemDamage);
        if (mol == null)
        {
            itemstack.setItemDamage(0);
            mol = MoleculeEnum.getById(0);
        }
        return mol;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.DRINK;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 16;
    }


    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase entity)
    {
        if(!(entity instanceof EntityPlayer))
            return itemStack;

        EntityPlayer entityPlayer = (EntityPlayer) entity;

        if (!entityPlayer.capabilities.isCreativeMode)
        {
            --itemStack.stackSize;
        }

        if (world.isRemote)

        {
            return itemStack;
        }

        MoleculeEnum molecule = getMolecule(itemStack);
        PharmacologyEffectRegistry.applyEffect(molecule, entityPlayer);
        world.playSound(entityPlayer, entityPlayer.getPosition(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F); // Thanks mDiyo!
        return itemStack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
    {
        player.setActiveHand(hand);

        RayTraceResult rayTrace = this.rayTrace(world, player, false);
        if (rayTrace == null || player.isSneaking())
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
        }

        if (rayTrace.typeOfHit == RayTraceResult.Type.BLOCK
        		&& Settings.vialPlacing)
        {
            int blockX = rayTrace.getBlockPos().getX();
            int blockY = rayTrace.getBlockPos().getY();
            int blockZ = rayTrace.getBlockPos().getZ();

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

        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
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
            RadiationInfo radioactivity = ElementItem.getRadiationInfo(itemStack, world);
            long worldtime = world.getTotalWorldTime();
            long leftTime = radioactivity.radioactivity.getLife() - (worldtime - radioactivity.decayStarted);
            MoleculeEnum molecule = getMolecule(itemStack);
            Fluid fluid = FluidHelper.molecules.get(molecule);
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
                        RadiationInfo anotherRadiation = ElementItem.getRadiationInfo(stack, world);
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

            Block block = Blocks.FLOWING_WATER;
            if (getMolecule(itemStack) != MoleculeEnum.water)
            {
                block = FluidHelper.moleculeBlocks.get(fluid);
            }
            world.setBlockState(pos, block.getStateFromMeta(0), 3);
            TileEntity tile = world.getTileEntity(pos);
            if (radioactivity.isRadioactive() && tile instanceof RadiationFluidTileEntity)
            {
                ((RadiationFluidTileEntity) tile).info = radioactivity;
            }
        }
        return itemStack;
    }

    public static String getRoomState(ItemStack itemstack)
    {
        int id = itemstack.getItemDamage();
        return (MoleculeEnum.molecules.get(id) == null) ? "null" : MoleculeEnum.molecules.get(id).roomState().descriptiveName();
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

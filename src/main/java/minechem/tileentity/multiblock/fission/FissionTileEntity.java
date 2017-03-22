package minechem.tileentity.multiblock.fission;

import minechem.MinechemItemsRegistration;
import minechem.Settings;
import minechem.item.blueprint.BlueprintFission;
import minechem.item.element.ElementEnum;
import minechem.item.element.ElementItem;
import minechem.network.MessageHandler;
import minechem.network.message.FissionUpdateMessage;
import minechem.tileentity.multiblock.MultiBlockTileEntity;
import minechem.utils.MinechemUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class FissionTileEntity extends MultiBlockTileEntity implements ISidedInventory
{

    public static int[] kInput =
    {
        0
    };
    public static int[] kOutput =
    {
        2
    };

    public static int kStartInput = 0;

    public FissionTileEntity()
    {
        super(Settings.maxFissionStorage);
        inventory = new ItemStack[getSizeInventory()];
        setBlueprint(new BlueprintFission());
    }

    @Override
    public void update()
    {
        super.update();
        if (!completeStructure)
        {
            return;
        }
        if (!world.isRemote)
        {
            if (inventory[kStartInput] != null)
            {
                if (inputIsFissionable())
                {
                    if (useEnergy(getEnergyNeeded()))
                    {
                        ItemStack fissionResult = getFissionOutput();
                        addToOutput(fissionResult);
                        removeInputs();
                    }
                }
            }
            FissionUpdateMessage message = new FissionUpdateMessage(this);
            MessageHandler.INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), Settings.UpdateRadius));
        }
    }

    public boolean inputIsFissionable()
    {
        ItemStack fissionResult = getFissionOutput();
        if (fissionResult != null)
        {
            if (inventory[kOutput[0]] == null)
            {
                return true;
            }
            boolean sameItem = fissionResult.getItem() == inventory[kOutput[0]].getItem() && fissionResult.getItemDamage() == inventory[kOutput[0]].getItemDamage();
            return inventory[kOutput[0]].stackSize < 64 && sameItem;
        }
        return false;
    }

    private void addToOutput(ItemStack fusionResult)
    {
        if (fusionResult == null)
        {
            return;
        }

        if (inventory[kOutput[0]] == null)
        {
            ItemStack output = fusionResult.copy();
            inventory[kOutput[0]] = output;
        } else
        {
            inventory[kOutput[0]].stackSize += 2;
        }
    }

    private void removeInputs()
    {
        decrStackSize(kInput[0], 1);
    }

    private ItemStack getFissionOutput()
    {
        if (inventory[kInput[0]] != null && inventory[kInput[0]].getItem() instanceof ElementItem && inventory[kInput[0]].getItemDamage() > 0)
        {
            int mass = ElementItem.getElement(inventory[kInput[0]]).atomicNumber();
            int newMass = mass / 2;
            if (newMass > 0 && ElementEnum.getByID(newMass)!=null)
            {
                return new ItemStack(MinechemItemsRegistration.element, 2, newMass);
            } else
            {
                return null;
            }
        } else
        {
            return null;
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 3;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int i) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {

        this.inventory[slot] = itemstack;

    }

    @Override
    public String getName()
    {
        return "container.minechemFission";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer)
    {
        return completeStructure;

    }

    @Override
    public void openInventory(EntityPlayer entityPlayer)
    {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer)
    {

    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        NBTTagList inventoryTagList = MinechemUtil.writeItemStackArrayToTagList(inventory);
        nbtTagCompound.setTag("inventory", inventoryTagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        inventory = new ItemStack[getSizeInventory()];
        MinechemUtil.readTagListToItemStackArray(nbtTagCompound.getTagList("inventory", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND), inventory);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        if (slot != 2 && itemstack.getItem() instanceof ElementItem)
        {
            if (slot == 1 && itemstack.getItemDamage() == 91)
            {
                return true;
            }
            if (slot == 0)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing enumFacing) {
        switch (enumFacing)
        {
            case DOWN:
                return FissionTileEntity.kOutput;
            default:
                return FissionTileEntity.kInput;
        }
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, EnumFacing facing)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, EnumFacing facing)
    {
        return false;
    }

    @Override
    public int getEnergyNeeded()
    {
        if (inventory[0] != null)
        {
            return (inventory[0].getItemDamage()) * Settings.fissionMultiplier;
        }
        return 0;
    }

    @Override
    public int receiveEnergy(EnumFacing enumFacing, int i, boolean b) {
        return 0;
    }

    @Override
    public int getEnergyStored(EnumFacing enumFacing) {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing enumFacing) {
        return 0;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing enumFacing) {
        return false;
    }
}

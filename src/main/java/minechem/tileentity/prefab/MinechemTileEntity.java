package minechem.tileentity.prefab;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

import javax.annotation.Nullable;

public abstract class MinechemTileEntity extends MinechemTileEntityBase implements IInventory
{
    public ItemStack[] inventory;

    public MinechemTileEntity()
    {

    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new SPacketUpdateTileEntity(pos, 0, tagCompound);
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        return super.writeToNBT(nbt);
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return this.inventory[var1];
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        if (this.inventory[slot] != null)
        {
            ItemStack itemstack;
            if (this.inventory[slot].stackSize <= amount)
            {

                itemstack = this.inventory[slot];
                this.inventory[slot] = null;
                return itemstack;
            } else
            {
                itemstack = this.inventory[slot].splitStack(amount);
                if (this.inventory[slot].stackSize == 0)
                {
                    this.inventory[slot] = null;
                }
                return itemstack;
            }
        } else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        if (this.inventory[slot] != null)
        {
            ItemStack itemstack = this.inventory[slot];
            this.inventory[slot] = null;
            return itemstack;
        } else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        this.inventory[slot] = itemstack;
    }
}

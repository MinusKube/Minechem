package minechem.tileentity.prefab;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BoundedInventory implements IInventory
{

    private final IInventory _inv;
    private final int[] _slots;

    public BoundedInventory(IInventory inv, int[] slots)
    {
        if (inv == null)
        {
            throw new IllegalArgumentException("inv: must not be null");
        }
        if (slots == null)
        {
            throw new IllegalArgumentException("slots: must not be null");
        }
        for (int i = 0; i < slots.length; i++)
        {
            if (i < 0 || i >= inv.getSizeInventory())
            {
                throw new IllegalArgumentException("slot: out of bounds");
            }
        }

        _inv = inv;
        _slots = slots;
    }

    public ItemStack[] copyInventoryToArray()
    {
        ItemStack[] itemstacks = new ItemStack[getSizeInventory()];
        for (int slot = 0; slot < getSizeInventory(); slot++)
        {
            ItemStack itemstack = getStackInSlot(slot);
            if (itemstack != null)
            {
                itemstacks[slot] = itemstack.copy();
            } else
            {
                itemstacks[slot] = null;
            }
        }
        return itemstacks;
    }

    public List<ItemStack> copyInventoryToList()
    {
        List<ItemStack> itemstacks = new ArrayList<ItemStack>();
        for (int slot = 0; slot < getSizeInventory(); slot++)
        {
            if (getStackInSlot(slot) != null)
            {
                itemstacks.add(getStackInSlot(slot).copy());
            }
        }
        return itemstacks;
    }

    public void setInventoryStacks(ItemStack[] itemstacks)
    {
        for (int slot = 0; slot < itemstacks.length; slot++)
        {
            setInventorySlotContents(slot, itemstacks[slot]);
        }
    }

    @Override
    public int getSizeInventory()
    {
        return _slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return _inv.getStackInSlot(_slots[slot]);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        return _inv.decrStackSize(_slots[slot], amount);
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int i) {
        return _inv.removeStackFromSlot(_slots[i]);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        _inv.setInventorySlotContents(_slots[slot], stack);
    }

    @Override
    public String getName()
    {
        return _inv.getName();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return _inv.getInventoryStackLimit();
    }

    @Override
    public void markDirty()
    {
        _inv.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return _inv.isUseableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        _inv.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        _inv.closeInventory(player);
    }

    @Override
    public boolean hasCustomName()
    {
        return _inv.hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return _inv.getDisplayName();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return _inv.isItemValidForSlot(_slots[i], itemstack);
    }

    @Override
    public int getField(int i) {
        return _inv.getField(i);
    }

    @Override
    public void setField(int i, int i1) {
        _inv.setField(i, i1);
    }

    @Override
    public int getFieldCount() {
        return _inv.getFieldCount();
    }

    @Override
    public void clear() {
        _inv.clear();
    }

}

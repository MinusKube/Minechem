package minechem.item.chemistjournal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChemistJournalContainer extends Container
{

    public ChemistJournalGui gui;

    public ChemistJournalContainer(InventoryPlayer inventoryPlayer)
    {

    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        return null;
    }

    @Override
    public ItemStack slotClick(int par1, int par2, ClickType par3, EntityPlayer par4EntityPlayer)
    {
        return null;
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack)
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack)
    {
    }

    @Override
    public Slot getSlotFromInventory(IInventory iInventory, int slot)
    {
        Slot aSlot = new Slot(iInventory, slot, 0, 0);
        aSlot.slotNumber = 0;
        return aSlot;
    }

}

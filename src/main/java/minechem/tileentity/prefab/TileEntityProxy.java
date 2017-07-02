package minechem.tileentity.prefab;

import minechem.MinechemBlocksGeneration;
import minechem.Settings;
import minechem.tileentity.multiblock.fission.FissionTileEntity;
import minechem.tileentity.multiblock.fusion.FusionTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityProxy extends MinechemTileEntityElectric implements ISidedInventory
{

    public TileEntityProxy()
    {
        super(Settings.energyPacketSize);
    }

    public TileEntity manager;
    int managerXOffset;
    int managerYOffset;
    int managerZOffset;

    @Override
    public void update()
    {
        if (this.manager != null)
        {
            int ammountReceived = ((MinechemTileEntityElectric) manager).receiveEnergy(this.getEnergyStored(), true);
            if (ammountReceived > 0)
            {
                ((MinechemTileEntityElectric) manager).receiveEnergy(ammountReceived, false);
                this.useEnergy(ammountReceived);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        if (manager != null)
        {
            nbtTagCompound.setInteger("managerXOffset", manager.getPos().getX());
            nbtTagCompound.setInteger("managerYOffset", manager.getPos().getY());
            nbtTagCompound.setInteger("managerZOffset", manager.getPos().getZ());
        }

        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        managerXOffset = nbtTagCompound.getInteger("managerXOffset");
        managerYOffset = nbtTagCompound.getInteger("managerYOffset");
        managerZOffset = nbtTagCompound.getInteger("managerZOffset");
        if (worldObj != null)
        {
            manager = worldObj.getTileEntity(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset));
        }

    }

    public void setManager(TileEntity managerTileEntity)
    {

        this.manager = managerTileEntity;
        if (managerTileEntity != null)
        {
            this.managerXOffset = managerTileEntity.getPos().getX() - pos.getX();
            this.managerYOffset = managerTileEntity.getPos().getY() - pos.getY();
            this.managerZOffset = managerTileEntity.getPos().getZ() - pos.getZ();
        }
    }

    public TileEntity getManager()
    {
        // Return the next block in sequence but never the TileEntityProxy.
        if (worldObj.getTileEntity(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset)) != null
                && !(worldObj.getTileEntity(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset)) instanceof TileEntityProxy))
        {
            return worldObj.getTileEntity(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset));
        }

        // Return the entire fusion generator as a whole (indicating the structure is complete).
        if (worldObj.getBlockState(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset)) == MinechemBlocksGeneration.fusion)
        {
            this.manager = buildManagerBlock();
            return this.manager;
        }

        return null;

    }

    private TileEntity buildManagerBlock()
    {
        IBlockState state = this.worldObj.getBlockState(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset));

        if (state.getBlock().getMetaFromState(state) == 2)
        {
            FusionTileEntity fusion = new FusionTileEntity();
            fusion.setWorldObj(this.worldObj);
            fusion.setPos(new BlockPos(this.managerXOffset + pos.getX(),
                    this.managerYOffset + pos.getY(),
                    this.managerZOffset + pos.getZ()));

            fusion.setBlockType(MinechemBlocksGeneration.fusion);
            worldObj.setTileEntity(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset), fusion);
        }

        state = this.worldObj.getBlockState(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset));
        if (state.getBlock().getMetaFromState(state) == 3)
        {
            FissionTileEntity fission = new FissionTileEntity();
            fission.setWorldObj(this.worldObj);
            fission.setPos(new BlockPos(this.managerXOffset + pos.getX(),
                    this.managerYOffset + pos.getY(),
                    this.managerZOffset + pos.getZ()));
            fission.setBlockType(MinechemBlocksGeneration.fusion);
            worldObj.setTileEntity(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset), fission);
        }
        return worldObj.getTileEntity(new BlockPos(pos.getX() + managerXOffset, pos.getY() + managerYOffset, pos.getZ() + managerZOffset));

    }

    @Override
    public int getSizeInventory()
    {
        if (this.manager != null && this.manager != this)
        {
            return ((ISidedInventory) this.manager).getSizeInventory();
        }
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        if (this.getManager() != null && this.getManager() instanceof ISidedInventory)
        {
            return ((ISidedInventory) this.getManager()).getStackInSlot(i);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.getManager() != null && this.getManager() instanceof ISidedInventory)
        {
            return ((ISidedInventory) this.getManager()).decrStackSize(i, j);
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int i)
    {
        if (this.getManager() != null && this.getManager() instanceof ISidedInventory)
        {
            return ((ISidedInventory) this.getManager()).removeStackFromSlot(i);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        if (this.getManager() != null && this.getManager() instanceof ISidedInventory)
        {
            ((ISidedInventory) this.getManager()).setInventorySlotContents(i, itemstack);
        }
    }

    @Override
    public String getName()
    {
        return "Multiblock Minechem proxy";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        if (this.manager != null && this.manager != this)
        {
            return ((ISidedInventory) this.getManager()).getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        if (this.manager != null && this.manager != this)
        {
            return ((ISidedInventory) this.getManager()).isItemValidForSlot(i, itemstack);
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
        if (this.manager != null && this.manager != this)
        {
            return ((ISidedInventory) this.getManager()).getSlotsForFace(enumFacing);
        }
        return new int[0];
    }


    @Override
    public boolean canInsertItem(int slot, ItemStack itemstack, EnumFacing facing)
    {
        // Cannot insert items into reactor with automation disabled.
        return Settings.AllowAutomation && isItemValidForSlot(slot, itemstack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing facing)
    {
        // Cannot extract items from reactor with automation disabled.
        // Can only extract from the bottom.
        return Settings.AllowAutomation && facing.getIndex() == 0 && slot == 2;
    }

    @Override
    public int getEnergyNeeded()
    {
        return 0;
    }
}

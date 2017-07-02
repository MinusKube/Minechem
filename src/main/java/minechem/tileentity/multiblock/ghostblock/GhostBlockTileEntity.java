package minechem.tileentity.multiblock.ghostblock;

import minechem.Settings;
import minechem.item.blueprint.BlueprintBlock;
import minechem.item.blueprint.MinechemBlueprint;
import minechem.network.MessageHandler;
import minechem.network.message.GhostBlockMessage;
import minechem.tileentity.prefab.MinechemTileEntity;
import minechem.utils.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class GhostBlockTileEntity extends MinechemTileEntity
{
    private MinechemBlueprint blueprint;
    private int blockID;

    public void setBlueprintAndID(MinechemBlueprint blueprint, int blockID)
    {
        setBlueprint(blueprint);
        setBlockID(blockID);

        BlueprintBlock bp = blueprint.getBlockLookup().get(this.blockID);

        this.worldObj.setBlockState(pos, bp.block.getStateFromMeta(bp.metadata), 3);
        if (worldObj != null && !worldObj.isRemote)
        {
            GhostBlockMessage message = new GhostBlockMessage(this);
            MessageHandler.INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), Settings.UpdateRadius));
        }
    }

    public void setBlueprint(MinechemBlueprint blueprint)
    {
        this.blueprint = blueprint;
    }

    public MinechemBlueprint getBlueprint()
    {
        return this.blueprint;
    }

    public void setBlockID(int blockID)
    {
        this.blockID = blockID;
    }

    public int getBlockID()
    {
        return this.blockID;
    }

    public ItemStack getBlockAsItemStack()
    {
        try
        {
            BlueprintBlock blueprintBlock = this.blueprint.getBlockLookup().get(this.blockID);
            if (blueprintBlock != null)
            {
                return new ItemStack(blueprintBlock.block, 1, blueprintBlock.metadata);
            }
        } catch (Exception e)
        {
            // this code has now failed
            // it cannot be recovered
            // snowflake on hot iron
            LogHelper.debug("Block generated an exception at: " + pos.toString());
        }
        return null;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        if (blueprint != null)
        {
            nbtTagCompound.setInteger("blueprintID", blueprint.id);
        }

        nbtTagCompound.setInteger("blockID", blockID);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.blockID = nbtTagCompound.getInteger("blockID");
        int blueprintID = nbtTagCompound.getInteger("blueprintID");
        this.blueprint = MinechemBlueprint.blueprints.get(blueprintID);
    }

    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int i) {
        return null;
    }

    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
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
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer entityplayer)
    {

    }

    @Override
    public void closeInventory(EntityPlayer entityplayer)
    {

    }
}

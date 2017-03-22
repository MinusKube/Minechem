package minechem.tileentity.prefab;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class MinechemTileEntityBase extends TileEntity implements ITickable
{
    protected long ticks = 0;

    public void setBlockType(Block block) {
        this.blockType = block;
    }

    /**
     * Called on the TileEntity's first tick.
     */
    //TODO: Remove this once its reason for being is found
    //public void initiate()
    //{
    //}
    @Override
    public void update()
    {
        //if (this.ticks == 0)
        //{
        //this.initiate();
        //}

        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 1;
        }

        this.ticks++;
    }
}

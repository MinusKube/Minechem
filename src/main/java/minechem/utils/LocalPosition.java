package minechem.utils;

import net.minecraft.util.EnumFacing;

public class LocalPosition extends Position
{

    DirectionMultiplier multiplier;

    public class Pos3
    {
        public int x;
        public int y;
        public int z;
    }

    public LocalPosition(double x, double y, double z, EnumFacing orientation)
    {
        super(x, y, z, orientation);
        this.multiplier = DirectionMultiplier.map.get(orientation);
    }

    public int getLocalX(int x)
    {
        return (int) this.x + (x * this.multiplier.xMultiplier);
    }

    public int getLocalY(int y)
    {
        return (int) this.y + (y * this.multiplier.yMultiplier);
    }

    public int getLocalZ(int z)
    {
        return (int) this.z + (z * this.multiplier.zMultiplier);
    }

    public Pos3 getLocalPos(int x, int y, int z)
    {
        Pos3 pos = new Pos3();
        pos.y = getLocalY(y);
        if (this.orientation == EnumFacing.SOUTH || this.orientation == EnumFacing.NORTH)
        {
            pos.x = getLocalX(z);
            pos.z = getLocalZ(x);
        } else
        {
            pos.x = getLocalX(x);
            pos.z = getLocalZ(z);
        }
        return pos;
    }

}

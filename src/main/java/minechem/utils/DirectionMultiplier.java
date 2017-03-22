package minechem.utils;

import java.util.HashMap;

import net.minecraft.util.EnumFacing;

public enum DirectionMultiplier
{
    NORTH(1, 1, 1), EAST(-1, 1, 1), SOUTH(-1, 1, -1), WEST(1, 1, -1), UP(1, 1, 1), DOWN(1, -1, 1),;

    public static HashMap<EnumFacing, DirectionMultiplier> map = new HashMap<EnumFacing, DirectionMultiplier>()
    {
        private static final long serialVersionUID = 1L;


        {
            put(EnumFacing.NORTH, NORTH);
            put(EnumFacing.EAST, EAST);
            put(EnumFacing.SOUTH, SOUTH);
            put(EnumFacing.WEST, WEST);
        }
    };
    public int xMultiplier;
    public int yMultiplier;
    public int zMultiplier;

    private DirectionMultiplier(int x, int y, int z)
    {
        this.xMultiplier = x;
        this.yMultiplier = y;
        this.zMultiplier = z;
    }
}

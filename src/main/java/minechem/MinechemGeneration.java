package minechem;

import java.util.Random;
import minechem.utils.LogHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class MinechemGeneration implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (Settings.generateOre) {
            if (world.provider.isSurfaceWorld()) {
                for (int k = 0; k <= Settings.UraniumOreDensity; k++) {
                    int firstBlockXCoord = (16 * chunkX) + random.nextInt(16);
                    int firstBlockYCoord = random.nextInt(50);
                    int firstBlockZCoord = (16 * chunkZ) + random.nextInt(16);
                    int oreCount = random.nextInt(Settings.UraniumOreClusterSize + 10);

                    (new WorldGenMinable(MinechemBlocksGeneration.uranium.getBlockState().getBaseState(), oreCount)).generate(world, random, new BlockPos(firstBlockXCoord, firstBlockYCoord, firstBlockZCoord));
                    LogHelper.debug("Minechem generated Uranium generated at:");
                    LogHelper.debug("X :" + firstBlockXCoord);
                    LogHelper.debug("Y :" + firstBlockYCoord);
                    LogHelper.debug("Z :" + firstBlockZCoord);
                }
            }
        }
    }
}

package minechem;

import minechem.block.BlockUraniumOre;
import minechem.radiation.RadiationFluidTileEntity;
import minechem.tileentity.blueprintprojector.BlueprintProjectorBlock;
import minechem.tileentity.blueprintprojector.BlueprintProjectorTileEntity;
import minechem.tileentity.decomposer.DecomposerBlock;
import minechem.tileentity.decomposer.DecomposerTileEntity;
import minechem.tileentity.leadedchest.LeadedChestBlock;
import minechem.tileentity.leadedchest.LeadedChestTileEntity;
import minechem.tileentity.microscope.MicroscopeBlock;
import minechem.tileentity.microscope.MicroscopeTileEntity;
import minechem.tileentity.multiblock.fission.FissionTileEntity;
import minechem.tileentity.multiblock.fusion.FusionBlock;
import minechem.tileentity.multiblock.fusion.FusionItemBlock;
import minechem.tileentity.multiblock.fusion.FusionTileEntity;
import minechem.tileentity.multiblock.ghostblock.GhostBlock;
import minechem.tileentity.multiblock.ghostblock.GhostBlockItem;
import minechem.tileentity.multiblock.ghostblock.GhostBlockTileEntity;
import minechem.tileentity.prefab.TileEntityProxy;
import minechem.tileentity.synthesis.SynthesisBlock;
import minechem.tileentity.synthesis.SynthesisTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MinechemBlocksGeneration
{
    public static Block decomposer;
    public static Block microscope;
    public static Block synthesis;
    public static Block ghostBlock;
    public static Block blueprintProjector;
    public static Block fusion;
    public static Block printer;
    public static Block leadChest;

    public static Block uranium;
    public static Material materialGhost = new MaterialTransparent(MapColor.AIR);

    public static void registerBlocks()
    {

        // Decomposer
        decomposer = new DecomposerBlock();
        GameRegistry.register(decomposer);
        GameRegistry.register(new ItemBlock(decomposer).setRegistryName(decomposer.getRegistryName()));
        GameRegistry.registerTileEntity(DecomposerTileEntity.class, decomposer.getUnlocalizedName());

        // Microscope.
        microscope = new MicroscopeBlock();
        GameRegistry.register(microscope);
        GameRegistry.register(new ItemBlock(microscope).setRegistryName(microscope.getRegistryName()));
        GameRegistry.registerTileEntity(MicroscopeTileEntity.class, microscope.getUnlocalizedName());

        // Chemical Synthesis Machine.
        synthesis = new SynthesisBlock();
        GameRegistry.register(synthesis);
        GameRegistry.register(new ItemBlock(synthesis).setRegistryName(synthesis.getRegistryName()));
        GameRegistry.registerTileEntity(SynthesisTileEntity.class, synthesis.getUnlocalizedName());

        // Fusion Reactor.
        fusion = new FusionBlock();
        GameRegistry.register(fusion);
        GameRegistry.register(new FusionItemBlock(fusion).setRegistryName(fusion.getRegistryName()));
        GameRegistry.registerTileEntity(FusionTileEntity.class, fusion.getUnlocalizedName());

        // Ghost Block.
        ghostBlock = new GhostBlock();
        GameRegistry.register(ghostBlock);
        GameRegistry.register(new GhostBlockItem(ghostBlock).setRegistryName(ghostBlock.getRegistryName()));
        GameRegistry.registerTileEntity(GhostBlockTileEntity.class, ghostBlock.getUnlocalizedName());

        // Blueprint Projector.
        blueprintProjector = new BlueprintProjectorBlock();
        GameRegistry.register(blueprintProjector);
        GameRegistry.register(new ItemBlock(blueprintProjector).setRegistryName(blueprintProjector.getRegistryName()));
        GameRegistry.registerTileEntity(BlueprintProjectorTileEntity.class, blueprintProjector.getUnlocalizedName());

        // Uranium Ore (World Gen).
        uranium = new BlockUraniumOre();
        GameRegistry.register(uranium);
        GameRegistry.register(new ItemBlock(uranium).setRegistryName(uranium.getRegistryName()));
        OreDictionary.registerOre("oreUranium", new ItemStack(uranium));

        // Leaded Chest (for storing radioactive isotopes).
        leadChest = new LeadedChestBlock();
        GameRegistry.register(leadChest);
        GameRegistry.register(new ItemBlock(leadChest).setRegistryName(leadChest.getRegistryName()));
        GameRegistry.registerTileEntity(LeadedChestTileEntity.class, leadChest.getUnlocalizedName());

        // Fission Reactor.
        GameRegistry.registerTileEntity(FissionTileEntity.class, "fissionReactor");

        // Tile Entity Proxy.
        GameRegistry.registerTileEntity(TileEntityProxy.class, "minchem.tileEntityProxy");

        GameRegistry.registerTileEntity(RadiationFluidTileEntity.class, "minechem.tileEntityRadiationFluid");
    }
}

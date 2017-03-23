package minechem.fluid;

import minechem.Minechem;
import minechem.item.bucket.MinechemBucketHandler;
import minechem.item.element.ElementEnum;
import minechem.item.molecule.MoleculeEnum;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.IdentityHashMap;
import java.util.Map;

public class FluidHelper
{

    public static Map<MoleculeEnum, FluidMolecule> molecules = new IdentityHashMap<MoleculeEnum, FluidMolecule>();
    public static Map<ElementEnum, FluidElement> elements = new IdentityHashMap<ElementEnum, FluidElement>();

    public static Map<FluidMolecule, FluidBlockMolecule> moleculeBlocks = new IdentityHashMap<FluidMolecule, FluidBlockMolecule>();
    public static Map<FluidElement, FluidBlockElement> elementsBlocks = new IdentityHashMap<FluidElement, FluidBlockElement>();

    public static void registerElement(ElementEnum element)
    {
        FluidElement fluid = new FluidElement(element);
        elements.put(element, fluid);
        elementsBlocks.put(fluid, new FluidBlockElement(fluid));

        GameRegistry.register(elementsBlocks.get(fluid));
        GameRegistry.register(new ItemBlock(elementsBlocks.get(fluid)).setRegistryName(elementsBlocks.get(fluid).getRegistryName()));

        Minechem.PROXY.onAddFluid(fluid, elementsBlocks.get(fluid));
        MinechemBucketHandler.getInstance().registerCustomMinechemBucket(elementsBlocks.get(fluid), element, "element.");
    }

    public static void registerMolecule(MoleculeEnum molecule)
    {
        FluidMolecule fluid = new FluidMolecule(molecule);
        molecules.put(molecule, fluid);
        moleculeBlocks.put(fluid, new FluidBlockMolecule(fluid));

        GameRegistry.register(moleculeBlocks.get(fluid));
        GameRegistry.register(new ItemBlock(moleculeBlocks.get(fluid)).setRegistryName(moleculeBlocks.get(fluid).getRegistryName()));

        Minechem.PROXY.onAddFluid(fluid, moleculeBlocks.get(fluid));
        MinechemBucketHandler.getInstance().registerCustomMinechemBucket(moleculeBlocks.get(fluid), molecule, "molecule.");
    }
}

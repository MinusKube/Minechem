package minechem.item.bucket;

import minechem.Minechem;
import minechem.fluid.MinechemBucketDispenser;
import minechem.fluid.MinechemFluid;
import minechem.fluid.MinechemFluidBlock;
import minechem.item.MinechemChemicalType;
import minechem.item.element.Element;
import minechem.item.element.ElementEnum;
import minechem.item.molecule.Molecule;
import minechem.item.molecule.MoleculeEnum;
import minechem.potion.PotionChemical;
import minechem.radiation.RadiationEnum;
import minechem.radiation.RadiationFluidTileEntity;
import minechem.radiation.RadiationInfo;
import minechem.reference.Reference;
import minechem.tileentity.decomposer.DecomposerRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinechemBucketHandler
{
    private static MinechemBucketHandler instance;
    public Map<MinechemFluidBlock, MinechemBucketItem> buckets = new HashMap<MinechemFluidBlock, MinechemBucketItem>();

    public static MinechemBucketHandler getInstance()
    {
        if (instance == null)
        {
            instance = new MinechemBucketHandler();
        }
        return instance;
    }

    private MinechemBucketHandler()
    {

    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event)
    {
        ItemStack result = fillCustomBucket(event.getWorld(), event.getTarget());

        if (result == null)
        {
            return;
        }

        event.setFilledBucket(result);
        event.setResult(Event.Result.ALLOW);
    }

    private ItemStack fillCustomBucket(World world, RayTraceResult pos)
    {
        IBlockState state = world.getBlockState(pos.getBlockPos());
        Block block = state.getBlock();

        Item bucket = buckets.get(block);

        if (bucket != null && block.getMetaFromState(state) == 0)
        {
            ItemStack stack = new ItemStack(bucket);
            TileEntity tile = world.getTileEntity(pos.getBlockPos());
            RadiationEnum radiation = ((MinechemBucketItem) bucket).chemical.radioactivity();
            if (tile != null && radiation != RadiationEnum.stable && tile instanceof RadiationFluidTileEntity && ((RadiationFluidTileEntity) tile).info != null)
            {
                RadiationInfo.setRadiationInfo(((RadiationFluidTileEntity) tile).info, stack);
            }
            world.setBlockToAir(pos.getBlockPos());
            return stack;
        } else
        {
            return null;
        }
    }

    public MinechemBucketItem getBucket(MinechemChemicalType type)
    {
        if (type != null)
        {
            for (MinechemFluidBlock block : buckets.keySet())
            {
                if (block.getFluid() instanceof MinechemFluid)
                {
                    MinechemChemicalType blockType = ((MinechemFluid) block.getFluid()).getChemical();
                    if (type == blockType)
                    {
                        return buckets.get(block);
                    }
                }
            }
        }
        return null;
    }

    public void registerCustomMinechemBucket(MinechemFluidBlock block, MinechemChemicalType type, String prefix)
    {
        if (buckets.get(block) != null)
        {
            return;
        }

        MinechemBucketItem bucket = new MinechemBucketItem(block, block.getFluid(), type);
        GameRegistry.registerItem(bucket, Reference.ID + "Bucket." + prefix + block.getFluid().getName());
        FluidContainerRegistry.registerFluidContainer(block.getFluid(), new ItemStack(bucket), new ItemStack(Items.BUCKET));
        buckets.put(block, bucket);
        Minechem.PROXY.onAddBucket(bucket);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(bucket, MinechemBucketDispenser.dispenser);
    }

    public void registerBucketRecipes()
    {
        GameRegistry.addRecipe(new MinechemBucketRecipe());
        GameRegistry.addRecipe(new MinechemBucketReverseRecipe());

        for (MinechemBucketItem bucket : buckets.values())
        {
            registerBucketDecomposerRecipe(new ItemStack(bucket), bucket.chemical);
        }
    }

    private void registerBucketDecomposerRecipe(ItemStack itemStack, MinechemChemicalType type)
    {
        ArrayList<PotionChemical> tubes = new ArrayList<PotionChemical>();
        tubes.add(new Element(ElementEnum.Fe, 48));
        if (type instanceof ElementEnum)
        {
            tubes.add(new Element((ElementEnum) type, 8));
        } else if (type instanceof MoleculeEnum)
        {
            tubes.add(new Molecule((MoleculeEnum) type, 8));
        }
        DecomposerRecipe.add(new DecomposerRecipe(itemStack, tubes));
    }
}

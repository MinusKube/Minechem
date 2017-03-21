package minechem.fluid;

import minechem.Settings;
import minechem.potion.PharmacologyEffectRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FluidBlockMolecule extends MinechemFluidBlock
{

    public FluidBlockMolecule(MinechemFluid fluid, Material material)
    {
        super(fluid, material);
    }

    public FluidBlockMolecule(MinechemFluid fluid)
    {
        super(fluid, materialFluidBlock);
        this.setRegistryName(fluidName);
        this.setUnlocalizedName(fluidName);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        if (entity instanceof EntityLivingBase && Settings.fluidEffects && getFluid() instanceof FluidMolecule)
        {
            PharmacologyEffectRegistry.applyEffect(((FluidMolecule) getFluid()).molecule, (EntityLivingBase) entity);

            int power = ((FluidMolecule) getFluid()).molecule.radioactivity().ordinal();
            if (power > 0)
            {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.POISON, 10, power - 1));
            }
        }
    }
}

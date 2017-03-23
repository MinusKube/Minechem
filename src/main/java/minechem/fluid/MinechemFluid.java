package minechem.fluid;

import minechem.item.ChemicalRoomStateEnum;
import minechem.item.MinechemChemicalType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

abstract public class MinechemFluid extends Fluid
{

    private int quanta;

    public MinechemFluid(String fluidName, ChemicalRoomStateEnum roomstatus)
    {
        super(fluidName, new ResourceLocation(fluidName + "_still"), new ResourceLocation(fluidName + "_flow"));

        setGaseous(roomstatus.isGas());
        setViscosity(roomstatus.getViscosity());
        setDensity(roomstatus.isGas() ? -10 : roomstatus == ChemicalRoomStateEnum.SOLID ? 0 : 10);
        setQuanta(roomstatus.getQuanta());

        FluidRegistry.registerFluid(this);
    }

    public void setQuanta(int quanta)
    {
        this.quanta = quanta;
    }

    public int getQuanta()
    {
        return quanta;
    }

    abstract public ItemStack getOutputStack();

    abstract public MinechemChemicalType getChemical();

    @Override
    public String getUnlocalizedName()
    {
        return super.getUnlocalizedName() + ".name";
    }
}

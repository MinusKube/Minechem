package minechem.fluid;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FluidTextureStitchHandler
{

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event)
    {
        /*if (event.map.getTextureType() == 0)
        {
            IIconRegister ir = event.map;
            for (FluidElement fluidElement : FluidHelper.elements.values())
            {
                fluidElement.setIcons(ir.registerIcon(Textures.IIcon.FUILD_STILL), ir.registerIcon(Textures.IIcon.FLUID_FLOW));
            }
            for (FluidMolecule fluidMolecule : FluidHelper.molecules.values())
            {
                fluidMolecule.setIcons(ir.registerIcon(Textures.IIcon.FUILD_STILL), ir.registerIcon(Textures.IIcon.FLUID_FLOW));
            }
        }*/
    }
}

package minechem.proxy;

import minechem.Minechem;
import minechem.fluid.FluidTextureStitchHandler;
import minechem.fluid.MinechemFluid;
import minechem.fluid.MinechemFluidBlock;
import minechem.gui.GuiHandler;
import minechem.item.bucket.MinechemBucketItem;
import minechem.render.FluidItemRenderingHandler;
import minechem.sound.MinechemSoundEvent;
import minechem.tileentity.blueprintprojector.BlueprintProjectorTileEntity;
import minechem.tileentity.blueprintprojector.BlueprintProjectorTileEntityRenderer;
import minechem.tileentity.decomposer.DecomposerTileEntity;
import minechem.tileentity.decomposer.DecomposerTileEntityRenderer;
import minechem.tileentity.leadedchest.LeadedChestTileEntity;
import minechem.tileentity.leadedchest.LeadedChestTileEntityRenderer;
import minechem.tileentity.microscope.MicroscopeTileEntity;
import minechem.tileentity.microscope.MicroscopeTileEntityRenderer;
import minechem.tileentity.synthesis.SynthesisTileEntity;
import minechem.tileentity.synthesis.SynthesisTileEntityRenderer;
import minechem.utils.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    //public FluidItemRenderingHandler fluidItemRenderingHandler;

    @Override
    public void registerRenderers()
    {

        LogHelper.debug("Registering GUI and Container handlers...");
        NetworkRegistry.INSTANCE.registerGuiHandler(Minechem.INSTANCE, new GuiHandler());

        /*MinecraftForgeClient.registerItemRenderer(MinechemItemsRegistration.element, new ElementItemRenderer());
        MinecraftForgeClient.registerItemRenderer(MinechemItemsRegistration.molecule, new MoleculeItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MinechemBlocksGeneration.microscope), new MicroscopeItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MinechemBlocksGeneration.decomposer), new DecomposerItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MinechemBlocksGeneration.synthesis), new SynthesisItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MinechemBlocksGeneration.blueprintProjector), new BlueprintProjectorItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MinechemBlocksGeneration.leadChest), new LeadedChestItemRenderer());*/

        ClientRegistry.bindTileEntitySpecialRenderer(MicroscopeTileEntity.class, new MicroscopeTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DecomposerTileEntity.class, new DecomposerTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(SynthesisTileEntity.class, new SynthesisTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(BlueprintProjectorTileEntity.class, new BlueprintProjectorTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(LeadedChestTileEntity.class, new LeadedChestTileEntityRenderer());
    }

    @Override
    public void registerHooks()
    {
        MinecraftForge.EVENT_BUS.register(new MinechemSoundEvent());
        MinecraftForge.EVENT_BUS.register(new FluidTextureStitchHandler());
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().world;
    }

    @Override
    public void registerTickHandlers()
    {
        super.registerTickHandlers();

    }

    @Override
    public EntityPlayer getPlayer(MessageContext context)
    {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public void onAddFluid(MinechemFluid fluid, MinechemFluidBlock block)
    {
        super.onAddFluid(fluid, block);

        /*if (fluidItemRenderingHandler == null)
        {
            fluidItemRenderingHandler = new FluidItemRenderingHandler();
        }*/
        //MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), fluidItemRenderingHandler);
    }

    @Override
    public void onAddBucket(MinechemBucketItem item)
    {
        super.onAddBucket(item);

        //MinecraftForgeClient.registerItemRenderer(item, bucketItemRenderer);
    }
}

package minechem.network;

import minechem.network.message.ChemistJournalActiveItemMessage;
import minechem.network.message.DecomposerDumpFluidMessage;
import minechem.network.message.DecomposerUpdateMessage;
import minechem.network.message.FissionUpdateMessage;
import minechem.network.message.FusionUpdateMessage;
import minechem.network.message.GhostBlockMessage;
import minechem.network.message.PolytoolUpdateMessage;
import minechem.network.message.SynthesisUpdateMessage;
import minechem.reference.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHandler implements IMessageHandler
{
    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.ID);
    public static int id = 0;

    public static void init()
    {
        INSTANCE.registerMessage(SynthesisUpdateMessage.class, SynthesisUpdateMessage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(DecomposerUpdateMessage.class, DecomposerUpdateMessage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(PolytoolUpdateMessage.class, PolytoolUpdateMessage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(GhostBlockMessage.class, GhostBlockMessage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(ChemistJournalActiveItemMessage.class, ChemistJournalActiveItemMessage.class, id++, Side.SERVER);
        INSTANCE.registerMessage(FissionUpdateMessage.class, FissionUpdateMessage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(FusionUpdateMessage.class, FusionUpdateMessage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(DecomposerDumpFluidMessage.class, DecomposerDumpFluidMessage.class, id++, Side.SERVER);
    }

    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx)
    {
        return null;
    }
}

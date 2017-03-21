package minechem.network.message;

import io.netty.buffer.ByteBuf;
import minechem.item.blueprint.MinechemBlueprint;
import minechem.tileentity.multiblock.ghostblock.GhostBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GhostBlockMessage implements IMessage, IMessageHandler<GhostBlockMessage, IMessage>
{
    private int posX, posY, posZ;
    private int blueprintID, ghostblockID;

    public GhostBlockMessage()
    {

    }

    public GhostBlockMessage(GhostBlockTileEntity tile)
    {
        this.posX = tile.getPos().getX();
        this.posY = tile.getPos().getY();
        this.posZ = tile.getPos().getZ();

        this.blueprintID = tile.getBlueprint().id;
        this.ghostblockID = tile.getBlockID();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();

        this.blueprintID = buf.readInt();
        this.ghostblockID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);

        buf.writeInt(this.blueprintID);
        buf.writeInt(this.ghostblockID);
    }

    @Override
    public IMessage onMessage(GhostBlockMessage message, MessageContext ctx)
    {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().world.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));
        if (tileEntity instanceof GhostBlockTileEntity)
        {
            ((GhostBlockTileEntity) tileEntity).setBlueprintAndID(MinechemBlueprint.blueprints.get(message.blueprintID), message.ghostblockID);
        }
        return null;
    }
}

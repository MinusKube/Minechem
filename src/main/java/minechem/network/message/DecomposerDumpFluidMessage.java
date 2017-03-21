package minechem.network.message;

import io.netty.buffer.ByteBuf;
import minechem.tileentity.decomposer.DecomposerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DecomposerDumpFluidMessage implements IMessage, IMessageHandler<DecomposerDumpFluidMessage, IMessage>
{
    int posX,posY,posZ;

    public DecomposerDumpFluidMessage()
    {}

    public DecomposerDumpFluidMessage(DecomposerTileEntity tile)
    {
        this.posX = tile.getPos().getX();
        this.posY = tile.getPos().getY();
        this.posZ = tile.getPos().getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);
    }

    @Override
    public IMessage onMessage(DecomposerDumpFluidMessage message, MessageContext ctx)
    {
        TileEntity tileEntity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));
        if (tileEntity instanceof DecomposerTileEntity)
        {
            ((DecomposerTileEntity) tileEntity).dumpFluid();
        }
        return null;
    }
}

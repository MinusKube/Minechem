package minechem.network.message;

import io.netty.buffer.ByteBuf;
import minechem.tileentity.decomposer.DecomposerTileEntity;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class DecomposerUpdateMessage extends SPacketUpdateTileEntity implements IMessage, IMessageHandler<DecomposerUpdateMessage, IMessage>
{
    private int posX, posY, posZ;
    private int energyStored, state;
    private int fluidAmount;
    private String fluidName;

    public DecomposerUpdateMessage()
    {

    }

    public DecomposerUpdateMessage(DecomposerTileEntity tile)
    {
        super(tile.getPos(), tile.getBlockMetadata(), tile.getUpdateTag());

        this.posX = tile.getPos().getX();
        this.posY = tile.getPos().getY();
        this.posZ = tile.getPos().getZ();

        this.energyStored = tile.getEnergyStored();
        this.state = tile.getState().ordinal();

        if (tile.tank != null)
        {
            this.fluidName = tile.tank.getFluid().getName();
            this.fluidAmount = tile.tank.amount;
        } else
        {
            this.fluidName = null;
            this.fluidAmount = -1;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();

        this.energyStored = buf.readInt();
        this.state = buf.readInt();

        byte[] data = new byte[buf.readableBytes()];

        if(data.length > 0) {
            buf.readBytes(data);

            this.fluidName = new String(data);
            this.fluidAmount = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.posX);
        buf.writeInt(this.posY);
        buf.writeInt(this.posZ);

        buf.writeInt(this.energyStored);
        buf.writeInt(this.state);

        if(this.fluidName != null) {
            buf.writeBytes(this.fluidName.getBytes());
            buf.writeInt(this.fluidAmount);
        }
    }

    @Override
    public IMessage onMessage(DecomposerUpdateMessage message, MessageContext ctx)
    {
        TileEntity tileEntity;
        if (ctx.side == Side.CLIENT)
        {
            tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));
        } else
        {
            tileEntity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getTileEntity(new BlockPos(message.posX, message.posY, message.posZ));
        }
        if (tileEntity instanceof DecomposerTileEntity)
        {
            ((DecomposerTileEntity) tileEntity).syncEnergyValue(message.energyStored);
            ((DecomposerTileEntity) tileEntity).setState(message.state);
            FluidStack tankStack = null;
            if (message.fluidName != null)
            {
                tankStack = new FluidStack(FluidRegistry.getFluid(message.fluidName), message.fluidAmount);
            }
            ((DecomposerTileEntity) tileEntity).tank = tankStack;
        }
        return null;

    }


}

package survivalgame.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import survivalgame.server.Server;
import survivalgame.server.network.packet.Packet;
import survivalgame.server.network.packet.PacketRegistry;

import java.nio.ByteOrder;
import java.util.List;

public class PacketEncoder extends MessageToMessageEncoder<Packet> {
    PacketRegistry reg;
    
    public PacketEncoder(PacketRegistry r) {
        this.reg = r;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer().order(ByteOrder.LITTLE_ENDIAN);
        int packetId = reg.CLIENTBOUND.getPacketId(packet.getClass());
        if (packetId == -1) {
            throw new IllegalArgumentException("Provided packet is not registered as a clientbound packet!");
        }
        
        buf.writeByte(packetId);
        packet.writeData(buf);
        out.add(new BinaryWebSocketFrame(buf));
        
        Server.log.finest("Sent packet ID " + packetId + " (" + packet.getClass().getSimpleName() + ") to " + ctx.channel().remoteAddress());
    }

}

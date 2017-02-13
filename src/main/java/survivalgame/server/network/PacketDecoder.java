package survivalgame.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import survivalgame.server.Server;
import survivalgame.server.network.exceptions.UnknownPacketException;
import survivalgame.server.network.packet.Packet;
import survivalgame.server.network.packet.PacketRegistry;

import java.nio.ByteOrder;
import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<WebSocketFrame> {
    private PacketRegistry reg;
    
    public PacketDecoder(PacketRegistry reg) {
        this.reg = reg;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        ByteBuf buf = frame.content().order(ByteOrder.LITTLE_ENDIAN);
        if (buf.capacity() < 1) {
            // Discard empty messages
            return;
        }

        buf.resetReaderIndex();
        int packetId = buf.readUnsignedByte();
        Packet packet = reg.SERVERBOUND.constructPacket(packetId);

        if (packet == null) {
            throw new UnknownPacketException("Unknown packet ID: " + packetId);
        }

        Server.log.finest("Received packet ID " + packetId + " (" + packet.getClass().getSimpleName() + ") from " + ctx.channel().remoteAddress());

        packet.readData(buf);
        out.add(packet);
    }

}

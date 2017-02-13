package survivalgame.server.network.packet.inbound;

import io.netty.buffer.ByteBuf;
import survivalgame.server.network.exceptions.WrongDirectionException;
import survivalgame.server.network.packet.Packet;

public class HandshakePacket extends Packet {
    public int protocolVersion;

    @Override
    public void writeData(ByteBuf buf) {
        throw new WrongDirectionException();
    }

    @Override
    public void readData(ByteBuf buf) {
        protocolVersion = buf.readInt();
    }

}

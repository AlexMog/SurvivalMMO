package survivalgame.server.network.packet.outbound;

import io.netty.buffer.ByteBuf;
import survivalgame.server.network.exceptions.WrongDirectionException;
import survivalgame.server.network.packet.Packet;

public class HandshakeAnswer extends Packet {
    private int mProtcolVersion;
    private boolean status;

    public HandshakeAnswer(int mProtcolVersion, boolean status) {
        super();
        this.mProtcolVersion = mProtcolVersion;
        this.status = status;
    }
    
    @Override
    public void writeData(ByteBuf buf) {
        buf.writeInt(mProtcolVersion);
        buf.writeBoolean(status);
    }

    @Override
    public void readData(ByteBuf buf) {
        throw new WrongDirectionException();
    }

}

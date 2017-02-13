package survivalgame.server.network.packet.both;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import survivalgame.server.network.packet.Packet;

public class RpcPacket extends Packet {
    public int entityId;
    public byte rpcId;
    public List<Object> datas;
    private ByteBuf mBuffer;

    public RpcPacket(int entityId, byte rpcId, Object...datas) {
        this.entityId = entityId;
        this.rpcId = rpcId;
        this.datas = Arrays.asList(datas);
    }
    
    @Override
    public void writeData(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(rpcId);
        if (datas != null) writeObjectArray(buf, datas);
    }

    @Override
    public void readData(ByteBuf buf) {
        entityId = buf.readInt();
        rpcId = buf.readByte();
        mBuffer = buf;
    }
    
    public List<Object> unpack(List<Class<?>> types) {
        return readObjectArray(mBuffer, types);
    }
}

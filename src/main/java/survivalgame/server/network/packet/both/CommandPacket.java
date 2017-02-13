package survivalgame.server.network.packet.both;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import survivalgame.server.network.packet.Packet;

public class CommandPacket extends Packet {
    public byte commandId;
    public List<Object> datas;
    private ByteBuf mBuffer;
    
    public CommandPacket(byte commandId, Object...datas) {
        this.commandId = commandId;
        this.datas = Arrays.asList(datas);
    }
    
    @Override
    public void writeData(ByteBuf buf) {
        buf.writeByte(commandId);
        if (datas != null) writeObjectArray(buf, datas);
    }

    @Override
    public void readData(ByteBuf buf) {
        commandId = buf.readByte();
        mBuffer = buf;
    }

    public List<Object> unpack(List<Class<?>> types) {
        return readObjectArray(mBuffer, types);
    }
}

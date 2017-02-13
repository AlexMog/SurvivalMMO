package survivalgame.server.network.packet;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public abstract class Packet {

    /**
     * Writes packet data, excluding the packet ID, to the specified buffer.
     * <p>
     *
     * @param buf
     */
    public abstract void writeData(ByteBuf buf);

    /**
     * Reads packet data, excluding the packet ID, from the specified buffer.
     * <p>
     *
     * @param buf
     */
    public abstract void readData(ByteBuf buf);

    public static String readUTF8(ByteBuf in) {
        ByteBuf buffer = in.alloc().buffer();
        byte b;
        while (in.readableBytes() > 0 && (b = in.readByte()) != 0) {
            buffer.writeByte(b);
        }

        return buffer.toString(Charsets.UTF_8);
    }

    public static String readUTF16(ByteBuf in) {
        in = in.order(ByteOrder.BIG_ENDIAN);
        ByteBuf buffer = in.alloc().buffer();
        char chr;
        while (in.readableBytes() > 1 && (chr = in.readChar()) != 0) {
            buffer.writeChar(chr);
        }

        return buffer.toString(Charsets.UTF_16LE);
    }

    public static void writeUTF8(ByteBuf out, String s) {
        out.writeBytes(s.getBytes(Charsets.UTF_8));
        out.writeByte(0);
    }

    public static void writeUTF16(ByteBuf out, String s) {
        out.order(ByteOrder.BIG_ENDIAN).writeBytes(s.getBytes(Charsets.UTF_16LE));
        out.writeChar(0);
    }
    
    // TODO ce code est dégueu, flemme de le repenser, faut le repenser. A bientôt le Mog qui aura dormis <3.
    public static void writeObjectArray(ByteBuf out, List<Object> objects) {
        for (Object o : objects) {
            if (o instanceof Byte) {
                out.writeByte((Byte) o);
            } else if (o instanceof Boolean) {
                out.writeBoolean((Boolean) o);
            } else if (o instanceof Integer) {
                out.writeInt((Integer) o);
            } else if (o instanceof Float) {
                out.writeFloat((Float) o);
            } else if (o instanceof Double) {
                out.writeDouble((Double) o);
            } else if (o instanceof Short) {
                out.writeShort((Short) o);
            } else if (o instanceof Long) {
                out.writeLong((Long) o);
            }
        }
    }
    
    // TODO Pareil, tu es dégueu Mog.
    public static List<Object> readObjectArray(ByteBuf in, List<Class<?>> types) {
        List<Object> ret = new ArrayList<>();
        for (Class<?> t : types) {
            if (t.isAssignableFrom(Byte.class)) {
                ret.add(in.readByte());
            } else if (t.isAssignableFrom(Boolean.class)) {
                ret.add(in.readBoolean());
            } else if (t.isAssignableFrom(Integer.class)) {
                ret.add(in.readInt());
            } else if (t.isAssignableFrom(Float.class)) {
                ret.add(in.readFloat());
            } else if (t.isAssignableFrom(Double.class)) {
                ret.add(in.readDouble());
            } else if (t.isAssignableFrom(Short.class)) {
                ret.add(in.readShort());
            } else if (t.isAssignableFrom(Long.class)) {
                ret.add(in.readLong());
            }
        }
        return ret;
    }
}

package survivalgame.server.network.packet;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class PacketRegistry {
    
    public final static ProtocolDirection GAMECLIENTBOUND = new ProtocolDirection("CLIENTBOUND");
    public static final ProtocolDirection GAMESERVERBOUND = new ProtocolDirection("SERVERBOUND");

    public ProtocolDirection CLIENTBOUND = new ProtocolDirection("CLIENTBOUND");
    public ProtocolDirection SERVERBOUND = new ProtocolDirection("SERVERBOUND");

    public PacketRegistry() {
        CLIENTBOUND = GAMECLIENTBOUND;
        SERVERBOUND = GAMESERVERBOUND;
    }
    
    static {
        // Clientbound packets
//        GAMECLIENTBOUND.registerPacket(1, PacketOutRules.class);

        // Serverbound packets
//        GAMESERVERBOUND.registerPacket(0, PacketInSetNick.class);
    }

    public static class ProtocolDirection {

        private final TIntObjectMap<Class<? extends Packet>> packetClasses = new TIntObjectHashMap<>(10, 0.5F);
        private final TObjectIntMap<Class<? extends Packet>> reverseMapping = new TObjectIntHashMap<>(10, 0.5F, -1);
        private final String name;

        public ProtocolDirection(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void registerPacket(int packetId, Class<? extends Packet> clazz) {
            if (packetClasses.containsKey(packetId)) {
                throw new IllegalArgumentException("Packet with ID " + packetId + " is already registered for " + this + "!");
            }

            if (reverseMapping.containsKey(clazz)) {
                throw new IllegalArgumentException("Packet with class " + clazz + " is already registered for " + this + "!");
            }

            packetClasses.put(packetId, clazz);
            reverseMapping.put(clazz, packetId);
        }

        public int getPacketId(Class<? extends Packet> clazz) {
            return reverseMapping.get(clazz);
        }

        public Class<? extends Packet> getPacketClass(int packetId) {
            return packetClasses.get(packetId);
        }

        public Packet constructPacket(int packetId) {
            Class<? extends Packet> clazz = getPacketClass(packetId);
            if (clazz == null) {
                return null;
            }

            try {
                return clazz.newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public String toString() {
            return "ProtocolDirection{" + "name=" + name + '}';
        }
    }
}

package survivalgame.server.util;

import survivalgame.server.network.PlayerConnection;
import survivalgame.server.network.packet.both.RpcPacket;

public class RpcWrapper {
    public final RpcPacket packet;
    public final PlayerConnection connection;
    
    public RpcWrapper(RpcPacket p, PlayerConnection c) {
        packet = p;
        connection = c;
    }
}

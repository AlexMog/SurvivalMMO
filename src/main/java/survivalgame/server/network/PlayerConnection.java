package survivalgame.server.network;

import com.artemis.Entity;
import com.google.common.base.Preconditions;

import io.netty.channel.Channel;
import survivalgame.server.network.packet.Packet;
import survivalgame.server.network.packet.both.CommandPacket;
import survivalgame.server.network.packet.both.RpcPacket;
import survivalgame.server.network.packet.inbound.HandshakePacket;
import survivalgame.server.network.packet.outbound.HandshakeAnswer;
import survivalgame.server.util.MethodWrapper;
import survivalgame.server.world.Player;
import survivalgame.server.world.ecs.components.NetworkComponent;
import survivalgame.server.world.ecs.managers.NetIdManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerConnection {
    private static final int HANDSHAKE = 0, SYNC = 1, SYNC_DONE = 2, AUTHENTICATED = 3;
    
    private final Player mPlayer;
    private final Channel mChannel;
    private int mState = HANDSHAKE;
    private int mProtocolVersion;
    private String mAuthToken;
    private static final Map<Byte, MethodWrapper> mCommands = new HashMap<>();
    
    public PlayerConnection(Player player, Channel channel) {
        this.mPlayer = player;
        this.mChannel = channel;
    }
    
    public static void registerCommand(byte commandId, Method method) {
        mCommands.put(commandId, new MethodWrapper(method));
    }
    
    public SocketAddress getRemoteAddress() {
        return mChannel.remoteAddress();
    }

    private void sendPacket(Packet packet) {
        mChannel.writeAndFlush(packet);
    }
    
    public void sendCommand(byte commandId, Object...datas) {
        sendPacket(new CommandPacket(commandId, datas));
    }
    
    public void sendRpc(int entityId, byte rpcId, Object...datas) {
        sendPacket(new RpcPacket(entityId, rpcId, datas));
    }

    public void handle(Packet packet) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (packet instanceof RpcPacket) {
            afterState(SYNC_DONE);
            if (mPlayer.getWorldManager() != null) {
                RpcPacket p = (RpcPacket) packet;
                Entity e = mPlayer.getWorldManager().getWorld().getSystem(NetIdManager.class).getEntity(p.entityId);
                if (e != null) {
                    NetworkComponent netComp = e.getComponent(NetworkComponent.class);
                    if (netComp != null) netComp.addRpcToQueue(p, this);
                }
            }
        } else if (packet instanceof CommandPacket) {
            notState(HANDSHAKE);
            CommandPacket p = (CommandPacket) packet;
            MethodWrapper wrap = mCommands.get(p.commandId);
            if (wrap != null) wrap.execute(this, p.unpack(wrap.getTypes()));
        } else if (packet instanceof HandshakePacket) {
            mProtocolVersion = ((HandshakePacket) packet).protocolVersion;
            mState = SYNC;
            sendPacket(new HandshakeAnswer(/* TODO */ 0, true));
            // TODO Protocol check
        }
    }
    
    private void afterState(int state) {
        Preconditions.checkState(mState < state, "Connection is < " + state + " state!");
    }
    
    private void notState(int state) {
        Preconditions.checkState(mState != state, "Connection is " + state + " state!");
    }
    
    private void checkState(int state) {
        Preconditions.checkState(mState == state, "Connection is not in " + state + " state!");
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(mChannel);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlayerConnection other = (PlayerConnection) obj;
        if (!Objects.equals(mChannel, other.mChannel)) {
            return false;
        }
        return true;
    }
}

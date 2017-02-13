package survivalgame.server.world.ecs.components;

import java.util.Deque;
import java.util.LinkedList;

import com.artemis.Component;

import survivalgame.server.network.PlayerConnection;
import survivalgame.server.network.packet.both.RpcPacket;
import survivalgame.server.util.RpcWrapper;
import survivalgame.server.world.Player;

public class NetworkComponent extends Component {
    // Optimize manually the dequeue
    private Deque<RpcWrapper> mRpcSafeQueue = new LinkedList<>();
    private Player mOwner;
    private Relation mRelation;
    
    public void setOwner(Player p) {
        mOwner = p;
    }
    
    public void setRelation(Relation r) {
        mRelation = r;
    }
    
    public Player getOwner() {
        return mOwner;
    }
    
    public Relation getRelation() {
        return mRelation;
    }
    
    public void addRpcToQueue(RpcPacket p, PlayerConnection c) {
        synchronized(mRpcSafeQueue) {
            mRpcSafeQueue.addLast(new RpcWrapper(p, c));
        }
    }
    
    public Deque<RpcWrapper> getRpcPacketsToProcess() {
        return mRpcSafeQueue;
    }
    
    public enum Relation {
        Owner,
        Peer
    }
}

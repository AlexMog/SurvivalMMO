package survivalgame.server.world;

import io.netty.channel.Channel;
import survivalgame.server.network.PlayerConnection;

public class Player {
    private final PlayerConnection mPlayerConnection;
    private final PlayerTracker mTracker;
    private WorldManager mWorldManager;

    public Player(Channel channel) {
        this.mPlayerConnection = new PlayerConnection(this, channel);
        this.mTracker = new PlayerTracker(this);
    }
    
    public void setWorldManager(WorldManager worldManager) {
        mWorldManager = worldManager;
    }
    
    public WorldManager getWorldManager() {
        return mWorldManager;
    }

    public PlayerConnection getConnection() {
        return mPlayerConnection;
    }
    
    public PlayerTracker getTracker() {
        return mTracker;
    }
}

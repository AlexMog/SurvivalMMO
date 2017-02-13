package survivalgame.server.world;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.UuidEntityManager;

public class WorldManager {
    private World mWorld;
    
    public void init() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(new UuidEntityManager())
                .build();
        mWorld = new World(config);
    }
    
    public void update(float delta) {
        mWorld.setDelta(delta);
        mWorld.process();
    }
    
    public World getWorld() {
        return mWorld;
    }
}

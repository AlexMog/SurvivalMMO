package survivalgame.server.world;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;

import survivalgame.server.world.ecs.managers.NetIdManager;
import survivalgame.server.world.ecs.systems.NetworkEntitiesSystem;

public class WorldManager {
    private World mWorld;
    private Globals mLuaGlobals = JsePlatform.standardGlobals();
    
    public void init() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(new NetIdManager())
                .with(new NetworkEntitiesSystem())
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
    
    public Globals getLuaGlobals() {
        return mLuaGlobals;
    }
}

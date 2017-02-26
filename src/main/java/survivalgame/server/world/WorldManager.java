package survivalgame.server.world;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;

import survivalgame.server.world.ecs.managers.NetIdManager;
import survivalgame.server.world.ecs.systems.CollisionSystem;
import survivalgame.server.world.ecs.systems.NetworkEntitiesSystem;
import survivalgame.server.world.ecs.systems.ScriptSystem;

public class WorldManager extends Thread {
    private static final int FPS = 60;
    private static final float WAIT = 1000 / FPS;
    private static final float MS_PER_TICK = 10;
    private World mWorld;
    private Globals mLuaGlobals = JsePlatform.standardGlobals();
    
    public void init() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(new NetIdManager())
                .with(new CollisionSystem())
                .with(new ScriptSystem())
                .with(new NetworkEntitiesSystem())
                .build();
        mWorld = new World(config);
    }
    
    private void update(float delta) {
        mWorld.setDelta(delta);
        mWorld.process();
    }
    
    @Override
    public void run() {
        long currTicks = 0;
        float lag = 0;
        while (true) {
            long prevTicks = currTicks;
            currTicks = System.currentTimeMillis();
            
            long delta = currTicks - prevTicks;
            
            if (delta == 0) continue;
            
            lag += (float)delta;
            
            
            while (lag >= MS_PER_TICK) {
                update(MS_PER_TICK);
                lag -= MS_PER_TICK;
            }
            
            if (delta < WAIT) {
                try {
                    Thread.sleep((long) (WAIT - delta));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public World getWorld() {
        return mWorld;
    }
    
    public Globals getLuaGlobals() {
        return mLuaGlobals;
    }
}

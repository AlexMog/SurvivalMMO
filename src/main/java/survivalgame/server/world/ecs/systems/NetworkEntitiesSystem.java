package survivalgame.server.world.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import survivalgame.server.world.ecs.components.NetworkComponent;

public class NetworkEntitiesSystem extends EntityProcessingSystem {
    private ComponentMapper<NetworkComponent> mNetIdComponents;
    
    public NetworkEntitiesSystem() {
        super(Aspect.all(NetworkComponent.class));
    }
    
    @Override
    protected void process(Entity e) {
        NetworkComponent c = mNetIdComponents.get(e);
        
        // Process RPCs
        synchronized (c.getRpcPacketsToProcess()) {
            // TODO
        }
    }

}

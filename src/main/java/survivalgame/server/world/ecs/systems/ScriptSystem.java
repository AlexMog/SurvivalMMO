package survivalgame.server.world.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import survivalgame.server.world.ecs.components.ScriptableComponent;

public class ScriptSystem extends EntityProcessingSystem {
    private ComponentMapper<ScriptableComponent> mScriptableComponents;
    
    public ScriptSystem() {
        super(Aspect.all(ScriptableComponent.class));
    }

    @Override
    protected void process(Entity e) {
        ScriptableComponent c = mScriptableComponents.get(e);
        
    }

}

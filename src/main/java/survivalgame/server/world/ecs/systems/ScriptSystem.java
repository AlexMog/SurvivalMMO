package survivalgame.server.world.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import survivalgame.server.world.ecs.components.BodyComponent;
import survivalgame.server.world.ecs.components.ScriptableComponent;
import survivalgame.server.world.ecs.components.ScriptableComponent.Events;
import survivalgame.utils.Quadtree.BodyEntity;

public class ScriptSystem extends EntityProcessingSystem {
    private ComponentMapper<ScriptableComponent> mScriptableComponents;
    
    public ScriptSystem() {
        super(Aspect.all(ScriptableComponent.class));
    }

    @Override
    protected void process(Entity e) {
        ScriptableComponent c = mScriptableComponents.get(e);
        if (!c.isEnabled()) return;
        
        BodyComponent bodyComponent = e.getComponent(BodyComponent.class);
        if (bodyComponent != null) {
            for (BodyEntity ent : bodyComponent.getEntered()) c.executeMethod(Events.CollisionEnter, ent);
            for (BodyEntity ent : bodyComponent.getExited()) c.executeMethod(Events.CollisionExit, ent);
            for (BodyEntity ent : bodyComponent.getCurrentCollisions()) c.executeMethod(Events.CollisionStay, ent);
        }
        
        c.executeMethod(Events.Update);
    }

}

package survivalgame.server.world.ecs.managers;

import com.artemis.Entity;
import com.artemis.Manager;

import survivalgame.server.world.ecs.components.EnablableComponent;
import survivalgame.server.world.ecs.components.ScriptableComponent;

public class EnableManager extends Manager {
    @Override
    public void added(Entity e) {
        EnablableComponent c = e.getComponent(EnablableComponent.class);
        if (c != null) {
            c.setEnabled(true);
            if (c instanceof ScriptableComponent) ((ScriptableComponent)c).awake();
        }
    }
    
    @Override
    public void deleted(Entity e) {}
}

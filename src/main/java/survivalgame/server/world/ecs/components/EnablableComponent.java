package survivalgame.server.world.ecs.components;

import com.artemis.Component;
import com.artemis.Entity;

public class EnablableComponent extends Component {
    protected boolean mEnabled = false;
    protected Entity mEntity = null;
    
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }
    
    public void setEntity(Entity e) {
        mEntity = e;
    }
    
    public Entity getEntity() {
        return mEntity;
    }
    
    public boolean isEnabled() {
        return mEnabled;
    }
}

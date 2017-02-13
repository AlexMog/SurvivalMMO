package survivalgame.server.world.ecs.managers;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.utils.Bag;

public class NetIdManager extends Manager {
    private final Map<Integer, Entity> mIdToEntity;
    private final Bag<Integer> mEntityToId;
    private int mNextId = 0;

    public NetIdManager() {
        this.mIdToEntity = new HashMap<Integer, Entity>();
        this.mEntityToId = new Bag<Integer>();
    }

    @Override
    public void deleted(Entity e) {
        Integer id = mEntityToId.safeGet(e.getId());
        if (id == null)
            return;

        Entity oldEntity = mIdToEntity.get(id);
        if (oldEntity != null && oldEntity.equals(e)) mIdToEntity.remove(id);

        mEntityToId.set(e.getId(), null);
    }
    
    public void updatedUuid(Entity e, Integer newId) {
        setId(e, newId);
    }
    
    public Entity getEntity(Integer id) {
        return mIdToEntity.get(id);
    }

    public Integer getId(Entity e) {
        Integer id = mEntityToId.safeGet(e.getId());
        if (id == null) setId(e, mNextId++);
        
        return id;
    }
    
    public void setId(Entity e, Integer newId) {
        Integer oldId = mEntityToId.safeGet(e.getId());
        if (oldId != null) mIdToEntity.remove(oldId);
        
        mIdToEntity.put(newId, e);
        mEntityToId.set(e.getId(), newId);
    }
}


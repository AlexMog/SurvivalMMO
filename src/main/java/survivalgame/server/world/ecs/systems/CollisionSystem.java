package survivalgame.server.world.ecs.systems;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;

import survivalgame.server.world.ecs.components.BodyComponent;
import survivalgame.utils.Quadtree;
import survivalgame.utils.Quadtree.BodyEntity;

public class CollisionSystem extends EntitySystem {
    // TODO Initalize bounds depending on world default bounds size.
    private Quadtree mQuadTree = new Quadtree(0, new Rectangle(0, 0, 0, 0));
    private List<BodyEntity> mCollideList = new ArrayList<>();

    public CollisionSystem() {
        super(Aspect.all(BodyComponent.class));
    }

    @Override
    protected void processSystem() {
        Bag<Entity> entities = getEntities();
        mQuadTree.clear();
        // Add the quadtree entities.
        for (Entity e : entities) mQuadTree.insert(e);
        
        // Process each entities to define who collides it.
        // This process uses 2D (see from top) positions. The collision is done only if the entity
        // is on the same "y width position" of the other entity that is processed.
        for (Entity e : entities) {
            BodyComponent c = e.getComponent(BodyComponent.class);
            mQuadTree.collideList(mCollideList, c);

            c.beforeCollisionsCheck();
            
            for (BodyEntity be : mCollideList) if (be.body.intersects(c.getBounds())) c.addToCurrentCollisions(be);
            
            c.afterCollisionsCheck();
        }
    }

}

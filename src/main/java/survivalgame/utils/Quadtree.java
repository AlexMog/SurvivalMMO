package survivalgame.utils;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.artemis.Entity;

import survivalgame.server.world.ecs.components.BodyComponent;

public class Quadtree {
    
    private static int MAX_OBJECTS = 10;
    private static int MAX_LEVELS = 5;
    
    private int mLevel;
    private List<BodyEntity> mEntities;
    private Rectangle mBounds;
    private Quadtree[] mNodes;

    public static class BodyEntity {
        public Entity entity;
        public BodyComponent body;
        
        public BodyEntity(Entity entity) {
            this.entity = entity;
            body = entity.getComponent(BodyComponent.class);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null) return false;
            
            if (!this.getClass().isInstance(obj)) return false;
            
            BodyEntity ob = (BodyEntity) obj;
            
            return ob.body == this.body && ob.entity == this.entity;
        }
    }
    
    public Quadtree(int level, Rectangle bounds) {
        mBounds = bounds;
        mLevel = level;
        mEntities = new ArrayList<BodyEntity>();
        mNodes = new Quadtree[4];
    }
    
    public void clear() {
        mEntities.clear();
        for (int i = 0; i < mNodes.length; ++i) {
            if (mNodes[i] != null) {
                mNodes[i].clear();
                mNodes[i] = null;
            }
        }
    }
    
    private void split() {
        int newWidth = (int) (mBounds.getWidth() / 2);
        int newHeight = (int) (mBounds.getHeight() / 2);
        int x = (int)mBounds.getX();
        int y = (int)mBounds.getY();
        
        mNodes[0] = new Quadtree(mLevel + 1, new Rectangle(x + newWidth, y, newWidth, newHeight));
        mNodes[1] = new Quadtree(mLevel + 1, new Rectangle(x, y, newWidth, newHeight));
        mNodes[2] = new Quadtree(mLevel + 1, new Rectangle(x, y + newHeight, newWidth, newHeight));
        mNodes[3] = new Quadtree(mLevel + 1, new Rectangle(x + newWidth, y + newHeight, newWidth, newHeight));
    }

    private int getIndex(BodyEntity entity) {
        Shape rect = entity.body.getBounds().getRectangle();
        int index = -1;
        double verticalMidPoint = mBounds.getX() + (mBounds.getWidth() / 2);
        double horizontalMidPoint = mBounds.getY() + (mBounds.getHeight() / 2);
        
        // Fits the top quadrant
        boolean topQuad = rect.getY() < horizontalMidPoint && rect.getY() + rect.getHeight() < horizontalMidPoint;
        // Fits the bottom quadrant
        boolean botQuad = rect.getY() > horizontalMidPoint;
        
        // Fits the left quadrant
        if (rect.getX() < verticalMidPoint && rect.getX() + rect.getWidth() < verticalMidPoint) {
            if (topQuad) {
                index = 1;
            } else if (botQuad) {
                index = 2;
            }
        // Fits the right quadrant
        } else if (rect.getX() > verticalMidPoint) {
            if (topQuad) {
                index = 0;
            } else if (botQuad) {
                index = 3;
            }
        }
        return index;
    }
    
    private int getIndex(BodyComponent e) {
        int i = 0;
        for (BodyEntity be : mEntities) if (be.body == e) return i++;
        return -1;
    }
    
    public void insert(Entity e) {
        insert(new BodyEntity(e));
    }
    
    public void insert(BodyEntity entity) {
        if (mNodes[0] != null) {
            int index = getIndex(entity);
            
            if (index != -1) {
                mNodes[index].insert(entity);
                return;
            }
        }
        
        mEntities.add(entity);
        
        if (mEntities.size() > MAX_OBJECTS && mLevel < MAX_LEVELS) {
            if (mNodes[0] == null) {
                split();
            }
            
            int i = 0;
            while (i < mEntities.size()) {
                int index = getIndex(mEntities.get(i));
                if (index != -1) {
                    mNodes[index].insert(mEntities.remove(i));
                } else {
                    ++i;
                }
            }
        }
    }
    
    public List<BodyEntity> collideList(List<BodyEntity> returnObjects, BodyComponent entity) {
        int index = getIndex(entity);
        if (index != -1 && mNodes[0] != null) {
            mNodes[index].collideList(returnObjects, entity);
        }
        returnObjects.addAll(mEntities);
        return returnObjects;
    }
}

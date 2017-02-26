package survivalgame.server.world.ecs.components;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.artemis.Component;

import survivalgame.utils.Quadtree.BodyEntity;

public class BodyComponent extends Component {
    private final Rectangle mBounds = new Rectangle(0, 0, 0, 0);
    private final List<BodyEntity> mCurrentCollisions = new ArrayList<>();
    private final List<BodyEntity> mOldCollisions = new ArrayList<>();
    private final List<BodyEntity> mEntered = new ArrayList<>();
    private final List<BodyEntity> mExited = new ArrayList<>();
    
    public float[] getPosition() {
        return mBounds.getCenter();
    }
    
    public void setPosition(Vector2f position) {
        mBounds.setCenterX(position.x);
        mBounds.setCenterY(position.y);
    }
    
    public void setSize(float width, float height) {
        mBounds.setSize(width, height);
    }
    
    public void setPosition(float x, float y) {
        mBounds.setCenterX(x);
        mBounds.setCenterY(y);
    }
    
    public Rectangle getBounds() {
        return mBounds;
    }
    
    public boolean intersects(Rectangle body2) {
        return body2.contains(mBounds); // TODO Add Z-axis
    }
    
    public List<BodyEntity> getCurrentCollisions() {
        return mCurrentCollisions;
    }
    
    public List<BodyEntity> getOldCollisions() {
        return mOldCollisions;
    }
    
    public List<BodyEntity> getEntered() {
        return mEntered;
    }
    
    public List<BodyEntity> getExited() {
        return mExited;
    }

    public void addToCurrentCollisions(BodyEntity be) {
        if (!mCurrentCollisions.contains(be)) mEntered.add(be);
        mCurrentCollisions.add(be);
    }
    
    public void beforeCollisionsCheck() {
        mCurrentCollisions.clear();
        mEntered.clear();
        mExited.clear();
    }
    
    public void afterCollisionsCheck() {
        for (BodyEntity be : mOldCollisions) if (!mCurrentCollisions.contains(be)) mExited.add(be);
        mOldCollisions.clear();
        mOldCollisions.addAll(mCurrentCollisions);
    }
}

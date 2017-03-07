package survivalgame.server.world.ecs.components;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Vector2f;

import com.artemis.Component;

import survivalgame.server.world.collisions.BodyBounds;
import survivalgame.utils.Quadtree.BodyEntity;

public class BodyComponent extends Component {
    private final BodyBounds mBounds = new BodyBounds();
    private final List<BodyEntity> mCurrentCollisions = new ArrayList<>();
    private final List<BodyEntity> mOldCollisions = new ArrayList<>();
    private final List<BodyEntity> mEntered = new ArrayList<>();
    private final List<BodyEntity> mExited = new ArrayList<>();
    
    public float[] getPosition() {
        return mBounds.getPosition();
    }
    
    public void setPosition(Vector2f position) {
        mBounds.setPosition(position);
    }
    
    public void setSize(float width, float height) {
        mBounds.setSize(width, height);
    }
    
    public void setPosition(float x, float y) {
        mBounds.setPosition(x, y);
    }
    
    public BodyBounds getBounds() {
        return mBounds;
    }
    
    public boolean intersects(BodyBounds body2) {
        return body2.contains(mBounds);
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

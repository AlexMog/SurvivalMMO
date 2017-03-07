package survivalgame.server.world.collisions;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class BodyBounds {
    private final Rectangle mBounds = new Rectangle(0, 0, 0, 0);

    public float[] getPosition() {
        return mBounds.getCenter();
    }
    
    public void setPosition(Vector2f position) {
        mBounds.setCenterX(position.x);
        mBounds.setCenterY(position.y);
    }
    
    public void setPosition(float x, float y) {
        mBounds.setCenterX(x);
        mBounds.setCenterY(y);
    }
    
    public void setSize(float width, float height) {
        mBounds.setSize(width, height);
    }
    
    public Rectangle getRectangle() {
        return mBounds;
    }

    public boolean contains(BodyBounds mBounds2) {
        return mBounds2.getRectangle().contains(mBounds);
    }
}

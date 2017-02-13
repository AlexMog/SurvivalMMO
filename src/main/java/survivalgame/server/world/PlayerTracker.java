package survivalgame.server.world;

import com.google.common.collect.ImmutableList;

import survivalgame.server.network.PlayerConnection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerTracker {

    private final Player player;
    private final PlayerConnection conn;
    // World
    private final Set<Integer> visibleEntities = new HashSet<>();
    private double rangeX;
    private double rangeY;
    private double centerX;
    private double centerY;
    private double viewLeft;
    private double viewRight;
    private double viewTop;
    private double viewBottom;
    private long lastViewUpdateTick = 0L;

    public PlayerTracker(Player player) {
        this.player = player;
        this.conn = player.getConnection();
    }

    private void updateRange() {
        double totalSizeX = 1.0D;
        double totalSizeY = 1.0D;
/*        for (Entity e : player.getEntities()) {
            totalSizeX += e.getWidth();
            totalSizeY += e.getHeight();
        }*/

//        rangeX = world.getView().getBaseX() / Math.pow(Math.min(64.0D / totalSizeX, 1), 0.4D);
//        rangeY = world.getView().getBaseY() / Math.pow(Math.min(64.0D / totalSizeY, 1), 0.4D);
    }

    private void updateCenter() {
/*        if (player.getEntities().isEmpty()) {
            return;
        }

        int size = player.getEntities().size();
        double x = 0;
        double y = 0;

        for (Entity e : player.getEntities()) {
            x += e.getBounds().getX();
            y += e.getBounds().getY();
        }

        this.centerX = x / size;
        this.centerY = y / size;*/
    }

    public void updateView() {
/*        updateRange();
        updateCenter();

        viewTop = centerY - rangeY;
        viewBottom = centerY + rangeY;
        viewLeft = centerX - rangeX;
        viewRight = centerX + rangeX;
        
        lastViewUpdateTick = world.getServer().getTick();*/
    }

    private List<Integer> calculateEntitiesInView() {
/*        return world
                .getEntities()
                .stream()
                .filter((e) -> e.getBounds().getY() <= viewBottom && e.getBounds().getY() >= viewTop && e.getBounds().getX() <= viewRight
                        && e.getBounds().getX() >= viewLeft).mapToInt((e) -> e.getID()).boxed().collect(Collectors.toList());*/
        return null;
    }

    public List<Integer> getVisibleEntities() {
        return ImmutableList.copyOf(visibleEntities);
    }

    public void updateNodes() {
/*        // Process the removal queue
        Set<Integer> updates = new HashSet<>();
        Set<EntityImpl> removals = new HashSet<>();
        synchronized (removalQueue) {
            removals.addAll(removalQueue);
            removalQueue.clear();
        }

        // Update the view, if needed
        if (world.getServer().getTick() - lastViewUpdateTick >= 5) {
            updateView();

            // Get the new list of entities in view
            List<Integer> newVisible = calculateEntitiesInView();

            synchronized (visibleEntities) {
                // Destroy now-invisible entities
                for (Iterator<Integer> it = visibleEntities.iterator(); it.hasNext();) {
                    int id = it.next();
                    if (!newVisible.contains(id)) {
                        // Remove from player's screen
                        it.remove();
                        removals.add(world.getEntity(id));
                    }
                }

                // Add new entities to the client's screen
                for (int id : newVisible) {
                    if (!visibleEntities.contains(id)) {
                        visibleEntities.add(id);
                        updates.add(id);
                    }
                }
            }
        }

        // Update entities that need to be updated
        for (Iterator<Integer> it = visibleEntities.iterator(); it.hasNext();) {
            int id = it.next();
            EntityImpl entity = world.getEntity(id);
            if (entity == null) {
                // Prune invalid entity from the list
                it.remove();
                continue;
            }

            if (entity.shouldUpdate()) {
                updates.add(id);
            }
        }
        
        if (!removals.isEmpty()) {
            for (Iterator<EntityImpl> it = removals.iterator(); it.hasNext();) {
                if (it.next() == null) {
                    it.remove();
                }
            }
        }

        conn.sendPacket(new PacketOutTicUpdate(world, removals, updates, player));*/
    }
}

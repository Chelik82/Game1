public class Obstacle {
    private String type; // "trap" или "item"
    private int lane;
    private int distanceToPlayer; // 0 - это игрок, 10 - далеко впереди

    public Obstacle(String type, int lane, int distance) {
        this.type = type;
        this.lane = lane;
        this.distanceToPlayer = distance;
    }

    public String getType() {
        return type;
    }

    public int getLane() {
        return lane;
    }

    public int getDistance() {
        return distanceToPlayer;
    }

    // Движение препятствия навстречу игроку
    public void moveTowardsPlayer() {
        distanceToPlayer--;
    }
}
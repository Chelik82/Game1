public class Monster {
    private int lane;
    private int distanceBehind; // Насколько далеко позади игрока (в клетках)

    public Monster(int startLane, int startDistance) {
        this.lane = startLane;
        this.distanceBehind = startDistance;
    }

    public int getLane() {
        return lane;
    }

    public int getDistanceBehind() {
        return distanceBehind;
    }

    public void moveToLane(int newLane) {
        this.lane = newLane;
    }

    public void getCloser() {
        distanceBehind--;
    }

    public void fallBehind() {
        distanceBehind++;
    }
}
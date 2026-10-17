public class Player {
    private int lane; // 0, 1 или 2 (левая, центральная, правая полоса)

    public Player(int startLane) {
        this.lane = startLane;
    }

    public int getLane() {
        return lane;
    }

    public void moveLeft() {
        if (lane > 0) {
            lane--;
        }
    }

    public void moveRight() {
        if (lane < 2) {
            lane++;
        }
    }
}
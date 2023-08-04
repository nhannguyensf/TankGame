package tankrotationexample.game;

public interface PowerUp {
    void applyPowerUp(Tank tank);

    void onHit(); // Method to handle when the power-up is hit

    boolean isActive(); // Method to determine if the power-up is still active
}

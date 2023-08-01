package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

import java.awt.*;

public abstract class GameObject {
    public static GameObject newInstance(String type, float x, float y) {
        return switch (type) {
            case "9", "3" -> new Wall(x, y, ResourceManager.getSprite("unbreak"));
            case "2" -> new BreakableWall(x, y, ResourceManager.getSprite("break1"));
            case "4" -> new Health(x, y, ResourceManager.getSprite("health"));
            case "5" -> new Speed(x, y, ResourceManager.getSprite("speed"));
            case "6" -> new Shield(x, y, ResourceManager.getSprite("shield"));
            default -> throw new UnsupportedOperationException();
        };
    }

    public abstract void drawImage(Graphics g);
}

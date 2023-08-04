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
            case "11" -> new Tank(x, y, 0, 0, (short) 0, ResourceManager.getSprite("tank1"));
            case "22" -> new Tank(x, y, 0, 0, (short) 180, ResourceManager.getSprite("tank2"));
            case "33" -> new BotAI(x, y, 0, 0, (short) 0, ResourceManager.getSprite("tankAI"));
            case "44" -> new BotAI(x, y, 0, 0, (short) 180, ResourceManager.getSprite("tankAI"));
            default -> throw new IllegalArgumentException("%s type not supported".formatted(type));
        };
    }

    public abstract void collides(GameObject obj2);

    public abstract void drawImage(Graphics g);

    public abstract Rectangle getHitBox();

    public abstract boolean isActive();
}

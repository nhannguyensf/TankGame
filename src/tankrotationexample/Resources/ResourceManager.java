package tankrotationexample.Resources;

import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();
    private final static Map<String, Sound> sounds = new HashMap<>();
    private final static Map<String, Integer> animationInfo = new HashMap<>() {{
        put("bullethit", 24);
        put("bulletshoot", 24);
        put("powerpick", 24);
        put("puffsmoke", 24);
        put("rocketflame", 24);
        put("rockethit", 24);
    }};


    private static BufferedImage loadSprites(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path)));
    }

    private static void initSprites() {
        try {
            ResourceManager.sprites.put("tank1", loadSprites("tank/tank1.png"));
            ResourceManager.sprites.put("tank2", loadSprites("tank/tank2.png"));

            ResourceManager.sprites.put("bullet", loadSprites("bullet/bullet.jpg"));
            ResourceManager.sprites.put("rocket1", loadSprites("bullet/rocket1.png"));
            ResourceManager.sprites.put("rocket2", loadSprites("bullet/rocket2.png"));

            ResourceManager.sprites.put("unbreak", loadSprites("walls/unbreak.jpg"));
            ResourceManager.sprites.put("break1", loadSprites("walls/break1.jpg"));
            ResourceManager.sprites.put("break2", loadSprites("walls/break2.jpg"));

            ResourceManager.sprites.put("speed", loadSprites("powerups/speed.png"));
            ResourceManager.sprites.put("health", loadSprites("powerups/health1.png"));
            ResourceManager.sprites.put("shield", loadSprites("powerups/shield.png"));

            ResourceManager.sprites.put("floor", loadSprites("floor/bg.bmp"));
            ResourceManager.sprites.put("menu", loadSprites("menu/title.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void initAnimations() {
//        String baseName = "animations/%s/%s_%04d.png";
//        animation.forEach((animationName, frameCount) -> {
//            for (int i = 0; i < frameCount; i++) {
//                String spritePath = baseName.formatted()
//            });
//        }
//    }

    public static void loadResources() {
        ResourceManager.initSprites();
    }

    public static BufferedImage getSprite(String type) {
        if (!ResourceManager.sprites.containsKey(type)) {
            throw new RuntimeException("%s is missing from sprite resources".formatted(type));
        }
        return ResourceManager.sprites.get(type);
    }

    public static Sound getSounds(String type) {
        if (!ResourceManager.sounds.containsKey(type)) {
            throw new RuntimeException("%s is missing from sound resources".formatted(type));
        }
        return ResourceManager.sounds.get(type);
    }

    public static Object getAnimation(String bullet) {
        return null;
    }
}

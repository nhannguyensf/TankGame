package tankrotationexample.Resources;

import tankrotationexample.game.Sound;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, List<BufferedImage>> animations = new HashMap<>();
    private final static Map<String, Sound> sounds = new HashMap<>();
    private final static Map<String, Integer> animationInfo = new HashMap<>() {{
        put("bullethit", 24);
        put("bulletshoot", 24);
        put("powerpick", 32);
        put("puffsmoke", 32);
        put("rocketflame", 16);
        put("rockethit", 32);
    }};


    private static BufferedImage loadSprites(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResource(path)));
    }

    private static void initSprites() {
        try {
            ResourceManager.sprites.put("tank1", loadSprites("tank/tank1.png"));
            ResourceManager.sprites.put("tank2", loadSprites("tank/tank2.png"));
            ResourceManager.sprites.put("tankAI", loadSprites("tank/jet5.png"));


            ResourceManager.sprites.put("bullet", loadSprites("bullet/bullet.jpg"));
            ResourceManager.sprites.put("rocket1", loadSprites("bullet/rocket1.png"));
            ResourceManager.sprites.put("rocket2", loadSprites("bullet/rocket2.png"));

            ResourceManager.sprites.put("unbreak", loadSprites("walls/unbreak.jpg"));
            ResourceManager.sprites.put("break1", loadSprites("walls/break1.jpg"));
            ResourceManager.sprites.put("break2", loadSprites("walls/break2.jpg"));

            ResourceManager.sprites.put("speed", loadSprites("powerups/speed.png"));
            ResourceManager.sprites.put("health", loadSprites("powerups/health1.png"));
            ResourceManager.sprites.put("shield", loadSprites("powerups/shield.png"));

//            ResourceManager.sprites.put("floor", loadSprites("floor/floor1.jpeg"));
            ResourceManager.sprites.put("floor", loadSprites("floor/bg.bmp"));
            ResourceManager.sprites.put("menu", loadSprites("menu/title.png"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initAnimations() {
        String baseName = "animations/%s/%s_%04d.png";
        animationInfo.forEach((animationName, frameCount) -> {
            List<BufferedImage> frames = new ArrayList<>();
            try {
                for (int i = 0; i < frameCount; i++) {
                    String spritePath = baseName.formatted(animationName, animationName, i);
                    frames.add(loadSprites(spritePath));
                }
                ResourceManager.animations.put(animationName, frames);
            } catch (IOException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        });
    }

    public static void loadResources() {
        ResourceManager.initSprites();
        ResourceManager.initAnimations();
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

    public static List<BufferedImage> getAnimation(String type) {
        if (!ResourceManager.animations.containsKey(type)) {
            throw new RuntimeException("%s is missing from animation resources".formatted(type));
        }
        return ResourceManager.animations.get(type);
    }

//    public static void main(String[] args) {
//        ResourceManager.loadResources();
//        System.out.println();
//    }
}

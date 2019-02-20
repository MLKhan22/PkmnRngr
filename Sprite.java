import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
    
    private static BufferedImage spriteSheet;
    private static int TILE_WIDTH;
    private static int TILE_HEIGHT;
    
    public static BufferedImage loadSprite(String file) {
        
        BufferedImage sprite = null;
        
        try {
            sprite = ImageIO.read(new File("Sprites/" + file + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return sprite;
    }
    
    public static BufferedImage getSprite(int xGrid, int yGrid, String sheet, int width, int height) {
        TILE_WIDTH = width;
        TILE_HEIGHT = height;
        spriteSheet = loadSprite(sheet);
        
        return spriteSheet.getSubimage(xGrid * TILE_WIDTH, yGrid * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
    }
    
}
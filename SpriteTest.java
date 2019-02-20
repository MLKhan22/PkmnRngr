import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.event.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;

public class SpriteTest extends JFrame{
    public static Sprite spr;
    boolean isRunning = true;
    int fps = 30;
    int width = 1000;
    int height = 800;
    int widthI = 32;
    int heightI = 32;
    BufferedImage backBuffer;
    Insets insets;
    KeyHandler keyEvt;
    MouseHandler mouseEvt;
    Rectangle2D rect;
    
    double x = 100;
    double y = 100;

    public static void main(String[] args){
        SpriteTest test = new SpriteTest();
        test.run();
        System.exit(0);
    }
    
    // Runs the Game
    public void run(){
        initialize();
        long time = System.currentTimeMillis();
        while(isRunning){
            update();
            draw();
        }
        time = (1000 / fps) - (System.currentTimeMillis() - time);
        if (time > 0){
            try
            {
                Thread.sleep(time);
            }
            catch(Exception e){}
        }
        setVisible(false);
    }
    
    //Initializing Stuff
    void initialize(){
        setTitle("SpriteTest");
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        insets = getInsets();
        setSize(insets.left + width + insets.right,
                insets.top + height + insets.bottom);
        
        backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        keyEvt = new KeyHandler(this);
        mouseEvt = new MouseHandler(this);
        spr = new Sprite();
        rect = new Rectangle2D.Double();
    }
    void update(){
        x = Math.floor(MouseInfo.getPointerInfo().getLocation().x);
        y = Math.floor(MouseInfo.getPointerInfo().getLocation().y);

        if (keyEvt.isKeyDown(KeyEvent.VK_LEFT)){
            widthI -= 1;
        }
        if (keyEvt.isKeyDown(KeyEvent.VK_RIGHT)){
            widthI += 1;
        }
        if (keyEvt.isKeyDown(KeyEvent.VK_UP)){
            heightI -= 1;
        }
        if (keyEvt.isKeyDown(KeyEvent.VK_DOWN)){
            heightI += 1;
        }
    }
    void draw(){
        
        Graphics g = getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        Graphics bb = backBuffer.getGraphics();
        Graphics2D bbg = (Graphics2D) bb;
        
        bbg.setColor(Color.WHITE);
        bbg.fillRect(0, 0, width, height);
        bbg.setColor(Color.RED);
        //Animate
        //animate(pika,bbg);
        bbg.drawString("Tile_Width: "+String.valueOf(widthI),100,80);
        bbg.drawString("Tile_Height: "+String.valueOf(heightI),100,90);
        bbg.setColor(Color.BLUE);

        //Sprite Testing//////////////// getScaledInstance(200,300,Image.SCALE_DEFAULT)
        bbg.drawImage(spr.loadSprite("CapStyle"),(int)100, (int)100,null);
        
        int inix = (int)Math.floor(x);
        int iniy = (int)Math.floor(y);
        bbg.drawRect(inix,iniy,widthI,heightI);
        bbg.drawRect(inix+widthI,iniy,widthI,heightI);
        bbg.drawRect(inix+(2*widthI),iniy,widthI,heightI);
        
        bbg.drawRect(inix,iniy+heightI,widthI,heightI);
        bbg.drawRect(inix+widthI,iniy+heightI,widthI,heightI);
        bbg.drawRect(inix+(2*widthI),iniy+heightI,widthI,heightI);
        
        bbg.drawRect(inix,iniy+(2*heightI),widthI,heightI);
        bbg.drawRect(inix+widthI,iniy+(2*heightI),widthI,heightI);
        bbg.drawRect(inix+(2*widthI),iniy+(2*heightI),widthI,heightI);
        
        bbg.drawRect(inix,iniy+(3*heightI),widthI,heightI);
        bbg.drawRect(inix+widthI,iniy+(3*heightI),widthI,heightI);
        bbg.drawRect(inix+(2*widthI),iniy+(3*heightI),widthI,heightI);
        
        ////////////////////////////////

        g2.drawImage(backBuffer, insets.left-10, insets.top-60, this);
        
        
    }
    
}













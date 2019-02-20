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

public class PokemonRangerTest extends JFrame{
    
    public static HashMap<ArrayList<Double>,String> visits = new HashMap<ArrayList<Double>,String>();
    public static ArrayList<ArrayList<Double>> coords = new ArrayList<ArrayList<Double>>();
    
    public static ArrayList<ArrayList<Double>> drizzle = new ArrayList<ArrayList<Double>>();
    public static ArrayList<ArrayList<Double>> trail = new ArrayList<ArrayList<Double>>();
    
    public static HashMap<Line2D,String> lineVisits = new HashMap<Line2D,String>();
    public static ArrayList<Line2D> LINES = new ArrayList<Line2D>();
    boolean isRunning = true;
    int fps = 30;
    static double xScale = 1.8;
    static double yScale = 1.3;
    int width = (int)(Math.floor(1000*xScale));
    int height = (int)(Math.floor(800*yScale));
    
    public static int score;
    public static int health;
    public static int myHealth;
    public static int speed;
    public static int boostTime;
    public static boolean boost;
    public static boolean shot;
    public static int captureTimer;
    public static boolean captureOn;
    public static int animationTimer;
    public static int ballTimer;
    public static boolean gameOver;
    public static boolean closed;
    public static boolean out;
    public static boolean opening;
    public static int pokes;
    public static int backX;
    public static int backY;
    
    final int cTime = 40;
    final int maxBoostT = 50;
    final int maxHealth = 15;
    int pokeMaxH = 0;
    
    BufferedImage backBuffer;
    Insets insets;
    static KeyHandler keyEvt;
    static MouseHandler mouseEvt;
    Path2D containPath;
    Path2D intersectPath;
    Rectangle2D rect;
    public static Sprite spr;
    Pokemon poke;
    Pokemon pika;
    Pokemon Char;
    Pokemon Bulb;
    Pokemon Squirt;
    Bullet b;
    
    public static double pkmnX = 500;
    public static double pkmnY = 500;
    
    public static double x = 100;
    public static double y = 100;
    
    public static void main(String[] args){
        PokemonRangerTest game = new PokemonRangerTest();
        game.run();
        System.exit(0);
    }
    
    // Runs the Gamer
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
        setTitle("PokemonRanger_Knockoff");
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
        containPath = new Path2D.Double();
        intersectPath = new Path2D.Double();
        
        backX = 1;
        backY = 2;
        
        pika = new Pokemon();
        pika.pix_W = (int)(Math.floor(30*xScale));
        pika.pix_H = (int)(Math.floor(40*yScale));
        pika.speed = 10;
        pika.attSpeed = 20;
        pika.health = 5;
        pika.maxHealth = 5;
        
        Char = new Pokemon();
        Char.pix_W = (int)(Math.floor(50*xScale));
        Char.pix_H = (int)(Math.floor(45*yScale));
        Char.speed = 3;
        Char.attSpeed = 5;
        Char.health = 10;
        Char.maxHealth = 10;
        
        Bulb = new Pokemon();
        Bulb.pix_W = (int)(Math.floor(30*xScale));
        Bulb.pix_H = (int)(Math.floor(30*yScale));
        Bulb.speed = 6;
        Bulb.attSpeed = 5;
        Bulb.health = 7;
        Bulb.maxHealth = 7;
        
        Squirt = new Pokemon();
        Squirt.pix_W = (int)(Math.floor(30*xScale));
        Squirt.pix_H = (int)(Math.floor(30*yScale));
        Squirt.speed = 6;
        Squirt.attSpeed = 5;
        Squirt.health = 7;
        Squirt.maxHealth = 7;
        
        poke = new Pokemon();
        
        pokes = 0;
        
        spr = new Sprite();
        rect = new Rectangle2D.Double();
        
        score = 0;
        health = 10;
        myHealth = maxHealth;
        //speed = 10;
        boost = false;
        boostTime = maxBoostT;
        shot = false;
        b = new Bullet();
        captureTimer = cTime;
        captureOn = false;
        animationTimer = 0;
        closed = false;
        gameOver = false;
        out = false;
        ballTimer = 15;
        opening = true;
    }
    void update(){
        if(!gameOver){
            if(pokes == 3){
                poke.pix_W = Squirt.pix_W;
                poke.pix_H = Squirt.pix_H;
                health = Squirt.health;
                speed = Squirt.speed;
                b.speed = Squirt.attSpeed;
                pokeMaxH = Squirt.maxHealth;
                poke.speed = Squirt.speed;
            }
            if(pokes == 2){
                poke.pix_W = Bulb.pix_W;
                poke.pix_H = Bulb.pix_H;
                health = Bulb.health;
                speed = Bulb.speed;
                b.speed = Bulb.attSpeed;
                pokeMaxH = Bulb.maxHealth;
                poke.speed = Bulb.speed;
            }
            if(pokes == 1){
                poke.pix_W = Char.pix_W;
                poke.pix_H = Char.pix_H;
                health = Char.health;
                speed = Char.speed;
                b.speed = Char.attSpeed;
                pokeMaxH = Char.maxHealth;
                poke.speed = Char.speed;
            }
            if(pokes == 0){
                poke.pix_W = pika.pix_W;
                poke.pix_H = pika.pix_H;
                health = pika.health;
                speed = pika.speed;
                b.speed = pika.attSpeed;
                pokeMaxH = pika.maxHealth;
                poke.speed = pika.speed;
            }
            int numPix = 0;
            animationTimer++;
            if(animationTimer == 15) animationTimer = 0;
            if(!boost && (boostTime < maxBoostT)) boostTime++;
            if(boost && (boostTime > 0)){
                boostTime--;
                if(trail.size() < 6){
                    ArrayList<Double> pikaCoord = new ArrayList<Double>();
                    pikaCoord.add(pkmnX+20.0);
                    pikaCoord.add(pkmnY+40.0);
                    trail.add(pikaCoord);
                }
                else{
                    ArrayList<Double> pikaCoord = new ArrayList<Double>();
                    pikaCoord.add(pkmnX+20.0);
                    pikaCoord.add(pkmnY+40.0);
                    trail.add(pikaCoord);
                    trail.remove(0);
                }
            }
            if(boost && (boostTime == 0)){
                speed = 10;
                trail.clear();
            }
            
            // Random Drizzly Movement
            for(int i = 0; i < drizzle.size(); i++){
                ArrayList<Double> movedDrizz = new ArrayList<Double>();
                double rand = Math.random();
                double shake = 2*Math.random();
                /*if(rand < 0.5){
                    shake = shake*-1;
                }*/
                movedDrizz.add(drizzle.get(i).get(0)/*+shake*/);
                movedDrizz.add(drizzle.get(i).get(1)+shake);
                drizzle.set(i,movedDrizz);
                if(drizzle.get(i).get(1) > 1000){
                    numPix++;
                }
            }
            if(numPix == drizzle.size()){
                drizzle.clear();
            }
            x = Math.floor(mouseEvt.x);
            y = Math.floor(mouseEvt.y+50);
	
	    if(Double.compare(x,Math.floor(10*xScale)) < 0){
		x = Math.floor(10*xScale);
	    }
	    if(Double.compare(x,Math.floor(885*xScale)) > 0){
		x = Math.floor(885*xScale);
	    }
	    if(Double.compare(y,Math.floor(120*yScale)) < 0){
		y = Math.floor(120*yScale);
	    }
	    if(Double.compare(y,Math.floor(750*yScale)) > 0){
		y = Math.floor(750*yScale);
	    }

            if( coords.size() == 0 && LINES.size() == 0){
                ArrayList<Double> t = new ArrayList<Double>();
                Line2D tLine = new Line2D.Double();
                tLine.setLine(x,y,x,y);
                t.add(x);
                t.add(y);
                
                coords.add(t);
                visits.put(t,"visited");
                
                LINES.add(tLine);
                lineVisits.put(tLine,"visited");
                
                //containPath.moveTo(x,y);
                intersectPath.moveTo(x,y);
            }
            //x = Math.floor(MouseInfo.getPointerInfo().getLocation().x);
            //y = Math.floor(MouseInfo.getPointerInfo().getLocation().y);
            
            
            
            ArrayList<Double> coord = new ArrayList<Double>();
            coord.add(x);
            coord.add(y);
            
            Line2D line = new Line2D.Double();
            line.setLine(LINES.get(LINES.size()-1).getX2(),LINES.get(LINES.size()-1).getY2(),x,y);
            
            //////////////////////////////////////////////////////
            ///Key Press Stuff/////////
            //////////////////////////////////////////////////////
            
            ////////Change Background/////////
            /*if(keyEvt.isKeyDown(KeyEvent.VK_LEFT) && backX > 0){
                backX--;
            }
            if(keyEvt.isKeyDown(KeyEvent.VK_RIGHT) && backX < 3){
                backX++;
            }
            if(keyEvt.isKeyDown(KeyEvent.VK_DOWN) && backY < 4){
                backY++;
            }
            if(keyEvt.isKeyDown(KeyEvent.VK_UP) && backY > 0){
                backY--;
            }*/
	    if(keyEvt.isKeyDown(KeyEvent.VK_B)){
		backX = (int) (Math.floor((Math.random() * 4)));
		backY = (int) (Math.floor((Math.random() * 5)));
	    }
            ////////////////////////////////////
            
            // Switch Pokemon
            if(out && (keyEvt.isKeyDown(KeyEvent.VK_C))){
                out = false;
		if(pokes == 3){
                        pokes = 0;
                    }
                else pokes++;
		rect = new Rectangle2D.Double();
                    b.Down = true;
                    b.Left = true;
                    b.speed = 0;
                    b.timer = 0;
                    shot = false;
            }
            if(!out){
                if(ballTimer < 14){
                    ballTimer++;
                }
            }
            if(!out && !keyEvt.isKeyDown(KeyEvent.VK_C)){
                opening = true;
            }
            if(opening){
                if(ballTimer < 30){
                    ballTimer++;
                }
                if(ballTimer == 30){
                    ballTimer = 0;
                    out = true;
                    opening = false;
                }
            }
            // Speed Boost
            if(out && keyEvt.isKeyDown(KeyEvent.VK_X)){
                if(boostTime > 0){
                    boost = true;
                    speed = poke.speed*2;
                }
                else speed = poke.speed;
            }
            if(!keyEvt.isKeyDown(KeyEvent.VK_X)){
                boost = false;
                speed = poke.speed;
                trail.clear();
            }
            
            ////////Moving////////////////////////
            if(out && (keyEvt.isKeyDown(KeyEvent.VK_LEFT) && (pkmnX > 0))){
                pkmnX = pkmnX - (int)(Math.floor(speed*xScale));
                poke.Left = true;
            }
            
            if(out && (keyEvt.isKeyDown(KeyEvent.VK_RIGHT) && (pkmnX < (int)(Math.floor(800*xScale))))){
                pkmnX = pkmnX + (int)(Math.floor(speed*xScale));
                poke.Left = false;
            }
            
            
            ////////////////////
            // Use "S" key for down movement if bluetooth doesn't work
            if(out && (keyEvt.isKeyDown(KeyEvent.VK_DOWN) && (pkmnY < (int)(Math.floor(665*yScale))))){
                pkmnY = pkmnY + (int)(Math.floor(speed*yScale));
                poke.Down = true;
            }
            
            /*if(out && (keyEvt.isKeyDown(KeyEvent.VK_S) && (pkmnY < 665))){
             pkmnY = pkmnY + speed;
             pika.Down = true;
             }*/
            ///////////////////
            if(out && (keyEvt.isKeyDown(KeyEvent.VK_UP) && (pkmnY > 125))){
                pkmnY = pkmnY - (int)(Math.floor(speed*yScale));
                poke.Down = false;
            }
            /////////////////////////////////////
            
            // Shooting
            if(out && (keyEvt.isKeyDown(KeyEvent.VK_SPACE))){
                if(!shot){
                    shot = true;
                    shoot(rect,poke,b);
                }
            }
            
            // Check if a bullet exists
            if(shot){
                moveBullet(rect,b);
                if(b.timer > 20){
                    //System.out.println("Remove Bullet");
                    rect = new Rectangle2D.Double();
                    b.Down = true;
                    b.Left = true;
                    b.speed = 0;
                    b.timer = 0;
                    shot = false;
                }
            }
            
            // Check if Pokemon is moving
            if( !keyEvt.isKeyDown(KeyEvent.VK_LEFT) && !keyEvt.isKeyDown(KeyEvent.VK_RIGHT) &&
               !keyEvt.isKeyDown(KeyEvent.VK_DOWN) && !keyEvt.isKeyDown(KeyEvent.VK_UP)){
                poke.moving = false;
            }
            else poke.moving = true;
            
            
            //////////////////////////////////////////////////////
            //////////////////////////////////////////////////////
            //////////////////////////////////////////////////////
            
            
            //////////////////////////////////////////////////////
            ///Mousey Stuff/////////
            //////////////////////////////////////////////////////
            
            // Starts the Capturing process while mouse is pressed
            if(mouseEvt.pressStat){
                captureOn = true;
                if(captureTimer > 0){
                    if(!visits.containsKey(coord)){
                        
                        coords.add(coord);
                        visits.put(coord,"visited");
                        
                        //System.out.println("Adding Line");
                        //System.out.println("Lines: "+lineVisits.size());
                        LINES.add(line);
                        lineVisits.put(line,"visited");
                        //if(!closed){
                        //containPath.lineTo(coord.get(0),coord.get(1));
                        if(LINES.size() > 1 && (Double.compare(line.getX1(),line.getX2()) != 0 &&
                                                Double.compare(line.getY1(),line.getY2()) != 0 )){
                            for(int i = 0; i < LINES.size()-2; i++){
                                
                                //System.out.println("Line Size: "+LINES.size());
                                if( line.intersectsLine(LINES.get(i)) ){
                                    closed = true;
                                    //System.out.println("Closing Path");
                                    containPath.moveTo(LINES.get(i).getX2(),LINES.get(i).getY2());
                                    //containPath.lineTo(LINES.get(i).getX1(),LINES.get(i).getY1());
                                    for(int j = LINES.size()-2; j > 0; j--){
                                        if( j != i ){
                                            containPath.lineTo(LINES.get(j).getX2(),LINES.get(j).getY2());
                                            //System.out.println("Closing Path");
                                            //containPath.closePath();
                                        }
                                        else{
                                            containPath.lineTo(LINES.get(j).getX2(),LINES.get(j).getY2());
                                            containPath.lineTo(LINES.get(j).getX1(),LINES.get(j).getY1());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        //}
                        /*else{
                         containPath.lineTo(coord.get(0),coord.get(1));
                         containPath.closePath();
                         }*/
                        intersectPath.lineTo(coord.get(0),coord.get(1));
                        intersectPath.moveTo(coord.get(0),coord.get(1));
                        intersectPath.closePath(); 
                    }
			/*if(((x < (int)(Math.floor(10*xScale))) || (x > (int)(Math.floor(900*xScale)))) || ((y < (int)(Math.floor(100*xScale))) || (y > (int)(Math.floor(750*xScale))))){
			coords.clear();
                    	visits.clear();
                    
                    	LINES.clear();
                    	lineVisits.clear();
                    
                    	containPath.reset();
                    	intersectPath.reset();
			
    		    }*/
                }
                
                if(captureTimer == 0){
                    captureOn = false;
                    if(containPath.contains(pkmnX+10.0,pkmnY+20.0) && closed){
                        //System.out.println("Contains");
                        score += 10;
                        if(pokes == 3){
                            Squirt.health--;
                        }
                        if(pokes == 2){
                            Bulb.health--;
                        }
                        if(pokes == 1){
                            Char.health--;
                        }
                        if(pokes == 0){
                            pika.health--;
                        }
                    }
                    coords.clear();
                    visits.clear();
                    
                    LINES.clear();
                    lineVisits.clear();
                    
                    containPath.reset();
                    intersectPath.reset();
                }
                
            }
            
            
            // Reduces Timer by 1 during Capturing
            if(captureOn){
                captureTimer--;
            }
            
            // Check if Capture Path hits Pokemon or a bullet
            if((intersectPath.intersects(Math.floor(pkmnX+10.0),Math.floor(pkmnY+20.0),poke.pix_W,poke.pix_H)) ||
               (intersectPath.intersects(rect))){
                //System.out.println("Intersected");
                if(out && myHealth > 0){
                    myHealth--;
                }
                if(out && score > 0){
                    score -= 5;
                }
                coords.clear();
                visits.clear();
                
                LINES.clear();
                lineVisits.clear();
                
                containPath.reset();
                intersectPath.reset();
                
                closed = false;
            }
            
            // Clear Capture Path after MouseRelease
            if(!mouseEvt.pressStat){
                //System.out.println("Clearing");
                if(out &&(containPath.contains(pkmnX+10.0,pkmnY+20.0) && closed)){
                    //System.out.println("Contains");
                    score += 10;
                    if(pokes == 3){
                        Squirt.health--;
                    }
                    if(pokes == 2){
                        Bulb.health--;
                    }
                    if(pokes == 1){
                        Char.health--;
                    }
                    if(pokes == 0){
                        pika.health--;
                    }
                }
                coords.clear();
                visits.clear();
                
                LINES.clear();
                lineVisits.clear();
                
                containPath.reset();
                intersectPath.reset();
                drizzle = coords;
                coords.clear();
                visits.clear();
                
                LINES.clear();
                lineVisits.clear();
                
                containPath.reset();
                intersectPath.reset();
                captureTimer = cTime;
                captureOn = false;
                closed = false;
            }
            
            //////////////////////////////////////////////////////
            //////////////////////////////////////////////////////
            //////////////////////////////////////////////////////
            
            //System.out.println(visits.get(coord));
            //System.out.println(coords.get(coords.size()-1).get(0)+","+coords.get(coords.size()-1).get(1));
            
            // Test Close Path //
            // visits.containsKey(coord) && ((coords.get(coords.size()-1).get(0) != x) &&
            //                              (coords.get(coords.size()-1).get(1) != y))
            //
            //
            //
            //
            // path.contains(x+10.0,y) || path.contains(x-10.0,y) ||
            // path.contains(x,y-10.0) || path.contains(x,y+10.0)
            //
            //
            //
            // path.contains(((path.getBounds().x)+(path.getBounds().width/2)), ((path.getBounds().y)+(path.getBounds().height/2)))
            
            
            //Check if Game is over
            if(myHealth == 0){
                gameOver = true;
            }
            if(health == 0){
                gameOver = true;
            }
            
        }
    }
    
    /*
     //  Function that draws the game
     //  (Will remove hitboxes later)
     */
    void draw(){
        
        Graphics g = getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        Graphics bb = backBuffer.getGraphics();
        Graphics2D bbg = (Graphics2D) bb;
        
        bbg.setColor(Color.WHITE);
        bbg.fillRect(0, 0, width, height);
        
        bbg.drawImage(spr.getSprite(backX,backY,"Backgrounds",273,201).getScaledInstance((int)(Math.floor(900*xScale)),(int)(Math.floor(height*yScale)),Image.SCALE_DEFAULT),0,0,null);
        //bbg.setColor(Color.BLACK);
        //bbg.drawRect(0,0,900,height);
        //bbg.setColor(Color.RED);
        //bbg.drawImage(spr.getSprite(0,0,"pikaStand", 26, 27),(int)pkmnX, (int)pkmnY,null);
        if(closed) bbg.setColor(Color.YELLOW);
        else    bbg.setColor(Color.red);
        bbg.setStroke(new BasicStroke(5));
        bbg.draw(intersectPath);
        bbg.setStroke(new BasicStroke(1));
        //Animate//
	bbg.setColor(Color.YELLOW);
        for(int i = 0; i < trail.size(); i++){
            Integer tx = trail.get(i).get(0).intValue();
            Integer ty = trail.get(i).get(1).intValue();
            //bbg.drawRect(tx,ty,10,10);
            //System.out.println(Double.compare(Math.random(), 0.5));
            if(Double.compare(Math.random(), 0.5) < 0){
                bbg.drawImage(spr.getSprite(0,0,"Sparkle",48,48),tx,ty,null);
            }
            else {
                bbg.drawImage(spr.getSprite(1,0,"Sparkle",48,48),tx,ty,null);
            }
        }
        if(out){
            if(pokes == 0){
                animatePikachu(poke,bbg);
            }
            if(pokes == 1){
                animateCharizard(poke,bbg);
            }
            if(pokes == 2){
                animateBulbasaur(poke,bbg);
            }
            if(pokes == 3){
                animateSquirtle(poke,bbg);
            }
            if(health > -1){
                bbg.setColor(Color.RED);
            }
            if(health > (int)(Math.floor(pokeMaxH/4))){
                bbg.setColor(Color.YELLOW);
            }
            if(health > (int)(Math.floor(pokeMaxH/2))){
                bbg.setColor(Color.GREEN);
            }
            bbg.fillRect((int)Math.floor(pkmnX), (int)Math.floor(pkmnY), (int)(Math.floor((80/pokeMaxH)*health*xScale)), (int)(Math.floor(4*yScale)));
        }
        else{
            if(opening){
                animateOpen(bbg);
            }
            else animateBall(bbg);
        }
        bbg.setColor(Color.black);
        //////////
        
        //bbg.draw(intersectPath.getBounds().getBounds2D());
        /*bbg.drawRect((int)Math.floor(((intersectPath.getBounds().x)+(intersectPath.getBounds().width/2))),
         (int)Math.floor(((intersectPath.getBounds().y)+(intersectPath.getBounds().height/2))),
         10,10);*/
        //bbg.drawRect((int)Math.floor(pkmnX+10.0),(int)Math.floor(pkmnY+20.0),poke.pix_W,poke.pix_H);
        Font big = new Font ("Comic Sans MS", Font.PLAIN, 33);
        bbg.setStroke(new BasicStroke(50));
        bbg.setFont(big);
        bbg.drawString("Score: "+String.valueOf(score),(int)(Math.floor(100*xScale)),(int)(Math.floor(150*xScale)));
        bbg.setStroke(new BasicStroke(1));
        
        //bbg.drawString("Health: "+String.valueOf(health),(int)Math.floor(pkmnX),(int)Math.floor(pkmnY));
        
        
        //bbg.setColor(Color.BLUE);
        //bbg.drawOval((int)Math.floor(x), (int)Math.floor(y), 20, 20);
        
        
        //Sprite Testing//////////////// getScaledInstance(200,300,Image.SCALE_DEFAULT)
        /*bbg.drawImage(spr.loadSprite("pikaRun"),(int)100, (int)100,null);
         int width = 31;
         int height = 32;
         int inix = 100;
         int iniy = 100;
         bbg.drawRect(inix,inix,width,height);
         bbg.drawRect(inix+width,inix,width,height);
         bbg.drawRect(inix+(2*width),inix,width,height);
         
         bbg.drawRect(inix,inix+height,width,height);
         bbg.drawRect(inix+width,inix+height,width,height);
         bbg.drawRect(inix+(2*width),inix+height,width,height);
         
         bbg.drawRect(inix,inix+(2*height),width,height);
         bbg.drawRect(inix+width,inix+(2*height),width,height);
         bbg.drawRect(inix+(2*width),inix+(2*height),width,height);
         
         bbg.drawRect(inix,inix+(3*height),width,height);
         bbg.drawRect(inix+width,inix+(3*height),width,height);
         bbg.drawRect(inix+(2*width),inix+(3*height),width,height);
         */
        ////////////////////////////////
        
        //bbg.setColor(Color.RED);
        //bbg.draw(containPath);
        
        animateCursor(bbg, "Cursor", "CapStyle", 50,50, 60, 50);
        if(shot){
            //bbg.setColor(Color.YELLOW);
            //bbg.draw(rect);
            if(pokes == 3)   animateShot(bbg,"162",40,40,rect);
            //bbg.drawImage(spr.getSprite(0,0,"162",40,40),(int)rect.getX()-25, (int)rect.getY()-10,null);
            if(pokes == 2)   animateShot(bbg,"153",24,24,rect);
            //bbg.drawImage(spr.getSprite(0,0,"153",24,24),(int)rect.getX()-25, (int)rect.getY()-10,null);
            if(pokes == 1)   animateShot(bbg,"174",55,70,rect);
            //bbg.drawImage(spr.getSprite(0,0,"174",55,70),(int)rect.getX()-25, (int)rect.getY()-10,null);
            if(pokes == 0)  animateShot(bbg,"176",40,40,rect);
            //bbg.drawImage(spr.getSprite(0,0,"176",40,40),(int)rect.getX()-25, (int)rect.getY()-10,null);
        }
        bbg.setColor(Color.CYAN);
        for(int i = 0; i < drizzle.size(); i++){
            Integer dx = drizzle.get(i).get(0).intValue();
            Integer dy = drizzle.get(i).get(1).intValue();
            bbg.drawOval(dx,dy,2,2);
        }
        /*bbg.setColor(Color.RED);
         for(int i = 0; i < coords.size(); i++){
         Integer cx = coords.get(i).get(0).intValue();
         Integer cy = coords.get(i).get(1).intValue();
         bbg.drawOval(cx,cy,5,5);
         }*/
        bbg.setStroke(new BasicStroke(10));
        bbg.setColor(Color.DARK_GRAY);
        bbg.fillRect(0,0, (int)(Math.floor(width*xScale)), (int)(Math.floor(110*yScale)));
        bbg.fillRect(0,(int)(Math.floor(750*yScale)), (int)(Math.floor(width*xScale)), (int)(Math.floor(110*yScale)));
        bbg.setColor(Color.BLACK);
        bbg.drawRect((int)(Math.floor(900*xScale)),0,(int)(Math.floor(100*xScale)), (int)(Math.floor(height*yScale)));
        bbg.drawRect((int)(Math.floor(60*xScale)),(int)(Math.floor(50*yScale)),(int)(Math.floor(750*xScale)), (int)(Math.floor(50*yScale)));
        bbg.drawRect((int)(Math.floor(60*xScale)),(int)(Math.floor(760*yScale)),(int)(Math.floor(750*xScale)), (int)(Math.floor(50*yScale)));
        bbg.setColor(Color.DARK_GRAY);
        bbg.fillRect((int)(Math.floor(900*xScale)),0, (int)(Math.floor(100*xScale)), (int)(Math.floor(height*yScale)));
        bbg.setColor(Color.GRAY);
        bbg.fillRect((int)(Math.floor(60*xScale)),(int)(Math.floor(50*yScale)), (int)(Math.floor(750*xScale)), (int)(Math.floor(50*yScale)));
        bbg.fillRect((int)(Math.floor(60*xScale)),(int)(Math.floor(760*yScale)), (int)(Math.floor(750*xScale)), (int)(Math.floor(50*yScale)));
if(pokes == 3){
       	 bbg.setColor(Color.CYAN);
	}
if(pokes == 2){
       	 bbg.setColor(Color.GREEN.darker().darker().darker());
	}
if(pokes == 1){
       	 bbg.setColor(Color.ORANGE);
	}
	if(pokes == 0){
       	 bbg.setColor(Color.YELLOW);
	}
        bbg.fillRect((int)(Math.floor(60*xScale)),(int)(Math.floor(50*yScale)), (int)(Math.floor((750*xScale/maxBoostT)*boostTime)), (int)(Math.floor(50*yScale)));
        if(myHealth > -1){
            bbg.setColor(Color.RED);
        }
        if(myHealth > 2){
            bbg.setColor(Color.YELLOW);
        }
        if(myHealth > 6){
            bbg.setColor(Color.GREEN);
        }
        bbg.setFont(new Font("SansSerif", Font.BOLD, 10));
        bbg.drawString(String.valueOf(myHealth)+"/"+String.valueOf(maxHealth),(int)(Math.floor(15*xScale)),(int)(Math.floor(775*yScale)));
        bbg.fillRect((int)(Math.floor(60*xScale)),(int)(Math.floor(760*yScale)), (int)(Math.floor((750*xScale/maxHealth)*myHealth)), (int)(Math.floor(50*yScale)));
        bbg.setColor(Color.darkGray);
        bbg.fillRect((int)(Math.floor(900*xScale)),(int)(Math.floor(height*yScale-captureTimer*cTime)), (int)(Math.floor(100*xScale)), (int)(Math.floor(captureTimer*cTime*yScale)));
        bbg.setColor(Color.RED);
        bbg.fillRect((int)(Math.floor(920*xScale)),0, (int)(Math.floor(60*xScale)), (int)(Math.floor(height*yScale)));
        bbg.setColor(Color.BLUE);
        //bbg.fillRect(920,height-captureTimer*cTime, 60, captureTimer*cTime);
        bbg.fillRect((int)(Math.floor(920*xScale)),0, (int)(Math.floor(60*xScale)), (int)(Math.floor((height*yScale/cTime)*(cTime-captureTimer))));
        
        bbg.setFont(new Font("SansSerif", Font.BOLD, 40));
        if(gameOver && (health == 0)){
            bbg.setColor(Color.GREEN);
            bbg.drawString("Capture Complete: "+String.valueOf(score),(int)(Math.floor(200*xScale)),(int)(Math.floor(300*yScale)));
        }
        if(gameOver && (myHealth == 0)){
            bbg.setColor(Color.RED);
            bbg.drawString("Capture Fail: "+String.valueOf(score),(int)(Math.floor(300*xScale)),(int)(Math.floor(300*yScale)));
        }
        bbg.setColor(Color.BLACK);
        for(int b = 1; b < maxHealth; b++){
            bbg.fillRect((int)(Math.floor(60*xScale+(b * (750*xScale/maxHealth)))),(int)(Math.floor(760*yScale)), 5, 50);
        }
	
	bbg.setColor(Color.YELLOW);
	bbg.fillRect((int)(Math.floor(15*xScale)),(int)(Math.floor(50*yScale)), (int)(Math.floor(40*xScale)), (int)(Math.floor(50*yScale)));
	bbg.setColor(Color.GRAY);
	bbg.fillRect((int)(Math.floor(15*xScale)),(int)(Math.floor(60*yScale)), (int)(Math.floor(40*xScale)), (int)(Math.floor((45*yScale/maxBoostT)*(maxBoostT-boostTime))));
	bbg.drawImage(spr.loadSprite("Boost").getScaledInstance((int)(Math.floor(40*xScale)),(int)(Math.floor(45*yScale)),Image.SCALE_DEFAULT),(int)(Math.floor(15*xScale)),(int)(Math.floor(60*yScale)),null);
        
        g2.drawImage(backBuffer, insets.left-10, insets.top-60, this);
        
    }
    
    
    /*
     //  Functions to animate sprites
     */
    public static void animateCursor(Graphics2D bbg, String sprite, String sprite2, int W, int H, int W2, int H2){
	if(mouseEvt.pressStat){
        if(animationTimer < 5){
            bbg.drawImage(spr.getSprite(0,0,sprite,W,H).getScaledInstance(100,100,Image.SCALE_DEFAULT),(int)(Math.floor(x-50)), (int)(Math.floor(y-60)),null);
        }
        if(animationTimer >= 5 && animationTimer < 10){
            bbg.drawImage(spr.getSprite(1,0,sprite,W,H).getScaledInstance(100,100,Image.SCALE_DEFAULT),(int)(Math.floor(x-50)), (int)(Math.floor(y-60)),null);
        }
        if(animationTimer >= 10 && animationTimer <= 15){
            bbg.drawImage(spr.getSprite(2,0,sprite,W,H).getScaledInstance(100,100,Image.SCALE_DEFAULT),(int)(Math.floor(x-50)), (int)(Math.floor(y-60)),null);
        }
	}
else{
	if(animationTimer < 5){
            bbg.drawImage(spr.getSprite(0,0,sprite2,W2,H2)/*.getScaledInstance(70,70,Image.SCALE_DEFAULT)*/,(int)(Math.floor(x-40)), (int)(Math.floor(y-40)),null);
        }
        if(animationTimer >= 5 && animationTimer < 10){
            bbg.drawImage(spr.getSprite(1,0,sprite2,W2,H2)/*.getScaledInstance(70,70,Image.SCALE_DEFAULT)*/,(int)(Math.floor(x-40)), (int)(Math.floor(y-40)),null);
        }
        if(animationTimer >= 10 && animationTimer <= 15){
            bbg.drawImage(spr.getSprite(2,0,sprite2,W2,H2)/*.getScaledInstance(70,70,Image.SCALE_DEFAULT)*/,(int)(Math.floor(x-40)), (int)(Math.floor(y-40)),null);
        }
}

    }
    public static void animateShot(Graphics2D bbg, String sprite, int W, int H, Rectangle2D rect){
        if(animationTimer < 5){
            bbg.drawImage(spr.getSprite(0,0,sprite,W,H).getScaledInstance((int)(Math.floor(50*xScale)),(int)(Math.floor(50*yScale)),Image.SCALE_DEFAULT),(int)(Math.floor(rect.getX()-25)), (int)(Math.floor(rect.getY()-10)),null);
        }
        if(animationTimer >= 5 && animationTimer < 10){
            bbg.drawImage(spr.getSprite(0,1,sprite,W,H).getScaledInstance((int)(Math.floor(50*xScale)),(int)(Math.floor(50*yScale)),Image.SCALE_DEFAULT),(int)(Math.floor(rect.getX()-25)), (int)(Math.floor(rect.getY()-10)),null);
        }
        if(animationTimer >= 10 && animationTimer <= 15){
            bbg.drawImage(spr.getSprite(0,2,sprite,W,H).getScaledInstance((int)(Math.floor(50*xScale)),(int)(Math.floor(50*yScale)),Image.SCALE_DEFAULT),(int)(Math.floor(rect.getX()-25)), (int)(Math.floor(rect.getY()-10)),null);
        }
    }
    public static void animateBall(Graphics2D bbg){
        if(ballTimer < 5){
            bbg.drawImage(spr.getSprite(9,0,"Pokeball_Sprites2",16,25).getScaledInstance(34,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
            bbg.drawImage(spr.getSprite(5,1,"Smoke_Sprite",100,86).getScaledInstance(50,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
        }
        if(ballTimer >= 5 && ballTimer < 10){
            bbg.drawImage(spr.getSprite(9,0,"Pokeball_Sprites2",16,25).getScaledInstance(34,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
            bbg.drawImage(spr.getSprite(6,1,"Smoke_Sprite",100,86).getScaledInstance(50,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
        }
        if(ballTimer >= 10 && ballTimer <= 15){
            bbg.drawImage(spr.getSprite(10,0,"Pokeball_Sprites2",16,25).getScaledInstance(34,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
        }
    }
    public static void animateOpen(Graphics2D bbg){
        if(ballTimer >= 15 && ballTimer < 20){
            bbg.drawImage(spr.getSprite(10,0,"Pokeball_Sprites2",16,25).getScaledInstance(34,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
        }
        if(ballTimer >= 20 && ballTimer < 25){
            bbg.drawImage(spr.getSprite(9,0,"Pokeball_Sprites2",16,25).getScaledInstance(34,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
            bbg.drawImage(spr.getSprite(6,1,"Smoke_Sprite",100,86).getScaledInstance(50,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
        }
        if(ballTimer >= 25 && ballTimer <= 30){
            bbg.drawImage(spr.getSprite(9,0,"Pokeball_Sprites2",16,25).getScaledInstance(34,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
            bbg.drawImage(spr.getSprite(5,1,"Smoke_Sprite",100,86).getScaledInstance(50,50,Image.SCALE_DEFAULT),(int)pkmnX,(int)pkmnY,null);
        }
    }
    
    public static void animatePikachu(Pokemon poke, Graphics2D bbg){
        int scalew = 50;
        int scaleh = 60;
        int scalew2 = 40;
        int scaleh2 = 50;
        if(animationTimer < 5){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(0,0,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(0,1,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(0,2,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(0,3,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(0,0,"pikaStand", 27, 27).getScaledInstance((int)(Math.floor(scalew2*xScale)),(int)(Math.floor(scaleh2*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
            
        }
        if(animationTimer >= 5 && animationTimer < 10){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(1,0,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(1,1,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(1,2,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(1,3,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(1,0,"pikaStand", 27, 27).getScaledInstance((int)(Math.floor(scalew2*xScale)),(int)(Math.floor(scaleh2*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
        }
        if(animationTimer >= 10 && animationTimer <= 15){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(2,0,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(2,1,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(2,2,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(2,3,"pikaRun", 32, 32).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(2,0,"pikaStand", 26, 27).getScaledInstance((int)(Math.floor(scalew2*xScale)),(int)(Math.floor(scaleh2*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
        }
    }
    
    public static void animateBulbasaur(Pokemon poke, Graphics2D bbg){
        int w = 39;
        int h = 39;
        int standw = 37;
        int standh = 36;
        int scalew = 60;
        int scaleh = 60;
        int scalew2 = 55;
        int scaleh2 = 55;
        if(animationTimer < 5){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(0,0,"BulbasaurLeft", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(0,2,"BulbasaurLeft", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(3,2,"BulbasaurRight", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(3,0,"BulbasaurRight", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(0,0,"BulbasaurStand", standw, standh).getScaledInstance((int)(Math.floor(scalew2*xScale)),(int)(Math.floor(scaleh2*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
            
        }
        if(animationTimer >= 5 && animationTimer < 10){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(1,0,"BulbasaurLeft", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(1,2,"BulbasaurLeft", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(2,2,"BulbasaurRight", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(2,0,"BulbasaurRight", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(1,0,"BulbasaurStand", standw, standh).getScaledInstance((int)(Math.floor(scalew2*xScale)),(int)(Math.floor(scaleh2*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
        }
        if(animationTimer >= 10 && animationTimer <= 15){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(2,0,"BulbasaurLeft", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(2,2,"BulbasaurLeft", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(1,2,"BulbasaurRight", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(1,0,"BulbasaurRight", w, h).getScaledInstance((int)(Math.floor(scalew*xScale)),(int)(Math.floor(scaleh*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(2,0,"BulbasaurStand", standw, standh).getScaledInstance((int)(Math.floor(scalew2*xScale)),(int)(Math.floor(scaleh2*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
        }
    }
    
    public static void animateSquirtle(Pokemon poke, Graphics2D bbg){
        int w = 49;
        int h = 48;
        int standw = 48;
        int standh = 57;
        int wscale = 46;
        int hscale = 46;
        if(animationTimer < 5){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(0,0,"SquirtleDLUR", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(0,0,"SquirtleDRUL", w, h).getSubimage(3,0,w-3,h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(3,0,"SquirtleDLUR", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(3,0,"SquirtleDRUL", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(0,0,"SquirtleStand", standw, standh).getScaledInstance((int)(Math.floor(46*xScale)),(int)(Math.floor(50*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
            
        }
        if(animationTimer >= 5 && animationTimer < 10){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(1,0,"SquirtleDLUR", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(1,0,"SquirtleDRUL", w, h).getSubimage(3,0,w-3,h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(4,0,"SquirtleDLUR", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(4,0,"SquirtleDRUL", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(0,1,"SquirtleStand", standw, standh).getScaledInstance((int)(Math.floor(46*xScale)),(int)(Math.floor(50*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
        }
        if(animationTimer >= 10 && animationTimer <= 15){
            if(poke.moving){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(2,0,"SquirtleDLUR", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(2,0,"SquirtleDRUL", w, h).getSubimage(3,0,w-3,h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(5,0,"SquirtleDLUR", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(5,0,"SquirtleDRUL", w, h).getScaledInstance((int)(Math.floor(wscale*xScale)),(int)(Math.floor(hscale*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
            }
            else{
                bbg.drawImage(spr.getSprite(0,0,"SquirtleStand", standw, standh).getScaledInstance((int)(Math.floor(46*xScale)),(int)(Math.floor(50*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
        }
    }
    
    
    
    public static void animateCharizard(Pokemon poke, Graphics2D bbg){
        if(animationTimer < 7){
            if(poke.moving){
                //if(!shot){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(0,0,"CharLeft", 67, 54).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(0,1,"CharLeft", 67, 54).getSubimage(0,3,67,49).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(3,1,"CharRight", 67, 54).getSubimage(0,3,67,49).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(3,0,"CharRight", 67, 54).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                //}
            }
            else{
                bbg.drawImage(spr.getSprite(0,0,"CharStandAtt2", 67, 57).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                //bbg.drawImage(spr.loadSprite("CharAtt1")/*.getScaledInstance(75,75,Image.SCALE_DEFAULT)*/,(int)pkmnX, (int)pkmnY,null);
                //bbg.drawImage(spr.getSprite(1,0,"CharStandAtt2", 67, 57).getScaledInstance(75,75,Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
            
        }
        if(animationTimer >= 7 ){
            if(poke.moving){
                //if(!shot){
                if(poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(1,0,"CharLeft", 67, 54).getSubimage(3,0,64,54).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(1,1,"CharLeft", 67, 54).getSubimage(2,3,64,51).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && !poke.Down){
                    bbg.drawImage(spr.getSprite(2,1,"CharRight", 67, 54).getSubimage(0,3,65,49).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                if(!poke.Left && poke.Down){
                    bbg.drawImage(spr.getSprite(2,0,"CharRight", 67, 54).getSubimage(0,0,65,54).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                }
                //}
            }
            else{
                bbg.drawImage(spr.getSprite(1,0,"CharStandAtt2", 67, 57).getScaledInstance((int)(Math.floor(75*xScale)),(int)(Math.floor(75*yScale)),Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
                //bbg.drawImage(spr.loadSprite("CharAtt2")/*.getScaledInstance(75,75,Image.SCALE_DEFAULT)*/,(int)pkmnX, (int)pkmnY,null);
                //bbg.drawImage(spr.loadSprite("CharAtt1").getScaledInstance(55,75,Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
            }
        }
        /* if(animationTimer >= 10 && animationTimer <= 15){
         if(poke.moving){
         if(poke.Left && poke.Down){
         bbg.drawImage(spr.getSprite(2,0,"pikaRun", 32, 32).getScaledInstance(50,75,Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
         }
         if(poke.Left && !poke.Down){
         bbg.drawImage(spr.getSprite(2,1,"pikaRun", 32, 32).getScaledInstance(50,75,Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
         }
         if(!poke.Left && !poke.Down){
         bbg.drawImage(spr.getSprite(2,2,"pikaRun", 32, 32).getScaledInstance(50,75,Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
         }
         if(!poke.Left && poke.Down){
         bbg.drawImage(spr.getSprite(2,3,"pikaRun", 32, 32).getScaledInstance(50,75,Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
         }
         }
         else{
         bbg.drawImage(spr.getSprite(2,0,"pikaStand", 26, 27).getScaledInstance(40,60,Image.SCALE_DEFAULT),(int)pkmnX, (int)pkmnY,null);
         }
         }*/
    }
    
    /*
     //  Function to start the bullet
     */
    public static void shoot(Rectangle2D rect, Pokemon poke, Bullet b){
        double bx = -10.0;
        double by = 40.0;
        if(poke.moving){
            if(poke.Down){
                Bullet.Down = true;
                by = 60.0;
            }
            else{
                Bullet.Down = false;
                by = -20.0;
            }
            if(poke.Left){
                Bullet.Left = true;
		bx = -20.0;	
            }
            else{
                Bullet.Left = false;
	     	if( pokes == 1 ){
		   bx = 100.0;
		}
                else bx = 60.0;
            }
        }
        rect.setRect(pkmnX+bx,pkmnY+by,10,10);
        
    }
    
    /*
     //  Function to move the bullet's position
     */
    public static void moveBullet(Rectangle2D rectangle, Bullet b){
        int xLoc = 0;
        int yLoc = 0;
        if(b.Down){
            yLoc = b.speed;
        }
        else yLoc = (b.speed*-1);
        if(b.Left){
            xLoc = (b.speed*-1);
        }
        else xLoc = b.speed;
        
        rectangle.setRect(rectangle.getX()+(int)(Math.floor(xLoc*xScale)),rectangle.getY()+(int)(Math.floor(yLoc*yScale)),(int)(Math.floor(10*xScale)),(int)(Math.floor(10*yScale)));
        b.timer++;
    }
    
    
}

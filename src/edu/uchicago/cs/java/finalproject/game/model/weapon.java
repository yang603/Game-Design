package edu.uchicago.cs.java.finalproject.game.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ethan on 12/1/2014.
 */
public class weapon extends Sprite {

    private final double FIRE_POWER = 35.0;
    public weapon(Falcon fal, Point p){
        super();
        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        assignPolarPoints(pntCs);

        //a bullet expires after 20 frames
        setExpire( 20 );
        setRadius(6);

        double r = Math.hypot((p.getX()-fal.getCenter().getX()),(p.getY()-fal.getCenter().getY()));

        setDeltaX( fal.getDeltaX() +
                (p.getX()-fal.getCenter().getX())/r * FIRE_POWER );
        setDeltaY( fal.getDeltaY() +
                (p.getY()-fal.getCenter().getY())/r * FIRE_POWER );
        setCenter( fal.getCenter() );

        double ang = Math.atan((p.getX() - fal.getCenter().getX()) / (p.getY() - fal.getCenter().getY()));
        //set the bullet orientation to the falcon (ship) orientation
        setOrientation((int)ang);
    }

    //override the expire method - once an object expires, then remove it from the arrayList.
    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movFriends.remove(this);
        else
            setExpire(getExpire() - 1);
    }

    public void draw(Graphics g) {
        Image img2 = null;
        try {
            File sourceimage2 = new File("src\\image\\blueBloom.png");
            img2 = ImageIO.read(sourceimage2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.drawImage(img2, getCenter().x, getCenter().y, null);

        if(getExpire()==0){
            Explode ex = new Explode(getCenter().x, getCenter().y, g);
        }
    }
}

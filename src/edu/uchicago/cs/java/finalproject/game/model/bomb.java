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
public class bomb extends Sprite {
    int lx,ly;

    public bomb(Falcon fal){
        super();
        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        assignPolarPoints(pntCs);

        //a bullet expires after 20 frames
        setExpire(40);
        setRadius(6);
        this.lx = fal.getCenter().x;
        this.ly = fal.getCenter().y;
        setOrientation(0);
    }

    //override the expire method - once an object expires, then remove it from the arrayList.
    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movFriends.remove(this);
        else
            setExpire(getExpire() - 1);
    }

    public void draw(Graphics g) {
        Image img3 = null;
        try {
            File sourceimage3 = new File("src\\image\\blueBall.png");
            img3 = ImageIO.read(sourceimage3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setCenter(new Point(lx,ly));
        g.drawImage(img3, lx, ly, null);

        if(getExpire()==0){
            Explode ex = new Explode(getCenter().x, getCenter().y, g);
        }
    }
}

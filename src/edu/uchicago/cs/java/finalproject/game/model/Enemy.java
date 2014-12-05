package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ethan on 11/30/2014.
 */
public class Enemy extends Sprite {

    // ==============================================================
    // FIELDS
    // ==============================================================
    private int nSpin;
    private File sourceimage;
    private String path;
    private int x, y;
    private double scope;




    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    public Enemy(String path, int x, int y, double scope) {
        super();
        this.path = path;
        this.x = x;
        this.y = y;
        this.scope = scope;

        ArrayList<Point> pntCs = new ArrayList<Point>();
        assignPolarPoints(pntCs);

        //the spin will be either plus or minus 0-9
        int nSpin = Game.R.nextInt(10);
        if(nSpin %2 ==0)
            nSpin = -nSpin;
        setSpin(nSpin);

        //random delta-x
        int nDX = Game.R.nextInt(10);
        if(nDX %2 ==0)
            nDX = -nDX;
        setDeltaX(nDX);

        //random delta-y
        int nDY = Game.R.nextInt(10);
        if(nDY %2 ==0)
            nDY = -nDY;
        setDeltaY(nDY);

        setColor(Color.white);

        setCenter(new Point(x, y));

        setOrientation(Game.R.nextInt(360));


        setFadeValue(0);


    }


    // ==============================================================
    // METHODS
    // ==============================================================


    public void move() {
        super.move();
        setOrientation(getOrientation() + getSpin());
    } //end move



    public void draw(Graphics g) {

        //does the fading at the beginning or after hyperspace
        Color colShip = Color.white;

        drawShipWithColor(g, colShip);

    } //end draw()


    public void drawShipWithColor(Graphics g, Color col) {
        super.draw(g);
//		g.setColor(col);
//		g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        Image img = null;
        try {
//            sourceimage = new File("src\\image\\ships\\ship3.png");
            sourceimage = new File(path);
            img = ImageIO.read(sourceimage);
            System.out.println(img.getWidth(null));
        } catch (IOException e) {
            e.printStackTrace();
        }



        this.setCenter(getCenter());
        this.setRadius(img.getWidth(null)/4);
          g.drawImage(img, this.getCenter().x-img.getWidth(null)*1/2, this.getCenter().y-img.getHeight(null)*1/2, null);
//        g.drawImage(img, mc.getX()+this.getCenter().x, mc.getY()+this.getCenter().y, null);
//        g.drawOval(mc.getX()+this.getCenter().x, mc.getY()+this.getCenter().y, img.getWidth(null),img.getHeight(null));
//        g.drawRect(this.getCenter().x-this.getRadius(), this.getCenter().y-this.getRadius(), 2*this.getRadius(),2*this.getRadius());


    }

    public int getSpin() {
        return this.nSpin;
    }


    public void setSpin(int nSpin) {
        this.nSpin = nSpin;
    }


} //end class

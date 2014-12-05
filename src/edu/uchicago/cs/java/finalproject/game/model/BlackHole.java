package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uchicago.cs.java.finalproject.controller.Game;

import javax.imageio.ImageIO;


public class BlackHole extends Sprite {

    // ==============================================================
    // FIELDS
    // ==============================================================;



    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    public BlackHole() {
        super();

        ArrayList<Point> pntCs = new ArrayList<Point>();



        assignPolarPoints(pntCs);

        setColor(Color.white);

        //put falcon in the middle.
        setCenter(new Point(Game.DIM.width / 2, Game.DIM.height / 2));

        //with random orientation
        setOrientation(Game.R.nextInt(360));

        //this is the size of the falcon
        setRadius(50);

    }


    // ==============================================================
    // METHODS
    // ==============================================================

    public void move() {
        super.move();
    } //end move



    public void draw(Graphics g) {

        //does the fading at the beginning or after hyperspace
        g.setColor(Color.black);
        //BlackHole
        this.setCenter(new Point(650,60));
        g.fillOval(600, 10, 100, 100);


    } //end draw()





} //end class

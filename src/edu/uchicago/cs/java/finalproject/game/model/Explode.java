package edu.uchicago.cs.java.finalproject.game.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ethan on 12/1/2014.
 */
public class Explode extends Sprite{
    private ArrayList<Image> images;
    private int x;
    private int y;
    private int idx = 0;
    private Graphics g;

    public Explode(int x, int y, Graphics g){
        Point p = new Point(x,y);
        setCenter(p);
        images = new ArrayList<Image>();
        Image img = null;
        this.g = g;
        try {
            for(int i=1;i<7;i++) {
                File sourceimage = new File("src\\image\\explosion\\"+i+".png");
                img = ImageIO.read(sourceimage);
                images.add(img);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.x = x;
        this.y = y;
        draw(g);
    }

    public void draw( Graphics g ) {
        int i=0;
        for(Image imgs : images){
                int count = 100;
                while(count!=0){
                    count--;
                }
                g.drawImage(imgs, x-imgs.getWidth(null)/2, y-imgs.getHeight(null)/2, null);
        }
        images.clear();
    }

}

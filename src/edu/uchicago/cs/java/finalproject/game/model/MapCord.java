package edu.uchicago.cs.java.finalproject.game.model;

/**
 * Created by Ethan on 12/2/2014.
 */
public class MapCord {
    private static int x;
    private static int y;

    public MapCord(int a, int b,double scope){

        x=a+(int)((400-CommandCenter.getFalcon1().getCenter().x)*scope);
        y=b+(int)((300-CommandCenter.getFalcon1().getCenter().y)*scope);

    }

    public static int getX() {
        return x;
    }

    public static void setX(int x) {
        MapCord.x = x;
    }

    public static int getY() {
        return y;
    }

    public static void setY(int y) {
        MapCord.y = y;
    }
}

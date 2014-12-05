package edu.uchicago.cs.java.finalproject.game.blackjack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

/**
 * Created by Ethan on 11/16/2014.
 */
public class Poker {
    HashMap<Integer, Image> cards = new HashMap<Integer, Image>();//the poker image
    HashMap<Integer, Integer> cardsNum = new HashMap<Integer, Integer>();//the unused poker number

    public static void main(String[] args) {
        Poker p = new Poker();
    }

    public Poker() {

        Image imgn = null;
        try {
            for(int i=1;i<=53;i++) {
                File sourceimagen = new File("src\\image\\poker_image\\"+i+".jpg");
                imgn = ImageIO.read(sourceimagen);
                cards.put(i,imgn);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

        for(int i=1;i<=52;i++){
            cardsNum.put(i,6);
        }
    }



}

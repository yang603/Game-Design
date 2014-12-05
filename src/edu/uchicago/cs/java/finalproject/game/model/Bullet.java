package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.Point;
import java.util.ArrayList;

import edu.uchicago.cs.java.finalproject.controller.Game;


public class Bullet extends Sprite {

	  private final double FIRE_POWER = 35.0;

	 
	
public Bullet(Falcon fal){

		super();


		//defined the points on a cartesean grid
		ArrayList<Point> pntCs = new ArrayList<Point>();

		pntCs.add(new Point(0,3)); //top point

		pntCs.add(new Point(1,-1));
		pntCs.add(new Point(0,-2));
		pntCs.add(new Point(-1,-1));

		assignPolarPoints(pntCs);

		//a bullet expires after 20 frames
	    setExpire( 20 );
	    setRadius(6);


	    //everything is relative to the falcon ship that fired the bullet
	    setDeltaX( fal.getDeltaX() +
	               Math.cos( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
	    setDeltaY( fal.getDeltaY() +
	               Math.sin( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
	    setCenter( fal.getCenter() );



	    //set the bullet orientation to the falcon (ship) orientation
	    setOrientation(fal.getOrientation());


	}

    public Bullet(Enemy en){

        super();
        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        pntCs.add(new Point(0,1)); //top point

        pntCs.add(new Point(1,0));
        pntCs.add(new Point(0,-1));
        pntCs.add(new Point(-1,0));

        assignPolarPoints(pntCs);

        //a bullet expires after 20 frames
        setExpire( 20 );
        setRadius(6);


        double r = Math.hypot((CommandCenter.getFalcon1().getCenter().x-en.getCenter().x),(CommandCenter.getFalcon1().getCenter().y-en.getCenter().y));
        setDeltaX( en.getDeltaX() +
                (CommandCenter.getFalcon1().getCenter().x-en.getCenter().x)/r * FIRE_POWER );
        setDeltaY( en.getDeltaY() +
                (CommandCenter.getFalcon1().getCenter().y-en.getCenter().y)/r * FIRE_POWER );
        setCenter( en.getCenter() );
        //everything is relative to the falcon ship that fired the bullet

        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(CommandCenter.getFalcon1().getOrientation()-en.getOrientation());


    }

    //override the expire method - once an object expires, then remove it from the arrayList. 
	public void expire(){
 		if (getExpire() == 0)
 			CommandCenter.movFriends.remove(this);
		 else 
			setExpire(getExpire() - 1);
	}

}

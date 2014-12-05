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


public class Falcon extends Sprite {

	// ==============================================================
	// FIELDS 
	// ==============================================================
	
	private final double THRUST = .65;
    private boolean isAlive = true;
    private static int count=0;
	final int DEGREE_STEP = 6;
	
	private boolean bShield = false;
	private boolean bFlame = false;


    private boolean bProtected; //for fade in and out
	
	private boolean bThrusting = false;
	private boolean bTurningRight = false;
	private boolean bTurningLeft = false;
    private boolean bBack = false;
	
	private int nShield;
			
	private final double[] FLAME = { 23 * Math.PI / 24 + Math.PI / 2,
			Math.PI + Math.PI / 2, 25 * Math.PI / 24 + Math.PI / 2 };

	private int[] nXFlames = new int[FLAME.length];
	private int[] nYFlames = new int[FLAME.length];

	private Point[] pntFlames = new Point[FLAME.length];

	
	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public Falcon() {
		super();

		ArrayList<Point> pntCs = new ArrayList<Point>();
		


		assignPolarPoints(pntCs);

		setColor(Color.white);
		
		//put falcon in the middle.
		setCenter(new Point(Game.DIM.width / 2, Game.DIM.height / 2));
		
		//with random orientation
		setOrientation(Game.R.nextInt(360));
		
		//this is the size of the falcon
		setRadius(30);

		//these are falcon specific
		setProtected(true);
		setFadeValue(0);
	}
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================

	public void move() {

        if(this.isAlive()) {
            super.move();
        }else{
            Point pnt = getCenter();
            double dX = pnt.x + getDeltaX();
            double dY = pnt.y + getDeltaY();
            setCenter(new Point((int) dX, (int) dY));
        }

		if (bThrusting) {
			bFlame = true;
			double dAdjustX = Math.cos(Math.toRadians(getOrientation()))
					* THRUST;
			double dAdjustY = Math.sin(Math.toRadians(getOrientation()))
					* THRUST;
			setDeltaX(getDeltaX() + dAdjustX);
			setDeltaY(getDeltaY() + dAdjustY);
		}
        if (bBack) {
            bFlame = true;
            double dAdjustX = Math.cos(Math.toRadians(getOrientation()))
                    * THRUST;
            double dAdjustY = Math.sin(Math.toRadians(getOrientation()))
                    * THRUST;
            setDeltaX(getDeltaX() - dAdjustX);
            setDeltaY(getDeltaY() - dAdjustY);
        }
		if (bTurningLeft) {

			if (getOrientation() <= 0 && bTurningLeft) {
				setOrientation(360);
			}
			setOrientation(getOrientation() - DEGREE_STEP);
		} 
		if (bTurningRight) {
			if (getOrientation() >= 360 && bTurningRight) {
				setOrientation(0);
			}
			setOrientation(getOrientation() + DEGREE_STEP);
		} 
	} //end move

	public void rotateLeft() {
		bTurningLeft = true;
	}

	public void rotateRight() {
		bTurningRight = true;
	}

	public void stopRotating() {
		bTurningRight = false;
		bTurningLeft = false;
	}

	public void thrustOn() {
		bThrusting = true;
	}

	public void thrustOff() {
		bThrusting = false;
		bFlame = false;
	}

    public void backOn(){ bBack = true; }

    public void backOff(){
        bBack = false;
        bFlame = false;
    }

	private int adjustColor(int nCol, int nAdj) {
		if (nCol - nAdj <= 0) {
			return 0;
		} else {
			return nCol - nAdj;
		}
	}

	public void draw(Graphics g) {

		//does the fading at the beginning or after hyperspace
		Color colShip;
		if (getFadeValue() == 255) {
			colShip = Color.white;
		} else {
			colShip = new Color(adjustColor(getFadeValue(), 200), adjustColor(
					getFadeValue(), 175), getFadeValue());
		}

		//shield on
		if (bProtected||(nShield > 0)) {

			setShield(getShield() - 1);

			g.setColor(Color.yellow);
			g.drawOval(getCenter().x - getRadius(),
					getCenter().y - getRadius(), getRadius() * 2,
					getRadius() * 2);

		} //end if shield

		//thrusting
		if (bFlame) {
			g.setColor(colShip);
			//the flame
			for (int nC = 0; nC < FLAME.length; nC++) {
				if (nC % 2 != 0) //odd
				{
					pntFlames[nC] = new Point((int) (getCenter().x + 2
							* getRadius()
							* Math.sin(Math.toRadians(getOrientation())
									+ FLAME[nC])), (int) (getCenter().y - 2
							* getRadius()
							* Math.cos(Math.toRadians(getOrientation())
									+ FLAME[nC])));

				} else //even
				{
					pntFlames[nC] = new Point((int) (getCenter().x + getRadius()
							* 1.1
							* Math.sin(Math.toRadians(getOrientation())
									+ FLAME[nC])),
							(int) (getCenter().y - getRadius()
									* 1.1
									* Math.cos(Math.toRadians(getOrientation())
											+ FLAME[nC])));

				} //end even/odd else

			} //end for loop

			for (int nC = 0; nC < FLAME.length; nC++) {
				nXFlames[nC] = pntFlames[nC].x;
				nYFlames[nC] = pntFlames[nC].y;

			} //end assign flame points

			//g.setColor( Color.white );
			g.fillPolygon(nXFlames, nYFlames, FLAME.length);

		} //end if flame

		drawShipWithColor(g, colShip);

	} //end draw()

	public void drawShipWithColor(Graphics g, Color col) {
		super.draw(g);
//		g.setColor(col);
//		g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);

        Image img2 = null;
        try {
            File sourceimage2 = new File("src\\image\\ships\\ship.png");
            img2 = ImageIO.read(sourceimage2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // rotate the ship
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(Math.toRadians(getOrientation()),getCenter().x, getCenter().y);
        //draw shape/image (will be rotated)

        if(count==0){
            g2d.rotate(Math.toRadians(90),getCenter().x, getCenter().y);
        }
        g.drawImage(img2, this.getCenter().x-img2.getWidth(null)/2, this.getCenter().y-img2.getHeight(null)/2, null);
        g2d.setTransform(old);
        //things you draw after here will not be rotated
//        g.drawOval(this.getCenter().x-getRadius()/2, this.getCenter().y-getRadius()/2, this.getRadius(),this.getRadius());


    }

	public void fadeInOut() {
		if (getProtected()) {
			setFadeValue(getFadeValue() + 3);
		}
		if (getFadeValue() == 255) {
			setProtected(false);
		}
	}
	
	public void setProtected(boolean bParam) {
		if (bParam) {
			setFadeValue(0);
		}
		bProtected = bParam;
	}

//	public void setProtected(boolean bParam, int n) {
//		if (bParam && n % 3 == 0) {
//			setFadeValue(n);
//		} else if (bParam) {
//			setFadeValue(0);
//		}
//		bProtected = bParam;
//	}

	public boolean getProtected() {return bProtected;}
	public void setShield(int n) {nShield = n;}
	public int getShield() {return nShield;}

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
	
} //end class

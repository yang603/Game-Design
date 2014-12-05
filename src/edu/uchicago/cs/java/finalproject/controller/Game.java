
package edu.uchicago.cs.java.finalproject.controller;



import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.Clip;

import edu.uchicago.cs.java.finalproject.game.model.*;
import edu.uchicago.cs.java.finalproject.game.view.*;
import edu.uchicago.cs.java.finalproject.sounds.Sound;
import edu.uchicago.cs.java.finalproject.game.blackjack.*;
// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener, MouseListener {

	// ===============================================
	// FIELDS
	// ===============================================

	public static final Dimension DIM = new Dimension(800, 600); //the dimension of the game.
	private GamePanel gmpPanel;
	public static Random R = new Random();
	public final static int ANI_DELAY = 45; // milliseconds between screen
											// updates (animation)
	private Thread thrAnim;
	private int nLevel = 1;
	private int nTick = 0;
	private ArrayList<Tuple> tupMarkForRemovals;
	private ArrayList<Tuple> tupMarkForAdds;
	private boolean bMuted = true;
    private boolean isIn = false;

	private final int PAUSE = 32, // space key
			QUIT = 27, // esc key
			LEFT = 37, // rotate left; left arrow
			RIGHT = 39, // rotate right; right arrow
			UP = 38, // thrust; up arrow
            DOWN = 40,// stop ; down arrow
			START1 = 80, // P key
            START2 = 79, // O key
			FIRE = 78, // N key
			MUTE = 66, // B-key mute
            W =87, // Up
            A = 65, // Left
            D = 68, // Right
            S = 83, // Down
            J = 74, // Fire


	// for possible future use
	// HYPER = 68, 					// d key
	SHIELD = 81, 				// B key arrow
	// NUM_ENTER = 10, 				// hyp
	 SPECIAL = 77,// fire special weapon;  M key
     K = 75, 					// fire special weapon;  K key
    HELP = 72;

	private Clip clpThrust;
	private Clip clpMusicBackground;

	private static final int SPAWN_NEW_SHIP_FLOATER = 1200;



	// ===============================================
	// ==CONSTRUCTOR
	// ===============================================

	public Game() {

		gmpPanel = new GamePanel(DIM);
		gmpPanel.addKeyListener(this);
        gmpPanel.addMouseListener(this);


		clpThrust = Sound.clipForLoopFactory("whitenoise.wav");
		clpMusicBackground = Sound.clipForLoopFactory("music-background.wav");


	}

	// ===============================================
	// ==METHODS
	// ===============================================

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() { // uses the Event dispatch thread from Java 5 (refactored)
					public void run() {
						try {
							Game game = new Game(); // construct itself
							game.fireUpAnimThread();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void fireUpAnimThread() { // called initially
		if (thrAnim == null) {
			thrAnim = new Thread(this); // pass the thread a runnable object (this)
			thrAnim.start();
		}
	}

	// implements runnable - must have run method
	public void run() {

		// lower this thread's priority; let the "main" aka 'Event Dispatch'
		// thread do what it needs to do first
		thrAnim.setPriority(Thread.MIN_PRIORITY);

		// and get the current time
		long lStartTime = System.currentTimeMillis();

		// this thread animates the scene
		while (Thread.currentThread() == thrAnim) {
			tick();
			spawnNewShipFloater();
			gmpPanel.update(gmpPanel.getGraphics()); // update takes the graphics context we must 
														// surround the sleep() in a try/catch block
														// this simply controls delay time between 
														// the frames of the animation

			//this might be a good place to check for collisions
			checkCollisions();
			//this might be a god place to check if the level is clear (no more foes)
			//if the level is clear then spawn some big asteroids -- the number of asteroids 
			//should increase with the level. 
			checkNewLevel();

			try {
				// The total amount of time is guaranteed to be at least ANI_DELAY long.  If processing (update) 
				// between frames takes longer than ANI_DELAY, then the difference between lStartTime - 
				// System.currentTimeMillis() will be negative, then zero will be the sleep time
				lStartTime += ANI_DELAY;
				Thread.sleep(Math.max(0,
						lStartTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// just skip this frame -- no big deal
				continue;
			}
		} // end while
	} // end run

	private void checkCollisions() {


		
		//@formatter:off
		//for each friend in movFriends
			//for each foe in movFoes
				//if the distance between the two centers is less than the sum of their radii
					//mark it for removal
		
		//for each mark-for-removal
			//remove it
		//for each mark-for-add
			//add it
		//@formatter:on
		
		//we use this ArrayList to keep pairs of movMovables/movTarget for either
		//removal or insertion into our arrayLists later on
		tupMarkForRemovals = new ArrayList<Tuple>();
		tupMarkForAdds = new ArrayList<Tuple>();

		Point pntFriendCenter, pntFoeCenter;
		int nFriendRadiux, nFoeRadiux;

		for (Movable movFriend : CommandCenter.movFriends) {
			for (Movable movFoe : CommandCenter.movFoes) {

				pntFriendCenter = movFriend.getCenter();
				pntFoeCenter = movFoe.getCenter();
				nFriendRadiux = movFriend.getRadius();
				nFoeRadiux = movFoe.getRadius();

                if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux+movFoe.getRadius()*1.2)) {
                    if(movFoe instanceof Enemy) {
                        CommandCenter.movFoes.add(new Bullet((Enemy) movFoe));
                    }
                }


				//detect collision
				if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {

                    if((movFriend==CommandCenter.getFalcon1()||movFriend==CommandCenter.getFalcon1())&&(movFoe instanceof BlackHole)){
                        if(!isIn) {
                            isIn = true;
                            CommandCenter.setPaused(!CommandCenter.isPaused());
                            if (CommandCenter.isPaused())
                                stopLoopingSounds(clpMusicBackground, clpThrust);
                            else
                                clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
                            myPanel mp = new myPanel();
                            killFoe(movFoe);
                        }
                    }
                    else if(movFriend==CommandCenter.getFalcon1()) {
                        //falcon
                        if ((movFriend instanceof Falcon)) {
                            if (!CommandCenter.getFalcon1().getProtected()) {
                                ((Falcon) movFriend).thrustOff();
                                ((Falcon) movFriend).setAlive(false);
                                tupMarkForAdds.add(new Tuple(CommandCenter.getMovDebris(), movFriend));
                                tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
                                CommandCenter.spawnFalcon1(false);
                                killFoe(movFoe);
                            }
                        }
                        //not the falcon
                    }else if(movFriend==CommandCenter.getFalcon2()) {
                        if ((movFriend instanceof Falcon)) {
                            if (!CommandCenter.getFalcon2().getProtected()) {
                                ((Falcon) movFriend).thrustOff();
                                ((Falcon) movFriend).setAlive(false);
                                tupMarkForAdds.add(new Tuple(CommandCenter.getMovDebris(), movFriend));
                                tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
                                CommandCenter.spawnFalcon2(false);
                                killFoe(movFoe);
                            }
                        }
                        //not the falcon

                    }else {
                        if(!(movFoe instanceof BlackHole)) {
                            tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
                            killFoe(movFoe);
                        }
                    }//end else
					//explode/remove foe
					
					
				
				}//end if 
			}//end inner for
		}//end outer for


		//check for collisions between falcon and floaters
		if (CommandCenter.getFalcon1() != null){
			Point pntFalCenter = CommandCenter.getFalcon1().getCenter();
			int nFalRadiux = CommandCenter.getFalcon1().getRadius();
			Point pntFloaterCenter;
			int nFloaterRadiux;
			
			for (Movable movFloater : CommandCenter.movFloaters) {
				pntFloaterCenter = movFloater.getCenter();
				nFloaterRadiux = movFloater.getRadius();
	
				//detect collision
				if (pntFalCenter.distance(pntFloaterCenter) < (nFalRadiux + nFloaterRadiux)) {
	
					CommandCenter.setNumFalcons1(CommandCenter.getNumFalcons1()+1);
                    if(CommandCenter.getFalcon2()!=null) {
                        CommandCenter.setNumFalcons2(CommandCenter.getNumFalcons2() + 1);
                    }
					tupMarkForRemovals.add(new Tuple(CommandCenter.movFloaters, movFloater));
					Sound.playSound("pacman_eatghost.wav");
	
				}//end if 
			}//end inner for
		}//end if not null

        if (CommandCenter.getFalcon2() != null){
            Point pntFalCenter = CommandCenter.getFalcon2().getCenter();
            int nFalRadiux = CommandCenter.getFalcon2().getRadius();
            Point pntFloaterCenter;
            int nFloaterRadiux;

            for (Movable movFloater : CommandCenter.movFloaters) {
                pntFloaterCenter = movFloater.getCenter();
                nFloaterRadiux = movFloater.getRadius();

                //detect collision
                if (pntFalCenter.distance(pntFloaterCenter) < (nFalRadiux + nFloaterRadiux)) {


                    tupMarkForRemovals.add(new Tuple(CommandCenter.movFloaters, movFloater));
                    Sound.playSound("pacman_eatghost.wav");

                }//end if
            }//end inner for
        }//end if not null
		
		//remove these objects from their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForRemovals) 
			tup.removeMovable();
		
		//add these objects to their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForAdds) 
			tup.addMovable();

		//call garbage collection
		System.gc();
		
	}//end meth

	private void killFoe(Movable movFoe) {

//        Explode ex = new Explode(movFoe.getCenter().x, movFoe.getCenter().y);
        CommandCenter.setScore(CommandCenter.getScore() + 1);

		if (movFoe instanceof Asteroid){

			//we know this is an Asteroid, so we can cast without threat of ClassCastException
			Asteroid astExploded = (Asteroid)movFoe;
			//big asteroid 
			if(astExploded.getSize() == 0){
				//spawn two medium Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				
			} 
			//medium size aseroid exploded
			else if(astExploded.getSize() == 1){
				//spawn three small Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
			}
			//remove the original Foe	
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
		
			
		} 
		//not an asteroid
		else {
			//remove the original Foe
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
		}
		
		
		

		
		
		
		
	}

	//some methods for timing events in the game,
	//such as the appearance of UFOs, floaters (power-ups), etc. 
	public void tick() {
		if (nTick == Integer.MAX_VALUE)
			nTick = 0;
		else
			nTick++;
	}

	public int getTick() {
		return nTick;
	}

	private void spawnNewShipFloater() {
		//make the appearance of power-up dependent upon ticks and levels
		//the higher the level the more frequent the appearance
		if (nTick % (SPAWN_NEW_SHIP_FLOATER - nLevel * 10) == 0) {
            CommandCenter.movFloaters.add(new NewShipFloater());
		}
	}

	// Called when user presses 's'
	private void startGame1() {
		CommandCenter.clearAll();
		CommandCenter.initGame1();
		CommandCenter.setLevel(0);
		CommandCenter.setPlaying(true);
		CommandCenter.setPaused(false);
		//if (!bMuted)
		   // clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
	}

    private void startGame2() {
        CommandCenter.clearAll();
        CommandCenter.initGame2();
        CommandCenter.setLevel(0);
        CommandCenter.setPlaying(true);
        CommandCenter.setPaused(false);
        //if (!bMuted)
        // clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
    }

	//this method spawns new asteroids
	private void spawnAsteroids(int nNum) {
		for (int nC = 0; nC < nNum; nC++) {
			//Asteroids with size of zero are big
			CommandCenter.movFoes.add(new Asteroid(0));
		}
	}

    private void spawnEnemy(int nNum) {
            //Asteroids with size of zero are big
        CommandCenter.movFoes.add(new Enemy("src\\image\\ships\\ship2.png",1,300,2.3));
        CommandCenter.movFoes.add(new Enemy("src\\image\\ships\\ship16.png",500,100,2.9));
        CommandCenter.movFoes.add(new Enemy("src\\image\\ships\\ship14.png",1000,700,3.2));
        CommandCenter.movFoes.add(new Enemy("src\\image\\ships\\ship5.png",100,3,2.3));
        CommandCenter.movDebris.add(new Neutral("src\\image\\ships\\ship10.png", 10, 70, 1.5));
        CommandCenter.movDebris.add(new Neutral("src\\image\\ships\\ship8.png", 20, 400, 1.5));
        if(CommandCenter.getFalcon2()==null||CommandCenter.getNumFalcons2()==0) {
            CommandCenter.movFoes.add(new BlackHole());
            this.isIn = false;
        }
    }
	
	private boolean isLevelClear(){
		//if there are no more Asteroids on the screen

		boolean bAsteroidFree = true;
		for (Movable movFoe : CommandCenter.movFoes) {
			if (movFoe instanceof Asteroid){
				bAsteroidFree = false;
				break;
			}
		}
		
		return bAsteroidFree;

		
	}
	
	private void checkNewLevel(){
		
		if (isLevelClear() ){
			if (CommandCenter.getFalcon1() !=null)
				CommandCenter.getFalcon1().setProtected(true);
            if (CommandCenter.getFalcon2() !=null)
                CommandCenter.getFalcon2().setProtected(true);

			spawnAsteroids(CommandCenter.getLevel() + 2);
			CommandCenter.setLevel(CommandCenter.getLevel() + 1);
            spawnEnemy(1);
		}
	}
	
	
	

	// Varargs for stopping looping-music-clips
	private static void stopLoopingSounds(Clip... clpClips) {
		for (Clip clp : clpClips) {
			clp.stop();
		}
	}

	// ===============================================
	// KEYLISTENER METHODS
	// ===============================================

	@Override
	public void keyPressed(KeyEvent e) {
		Falcon fal1 = CommandCenter.getFalcon1();
        Falcon fal2 = CommandCenter.getFalcon2();
		int nKey = e.getKeyCode();
		// System.out.println(nKey);

		if (nKey == START1 && !CommandCenter.isPlaying())
			startGame1();

        if (nKey == START2 && !CommandCenter.isPlaying())
            startGame2();



		if (fal1 != null||fal2 != null) {

			switch (nKey) {
			case PAUSE:
				CommandCenter.setPaused(!CommandCenter.isPaused());
				if (CommandCenter.isPaused())
					stopLoopingSounds(clpMusicBackground, clpThrust);
				else
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case QUIT:
				System.exit(0);
				break;
			case UP:
				fal1.thrustOn();
				if (!CommandCenter.isPaused())
					clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
				break;
            case DOWN:
                fal1.backOn();
                if (!CommandCenter.isPaused())
                    clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
                break;
			case LEFT:
				fal1.rotateLeft();
				break;
			case RIGHT:
				fal1.rotateRight();
				break;

			// possible future use
			// case KILL:
			case SHIELD:
                fal1.setProtected(true);
                fal1.setShield(10);
                if(fal2!=null){
                    fal2.setProtected(true);
                    fal2.setShield(10);
                }
			// case NUM_ENTER:

			default:
				break;
			}
		}

        if (fal2 != null) {

            switch (nKey) {
                case W:
                    fal2.thrustOn();
                    if (!CommandCenter.isPaused())
                        clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
                    break;
                case S:
                    fal2.backOn();
                    if (!CommandCenter.isPaused())
                        clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
                    break;
                case A:
                    fal2.rotateLeft();
                    break;
                case D:
                    fal2.rotateRight();
                    break;

                // possible future use
                // case KILL:
                // case SHIELD:
                // case NUM_ENTER:

                default:
                    break;
            }
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Falcon fal1 = CommandCenter.getFalcon1();
        Falcon fal2 = CommandCenter.getFalcon2();
		int nKey = e.getKeyCode();
		 System.out.println(nKey);

		if (fal1 != null) {
			switch (nKey) {
			case FIRE:
				CommandCenter.movFriends.add(new Bullet(fal1));
				Sound.playSound("laser.wav");
				break;
				
			//special is a special weapon, current it just fires the cruise missile. 
			case SPECIAL:
				CommandCenter.movFriends.add(new Cruise(fal1));
				//Sound.playSound("laser.wav");
				break;
			case LEFT:
				fal1.stopRotating();
				break;
			case RIGHT:
				fal1.stopRotating();
				break;
			case UP:
				fal1.thrustOff();
				clpThrust.stop();
				break;
            case DOWN:
                fal1.backOff();
                clpThrust.stop();
			case MUTE:
				if (!bMuted){
					stopLoopingSounds(clpMusicBackground);
					bMuted = !bMuted;
				} 
				else {
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
					bMuted = !bMuted;
				}
				break;
			default:
				break;
			}
		}

        if (fal2 != null) {
            switch (nKey) {
                case J:
                    CommandCenter.movFriends.add(new Bullet(fal2));
                    Sound.playSound("laser.wav");
                    break;

                //special is a special weapon, current it just fires the cruise missile.
                case K:
                    CommandCenter.movFriends.add(new Cruise(fal2));
                    //Sound.playSound("laser.wav");
                    break;

                case A:
                    fal2.stopRotating();
                    break;
                case D:
                    fal2.stopRotating();
                    break;
                case W:
                    fal2.thrustOff();
                    clpThrust.stop();
                    break;
                case S:
                    fal2.backOff();
                    clpThrust.stop();
                case MUTE:
                    if (!bMuted){
                        stopLoopingSounds(clpMusicBackground);
                        bMuted = !bMuted;
                    }
                    else {
                        clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
                        bMuted = !bMuted;
                    }
                    break;


                default:
                    break;
            }
        }
	}

	@Override
	// Just need it b/c of KeyListener implementation
	public void keyTyped(KeyEvent e) {
	}


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getModifiers()==16) {
            Falcon fal1 = CommandCenter.getFalcon1();
            CommandCenter.movFriends.add(new weapon(fal1, e.getPoint()));
        }if(e.getModifiers()==4){
            Falcon fal1 = CommandCenter.getFalcon1();
            CommandCenter.movFriends.add(new bomb(fal1));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

// ===============================================
// ==A tuple takes a reference to an ArrayList and a reference to a Movable
//This class is used in the collision detection method, to avoid mutating the array list while we are iterating
// it has two public methods that either remove or add the movable from the appropriate ArrayList 
// ===============================================

class Tuple{
	//this can be any one of several CopyOnWriteArrayList<Movable>
	private CopyOnWriteArrayList<Movable> movMovs;
	//this is the target movable object to remove
	private Movable movTarget;
	
	public Tuple(CopyOnWriteArrayList<Movable> movMovs, Movable movTarget) {
		this.movMovs = movMovs;
		this.movTarget = movTarget;
	}
	
	public void removeMovable(){
		movMovs.remove(movTarget);
	}
	
	public void addMovable(){
		movMovs.add(movTarget);
	}

}

package edu.uchicago.cs.java.finalproject.game.model;

import java.util.concurrent.CopyOnWriteArrayList;

import edu.uchicago.cs.java.finalproject.sounds.Sound;

// I only want one Command Center and therefore this is a perfect candidate for static
// Able to get access to methods and my movMovables ArrayList from the static context.
public class CommandCenter {

//	private static int nNumFalcon;
    private static int nNumFalcon1;
    private static int nNumFalcon2;
	private static int nLevel;
	private static long lScore;
//	private static Falcon falShip;
    private static Falcon falShip1;
    private static Falcon falShip2;
	private static boolean bPlaying;
	private static boolean bPaused;
	
	// These ArrayLists are thread-safe
	public static CopyOnWriteArrayList<Movable> movDebris = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFriends = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFoes = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFloaters = new CopyOnWriteArrayList<Movable>();
	

	// Constructor made private - static Utility class only
	private CommandCenter() {}
	
	public static void initGame1(){
		setLevel(1);
		setScore(0);
		setNumFalcons1(3);
		spawnFalcon1(true);
	}
    public static void initGame2(){
        setLevel(1);
        setScore(0);
        setNumFalcons1(3);
        setNumFalcons2(3);
        spawnFalcon1(true);
        spawnFalcon2(true);
    }
	
	// The parameter is true if this is for the beginning of the game, otherwise false
	// When you spawn a new falcon, you need to decrement its number


    public static void spawnFalcon1(boolean bFirst) {

        if (getNumFalcons1() != 0) {
            falShip1 = new Falcon();
            movFriends.add(falShip1);
            if (!bFirst)
                setNumFalcons1(getNumFalcons1() - 1);
        }

        Sound.playSound("shipspawn.wav");
    }

    public static void spawnFalcon2(boolean bFirst) {

        if (getNumFalcons2() != 0) {
            falShip2 = new Falcon();
            movFriends.add(falShip2);
            if (!bFirst)
                setNumFalcons2(getNumFalcons2() - 1);
        }

        Sound.playSound("shipspawn.wav");
    }

	public static void clearAll(){
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();
	}

	public static boolean isPlaying() {
		return bPlaying;
	}

	public static void setPlaying(boolean bPlaying) {
		CommandCenter.bPlaying = bPlaying;
	}

	public static boolean isPaused() {
		return bPaused;
	}

	public static void setPaused(boolean bPaused) {
		CommandCenter.bPaused = bPaused;
	}
	
	public static boolean isGameOver() {		//if the number of falcons is zero, then game over
		if (getNumFalcons1() == 0&&getNumFalcons2() == 0) {
			return true;
		}
		return false;
	}

	public static int getLevel() {
		return nLevel;
	}

	public  static long getScore() {
		return lScore;
	}

	public static void setScore(long lParam) {
		lScore = lParam;
	}

	public static void setLevel(int n) {
		nLevel = n;
	}

//	public static int getNumFalcons() {
//		return nNumFalcon;
//	}
//
//	public static void setNumFalcons(int nParam) {
//		nNumFalcon = nParam;
//	}
//
//	public static Falcon getFalcon(){
//		return falShip;
//	}
//
//	public static void setFalcon(Falcon falParam){
//		falShip = falParam;
//	}

    public static int getNumFalcons1() {
        return nNumFalcon1;
    }

    public static void setNumFalcons1(int nParam) {
        nNumFalcon1 = nParam;
    }

    public static Falcon getFalcon1(){
        return falShip1;
    }

    public static void setFalcon1(Falcon falParam){
        falShip1 = falParam;
    }

    public static int getNumFalcons2() {
        return nNumFalcon2;
    }

    public static void setNumFalcons2(int nParam) {
        nNumFalcon2 = nParam;
    }

    public static Falcon getFalcon2(){
        return falShip2;
    }

    public static void setFalcon2(Falcon falParam){
        falShip2 = falParam;
    }

	public static CopyOnWriteArrayList<Movable> getMovDebris() {
		return movDebris;
	}



	public static CopyOnWriteArrayList<Movable> getMovFriends() {
		return movFriends;
	}



	public static CopyOnWriteArrayList<Movable> getMovFoes() {
		return movFoes;
	}


	public static CopyOnWriteArrayList<Movable> getMovFloaters() {
		return movFloaters;
	}


	
	
}

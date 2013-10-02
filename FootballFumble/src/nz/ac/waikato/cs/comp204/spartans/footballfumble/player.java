/*
 * File: Player.java - A Player class
 *
 * OD: Spartans
 * Copyright: Spartans, 28/08/13++
 * License: GNU GPL v2
 * 
 * Notes: This program holds the methods associated with the player class and also a player.
 * Issues: 
 * Reference: Sprite
 * Implements:
 * 
 */

package nz.ac.waikato.cs.comp204.spartans.footballfumble;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;

/**
 *  A Player class.
 * 
 * 	Listens for any touches on the screen and moves the player class towards it using {@link #update()}.
 *	@author Spartans
 * */
public class Player extends Sprite{
	
	private static int TOP_SPEED		= 5;
	private static int PLAYER_MASS 		= 70;
	private static double ACCELERATION 	= 2;		 //ensure acceleration is always positive
	private boolean isDown 				= false;
	private boolean firstTouch 			= false;	
	private double touchX, touchY, speed; 			 //ensure speed is never negative.
	
	/**
	 * Initializes the Player instance using {@link 
 	 * nz.ac.waikato.cs.comp204.spartans.footballfumble.Sprite#Sprite(int, int, int, int, DrawView, Bitmap, double)}
 	 * with an initial speed of (6,6).
	 * @param initX the initial x position of this sprite.
	 * @param initY the initial y position of this sprite.
	 * @param drawView the view which this sprite is to be drawn on.
	 * @param bitmap the bitmap image associated with this sprite.
	 */
	public Player(int initX, int initY, DrawView drawView, Bitmap bitmap) {
		super(initX, initY, 6, 6, drawView, bitmap, PLAYER_MASS);
	}
	
	// Sets conditions for when screen is touched
	View.OnTouchListener viewListen = new View.OnTouchListener(){
		public boolean onTouch(View v, MotionEvent e){
			firstTouch 	= true;
			touchX 		= e.getX();
			touchY		= e.getY();
			
			switch (e.getAction()){		
				case MotionEvent.ACTION_DOWN:	// when screen is touched
					isDown = true;
					break;
				case MotionEvent.ACTION_MOVE:
					isDown = true;	
					break;		            		
				case MotionEvent.ACTION_UP:		// when touch is removed
					isDown = false;
					break;
				case MotionEvent.ACTION_OUTSIDE:
					isDown = false;
					break;
			}				
		return true;	
		}
	};
	
	/**
	 * Checks for touches on the screen.
	 * <p>
	 * If a current touch is down, a direct path will be calculated for the player sprite to move toward. Each
	 * update a new sub-triangle is calculated and the x and y directional speeds are changed so that they
	 * keep following the current touch. 
	 * <p>
	 * If there is no touch the player will decelerate and eventually come to a halt.
	 */
	protected void update(){
		if(firstTouch){
			double distance, trigX, trigY, theta; 
			
			// If a touch is on the screen accelerate the player towards the touch position.
			if(isDown){
				if(speed + ACCELERATION <= TOP_SPEED){ // Check for if speed < 0 here.
					speed += ACCELERATION;
				}else{
						speed = TOP_SPEED;
				}
			}else{ // a touch is not on the screen so decelerate the player.
				if((speed -= ACCELERATION/5) >0){
					speed -= ACCELERATION/5;
				}else{
					speed = 0;
				}
			}
			// Calculate the appropriate triangle related to the touchx and touchy values
			trigX 		= Math.abs(touchX - x);
			trigY 		= Math.abs(touchY - y);
			distance	= Math.sqrt((trigX * trigX) + (trigY * trigY));
			theta 		= Math.acos(trigX / distance);	
			
			// If within 5 pixels of target, don't move. Due to the way android calculates
			// your current e.X and e.Y, there is a lot of variance and without this the player will vibrate underneath the finger.
			if (distance < 5){
				return;
			}
			
			// Calculating direction to move player
			if (touchX >= x){
				xSpeed		= speed * Math.cos(theta);
			} else {
				xSpeed 		= speed * Math.cos(theta + Math.PI);
			}
			
			if (touchY >= y){
				ySpeed		= speed * Math.sin(theta);
			} else {
				ySpeed		= speed * Math.sin(theta + Math.PI);
			}
			
			x 			+= xSpeed;
			y 			+= ySpeed;

		}
		drawView.setOnTouchListener(viewListen);
	}
}
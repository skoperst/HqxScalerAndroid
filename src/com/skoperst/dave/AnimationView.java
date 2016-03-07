package com.skoperst.dave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.skoperst.dave.micro2d.BitmapManager;
import com.skoperst.dave.micro2d.BlockItem;
import com.skoperst.dave.micro2d.TouchKey;
import com.skoperst.dave.micro2d.WorldPolicyManager;
import com.skoperst.hqx.HqxScaler;
import com.skoperst.hqx.RawBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.Button;

class AnimationView extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener, OnClickListener {
	class AnimationThread extends Thread {

		/** Are we running ? */
		private boolean mRun;

		/** Used to figure out elapsed time between frames */
		private long mLastTime;
		private long mLastTimeFpsFixer;

		/** Variables for the counter */
		private int frameSamplesCollected = 0;
		private int frameSampleTime = 0;
		private int fps = 0;

		private boolean isLeftPressed;
		private boolean isRightPressed;
		private boolean isJumpPressed;

		private WorldPolicyManager mGameModel;


		/** Handle to the surface manager object we interact with */
		private SurfaceHolder mSurfaceHolder;

		/** How to display the text */
		private Paint textPaint;

		float[] x = new float[10];
		float[] y = new float[10];
		boolean[] touched = new boolean[10];
		  
		public AnimationThread(SurfaceHolder surfaceHolder,WorldPolicyManager gameModel) {
			mSurfaceHolder = surfaceHolder;


			/** Initiate the text painter */
			textPaint = new Paint();
			textPaint.setARGB(255,255,255,255);
			textPaint.setTextSize(32);

			mLastTimeFpsFixer = 0;
			mLastTime = 0;
			mGameModel = gameModel;

			isLeftPressed = false;
			isRightPressed = false;
			isJumpPressed = false;
		}


		/**
		 * The actual game loop!
		 */
		@Override
		public void run() {
			while (mRun) {
				Canvas c = null;
				try {
					c = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) { 
						if (mGameModel.isGameReady()){
							if (c != null){
								updateGameModel();
								doDraw(c);
							}

						}
					}
				}finally {
					if (c != null) {                    	
						mSurfaceHolder.unlockCanvasAndPost(c);
						long now = System.currentTimeMillis();
						long dt = now - mLastTimeFpsFixer;
						if (dt< 33 && mLastTimeFpsFixer!=0){
							try {
								Thread.sleep(33 - dt);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						mLastTimeFpsFixer = System.currentTimeMillis();
					}
				}
			}
		}
		public void touchEvent(MotionEvent event){
			int action = event.getAction() & MotionEvent.ACTION_MASK;
		    int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
		    int pointerId = event.getPointerId(pointerIndex);
		    Log.v("DAVE","event: "+pointerId+" ac:"+action);
		    switch (action) {
		    case MotionEvent.ACTION_DOWN:
		    case MotionEvent.ACTION_POINTER_DOWN:
		      touched[pointerId] = true;
		      x[pointerId] = (int) event.getX(pointerIndex);
		      y[pointerId] = (int) event.getY(pointerIndex);
		      break;
		    case MotionEvent.ACTION_UP:
		    case MotionEvent.ACTION_POINTER_UP:
		    case MotionEvent.ACTION_CANCEL:
		    case MotionEvent.ACTION_OUTSIDE:
		      touched[pointerId] = false;
		      x[pointerId] = (int) event.getX(pointerIndex);
		      y[pointerId] = (int) event.getY(pointerIndex);
		      break;
		    case MotionEvent.ACTION_MOVE:
		      int pointerCount = event.getPointerCount();
		      for (int i = 0; i < pointerCount; i++) {
		        pointerIndex = i;
		        pointerId = event.getPointerId(pointerIndex);
		        x[pointerId] = (int) event.getX(pointerIndex);
		        y[pointerId] = (int) event.getY(pointerIndex);
		      }
		      break;
		    
		    }
		    for (int k =0; k<event.getPointerCount(); k++){
		    	if (mGameModel.mTouchKeyLeft.isTouchInKey((int)x[k],(int)y[k])){
		    		if (touched[k])
		    			isLeftPressed = true;
		    		else
		    			isLeftPressed = false;
		    	}
		    	else
		    			isLeftPressed = false;
		    	
		    	if ((mGameModel.mTouchKeyRight.isTouchInKey((int)x[k],(int)y[k]))){
		    		if (touched[k])
		    			isRightPressed = true;
		    		else
		    			isRightPressed = false;
		    	}
		    	else
		    		isRightPressed = false;
		    	
		    	if (mGameModel.mTouchKeyJump.isTouchInKey((int)x[k],(int)y[k])){
		    		if (touched[k])
		    			isJumpPressed = true;
		    		else
		    			isJumpPressed = false;
		    	}
		    		else
		    			isJumpPressed = false;
		    	
		    }
		  }
		


		private void updateGameModel() {

			//TODO: change to request
			mGameModel.updateGameObjects(isLeftPressed,isRightPressed,isJumpPressed);
//			List<AsyncClass> mAsyncClassList = AsyncClassManager.getClassList();
//			for (AsyncClass aClass : mAsyncClassList){
//				aClass.processRequests();
//				aClass.processResponses();
//			}
			
			long now = System.currentTimeMillis();

			if (mLastTime != 0) {

				//Time difference between now and last time we were here
				int time = (int) (now - mLastTime);
				frameSampleTime += time;
				frameSamplesCollected++;

				//After 10 frames
				if (frameSamplesCollected == 10) {

					//Update the fps variable
					fps = (int) (10000 / frameSampleTime);

					//Reset the sampletime + frames collected
					frameSampleTime = 0;
					frameSamplesCollected = 0;
				}        		
			}

			mLastTime = now;
		}

		/**
		 * Draws to the provided Canvas.
		 */
		private void doDraw(Canvas canvas) {
			if (!mGameModel.isGameReady())
				throw new RuntimeException();
			Log.v("DAVE","Drawing...");
			// Draw the background color. Operations on the Canvas accumulate
			// so this is like clearing the screen. In a real game you can 
			// put in a background image of course
			int buttonRadius = mGameModel.mBlockHeight;
			canvas.drawColor(Color.BLACK);
			Paint pYellow = new Paint();
			pYellow.setColor(Color.YELLOW);
			Paint pBlue = new Paint();
			pBlue.setColor(Color.MAGENTA);

			int baseX = (getWidth() - mGameModel.mScreenWidth)/2;
			
			List<String> bmplist = new ArrayList<String>();
			bmplist.add("redbrick.png");
			BlockItem newBlock = new BlockItem(getContext(), bmplist, 12, 5, 5, 64, 64);
			Bitmap dd = mGameModel.getBitmapForKey(newBlock.mBitmapKey);
			Log.v("DAVE","drawingXXX at:"+newBlock.mX+", "+newBlock.mY);
			canvas.drawBitmap(dd,newBlock.mX,newBlock.mY,pYellow);
			

			List<BlockItem> gameObjectList = mGameModel.getObjectsToDraw();
			Bitmap daveBitmap;
			try {
				daveBitmap = BitmapManager.loadBitmap(getContext(), "sprites/dave3.png");
				RawBitmap daveRawBitmap = new RawBitmap(daveBitmap);
				RawBitmap daveRawBitmap4x = new RawBitmap(daveBitmap.getWidth()*4, daveBitmap.getHeight()*4, false);
				RawBitmap daveRawBitmap16x = new RawBitmap(daveRawBitmap4x.getWidth()*4, daveRawBitmap4x.getHeight()*4, false);
				HqxScaler.scaleHq4x(daveRawBitmap, daveRawBitmap4x);
				HqxScaler.scaleHq4x(daveRawBitmap4x, daveRawBitmap16x);
				canvas.drawBitmap(daveRawBitmap.toBitmap(),5,5,pYellow);
				canvas.drawBitmap(daveRawBitmap4x.toBitmap(),105,105,pYellow);
				canvas.drawBitmap(daveRawBitmap16x.toBitmap(),305,205,pYellow);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			
//			for (BlockItem block: gameObjectList){
//				
//				Bitmap blockBitmap = mGameModel.getBitmapForKey(block.mBitmapKey);
//				canvas.drawBitmap(blockBitmap,baseX + block.mX,block.mY,pYellow);
//				if (block.mDebugHighlight){
//					canvas.drawLine(block.mX + baseX,
//									block.mY,
//									block.mX + baseX + block.mWidth -1,
//									block.mY,
//									pYellow);
//					canvas.drawLine(block.mX + baseX+ block.mWidth -1,
//									block.mY,
//									block.mX + baseX + block.mWidth-1,
//									block.mY+block.mHeight-1, pYellow);
//					canvas.drawLine(block.mX + baseX,
//									block.mY,
//									block.mX + baseX,
//									block.mY+block.mHeight - 1, pBlue);
//					canvas.drawLine(block.mX + baseX,
//									block.mY,
//									block.mX + baseX,
//									block.mY+block.mHeight - 1, pBlue);
//				}
//			}
			TouchKey keyLeft = mGameModel.getTouchKeyLeft();
			Bitmap leftBitmap;
			if (!isLeftPressed)
				 leftBitmap = mGameModel.getBitmapForKey(keyLeft.mBitmapIdle);
			else
				 leftBitmap = mGameModel.getBitmapForKey(keyLeft.mBitmapPressed);
			Log.v("DAVE","drawing at:"+keyLeft.mX+", "+keyLeft.mY);
			//canvas.drawBitmap(leftBitmap, keyLeft.mX, keyLeft.mY, pYellow);
			
			TouchKey keyRight = mGameModel.getTouchKeyRight();
			Bitmap rightBitmap;
			if (!isRightPressed)
				rightBitmap = mGameModel.getBitmapForKey(keyRight.mBitmapIdle);
			else 
				rightBitmap = mGameModel.getBitmapForKey(keyRight.mBitmapPressed);
			//canvas.drawBitmap(rightBitmap, keyRight.mX, keyRight.mY, pYellow);
			
			
			TouchKey keyJump = mGameModel.getTouchKeyJump();
			Bitmap jumpBitmap = mGameModel.getBitmapForKey(keyJump.mBitmapIdle);
			//canvas.drawBitmap(jumpBitmap, keyJump.mX, keyJump.mY, pYellow);

			//Bitmap b = StaticBLock.
			//canvas.draw
			//Draw fps center screen
			//canvas.drawText(fps + " fps", getWidth() / 2, getHeight() / 2, textPaint);
			//draw left/right
			//canvas.drawCircle(baseX+ buttonRadius, mGameModel.mScreenHeight + buttonRadius, buttonRadius, p);
			//canvas.drawCircle(baseX + buttonRadius*3 + 5, mGameModel.mScreenHeight + buttonRadius, buttonRadius, p);

			//draw jump
			//canvas.drawCircle(baseX+ mGameModel.mScreenWidth - buttonRadius, mGameModel.mScreenHeight + buttonRadius, buttonRadius, p);
			//canvas.drawBitmap(mGameModel.mRita, 0, 0, p);
			//canvas.restore();        
		}

		/**
		 * So we can stop/pauze the game loop
		 */
		public void setRunning(boolean b) {
			mRun = b;
		}


		public void updateResolution(int width, int height) {
			mGameModel.resizeScreenResolution(width, height);

		}      

	}

	/** The thread that actually draws the animation */
	private AnimationThread thread;


	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);


		// create thread only; it's started in surfaceCreated()
		thread = new AnimationThread(holder,new WorldPolicyManager(context));
		setOnTouchListener(this);

	}


	/**
	 * Obligatory method that belong to the:implements SurfaceHolder.Callback
	 */

	/* Callback invoked when the surface dimensions change. */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		thread.updateResolution(width,height);
	}

	/*
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
		thread.start();
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}


	public boolean onTouch(View v, MotionEvent event) {
		thread.touchEvent(event);
		return true;
	}


	public void onClick(View v) {
	}
}

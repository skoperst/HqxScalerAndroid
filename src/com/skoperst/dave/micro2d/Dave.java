package com.skoperst.dave.micro2d;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class Dave extends BlockItem{

	
	static private final int REQUEST_ACTIVE_BLOCK_DAVE_UPDATE = 1;	
	static private final int REQUEST_WORLD_POLICY_KEYS = 2;
	
	static private final int REQUEST_MOVE_DXDY = 83; 
	
	
	static private final int RESPONSE_KEYS_AVAILABLE = 3;
	
	public int mOffsetX;
	public int mOffsetY;
	
	public int mPreviousX;
	public int mMaxOffset;
	
	private List<String> mBitmapKeysRight;
	private List<String> mBitmapKeysLeft;
	
	//
	private int mCurrentLeftBitmapIndex;
	private int mCurrentRightBitmapIndex;
	
	public int mMaxSpeedX;
	public int mMaxSpeedY;
	public int mInitialSpeed;
	public int mSpeedY;
	public int mSpeedX;
	
	public List<String> mBitmapKeys;
	protected int mCurrentFrameIndex;

	
	public int mId;
	public int mWidth;
	public int mHeight;
	public boolean mDebugHighlight;
	
	public int mVx;
	public int mVy;
	
	
	public boolean mIsLeftPressed;
	public boolean mIsRightPressed;
	public boolean mIsJumpPressed;
	public boolean mIsKeysValid;
	
	
	
	public Dave(Context context, List<String> bitmapKeys, int id, int x,int y,int blockWidth, int blockHeight) {
		super(context,bitmapKeys,id,x,y,blockWidth,blockHeight);
		mX = x * blockWidth;
		mY = y * blockHeight;
		mPreviousX = 0;
		/*mMaxOffset = blockWidth/6;
		mOffsetX = mMaxOffset/2;
		mOffsetY = 0;
		//TODO: take this from xml
		*/
		mIsKeysValid = false;
		
		mWidth = blockWidth;
		mHeight = blockHeight;
		
		mSpeedX = mWidth/4;
		mSpeedY = (mHeight/4);

		mCurrentLeftBitmapIndex = 0;
		mCurrentRightBitmapIndex = 0;
		
		mBitmapKeysRight = new ArrayList<String>();
		mBitmapKeysRight.add(bitmapKeys.get(1));
		mBitmapKeysRight.add(bitmapKeys.get(1));
		mBitmapKeysRight.add(bitmapKeys.get(2));
		mBitmapKeysRight.add(bitmapKeys.get(2));
		mBitmapKeysRight.add(bitmapKeys.get(1));
		mBitmapKeysRight.add(bitmapKeys.get(1));
		mBitmapKeysRight.add(bitmapKeys.get(3));
		mBitmapKeysRight.add(bitmapKeys.get(3));

		
		
		mBitmapKeysLeft = new ArrayList<String>();
		mBitmapKeysLeft.add(bitmapKeys.get(4));
		mBitmapKeysLeft.add(bitmapKeys.get(4));
		mBitmapKeysLeft.add(bitmapKeys.get(5));
		mBitmapKeysLeft.add(bitmapKeys.get(5));
		mBitmapKeysLeft.add(bitmapKeys.get(4));
		mBitmapKeysLeft.add(bitmapKeys.get(4));
		mBitmapKeysLeft.add(bitmapKeys.get(6));
		mBitmapKeysLeft.add(bitmapKeys.get(6));

	}
	
	private void updateSprite(boolean isLeftPressed,boolean isRightPressed){
		
		if (isRightPressed){
			mBitmapKey = mBitmapKeysRight.get(mCurrentRightBitmapIndex);
			mCurrentRightBitmapIndex ++;
			if (mCurrentRightBitmapIndex >= mBitmapKeysRight.size())
				mCurrentRightBitmapIndex = 0;
		}else if (isLeftPressed){
			mBitmapKey = mBitmapKeysLeft.get(mCurrentLeftBitmapIndex);
			mCurrentLeftBitmapIndex ++;
			if (mCurrentLeftBitmapIndex >= mBitmapKeysLeft.size())
				mCurrentLeftBitmapIndex = 0;
		}
	}
	
	/**
	 * Update function for Dave, will be called every game tick to check if dave wants to do something
	 * @param isLeftPressed
	 * @param isRightPressed
	 * @param isJumpPressed
	 */
	public void update(boolean isLeftPressed,boolean isRightPressed, boolean isJumpPressed) {
//		
//		AsyncClassRequest r = new AsyncClassRequest(REQUEST_MOVE_DXDY);	
//		boolean daveNeedsUpdate = false;
//		Log.v("DAVE","jump:"+isJumpPressed);
//		if (isJumpPressed){
//			mVy = mSpeedY;	
//		}
//		if (mVy>0){
//			daveNeedsUpdate = true;
//			r.mValues.put("dy", -mVy);
//			mVy--;
//		}
//		else if (mVy<0){
//			daveNeedsUpdate = true;
//			r.mValues.put("dy", mVy);
//			mVy++;
//		}
//		else{
//			r.mValues.put("dy", 0);	
//		}
//		if (isRightPressed){
//			r.mValues.put("dx", mSpeedX);			
//		}
//		else if (isLeftPressed){
//			r.mValues.put("dx", -mSpeedX);		
//		}
//		else{
//			r.mValues.put("dx", 0);
//		}
//		if (isLeftPressed || isRightPressed || isJumpPressed || daveNeedsUpdate){
//			updateSprite(isLeftPressed,isRightPressed);
//			AsyncClassManager.sendRequestTo(mAsyncClassOwner, r);	
//		}

	}
	
	public void processRequests(){		
//			while(!requestList.isEmpty()){				
//				AsyncClassRequest request = requestList.remove(0);
//				
//				if (request.mId == REQUEST_ACTIVE_BLOCK_DAVE_UPDATE){
//					if (mIsKeysValid){
//						update(mIsLeftPressed, mIsRightPressed, mIsJumpPressed);
//						mIsKeysValid = false;
//					}					
//					AsyncClassRequest r = new AsyncClassRequest(REQUEST_WORLD_POLICY_KEYS);
//					r.mOrigin = getAsyncClassAddress();
//					AsyncClassManager.sendRequestTo(mAsyncClassOwner, r);				
//				}//ACTIVE_BLOCK_DAVE_UPDATE				
//			}	
	}
	
	
	public void processResponses() {
//		while(!responseList.isEmpty()){
//			AsyncClassResponse response = responseList.remove(0);
//			if (response.id == RESPONSE_KEYS_AVAILABLE){
//				mIsLeftPressed = (Boolean) response.getParameter("left");
//				mIsRightPressed = (Boolean) response.getParameter("right");
//				mIsJumpPressed = (Boolean) response.getParameter("jump");
//				mIsKeysValid = true;
//			}
//		}
//		
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}
}
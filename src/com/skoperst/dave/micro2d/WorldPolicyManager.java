package com.skoperst.dave.micro2d;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

/**
 * Under WorldPolicyManager responsabilities is organization and managing the rules which the world is 
 * acting by. I.E., every decision which is meta-physical is rules by this class
 * @author Skoperst
 *
 */
public class WorldPolicyManager{

	Context mContext;
	
	final static int REQUEST_WORLD_POLICY_MANAGER_UPDATE = 1;
	final static int REQUEST_WORLD_POLICY_MANAGER_TOUCH_KEYS = 2;
	final static int REQUEST_WORLD_POLICY_MANAGER_START_GAME = 4;

	//Dave->WorldPolicyManager: I want to move to direction dx,dy
	final static int REQUEST_MOVE_DXDY = 83;
	
	final static int RESPONSE_WORLD_POLOCY_KEYS_AVAILABLE = 3;
	
	
	final static String CLASSNAME = "WorldPolicyManager";
	
	
	BlockItem mRed;
	public Bitmap mRita;

	//	List<StaticBlock> mGameObjects;
	HashMap<String,Bitmap> mBitmapCache;

	int mBaseX = 0;
	int mBaseY = 0;

	public int mScreenWidth;
	int mScreenWidthGap;
	int mScreenHeight;
	int mBlockWidth;
	public int mBlockHeight;

	public BitmapManager mBitmapManager;

	public TouchKey mTouchKeyLeft;
	public TouchKey mTouchKeyRight;
	public TouchKey mTouchKeyJump;

	SoundPool mSoundPool;

	boolean isReady;
	Object syncMutex;
	int soundWalkId;
	int soundJumpId;
	int nowPlayingId;
	int nowPlayingJumpId;
	
	boolean mIsLeftPressed;
	boolean mIsRightPressed;
	boolean mIsJumpPressed;

	StaticBlockManager mStaticBlockManager;
	WorldPhysicsManager mWorldPhysicsManager;
	
	
	
	public WorldPolicyManager(Context context){
		
		//Creating static block manager
		mStaticBlockManager = new StaticBlockManager();
		
		//World physics manager
		mWorldPhysicsManager = new WorldPhysicsManager();
		
		
		mContext = context;
		syncMutex = new Object();
		synchronized (syncMutex) {
			isReady = false;
		}
		nowPlayingId = 0;
		nowPlayingJumpId = 0;
		mBitmapManager = BitmapManager.getInstance();
		mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		AssetManager am = context.getAssets();
		int a = 0;
		try {
			soundWalkId = mSoundPool.load(am.openFd("sounds/walk.ogg"),1);
			soundJumpId = mSoundPool.load(am.openFd("sounds/jumpfall.ogg"), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mSoundPool.play(a, 100, 100, 1, -1,1f);
		mScreenWidth = 640;
		mScreenWidthGap = 0;

		mScreenHeight = 320;
		mBlockWidth = 32;
		mBlockHeight = 32;


		mBitmapManager.precache(context, "redbrick.png");
		mBitmapManager.precache(context, "dirt.png");
		mBitmapManager.precache(context, "pipe.png");
		mBitmapManager.precache(context, "cup1.png");
		mBitmapManager.precache(context, "cup2.png");
		mBitmapManager.precache(context, "cup3.png");
		mBitmapManager.precache(context, "cup4.png");
		mBitmapManager.precache(context, "dave1.png");
		mBitmapManager.precache(context, "dave2.png");
		mBitmapManager.precache(context, "dave3.png");
		mBitmapManager.precache(context, "dave4.png");
		mBitmapManager.precache(context, "dave4.png");
		mBitmapManager.precache(context, "dave5.png");
		mBitmapManager.precache(context, "dave6.png");
		mBitmapManager.precache(context, "dave7.png");

		mBitmapManager.precacheAndScale(context, "keyleft.png",64);
		mBitmapManager.precacheAndScale(context, "keyright.png",64);	
		mBitmapManager.precacheAndScale(context, "keyup.png",64);

		mTouchKeyLeft = new TouchKey(mContext, "keyleft.png","keyleft.png",0,10,32,0);
		mTouchKeyRight = new TouchKey(mContext, "keyright.png", "keyright.png", 2, 10,32,0);
		mTouchKeyJump = new TouchKey(mContext, "keyup.png", "keyup.png", 18, 10, 32, 0);
		synchronized (syncMutex) {
			isReady = true;
		}

	}
	public TouchKey getTouchKeyRight(){
		return mTouchKeyRight;
	}
	public TouchKey getTouchKeyLeft(){
		return mTouchKeyLeft;
	}

	public TouchKey getTouchKeyJump(){
		return mTouchKeyJump;
	}

	public void resizeScreenResolution(int maxWidth,int maxHeight){
		synchronized (syncMutex) {
			isReady = false;
		}
		int bestBlockWidth = maxWidth / 20;
		int bestBlockHeight = maxHeight / 12; //10 for blocks, 2 for controls and labels
		bestBlockWidth = Math.min(bestBlockWidth, bestBlockHeight);
		mScreenWidth = bestBlockWidth * 20;
		mScreenWidthGap = maxWidth - mScreenWidth;
		mScreenHeight = bestBlockWidth * 10;
		mBlockWidth = bestBlockWidth;
		mBlockHeight = bestBlockWidth;

		mBitmapManager.updateScale(mContext,mBlockWidth);
		
		mBitmapManager.precacheAndScale(mContext, "keyleft.png", mBlockWidth*2);
		mBitmapManager.precacheAndScale(mContext, "keyright.png", mBlockWidth*2);
		mBitmapManager.precacheAndScale(mContext, "keyup.png", mBlockWidth*2);
		mTouchKeyLeft = new TouchKey(mContext, "keyleft.png","keyleft.png",0,10,mBlockWidth,0);
		mTouchKeyRight = new TouchKey(mContext, "keyright.png", "keyright.png", 2, 10,mBlockWidth,0);
		mTouchKeyJump = new TouchKey(mContext, "keyup.png", "keyup.png", 18, 10, mBlockWidth, 0);
		mTouchKeyLeft.updateGap(mScreenWidthGap/2);
		mTouchKeyRight.updateGap(mScreenWidthGap/2);
		mTouchKeyJump.updateGap(mScreenWidthGap/2);		


		synchronized (syncMutex) {
			isReady = true;
		}
	}

	public void updateGameObjects(boolean leftPress,boolean rightPress,boolean jumpPress){
		mIsLeftPressed = leftPress;
		mIsRightPressed = rightPress;
		mIsJumpPressed = jumpPress;
		
//		for (BlockItem block : mLevelManager.mAnimatedStaticBlocks){
//			AsyncClassRequest request = new AsyncClassRequest(REQUEST_WORLD_POLICY_MANAGER_UPDATE);
//			request.putParameter("block", block);
//			AsyncClassManager.sendRequestTo(mStaticBlockManager,request);
//		}
//		AsyncClassRequest request = new AsyncClassRequest(REQUEST_WORLD_POLICY_MANAGER_UPDATE);
//		AsyncClassManager.sendRequestTo(mLevelManager.mDave.getAsyncClassAddress(),request);
//		
//		if (collitionCanGoDown(mLevelManager.mDave)){
//			mLevelManager.mDave.mVy --;
//		}else{
//			mLevelManager.mDave.mVy = 0;
//		}
//		
//		if ((rightPress || leftPress ) && nowPlayingId == 0){
//			nowPlayingId = mSoundPool.play(soundWalkId, 0.9f, 0.9f, 1, -1, 1f);
//		}
//		if (jumpPress && nowPlayingJumpId == 0){
//			nowPlayingJumpId = mSoundPool.play(soundJumpId, 0.9f, 0.9f, 1, -1, 1f);
//		}
//		else if( (nowPlayingId!=0) && !(rightPress || leftPress)){
//			Log.v("davetest","stopping");
//			mSoundPool.stop(nowPlayingId);
//			nowPlayingId = 0;
//		}
//
//		//mLevelLayout.mDave.update(leftPress, rightPress, jumpPress);
//		//collitionDetect(mLevelLayout.mDave,mLevelLayout.mStaticBlocks,mLevelLayout.mAnimatedStaticBlocks);
	}

	public BlockItem getBlockAt(int x, int y){
		return null;
//		for (BlockItem s : mLevelManager.mStaticBlocks)
//			if (s.mX == x && s.mY == y)
//				return s;
//		return null;
	}


	

	/**
	 * Check if block upper is on block lower and allowing a percented offset, it means a block can be over a block
	 * even if it has some percentage which is actually not over the lower block
	 * @param upper
	 * @param lower
	 * @param percent
	 * @return
	 */
	private boolean collitionIsOnBlockPercentage(BlockItem upper,BlockItem lower,int percent){
		if (percent<0 || percent>100)
			throw new RuntimeException();

		Rect upperRect = new Rect(upper.mX,upper.mY,upper.mX + upper.mWidth - 1, upper.mY + upper.mHeight - 1);
		Rect lowerRect = new Rect(lower.mX,lower.mY,lower.mX + lower.mWidth - 1, lower.mY + lower.mHeight - 1);

		float calcOffsetPixels = upperRect.width();
		calcOffsetPixels = ((calcOffsetPixels)/100) * percent;
		int offsetPixels = (int)calcOffsetPixels;

		if (upperRect.bottom == lowerRect.top - 1)
			if (upperRect.right<=(lowerRect.right + offsetPixels) && upperRect.left>=(lowerRect.left - offsetPixels))
				return true;
		return false;

	}

	public List<BlockItem> getObjectsToDraw(){
		List<BlockItem> results = new ArrayList<BlockItem>();
		List<String> bmplist = new ArrayList<String>();
		bmplist.add("redbrick.png");
		results.add(new BlockItem(mContext, bmplist, 12, 30, 30, 64, 64));
//		results.addAll(mLevelManager.mAllBlocks);
//		results.add(mLevelManager.mDave);
		return results;
	}


	public class GameDrawingObject{
		public Bitmap mBitmap;
		public int mActualX;
		public int mActualY;
		public boolean mDebugHighlight;
		int mWidth;
		int mHeight;

		public GameDrawingObject(Bitmap bitmap,int actualX, int actualY,int width, int height, boolean debug){
			mBitmap = bitmap;
			mActualX = actualX;
			mActualY = actualY;
			mDebugHighlight = debug;
		}
	}



	public interface GameObjectUpdate{
		public void update();
	}

	public boolean isGameReady(){

		synchronized (syncMutex) {
			return isReady;
		}
	}




	

	public Bitmap getBitmapForKey(String bitmapKey) {
		Bitmap blockBitmap = mBitmapManager.getBitmap(mContext, bitmapKey);
		return blockBitmap;
	}
	public boolean onBlockMoveXY(BlockItem block, int dX, int dY) {
		// TODO Auto-generated method stub
		return false;
	}

	
	protected void precacheResources(){
		
	}
	protected void startGame(){
		
		
	}
	
	public void processRequests() {
		
//		while(!requestList.isEmpty()){
//			AsyncClassRequest request = requestList.remove(0);
//			
//			if (request.mId == REQUEST_WORLD_POLICY_MANAGER_TOUCH_KEYS){
//				request.respondWith(respondwithKeys());
//			}else if (request.mId == REQUEST_WORLD_POLICY_MANAGER_START_GAME){				
//				startGame(request);
//			}else if (request.mId == REQUEST_MOVE_DXDY){
//				int dx = (Integer)(request.mValues.get(new String("dx")));
//				int dy = (Integer)(request.mValues.get(new String("dy")));
//				
//				
//				
//				Log.v(CLASSNAME,"REQUEST_MOVE_DXDY: "+dx+","+dy);
//	
//				if (dx>0){
//					for (int i=0; i<dx; i++){
//						if (collitionCanGoRight(mLevelManager.mDave)){
//							mLevelManager.mDave.mX ++;
//						}else{
//							//request.respondWith(new AsyncClassResponse(WORLD_RESPONSE_OK));
//						}
//					}
//				}else if (dx<0){
//					int invDx =  (-1)* dx;
//					for (int i=0; i<invDx; i++){
//						if (collitionCanGoLeft(mLevelManager.mDave)){
//							mLevelManager.mDave.mX --;
//						}else{
//							//request.respondWith(new AsyncClassResponse(WORLD_RESPONSE_FAIL));
//						}
//					}
//				}
//				if (dy>0){
//					for (int i=0; i<dy; i++){
//						if (collitionCanGoUp(mLevelManager.mDave)){
//							mLevelManager.mDave.mY ++;
//						}else{
//							//request.respondWith(new AsyncClassResponse(WORLD_RESPONSE_OK));
//						}
//					}
//				}
//				if (dy<0){
//					int invDy =  (-1)* dy;
//					for (int i=0; i<invDy; i++){
//						if (collitionCanGoUp(mLevelManager.mDave)){
//							mLevelManager.mDave.mY --;
//						}else{
//							//request.respondWith(new AsyncClassResponse(WORLD_RESPONSE_OK));
//						}
//					}
//				}
//				
//			}
//			
//		}
		
	}
	
	
	
	private boolean collitionCanGoRight(BlockItem block){
		if (getBlockByPixel(block.mX + block.mWidth, block.mY) != null)
			return false;
		return true;

	}
	private boolean collitionCanGoLeft(BlockItem block){
		if (getBlockByPixel(block.mX - 1, block.mY) != null)
			return false;
		return true;

	}
	private boolean collitionCanGoUp(BlockItem block){
		if (getBlockByPixel(block.mX, block.mY-1) != null)
			return false;
		return true;

	}
	
	private boolean collitionCanGoDown(BlockItem block){
		if (getBlockByPixel(block.mX, block.mY+block.mHeight) != null)
			return false;
		return true;

	}
	
	private BlockItem getBlockByPixel(int x,int y){
		return null;
//		for (BlockItem b : mLevelManager.mAllBlocks)
//			if (x>=b.mX && x<=(b.mX + b.mWidth -1) && y>=b.mY && y<= (b.mY+b.mHeight -1))
//				return b;
//		return null;
	}
	
}

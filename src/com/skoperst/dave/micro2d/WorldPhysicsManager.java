package com.skoperst.dave.micro2d;

import java.util.List;

/**
 * World physics manager.
 * This manager is like god, it can receive requests from blocks and change their physical state in the world
 * which includes moving them, killing them, placing etc... it works via WorldRequest and WorldResponse
 * @author Skoperst
 *
 */
public class WorldPhysicsManager{
	
	public int mWorldScale;
	
	private List<BlockItem> mBlocks;
	
	static private final int WORLD_REQUEST_DX = 1; 
	static private final int WORLD_REQUEST_DV = 2;
	static private final int WORLD_REQUEST_ADD_BLOCK = 3;
	
	static private final int REQUEST_MOVE_DXDY = 83; 
	
	
	static private final int WORLD_RESPONSE_OK = 1;
	static private final int WORLD_RESPONSE_FAIL = 2;
	
	
	public WorldPhysicsManager() {
		
		mBlocks = null;
		
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * The world measure is a block, and its exactly 32 pixels
	 */
	public final static int GODS_BLOCK_SIZE = 32;
	/**
	 * World cold and cruel facts, and they are public so dave & his friends can discover world physics and be smart
	 */
	public final static int PHYSICS_MAX_FALLING_SPEED = 16;
	public final static int PHYSICS_MAX_DISPLACMENT_X = 32;
	
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
		if (getBlockByPixel(block.mX, block.mY-1)!=null)
			return false;
		return true;
	}
	private boolean collitionCanGoDown(BlockItem block){
		if (getBlockByPixel(block.mX, block.mY + block.mHeight)!=null)
			return false;
		return true;
	}
	
	private BlockItem getBlockByPixel(int x,int y){
		for (BlockItem b : mBlocks)
			if (x>=b.mX && x<=(b.mX + b.mWidth -1) && y>=b.mY && y<= (b.mY+b.mHeight -1))
				return b;
		return null;
	}

	
	public void processRequests() {
//		
//		while(!requestList.isEmpty()){
//			AsyncClassRequest request = requestList.remove(0);
//		
//		if (request.mId == WORLD_REQUEST_ADD_BLOCK){			
//			//return AsyncClassResponse.responseOK();
//		}
//		
//		}
		//return AsyncClassResponse.responseOK();	
		
	}
	public void processResponses() {
		// TODO Auto-generated method stub
		
	}
}

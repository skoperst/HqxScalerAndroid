package com.skoperst.dave.micro2d;

import java.util.List;

import android.content.Context;


/**
 * Represents a block that does not move, but has some animation - like water, fire stars etc..
 * @author Skoperst
 *
 */
public class BlockItem{
	
	public List<String> mBitmapKeys;
	public int mCurrentFrameIndex;
	public int mCurrentFrameIndexReduced;
	public static final int mFPSReduceBy = 4;

	
	public String mBitmapKey;
	
	public int mId;
	public int mWidth;
	public int mHeight;
	public boolean mDebugHighlight;
	
	public int mX;
	public int mY;
	public int mVx;
	public int mVy;
	
	
	public BlockItem(Context context,List<String> bitmapKeys,int id,int x,int y,int width,int height){
		mId = id;
		mX = x * width;
		mY = y * height;
		mWidth = width;
		mHeight = height;
		mDebugHighlight = false;
		mCurrentFrameIndex = 0;
		mCurrentFrameIndexReduced = 0;
		mBitmapKeys = bitmapKeys;
		mBitmapKey = mBitmapKeys.get(mCurrentFrameIndexReduced);
	}



}

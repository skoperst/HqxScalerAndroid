package com.skoperst.dave.micro2d;


import android.content.Context;

public class TouchKey {

		public String mBitmapIdle;
		public String mBitmapPressed;
		public int mId;
		public int mX;
		public int mY;
		public int mScale;
		public int mLeftGap;

		public TouchKey(Context context,String bitmapKeyIdle,String bitmapKeyPressed,int x,int y,int scale,int leftgap){
	
			mBitmapIdle = bitmapKeyIdle;
			mBitmapPressed = bitmapKeyPressed;
			mScale = scale;
			mX = x * scale;
			mY = y * scale;
		}
		public void updateKeyScale(int scale){
			mScale = scale;
		}
		public void updateGap(int gap){
			mLeftGap = gap;
		}
		
		public boolean isTouchInKey(int x,int y){
			if ( (x-mLeftGap)>=mX && (x-mLeftGap)<=(mX+(2*mScale)))
				if ((y>= (mY)) && y<=(mY+(2*mScale)))
					return true;
			return false;
					
		}

}

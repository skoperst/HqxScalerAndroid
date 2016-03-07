package com.skoperst.hqx;

import android.util.Log;


public abstract class HqxScaler {
	static {
		System.loadLibrary("hqx");
	}
	
	private HqxScaler() {}
	
	private static native void hq4x(int[] src, int[] dst, int width, int height);
	private static native void hq2x(int[] src, int[] dst, int width, int height);
	
	private static void checkSizes(RawBitmap src, RawBitmap dest, int coeff) {
		if ((dest.getWidth() / src.getWidth() != coeff) 
				|| (dest.getHeight() / src.getHeight() != coeff)) {
			throw new IllegalArgumentException("Invalid src x dest sizes");
		}		
	}
	
	public static void scaleHq4x(RawBitmap src, RawBitmap dest) {
		checkSizes(src, dest, 4);
		System.loadLibrary("hqx");
		Log.v("dave", "src:"+src.getHeight()+"dest:"+dest.getHeight());
		hq4x(src.getPixels(), dest.getPixels(), src.getWidth(), src.getHeight());
		dest.fillAlpha(0xff);
	}
	public static void scaleHq2x(RawBitmap src, RawBitmap dest) {
		checkSizes(src, dest, 2);
		
		hq2x(src.getPixels(), dest.getPixels(), src.getWidth(), src.getHeight());
	}
}

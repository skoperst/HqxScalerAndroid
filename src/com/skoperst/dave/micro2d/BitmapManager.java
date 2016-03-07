package com.skoperst.dave.micro2d;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;




import com.skoperst.hqx.HqxScaler;
import com.skoperst.hqx.RawBitmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;



public class BitmapManager {

	private static BitmapManager mBitmapManager;
	private static HashMap<String,Bitmap> mBitmapCache;

	protected BitmapManager(){

	}
	public static BitmapManager getInstance(){
		if (mBitmapManager == null){
			mBitmapCache = new HashMap<String, Bitmap>();
			mBitmapManager = new BitmapManager();
		}
		return mBitmapManager;
	}
	public static Bitmap loadBitmap(Context context, String filename) throws IOException{
		AssetManager assets = context.getResources().getAssets();
		InputStream buf = new BufferedInputStream((assets.open(filename)));
		Bitmap bitmap = BitmapFactory.decodeStream(buf);
		// Drawable d = new BitmapDrawable(bitmap)
		return bitmap;
	}

	public void precache(Context context, String filename){
		if (mBitmapCache.containsKey(filename)){
			mBitmapCache.remove(filename);
		}
		if (!mBitmapCache.containsKey(filename)){
			try {
				mBitmapCache.put(filename, loadBitmap(context, "sprites/" + filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void precacheAndScale(Context context, String filename,int scale){
		if (mBitmapCache.containsKey(filename)){
			mBitmapCache.remove(filename);
		}
		if (!mBitmapCache.containsKey(filename)){
			try {
				Bitmap bitmap = loadBitmap(context, "sprites/" + filename);
				RawBitmap rawBitmap = new RawBitmap(bitmap);
				rawBitmap.fillAlpha(0x00);
				RawBitmap rawDestBitmap = new RawBitmap(rawBitmap.getWidth() * 4,
						rawBitmap.getHeight() * 4,
						rawBitmap.hasAlpha());

				// first invocation doesn't count, native method 
				// lookup penalty take place here
				// there is a way to avoid it - use JNI method registration
				HqxScaler.scaleHq4x(rawBitmap, rawDestBitmap);

				//   publishProgress("Done, " + (totalTime / iterations) + " ms");		        
				rawDestBitmap.fillAlpha(0xff);
				bitmap = rawDestBitmap.toBitmap();
				bitmap = getResizedBitmap(bitmap, scale, scale);
				mBitmapCache.put(filename, bitmap);


			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation

		Matrix matrix = new Matrix();

		// resize the bit map

		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;

	}


	public Bitmap getBitmap(Context context,String filename){
		if (mBitmapCache.containsKey(filename)){
			return mBitmapCache.get(filename);
		}else{
			precache(context, filename);
			if (!mBitmapCache.containsKey(filename))
				throw new RuntimeException();
		}

		return null;
	}
	public void updateScale(Context context,int mBlockWidth) {
		Set<String> keySet = mBitmapCache.keySet();
		Object[] keyArray = keySet.toArray();
		for (Object s : keyArray){
			precacheAndScale(context, (String)s, mBlockWidth);
		}

	}



}

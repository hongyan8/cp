package com.demo.lottery.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.view.View;

public class ClickEffectUtil
{

	private static ClickEffectUtil instance;

	private long lastOnClickTime = 0;

	private long lockTime = 750;

	private ClickEffectUtil()
	{
	}

	public static ClickEffectUtil getInstance()
	{
		if (instance == null)
		{
			instance = new ClickEffectUtil();
		}
		return instance;
	}

	public boolean isSmoothClick()
	{
		boolean isSmooth = true;
		long current = System.currentTimeMillis();

		if (0 == lastOnClickTime || Math.abs(current - lastOnClickTime) > lockTime)
		{

			lastOnClickTime = current;

			isSmooth = true;
		}
		else
		{
			isSmooth = false;
		}
		return isSmooth;
	}

	public void playAnimation(View view)
	{

	}

	public static Bitmap SavePixels(int x, int y, int w, int h, GL10 gl)
	{
		int b[] = new int[w * h];
		int bt[] = new int[w * h];
		IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
		for (int i = 0; i < h; i++)
		{
			for (int j = 0; j < w; j++)
			{
				int pix = b[i * w + j];
				int pb = (pix >> 16) & 0xff;
				int pr = (pix << 16) & 0x00ff0000;
				int pix1 = (pix & 0xff00ff00) | pr | pb;
				bt[(h - i - 1) * w + j] = pix1;
			}
		}
		Bitmap sb = Bitmap.createBitmap(bt, w, h, Config.RGB_565);
		return sb;
	}

	public static void SavePNG(int x, int y, int w, int h, String fileName, GL10 gl)
	{
		Bitmap bmp = SavePixels(x, y, w, h, gl);
		try
		{
			FileOutputStream fos = new FileOutputStream("/sdcard/android123/" + fileName);

			bmp.compress(CompressFormat.PNG, 100, fos);
			try
			{
				fos.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				fos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public void slowDownDialog(Context context)
	{

	}
}

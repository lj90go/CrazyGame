package com.game.jieluo.crazygame.math;

import android.util.Log;

/**
 * Created by edutech on 16/6/22.
 */
public class NSLog
{
    private static boolean ShowLog = true;

    public static void setShowLog(boolean boo)
    {
        ShowLog = boo;
    }

    public static void ELog(String TAG,String message)
    {
        if(message==null||!ShowLog)
        {
            return;
        }
        Log.e(TAG,message);
    }
    public static void WLog(String TAG,String message)
    {
        if(message==null||!ShowLog)
        {
            return;
        }
        Log.w(TAG,message);
    }
    public static void DLog(String TAG,String message)
    {
        if(message==null||!ShowLog)
        {
            return;
        }
        Log.d(TAG,message);
    }
    public static void ILog(String TAG,String message)
    {
        if(message==null||!ShowLog)
        {
            return;
        }
        Log.i(TAG,message);
    }
}

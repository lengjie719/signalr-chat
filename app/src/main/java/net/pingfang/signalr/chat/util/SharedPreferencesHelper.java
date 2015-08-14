package net.pingfang.signalr.chat.util;

import android.content.Context;
import android.content.SharedPreferences;

import net.pinggang.signalr.chat.R;

/**
 * Created by gongguopei87@gmail.com on 2015/8/13.
 */
public class SharedPreferencesHelper {
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public static SharedPreferencesHelper helper;

    private SharedPreferencesHelper(Context context) {
        String pref = context.getResources().getString(R.string.prefs_name);
        sp = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SharedPreferencesHelper newInstance(Context context) {
        if (helper == null)
        {
            helper = new SharedPreferencesHelper(context);
        }
        return helper;
    }

    public void putStringValue(String key, String value) {
        editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getStringValue(String key) {
        return getStringValue(key,null);
    }

    public String getStringValue(String key,String defalut)
    {
        return sp.getString(key, defalut);
    }

    public void setBooleanValue(String key,boolean value) {
        editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanValue(String key,boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public void putIntValue(String key, int value) {
        editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getIntValue(String key,int defaultV) {
        return sp.getInt(key, defaultV);
    }

    public void putFloatValue(String key,float value) {
        editor = sp.edit();
        editor.putFloat(key,value);
        editor.commit();
    }

    public float getFloatValue(String key,float defaultV) {
        return sp.getFloat(key,defaultV);
    }
}

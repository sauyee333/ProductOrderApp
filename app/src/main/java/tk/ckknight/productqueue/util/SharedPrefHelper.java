package tk.ckknight.productqueue.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sauyee on 21/1/18.
 */

public class SharedPrefHelper {
    private static String SECURE_PREF_KEY = "";
    private static String SECURE_PREF_FILE = "";

    private static SharedPreferences mSecurePrefs;

    private static void init(Context context) {
        if (mSecurePrefs == null) {
            mSecurePrefs = new SecurePreferences(context, SECURE_PREF_KEY, SECURE_PREF_FILE);
        }
    }

    public static String readStringFromSharedPref(Context context, String key, String defaultValue) {
        init(context);
        return mSecurePrefs.getString(key, defaultValue);
    }

    public static void writeStringToSharePref(Context context, String key, String value) {
        init(context);
        mSecurePrefs.edit().putString(key, value).commit();
    }

    public static void removeStringFromSharedPref(Context context, String key) {
        init(context);
        mSecurePrefs.edit().remove(key).commit();
    }

    public static boolean readBooleanFromSharedPref(Context context, String key, boolean defValue) {
        init(context);
        return mSecurePrefs.getBoolean(key, defValue);
    }

    public static void writeBooleanToSharePref(Context context, String key, boolean value) {
        init(context);
        mSecurePrefs.edit().putBoolean(key, value).commit();
    }

    public static long readLongFromSharedPref(Context context, String key, long defValue) {
        init(context);
        return mSecurePrefs.getLong(key, defValue);
    }

    public static void writeLongToSharePref(Context context, String key, long value) {
        init(context);
        mSecurePrefs.edit().putLong(key, value).apply();
    }

    public static void clearPrefData() {
        mSecurePrefs.edit().clear().commit();
    }

}

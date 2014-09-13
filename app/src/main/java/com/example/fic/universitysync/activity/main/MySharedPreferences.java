package com.example.fic.universitysync.activity.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fic on 10.9.2014.
 */
public class MySharedPreferences {
    static final String USER_ID_KEY = "user_id";

    public static void saveUserId(Context ctx, int userId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.commit();
    }

    public static int loadUserId(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(USER_ID_KEY, -1);
    }
}

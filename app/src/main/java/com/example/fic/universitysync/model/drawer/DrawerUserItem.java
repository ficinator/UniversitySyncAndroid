package com.example.fic.universitysync.model.drawer;

import com.example.fic.universitysync.model.data.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fic on 12.9.2014.
 */
public class DrawerUserItem extends DrawerItem {

    //TODO: zero for icon
    public DrawerUserItem(User user) {
        super(user.getId(), user.getThumb(), user.getFullName(), user.getUniversity(), 0);
    }
}

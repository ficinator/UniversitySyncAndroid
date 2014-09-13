package com.example.fic.universitysync.model.drawer;

import com.example.fic.universitysync.model.data.Group;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fic on 11.9.2014.
 */
public class DrawerGroupItem extends DrawerItem {

    //TODO: zeros for icon and count
    public DrawerGroupItem(Group group) {
        super(group.getId(), group.getThumb(), group.getName(), group.getUniversity(), 0);
    }
}

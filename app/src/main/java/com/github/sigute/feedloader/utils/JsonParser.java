package com.github.sigute.feedloader.utils;

import com.github.sigute.feedloader.feed.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to parse out feed out of JSON.
 *
 * @author Sigute
 */
public class JsonParser
{
    public static List<Post> parseOutJsonData(String jsonString) throws JSONException
    {
        List<Post> feed = new ArrayList<Post>();

        JSONArray json = new JSONArray(jsonString);
        for (int i = 0; i < json.length(); i++)
        {
            JSONObject postJsonObject = json.getJSONObject(i);
            Post post = parseOutPost(postJsonObject);
            feed.add(post);
        }

        return feed;
    }

    private static Post parseOutPost(JSONObject postJsonObject) throws JSONException
    {
        int userId = postJsonObject.getInt("userId");
        int id = postJsonObject.getInt("id");
        String title = postJsonObject.getString("title");
        String body = postJsonObject.getString("body");

        return new Post(userId, id, title, body);
    }
}

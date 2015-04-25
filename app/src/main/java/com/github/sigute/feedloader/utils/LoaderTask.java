package com.github.sigute.feedloader.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.github.sigute.feedloader.R;
import com.github.sigute.feedloader.exceptions.DatabaseInsertException;
import com.github.sigute.feedloader.exceptions.DatabaseSelectException;
import com.github.sigute.feedloader.exceptions.NetworkIOException;
import com.github.sigute.feedloader.exceptions.NetworkUnavailableException;
import com.github.sigute.feedloader.exceptions.ServerException;
import com.github.sigute.feedloader.exceptions.ServerResponseReadException;
import com.github.sigute.feedloader.feed.Post;

import org.json.JSONException;

import java.util.List;

/**
 * Loader task - retrieves data, parses JSON, checks offline database if there is no network.
 *
 * @author Sigute
 */
public class LoaderTask extends AsyncTask<Void, Void, Pair<List<Post>, String>>
{
    /**
     * Implement this listener to get feedback from the task.
     */
    public interface TaskListener
    {
        void onTaskStarted();

        void onTaskFinished(List<Post> feed);

        void onTaskFailure(String errorMessage);
    }

    Context context;
    private TaskListener listener;

    public LoaderTask(Context context, TaskListener taskListener)
    {
        this.context = context;
        this.listener = taskListener;
    }

    @Override
    protected void onPreExecute()
    {
        listener.onTaskStarted();
    }

    @Override
    protected Pair<List<Post>, String> doInBackground(Void[] voids)
    {
        List<Post> feed;
        String jsonString;
        try
        {
            jsonString = Server.retrieveJSONString(context);
        }
        catch (NetworkUnavailableException e)
        {
            return tryDatabaseWithErrorString(
                    context.getString(R.string.error_network_unavailable));
        }
        catch (ServerException e)
        {
            return tryDatabaseWithErrorString(context.getString(R.string.error_server));
        }
        catch (NetworkIOException e)
        {
            return tryDatabaseWithErrorString(context.getString(R.string.error_network_io));
        }
        catch (ServerResponseReadException e)
        {
            return tryDatabaseWithErrorString(
                    context.getString(R.string.error_server_response_read));
        }

        try
        {
            feed = JsonParser.parseOutJsonData(jsonString);
        }
        catch (JSONException e)
        {
            return tryDatabaseWithErrorString(context.getString(R.string.error_json_parsing));
        }

        storeFeedInDatabase(feed);
        return new Pair<>(feed, null);
    }

    private Pair<List<Post>, String> tryDatabaseWithErrorString(String errorString)
    {
        List<Post> feed = feedFromDatabase();
        if (feed != null)
        {
            storeFeedInDatabase(feed);
            return new Pair<>(feed, null);
        }
        return new Pair<>(null, errorString);
    }

    public void storeFeedInDatabase(List<Post> feed)
    {
        try
        {
            DatabaseHelper.getInstance(context).insertFeed(feed);
        }
        catch (DatabaseInsertException e)
        {
            // insert failed. I guess we are not storing data offline this time...
        }
    }

    private List<Post> feedFromDatabase()
    {
        try
        {
            return DatabaseHelper.getInstance(context).selectFeed();
        }
        catch (DatabaseSelectException e)
        {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Pair<List<Post>, String> pair)
    {
        if (pair.first != null)
        {
            listener.onTaskFinished(pair.first);
        }
        else
        {
            listener.onTaskFailure(pair.second);
        }
    }
}
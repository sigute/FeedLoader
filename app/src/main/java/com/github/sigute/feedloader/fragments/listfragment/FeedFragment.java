package com.github.sigute.feedloader.fragments.listfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.github.sigute.feedloader.feed.Post;
import com.github.sigute.feedloader.utils.LoaderTask;

import java.util.List;


/**
 * This fragment retrieves and shows the feed.
 *
 * @author Sigute
 */
public class FeedFragment extends ListFragment implements LoaderTask.TaskListener
{
    private FeedFragmentCallbacks callbacks = sDummyFeedFragmentCallbacks;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Must implement this! Returns data from fragment.
     */
    public interface FeedFragmentCallbacks
    {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(Post post);

        /**
         * Callback for when an error occurs.
         */
        void onFailure(String errorMessage);

        /**
         * Callback for when a refresh is completed.
         */
        void onRefreshCompleted();
    }

    // A dummy implementation used only when this fragment is not attached to an activity. Does not do anything.
    private static FeedFragmentCallbacks sDummyFeedFragmentCallbacks = new FeedFragmentCallbacks()
    {
        @Override
        public void onItemSelected(Post post)
        {
        }

        @Override
        public void onFailure(String errorMessage)
        {
        }

        @Override
        public void onRefreshCompleted()
        {
        }
    };

    private List<Post> feed;
    private boolean tabletMode;
    public static final String TABLET_MODE_KEY = "TABLET_MODE_KEY";

    public FeedFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        tabletMode = getArguments().getBoolean(TABLET_MODE_KEY, false);
        new LoaderTask(getActivity(), this).execute();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        getListView().setChoiceMode(
                tabletMode ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION))
        {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        if (!(activity instanceof FeedFragmentCallbacks))
        {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        callbacks = (FeedFragmentCallbacks) activity;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        callbacks = sDummyFeedFragmentCallbacks;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION)
        {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    private void setActivatedPosition(int position)
    {
        if (position == ListView.INVALID_POSITION)
        {
            getListView().setItemChecked(mActivatedPosition, false);
        }
        else
        {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        super.onListItemClick(listView, view, position, id);
        callbacks.onItemSelected(feed.get(position));
    }

    @Override
    public void onTaskStarted()
    {
        //could do something here in the future, like show a custom spinner?
    }

    @Override
    public void onTaskFinished(List<Post> feed)
    {
        this.feed = feed;
        FeedAdapter adapter = new FeedAdapter(getActivity(), feed);
        setListAdapter(adapter);
        callbacks.onRefreshCompleted();
    }

    @Override
    public void onTaskFailure(String errorMessage)
    {
        callbacks.onFailure(errorMessage);
    }
}

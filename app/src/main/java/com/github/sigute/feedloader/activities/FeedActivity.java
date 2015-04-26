package com.github.sigute.feedloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.sigute.feedloader.R;
import com.github.sigute.feedloader.feed.Post;
import com.github.sigute.feedloader.fragments.PostDetailFragment;
import com.github.sigute.feedloader.fragments.listfragment.FeedFragment;


/**
 * Activity shows list fragment and handles navigation to detail activity.
 * Errors while retrieving data are displayed in this activity.
 *
 * @author Sigute
 */
public class FeedActivity extends BaseActivity implements FeedFragment.FeedFragmentCallbacks
{
    private TextView errorView;
    private Menu menu;

    private boolean tabletMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        errorView = (TextView) findViewById(R.id.text_view_feed_error);
        errorView.setVisibility(View.GONE);

        if (findViewById(R.id.post_detail_container) != null)
        {
            //this view present only in wide layout, so this must be tablet mode
            tabletMode = true;
        }

        refreshFeed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_refresh:
                errorView.setVisibility(View.GONE);
                refreshFeed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshFeed()
    {
        menu.findItem(R.id.action_refresh).setEnabled(false);

        FeedFragment fragment = new FeedFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(FeedFragment.TABLET_MODE_KEY, tabletMode);
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_feed_container, fragment).commit();
    }

    @Override
    public void onItemSelected(Post post)
    {
        if (tabletMode)
        {
            //add a fragment in the same layout
            Bundle arguments = new Bundle();
            arguments.putInt(PostDetailFragment.PostKeys.ID, post.getId());
            arguments.putInt(PostDetailFragment.PostKeys.USER_ID, post.getUserId());
            arguments.putString(PostDetailFragment.PostKeys.TITLE, post.getTitle());
            arguments.putString(PostDetailFragment.PostKeys.BODY, post.getBody());
            PostDetailFragment fragment = new PostDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.post_detail_container, fragment).commit();
        }
        else
        {
            //open a new activity on phone
            Intent detailIntent = new Intent(this, PostDetailActivity.class);
            detailIntent.putExtra(PostDetailFragment.PostKeys.ID, post.getId());
            detailIntent.putExtra(PostDetailFragment.PostKeys.USER_ID, post.getUserId());
            detailIntent.putExtra(PostDetailFragment.PostKeys.TITLE, post.getTitle());
            detailIntent.putExtra(PostDetailFragment.PostKeys.BODY, post.getBody());
            startActivity(detailIntent);
        }
    }

    @Override
    public void onFailure(String errorMessage)
    {
        errorView.setText(errorMessage);
        errorView.setVisibility(View.VISIBLE);

        findViewById(R.id.fragment_feed_container).setVisibility(View.GONE);
    }

    @Override
    public void onRefreshCompleted()
    {
        menu.findItem(R.id.action_refresh).setEnabled(true);
    }
}

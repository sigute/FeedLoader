package com.github.sigute.feedloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
public class FeedActivity extends ActionBarActivity implements FeedFragment.FeedFragmentCallbacks
{
    private TextView errorView;
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
            ((FeedFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_feed))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(Post post)
    {
        if (tabletMode)
        {
            Bundle arguments = new Bundle();
            arguments.putInt(PostDetailFragment.PostKeys.ID,
                    getIntent().getIntExtra(PostDetailFragment.PostKeys.ID, -1));
            arguments.putInt(PostDetailFragment.PostKeys.USER_ID,
                    getIntent().getIntExtra(PostDetailFragment.PostKeys.USER_ID, -1));
            arguments.putString(PostDetailFragment.PostKeys.TITLE,
                    getIntent().getStringExtra(PostDetailFragment.PostKeys.TITLE));
            arguments.putString(PostDetailFragment.PostKeys.BODY,
                    getIntent().getStringExtra(PostDetailFragment.PostKeys.BODY));
            PostDetailFragment fragment = new PostDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.post_detail_container, fragment)
                    .commit();
        }
        else
        {
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

        findViewById(R.id.fragment_feed).setVisibility(View.GONE);
    }
}
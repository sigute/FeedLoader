package com.github.sigute.feedloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.github.sigute.feedloader.R;
import com.github.sigute.feedloader.fragments.PostDetailFragment;

/**
 * Activity shows detail fragment and handles navigation to list activity.
 *
 * @author Sigute
 */
public class PostDetailActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            NavUtils.navigateUpTo(this, new Intent(this, FeedActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

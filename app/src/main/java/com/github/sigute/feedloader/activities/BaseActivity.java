package com.github.sigute.feedloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

import com.github.sigute.feedloader.other.MyApplication;

/**
 * Base activity used to manage code common to all activities.
 * <p/>
 * Application state check adapted from http://stackoverflow.com/a/15573121/1615525
 *
 * @author Sigute
 */
public class BaseActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // the following prevents Android from taking screenshot for recent apps view
        // not strictly needed for PIN entry screen, so could filter
        // but this ensures that any activities added in the future don't have screenshots enabled by default
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //check if the application was closed and return to lock screen if that's the case
        MyApplication application = (MyApplication) getApplication();
        if (application.applicationWasClosed())
        {
            Intent feedActivityIntent = new Intent(this, LockScreenActivity.class);
            feedActivityIntent
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(feedActivityIntent);
            finish();
        }

        application.stopActivityTransitionTimer();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        ((MyApplication) getApplication()).startActivityTransitionTimer();
    }
}

package com.github.sigute.feedloader.other;

import android.app.Application;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class extends Android application and helps to manage lifecycle.
 *
 * @author Sigute
 */
public class MyApplication extends Application
{
    private Timer activityTransitionTimer;
    private TimerTask activityTransitionTimerTask;
    private boolean applicationWasClosed;

    // assuming two seconds - which still gives the user time to close and reopen app,
    // but very unlikely that someone else grabbed the phone in that time
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2 * 1000;

    public void startActivityTransitionTimer()
    {
        activityTransitionTimer = new Timer();
        activityTransitionTimerTask = new TimerTask()
        {
            public void run()
            {
                setApplicationWasClosed(true);
            }
        };

        activityTransitionTimer
                .schedule(activityTransitionTimerTask, MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer()
    {
        if (activityTransitionTimerTask != null)
        {
            activityTransitionTimerTask.cancel();
        }

        if (activityTransitionTimer != null)
        {
            activityTransitionTimer.cancel();
        }

        setApplicationWasClosed(false);
    }

    private void setApplicationWasClosed(boolean applicationWasClosed)
    {
        this.applicationWasClosed = applicationWasClosed;
    }

    public boolean applicationWasClosed()
    {
        return applicationWasClosed;
    }
}

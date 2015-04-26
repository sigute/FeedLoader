package com.github.sigute.feedloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.sigute.feedloader.R;
import com.github.sigute.feedloader.utils.DatabaseHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Activity manages user PIN entry.
 *
 * @author Sigute
 */
public class LockScreenActivity extends BaseActivity
{
    private Button okButton;
    private EditText pinEntryEditText;
    private TextView errorView;

    private Set<String> bannedPins;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        okButton = (Button) findViewById(R.id.button_lock_screen_ok);
        pinEntryEditText = (EditText) findViewById(R.id.edit_text_lock_screen_pin);
        errorView = (TextView) findViewById(R.id.text_view_lock_screen_error);
        errorView.setVisibility(View.GONE);

        setListeners();
        setUpBannedPins();
    }

    private void setListeners()
    {
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isPINCorrect())
                {
                    startFeedActivity();
                }
                else
                {
                    errorView.setText(getString(R.string.error_wrong_pin));
                    errorView.setVisibility(View.VISIBLE);
                }
            }
        });

        pinEntryEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    okButton.setEnabled(isPINValid());
                }
            }
        });

        pinEntryEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                //don't care
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                //don't care
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                okButton.setEnabled(isPINValid());
            }
        });

        pinEntryEditText.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    if (!isPINValid())
                    {
                        return false;
                    }

                    if (!isPINCorrect())
                    {
                        errorView.setText(getString(R.string.error_wrong_pin));
                        errorView.setVisibility(View.VISIBLE);
                        return false;
                    }

                    startFeedActivity();

                    return true;
                }
                return false;
            }
        });
    }

    private void setUpBannedPins()
    {
        //ideally this would read from file, but this will do for now...
        bannedPins = new HashSet<String>();
        // list of the PINs which are not allowed
        // but with 4-6 chars and numbers only... well, how long would that take to brute force?
        String bannedPinsArray[] = {//
                "1234", "12345", "123456", //
                "0000", "00000", "000000", //
                "1111", "11111", "111111", //
                "2222", "22222", "222222", //
                "3333", "33333", "333333", //
                "4444", "44444", "444444", //
                "5555", "55555", "555555", //
                "6666", "66666", "666666", //
                "7777", "77777", "777777", //
                "8888", "88888", "888888", //
                "9999", "99999", "999999", //
                //and some from this research http://www.datagenetics.com/blog/september32012/ just to really make sure :)
                "1212",//
                "1004",//
                "6969",//*sighs*
                "2000",// should this just ban the current year?
                "2001", "2015", // yes, let's do that
                "1122", //
                "1313", //
                "4321", "54321", "654321", //
                "1313", //
                "1010"};
        bannedPins.addAll(Arrays.asList(bannedPinsArray));
    }

    private boolean isPINValid()
    {
        String pin = pinEntryEditText.getText().toString();

        //length restrictions
        // 4-6 characters, max length limited in xml
        if (pin.length() < 4)
        {
            return false;
        }

        if (bannedPins.contains(pin))
        {
            //this should probably warn user, otherwise it will be confusing.
            //future development?
            return false;
        }

        return true;
    }

    private boolean isPINCorrect()
    {
        return DatabaseHelper.setUp(getApplicationContext(), pinEntryEditText.getText().toString());
    }

    private void startFeedActivity()
    {
        //start activity and remove itself, as back button should navigate from app, rather than back to pin screen
        Intent feedActivityIntent = new Intent(this, FeedActivity.class);
        feedActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(feedActivityIntent);
        finish();
    }
}

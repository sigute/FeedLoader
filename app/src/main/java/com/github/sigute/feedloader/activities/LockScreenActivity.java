package com.github.sigute.feedloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.sigute.feedloader.R;

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
    }

    private boolean isPINValid()
    {
        String pin = pinEntryEditText.getText().toString();

        if (pin.length() < 4)
        {
            return false;
        }

        return true;
    }

    private boolean isPINCorrect()
    {
        //TODO implement PIN checking
        return true;
    }

    private void startFeedActivity()
    {
        Intent feedActivityIntent = new Intent(this, FeedActivity.class);
        feedActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(feedActivityIntent);
        finish();
    }
}

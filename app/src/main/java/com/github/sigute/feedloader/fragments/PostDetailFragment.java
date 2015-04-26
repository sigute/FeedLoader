package com.github.sigute.feedloader.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sigute.feedloader.R;


/**
 * This fragment displays the post details.
 *
 * @author Sigute
 */
public class PostDetailFragment extends Fragment
{
    public class PostKeys
    {
        public static final String ID = "POST_ID";
        public static final String USER_ID = "POST_USER_ID";
        public static final String TITLE = "POST_TITLE";
        public static final String BODY = "POST_BODY";

    }

    //id is not used currently, but might be useful if in the future post comments need to be retrieved
    private int id;
    private int userId;
    private String title;
    private String body;

    public PostDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        id = getArguments().getInt(PostKeys.ID, -1);
        userId = getArguments().getInt(PostKeys.USER_ID, -1);
        title = getArguments().getString(PostKeys.TITLE, "");
        body = getArguments().getString(PostKeys.BODY, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);

        ((TextView) rootView.findViewById(R.id.text_view_post_user_id)).setText("" + userId);
        ((TextView) rootView.findViewById(R.id.text_view_post_title)).setText(title);
        ((TextView) rootView.findViewById(R.id.text_view_post_body)).setText(body);

        return rootView;
    }
}

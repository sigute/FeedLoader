package com.github.sigute.feedloader.fragments.listfragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.sigute.feedloader.R;
import com.github.sigute.feedloader.feed.Post;

import java.util.List;

/**
 * Adapter for Employee list.
 *
 * @author Sigute
 */
public class FeedAdapter extends ArrayAdapter<Post>
{
    List<Post> feed;

    public FeedAdapter(Context context, List<Post> feed)
    {
        super(context, 0, feed);
        this.feed = feed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        if (feed != null)
        {
            final Post post = feed.get(position);

            v = View.inflate(getContext(), R.layout.list_item_post, null);

            ((TextView) v.findViewById(R.id.text_view_list_post_user_id))
                    .setText("" + post.getUserId());
            ((TextView) v.findViewById(R.id.text_view_list_post_title)).setText(post.getTitle());
        }
        return v;
    }
}

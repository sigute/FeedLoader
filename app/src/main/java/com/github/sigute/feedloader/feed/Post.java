package com.github.sigute.feedloader.feed;

/**
 * This class represents a post from the feed and holds its attributes.
 *
 * @auther Sigute
 */
public class Post
{
    private int userId;
    private int id;
    private String title;
    private String body;

    @SuppressWarnings("unused")
    private Post()
    {
        //should not be called directly
    }

    public Post(int userId, int id, String title, String body)
    {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getUserId()
    {
        return userId;
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getBody()
    {
        return body;
    }
}

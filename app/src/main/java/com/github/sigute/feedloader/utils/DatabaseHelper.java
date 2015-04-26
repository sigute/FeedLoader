package com.github.sigute.feedloader.utils;


import android.content.ContentValues;
import android.content.Context;

import com.github.sigute.feedloader.exceptions.DatabaseInsertException;
import com.github.sigute.feedloader.exceptions.DatabaseSelectException;
import com.github.sigute.feedloader.feed.Post;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Database helper - provides methods to insert and select feed posts for offline viewing.
 * Helps to ensure only once instance of database is open at a time.
 * Methods are synchronized to ensure there are no clashes.
 *
 * @author Sigute
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "feed.db";
    private static final int DATABASE_VERSION = 1;

    private class TableNames
    {
        static final String POST = "post";
    }

    private class PostTableColumns
    {
        static final String ID = "id";
        static final String USER_ID = "user_id";
        static final String TITLE = "title";
        static final String BODY = "body";
    }

    public static final String CREATE_POST_TABLE = //
            "CREATE TABLE  " + TableNames.POST + //
                    "(" + //
                    PostTableColumns.ID + " text not null unique, " + //
                    PostTableColumns.USER_ID + " text not null," + //
                    PostTableColumns.TITLE + " text not null, " + //
                    PostTableColumns.BODY + " text not null" + //
                    ");";


    private static DatabaseHelper instance;
    private static SQLiteDatabase database;

    public static synchronized boolean setUp(Context context, String pin)
    {
        //remove any older version
        destroy();

        instance = new DatabaseHelper(context.getApplicationContext());

        //below code will throw an exception if the password is wrong
        //if the password was not set or matches, it will open the database
        try
        {
            database = instance.getWritableDatabase(pin);
        }
        catch (SQLiteException e)
        {
            //wrong password, probably
            return false;
        }

        return true;
    }

    public static synchronized DatabaseHelper getInstance()
    {
        if (instance == null)
        {
            throw new IllegalStateException("Database not initialised");
        }
        return instance;
    }

    public static synchronized void destroy()
    {
        if (instance != null)
        {
            database.close();
            instance = null;
        }
    }

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_POST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
    }

    public synchronized void insertFeed(List<Post> feed) throws DatabaseInsertException
    {
        for (Post post : feed)
        {
            ContentValues values = new ContentValues();
            values.put(PostTableColumns.ID, post.getId());
            values.put(PostTableColumns.USER_ID, post.getUserId());
            values.put(PostTableColumns.TITLE, post.getTitle());
            values.put(PostTableColumns.BODY, post.getBody());
            long result = database.replace(TableNames.POST, null, values);
            if (result == -1)
            {
                throw new DatabaseInsertException("Post insert failed");
            }
        }
    }

    public synchronized List<Post> selectFeed() throws DatabaseSelectException
    {
        List<Post> feed = new ArrayList<Post>();

        Cursor cursor = null;
        try
        {
            cursor = database.query(TableNames.POST, // table
                    new String[]{ // columns
                            PostTableColumns.ID, //
                            PostTableColumns.USER_ID, //
                            PostTableColumns.TITLE,//
                            PostTableColumns.BODY, //
                    }, null, null, null, null, null);

            if (cursor == null || !cursor.moveToNext())
            {
                throw new DatabaseSelectException("Could not select Post");
            }

            do
            {
                int id = Integer
                        .parseInt(cursor.getString(cursor.getColumnIndex(PostTableColumns.ID)));
                int userId = Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex(PostTableColumns.USER_ID)));
                String title = cursor.getString(cursor.getColumnIndex(PostTableColumns.TITLE));
                String body = cursor.getString(cursor.getColumnIndex(PostTableColumns.BODY));

                feed.add(new Post(id, userId, title, body));
            }
            while (cursor.moveToNext());
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }

        return feed;
    }
}

package com.github.sigute.feedloader;

import android.test.InstrumentationTestCase;

import com.github.sigute.feedloader.feed.Post;
import com.github.sigute.feedloader.utils.JsonParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Test cases for JsonParser
 *
 * @author Sigute
 */
public class JsonParserTest extends InstrumentationTestCase
{
    private String validJson;

    @Override
    protected void setUp() throws Exception
    {
        InputStream is = getInstrumentation().getTargetContext().getResources()
                .openRawResource(R.raw.json_test);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            json.append(line);
        }

        validJson = json.toString();
    }

    public void testParseOutInvalidJsonData() throws Exception
    {
        try
        {
            List<Post> feed = JsonParser.parseOutJsonData("invalidData");
            assertFalse("Parsed out feed from invalid string", feed != null);
        }
        catch (JSONException e)
        {
            assertTrue("Could not parse invalid string, as expected", true);
        }
    }

    public void testParseOutValidJsonData() throws Exception
    {
        List<Post> feed;
        try
        {
            feed = JsonParser.parseOutJsonData(validJson);
        }
        catch (JSONException e)
        {
            assertFalse("Could not parse the JSON string!", false);
            return;
        }

        assertTrue("Parsed out feed from string", feed != null);
        assertTrue("Parsed out three posts from feed", feed.size() == 3);

        for (int i = 0; i < 3; i++)
        {
            Post post = feed.get(i);
            assertTrue("Post has the right id", post.getId() == i);
            assertTrue("Post has correct userId", post.getUserId() == i);
            assertTrue("Post has correct title", post.getTitle().equals("Title" + i));
            assertTrue("Post has correct body", post.getBody().equals("Body" + i));
        }
    }
}
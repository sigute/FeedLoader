package com.github.sigute.feedloader;

import android.test.InstrumentationTestCase;

import com.github.sigute.feedloader.utils.DatabaseHelper;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Test case for the new pin.
 *
 * @author Sigute
 */
public class NewPinTest extends InstrumentationTestCase
{
    private String newPin = "12369";

    @Override
    protected void setUp() throws Exception
    {
        //delete database before starting the test, to ensure that it is initialised from start
        getInstrumentation().getTargetContext().deleteDatabase("feed.db");

        //load in SQLCipher dependencies
        SQLiteDatabase.loadLibs(getInstrumentation().getTargetContext());
    }

    public void testNewPin() throws Exception
    {
        if (DatabaseHelper.setUp(getInstrumentation().getTargetContext(), newPin))
        {
            assertTrue("New database set up", true);
        }
        else
        {
            assertFalse("Could not setup a new database", false);
        }
    }

    @Override
    protected void tearDown() throws Exception
    {
        //close & delete the database
        DatabaseHelper.destroy();
        getInstrumentation().getTargetContext().deleteDatabase("feed.db");
    }
}

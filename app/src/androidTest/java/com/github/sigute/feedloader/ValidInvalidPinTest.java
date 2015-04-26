package com.github.sigute.feedloader;

import android.test.InstrumentationTestCase;

import com.github.sigute.feedloader.utils.DatabaseHelper;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Test cases for pin to open database.
 * Should accept new/valid pin and
 *
 * @author Sigute
 */
public class ValidInvalidPinTest extends InstrumentationTestCase
{
    private String validPin = "12369";
    private String invalidPin = "96321";

    @Override
    protected void setUp() throws Exception
    {
        //delete database before starting the test, to ensure that the pins used will make sense
        getInstrumentation().getTargetContext().deleteDatabase("feed.db");

        //load in SQLCipher dependencies
        SQLiteDatabase.loadLibs(getInstrumentation().getTargetContext());

        //initialise a new database with the starting pin, to give something to test against
        if (DatabaseHelper.setUp(getInstrumentation().getTargetContext(), validPin))
        {
            DatabaseHelper.destroy();
        }
        else
        {
            assertFalse("Setup failed", false);
        }
    }

    public void testValidPin() throws Exception
    {
        if (DatabaseHelper.setUp(getInstrumentation().getTargetContext(), validPin))
        {
            assertTrue("Valid pin worked", true);
            DatabaseHelper.destroy();
        }
        else
        {
            assertFalse("Could not use valid pin", false);
        }
    }

    public void testInvalidPin() throws Exception
    {
        if (!DatabaseHelper.setUp(getInstrumentation().getTargetContext(), invalidPin))
        {
            assertTrue("Invalid pin did not work", true);
        }
        else
        {
            assertFalse("Invalid pin opened database", false);
            DatabaseHelper.destroy();
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

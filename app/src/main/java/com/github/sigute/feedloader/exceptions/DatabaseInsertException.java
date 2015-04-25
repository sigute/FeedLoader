package com.github.sigute.feedloader.exceptions;

/**
 * This exception is used when database insert fails.
 *
 * @author Sigute
 */
public class DatabaseInsertException extends Exception
{
    public DatabaseInsertException(String message)
    {
        super(message);
    }
}

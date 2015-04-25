package com.github.sigute.feedloader.exceptions;


/**
 * This exception is used when database read fails.
 *
 * @author Sigute
 */
public class DatabaseSelectException extends Exception
{
    public DatabaseSelectException(String message)
    {
        super(message);
    }
}

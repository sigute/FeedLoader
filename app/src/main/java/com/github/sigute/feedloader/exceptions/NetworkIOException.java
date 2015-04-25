package com.github.sigute.feedloader.exceptions;


/**
 * This exception is used when unspecified IO exception occurs when connecting to network.
 *
 * @author Sigute
 */
public class NetworkIOException extends Exception
{
    public NetworkIOException(String message)
    {
        super(message);
    }
}

package com.github.sigute.feedloader.exceptions;


/**
 * This exception is used when network is not available.
 *
 * @author Sigute
 */
public class NetworkUnavailableException extends Exception
{
    public NetworkUnavailableException(String message)
    {
        super(message);
    }
}

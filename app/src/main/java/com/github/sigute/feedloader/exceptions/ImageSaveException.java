package com.github.sigute.feedloader.exceptions;


/**
 * This exception is used when image cannot be saved in data store.
 *
 * @author Sigute
 */
public class ImageSaveException extends Exception
{
    public ImageSaveException(String message)
    {
        super(message);
    }
}

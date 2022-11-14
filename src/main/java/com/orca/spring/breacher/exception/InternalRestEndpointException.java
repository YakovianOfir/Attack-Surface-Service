package com.orca.spring.breacher.exception;

public class InternalRestEndpointException extends Exception
{
    public InternalRestEndpointException()
    {
        super();
    }

    public InternalRestEndpointException(String message)
    {
        super(message);
    }

    public InternalRestEndpointException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
package com.orca.spring.breacher.exception;

public class InvalidCommandLineException extends Exception
{
    public InvalidCommandLineException()
    {
        super();
    }

    public InvalidCommandLineException(String message)
    {
        super(message);
    }

    public InvalidCommandLineException(Throwable cause)
    {
        super(cause);
    }
}
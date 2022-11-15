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

    public InternalRestEndpointException(Throwable cause, String message, String arg1, String... args)
    {
        super(formatExceptionMessage(message, arg1, args), cause);
    }

    private static String formatExceptionMessage(String template, String arg1, String... args)
    {
        var tokens = new String[args.length + 1];

        System.arraycopy(args, 0, tokens, 0, args.length);

        tokens[args.length] = arg1;

        return String.format(template, tokens);
    }
}
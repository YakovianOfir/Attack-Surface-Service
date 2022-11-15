package com.orca.spring.breacher.utils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ServiceTestRandomUtils
{
    private ServiceTestRandomUtils()
    {}

    public static Double randomizeDouble()
    {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static Integer randomizeLowInteger()
    {
        return ThreadLocalRandom.current().nextInt(0, 255);
    }

    public static Integer randomizeHugeInteger()
    {
        return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }

    public static Integer randomizeIntegerInRange(Integer min, Integer max)
    {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static String randomizeHexString()
    {
        return Integer.toHexString(randomizeHugeInteger());
    }

    public static UUID randomizeGuid()
    {
        return UUID.randomUUID();
    }

    public static String randomizeAlphaNumericString()
    {
        return randomizeGuid().toString().replaceAll("-", "");
    }

    public static <E> E randomizeElementInList(List<E> list)
    {
        return list.get(randomizeIntegerInRange(0, list.size()));
    }
}

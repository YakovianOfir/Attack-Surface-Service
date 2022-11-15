package com.orca.spring.breacher.definitions;

import java.util.Arrays;
import java.util.List;

public final class AttackSurfaceServiceTestsConstants
{
    private AttackSurfaceServiceTestsConstants()
    {}

    public static final String CloudEnvironmentTestSample0 = "input-0.json";
    public static final String CloudEnvironmentTestSample1 = "input-1.json";
    public static final String CloudEnvironmentTestSample2 = "input-2.json";
    public static final String CloudEnvironmentTestSample3 = "input-3.json";
    public static final String CloudEnvironmentTestSampleLocation = "classpath*:CloudEnvironmentSamples/*.json";

    public static final List<String> CustomSampleAttackingTags = Arrays.asList("Tag-1", "Tag-2");
    public static final List<String> CustomSampleVictimTags = Arrays.asList("Tag-3");
}

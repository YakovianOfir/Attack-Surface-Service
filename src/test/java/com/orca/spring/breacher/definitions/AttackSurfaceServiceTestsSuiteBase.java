package com.orca.spring.breacher.definitions;

import com.orca.spring.breacher.environment.CloudEnvironmentTestSampleEngine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AttackSurfaceServiceTestsSuiteBase
{
    // region Dependencies

    @Autowired
    private ApplicationContext applicationContext;

    // endregion

    @BeforeAll
    public static void testSuiteInitialization()
    {
        CloudEnvironmentTestSampleEngine.Instance.trace();
    }

    @AfterAll
    public static void testSuiteTeardown()
    {
        CloudEnvironmentTestSampleEngine.Instance.unload();
    }

    @Test
    void contextLoads()
    {
        Assertions.assertNotNull(applicationContext);
    }
}

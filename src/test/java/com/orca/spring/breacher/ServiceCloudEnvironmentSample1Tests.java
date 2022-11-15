package com.orca.spring.breacher;

import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsConstants;
import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsSuiteBase;
import com.orca.spring.breacher.environment.CloudEnvironmentTestSampleEngine;
import com.orca.spring.breacher.topology.VirtualMachineAccessTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.randomizeAccessibleAsset;

class ServiceCloudEnvironmentSample1Tests extends AttackSurfaceServiceTestsSuiteBase
{
    static
    {
        CloudEnvironmentTestSampleEngine.Instance.load(AttackSurfaceServiceTestsConstants.CloudEnvironmentTestSample1);
    }

    // region Dependencies

    @Autowired
    private VirtualMachineAccessTable accessTable;

    // endregion

    @Test
    void testAnalyzeMachineAttackSurface_sanitySample1()
    {
        try
        {
            randomizeAccessibleAsset(accessTable);

            Assertions.fail();
        }
        catch (IndexOutOfBoundsException e)
        {
            Assertions.assertTrue(true, "Sample 1 has no attackable assets.");
        }
    }
}
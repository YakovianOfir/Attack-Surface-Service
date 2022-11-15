package com.orca.spring.breacher;

import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsConstants;
import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsSuiteBase;
import com.orca.spring.breacher.environment.CloudEnvironmentTestSampleEngine;
import com.orca.spring.breacher.topology.VirtualMachineAccessTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.randomizeAccessibleAsset;

class ServiceCloudEnvironmentSample0Tests extends AttackSurfaceServiceTestsSuiteBase
{
    static
    {
        CloudEnvironmentTestSampleEngine.Instance.load(AttackSurfaceServiceTestsConstants.CloudEnvironmentTestSample0);
    }

    // region Dependencies

    @Autowired
    private VirtualMachineAccessTable accessTable;

    // endregion

    @Test
    void testAnalyzeMachineAttackSurface_sanitySample0()
    {
        var accessibleAsset = randomizeAccessibleAsset(accessTable);
        var attackSurface = accessibleAsset.getValue().stream().map(vm -> vm.getIdentifier().id()).collect(Collectors.toList());

        Assertions.assertFalse(attackSurface.isEmpty());
    }
}
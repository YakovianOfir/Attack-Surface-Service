package com.orca.spring.breacher;

import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsSuiteBase;
import com.orca.spring.breacher.environment.CloudEnvironmentTestSampleEngine;
import com.orca.spring.breacher.model.CloudEnvironment;
import com.orca.spring.breacher.model.FwRule;
import com.orca.spring.breacher.model.Vm;
import com.orca.spring.breacher.topology.VirtualMachineAccessTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsConstants.CustomSampleAttackingTags;
import static com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsConstants.CustomSampleVictimTags;
import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.*;

@Slf4j
class ServiceCloudEnvironmentToySampleTests extends AttackSurfaceServiceTestsSuiteBase
{
    static
    {
        var attackingAssets = randomizeVmsWithTags(CustomSampleAttackingTags);
        var victimAssets = randomizeVmsWithTags(CustomSampleVictimTags);

        var virtualMachines = new ArrayList<Vm>();
        virtualMachines.addAll(attackingAssets);
        virtualMachines.addAll(victimAssets);

        var fwRuleAllow1to3 = randomizeFwRuleWithTags(CustomSampleAttackingTags.get(0), CustomSampleVictimTags.get(0));
        var fwRuleAllow2to3 = randomizeFwRuleWithTags(CustomSampleAttackingTags.get(1), CustomSampleVictimTags.get(0));

        var firewallRules = new ArrayList<FwRule>();
        firewallRules.add(fwRuleAllow1to3);
        firewallRules.add(fwRuleAllow2to3);

        var cloudEnvironment = new CloudEnvironment();
        cloudEnvironment.setVms(virtualMachines);
        cloudEnvironment.setFwRules(firewallRules);

        CloudEnvironmentTestSampleEngine.Instance.load(cloudEnvironment);
    }

    // region Dependencies

    @Autowired
    private VirtualMachineAccessTable accessTable;

    // endregion

    @Test
    void testAnalyzeMachineAttackSurface_sanityCustomSample()
    {
        var accessibleAsset = randomizeAccessibleAsset(accessTable);

        log.info("[Test-Custom-Environment-Sample]: Found accessible asset ({})", accessibleAsset.getKey().id());

        var attackSurface = accessibleAsset.getValue().stream()
                .map(vm -> vm.getIdentifier().id())
                .collect(Collectors.toList());

        log.info("[Test-Custom-Environment-Sample]: Attack surface ({})->({})", attackSurface, accessibleAsset.getKey().id());

        var attackSurfaceTags = accessibleAsset.getValue().stream()
                .map(vm -> vm.getTags())
                .flatMap(Collection::stream)
                .distinct()
                .map(tag -> tag.tag())
                .collect(Collectors.toList());

        log.info("[Test-Custom-Environment-Sample]: Attack surface tags ({})->({})", attackSurfaceTags, accessibleAsset.getKey().id());

        Assertions.assertFalse(attackSurface.isEmpty());
        Assertions.assertIterableEquals(CustomSampleAttackingTags, attackSurfaceTags);
    }
}
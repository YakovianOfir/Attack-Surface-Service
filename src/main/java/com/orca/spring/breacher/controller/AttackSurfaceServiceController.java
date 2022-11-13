package com.orca.spring.breacher.controller;

import com.orca.spring.breacher.definitions.CloudAssetIdentifier;
import com.orca.spring.breacher.statistics.AttackSurfaceServiceStatistics;
import com.orca.spring.breacher.statistics.ServiceRuntimeStatistics;
import com.orca.spring.breacher.topology.CloudAssetsTopology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.*;

@Slf4j
@RestController
@RequestMapping(value = RestServiceApiPath, method = RequestMethod.GET)
public class AttackSurfaceServiceController
{
    // region Dependencies

    @Autowired
    private CloudAssetsTopology assetsTopology;

    @Autowired
    private AttackSurfaceServiceStatistics serviceStatistics;

    // endregion

    @RequestMapping(RestEndpointAttack)
    public List<String> analyzeMachineAttackSurface(@RequestParam(name = RestEndpointAttackQueryParamVmId) String machineIdentifier)
    {
        var assetIdentifier = new CloudAssetIdentifier(machineIdentifier);
        var attackSurfaceVms = assetsTopology.analyzeAttackSurface(assetIdentifier);

        return attackSurfaceVms.stream().map(vm -> vm.getIdentifier().id()).collect(Collectors.toList());
    }

    @RequestMapping(RestEndpointStats)
    public ServiceRuntimeStatistics fetchServiceRuntimeStatistics()
    {
        return serviceStatistics.fetch();
    }
}

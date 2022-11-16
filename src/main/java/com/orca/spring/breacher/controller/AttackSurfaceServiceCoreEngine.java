package com.orca.spring.breacher.controller;

import com.orca.spring.breacher.definitions.CloudAssetIdentifier;
import com.orca.spring.breacher.exception.VirtualMachineNotFoundException;
import com.orca.spring.breacher.statistics.ServiceRuntimeStatisticsFetcher;
import com.orca.spring.breacher.statistics.ServiceRuntimeStatistics;
import com.orca.spring.breacher.topology.CloudAssetsTopology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AttackSurfaceServiceCoreEngine
{
    // region Dependencies

    @Autowired
    private CloudAssetsTopology assetsTopology;

    @Autowired
    private ServiceRuntimeStatisticsFetcher statisticsFetcher;

    // endregion

    public List<String> analyzeMachineAttackSurface(String machineIdentifier) throws VirtualMachineNotFoundException
    {
        log.debug("[Core-Engine]: Xlat a native machine identifier ({}) -> ", machineIdentifier);
        var targetAsset = new CloudAssetIdentifier(machineIdentifier);

        log.debug("[Core-Engine]: Analyze the attack surface of the asset ({}) -> ", machineIdentifier);
        var attackSurface = assetsTopology.analyzeAttackSurface(targetAsset);

        log.debug("[Core-Engine]: Xlat the resulting attack surface to controller semantics ({}) -> ", attackSurface);
        return attackSurface.stream().map(i -> i.id()).collect(Collectors.toList());
    }

    public ServiceRuntimeStatistics fetchServiceRuntimeStatistics()
    {
        log.debug("[Core-Engine]: Fetching runtime service statistics via Actuator.");
        var currentStatistics = statisticsFetcher.fetch();

        log.debug("[Core-Engine]: Xlat the resulting statistics to controller semantics ({}) -> ", currentStatistics);
        return currentStatistics;
    }
}

package com.orca.spring.breacher.controller;

import com.orca.spring.breacher.definitions.CloudAssetIdentifier;
import com.orca.spring.breacher.exception.VirtualMachineNotFoundException;
import com.orca.spring.breacher.statistics.AttackSurfaceServiceStatistics;
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
    private AttackSurfaceServiceStatistics serviceStatistics;

    // endregion

    public List<String> analyzeMachineAttackSurface(String machineIdentifier) throws VirtualMachineNotFoundException
    {
        var targetAsset = new CloudAssetIdentifier(machineIdentifier);
        var attackSurface = assetsTopology.analyzeAttackSurface(targetAsset);

        return attackSurface.stream().map(i -> i.id()).collect(Collectors.toList());
    }

    public ServiceRuntimeStatistics fetchServiceRuntimeStatistics()
    {
        return serviceStatistics.fetch();
    }
}

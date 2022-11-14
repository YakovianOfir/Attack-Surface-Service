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
        return assetsTopology.analyzeAttackSurface(new CloudAssetIdentifier(machineIdentifier));
    }

    public ServiceRuntimeStatistics fetchServiceRuntimeStatistics()
    {
        return serviceStatistics.fetch();
    }
}

package com.orca.spring.breacher.controller;

import com.orca.spring.breacher.topology.CloudAssetIdentifier;
import com.orca.spring.breacher.topology.CloudAssetsTopology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AttackSurfaceServiceController
{
    // region Dependencies

    @Autowired
    private CloudAssetsTopology assetsTopology;

    // endregion

    @RequestMapping("/attack")
    public List<String> analyzeVirtualMachineAttackSurface()
    {
        var assetIdentifier = new CloudAssetIdentifier("vm-ab51cba10");
        var attackSurfaceVms = assetsTopology.analyzeAttackSurface(assetIdentifier);

        return attackSurfaceVms.stream().map(vm -> vm.getVmId()).collect(Collectors.toList());
    }

    @RequestMapping("/stats")
    public Integer fetchServiceRuntimeStatistics()
    {
        return 0;
    }
}

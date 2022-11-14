package com.orca.spring.breacher.topology;

import com.orca.spring.breacher.definitions.CloudAssetIdentifier;
import com.orca.spring.breacher.exception.VirtualMachineNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CloudAssetsTopology
{
    // region Dependencies

    @Autowired
    private VirtualMachineAccessTable machineAccessTable;

    // endregion

    public List<String> analyzeAttackSurface(CloudAssetIdentifier targetAsset) throws VirtualMachineNotFoundException
    {
        return
            machineAccessTable.getAttackSurfaceForCloudAsset(targetAsset).stream()
            .map(vm -> vm.getIdentifier().id())
            .collect(Collectors.toList());
    }
}

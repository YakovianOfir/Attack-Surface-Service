package com.orca.spring.breacher.topology;

import com.orca.spring.breacher.definitions.CloudAssetIdentifier;
import com.orca.spring.breacher.definitions.VirtualMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class CloudAssetsTopology
{
    // region Dependencies

    @Autowired
    private VirtualMachineAccessTable machineAccessTable;

    // endregion

    public Set<VirtualMachine> analyzeAttackSurface(CloudAssetIdentifier targetAsset)
    {
        return machineAccessTable.getAttackSurfaceForCloudAsset(targetAsset);
    }
}

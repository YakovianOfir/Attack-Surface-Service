package com.orca.spring.breacher.topology;

import com.orca.spring.breacher.definitions.CloudAssetIdentifier;
import com.orca.spring.breacher.definitions.CloudAssetTag;
import com.orca.spring.breacher.definitions.VirtualMachine;
import com.orca.spring.breacher.exception.VirtualMachineNotFoundException;
import com.orca.spring.breacher.model.Vm;
import com.orca.spring.breacher.settings.AttackSurfaceServiceSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VirtualMachineAccessTable
{
    // region Fields

    private final Map<CloudAssetIdentifier, Set<VirtualMachine>> machineAccessMap = new HashMap<>();

    // endregion

    @Autowired
    public VirtualMachineAccessTable(AttackSurfaceServiceSettings settings, FirewallAccessTable firewallAccessTable)
    {
        var virtualMachineDefinitions = settings.getCloudEnvironment().getVms();

        log.info("Constructing formal VM assets... [({}) VM Definitions]", virtualMachineDefinitions.size());
        var virtualMachines = constructVirtualMachineCloudAssets(virtualMachineDefinitions);

        log.info("Constructing the VM Access Table... [({}) VM Assets]", virtualMachines.size());
        constructVirtualMachineAccessTable(firewallAccessTable, virtualMachines);

        log.debug("VM Access Table is present. (OK) [({})]", machineAccessMap);
    }

    public Set<VirtualMachine> getAttackSurfaceForCloudAsset(CloudAssetIdentifier assetIdentifier) throws VirtualMachineNotFoundException
    {
        validateCloudAssetIdentifier(assetIdentifier);

        return machineAccessMap.get(assetIdentifier);
    }

    private void validateCloudAssetIdentifier(CloudAssetIdentifier assetIdentifier) throws VirtualMachineNotFoundException
    {
        if (!machineAccessMap.containsKey(assetIdentifier))
        {
            throw new VirtualMachineNotFoundException(String.format("Cannot found VM asset with ID -> (%s)", assetIdentifier));
        }
    }

    private static List<VirtualMachine> constructVirtualMachineCloudAssets(List<Vm> virtualMachineDefinitions)
    {
        return
            virtualMachineDefinitions.stream()
            .map(vm -> new VirtualMachine(vm))
            .collect(Collectors.toList());
    }

    private static Set<VirtualMachine> resolveVirtualMachinesWithAssetTags(List<VirtualMachine> virtualMachines, Set<CloudAssetTag> assetTags)
    {
        return
            virtualMachines.stream()
            .filter(vm -> vm.hasTag(assetTags))
            .collect(Collectors.toSet());
    }

    private void constructVirtualMachineAccessTable(FirewallAccessTable firewallAccessTable, List<VirtualMachine> virtualMachines)
    {
        for (var vm : virtualMachines)
        {
            log.info("\t Resolving allowed traffic sources for VM... [({})]", vm);
            var allowedSourceTags = firewallAccessTable.resolveAllowedTrafficSourcesForVirtualMachine(vm);

            if (allowedSourceTags.isEmpty())
            {
                log.info("\t No traffic sources are allowed for VM [({})]. Skipping...", vm);
                continue;
            }

            log.info("\t Resolving accessible virtual machines for VM... [({})]", vm);
            var accessibleVirtualMachines = resolveVirtualMachinesWithAssetTags(virtualMachines, allowedSourceTags);

            if (accessibleVirtualMachines.isEmpty())
            {
                log.info("\t No accessible virtual machines for VM [({})]. Skipping...", vm);
                continue;
            }

            log.info("\t Constructing VM Access Table entry... [({})]", vm);
            constructVirtualMachineAccessTableEntry(vm, accessibleVirtualMachines);
        }
    }

    private void constructVirtualMachineAccessTableEntry(VirtualMachine vm, Set<VirtualMachine> accessibleVirtualMachines)
    {
        if (machineAccessMap.containsKey(vm.getIdentifier()))
        {
            throw new UnsupportedOperationException();
        }

        machineAccessMap.put(vm.getIdentifier(), accessibleVirtualMachines);
    }
}

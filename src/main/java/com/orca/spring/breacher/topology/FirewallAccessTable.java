package com.orca.spring.breacher.topology;

import com.orca.spring.breacher.definitions.CloudAssetTag;
import com.orca.spring.breacher.definitions.VirtualMachine;
import com.orca.spring.breacher.model.FwRule;
import com.orca.spring.breacher.settings.AttackSurfaceServiceSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FirewallAccessTable
{
    // region Fields

    private final Map<CloudAssetTag, Set<CloudAssetTag>> firewallAccessMap = new HashMap<>();

    // endregion

    @Autowired
    public FirewallAccessTable(AttackSurfaceServiceSettings settings)
    {
        var firewallRules = settings.getCloudEnvironment().getFwRules();

        log.info("Resolving all traffic destination tags... [({}) FW Rules]", firewallRules.size());
        var targetTags = resolveAllTrafficDestinationTags(firewallRules);

        log.info("Constructing the Firewall Access Table... [({}) Destination Tags]", targetTags.size());
        constructFirewallAccessTable(firewallRules, targetTags);

        log.debug("Firewall Access Table is present. (OK) [({})]", firewallAccessMap);
    }

    private static Set<CloudAssetTag> resolveAllTrafficDestinationTags(List<FwRule> fwRules)
    {
        return
            fwRules.stream()
            .map(fwRule -> fwRule.getDestTag())
            .map(targetTag -> new CloudAssetTag(targetTag))
            .collect(Collectors.toSet());
    }

    private void constructFirewallAccessTable(List<FwRule> firewallRules, Set<CloudAssetTag> targetTags)
    {
        for (var targetTag : targetTags)
        {
            log.info("\t Resolving allowed traffic sources for destination... [({})]", targetTag.tag());
            var allowedSourceTags = resolveTrafficSourceTagsForDestination(firewallRules, targetTag);

            if (allowedSourceTags.isEmpty())
            {
                log.info("\t No traffic sources are allowed for destination [({})]. Skipping...", targetTag.tag());
                continue;
            }

            log.info("\t Constructing Firewall Access Table entry... [({})]", targetTag.tag());
            constructFirewallAccessTableEntry(targetTag, allowedSourceTags);
        }
    }

    private void constructFirewallAccessTableEntry(CloudAssetTag targetTag, Set<CloudAssetTag> allowedSourceTags)
    {
        if (firewallAccessMap.containsKey(targetTag))
        {
            throw new UnsupportedOperationException();
        }

        firewallAccessMap.put(targetTag, allowedSourceTags);
    }

    private static Set<CloudAssetTag> resolveTrafficSourceTagsForDestination(List<FwRule> fwRules, CloudAssetTag targetTag)
    {
        return
            fwRules.stream()
            .filter(fwRule -> targetTag.tag().equals(fwRule.getDestTag()))
            .map(fwRule -> fwRule.getSourceTag())
            .map(sourceTag -> new CloudAssetTag(sourceTag))
            .collect(Collectors.toSet());
}

    public Set<CloudAssetTag> resolveAllowedTrafficSourcesForVirtualMachine(VirtualMachine vm)
    {
        return
            vm.getTags().stream()
            .map(tag -> getAllowedTrafficSourcesForDestinationTag(tag))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    private Set<CloudAssetTag> getAllowedTrafficSourcesForDestinationTag(CloudAssetTag targetTag)
    {
        return
            firewallAccessMap.containsKey(targetTag) ?
            firewallAccessMap.get(targetTag) :
            Collections.emptySet();
    }
}

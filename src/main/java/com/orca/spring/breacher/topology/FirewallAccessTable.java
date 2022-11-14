package com.orca.spring.breacher.topology;

import com.orca.spring.breacher.definitions.CloudAssetTag;
import com.orca.spring.breacher.definitions.FirewallRule;
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
        var firewallRulesDefinitions = settings.getCloudEnvironment().getFwRules();

        log.info("Constructing formal FW rules... [({}) Rule Definitions]", firewallRulesDefinitions.size());
        var firewallRules = constructFirewallRules(firewallRulesDefinitions);

        log.info("Resolving all traffic destination tags... [({}) FW Rules]", firewallRules.size());
        var targetTags = resolveAllTrafficDestinationTags(firewallRules);

        log.info("Constructing the Firewall Access Table... [({}) Destination Tags]", targetTags.size());
        constructFirewallAccessTable(firewallRules, targetTags);

        log.debug("Firewall Access Table is present. (OK) [({})]", firewallAccessMap);
    }

    public Set<CloudAssetTag> resolveAllowedTrafficSourcesForVirtualMachine(VirtualMachine vm)
    {
        return
            vm.getTags().stream()
            .map(tag -> getAllowedTrafficSourcesForDestinationTag(tag))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    private static List<FirewallRule> constructFirewallRules(List<FwRule> firewallRulesDefinitions)
    {
        return
            firewallRulesDefinitions.stream()
            .map(rule -> new FirewallRule(rule))
            .collect(Collectors.toList());
    }

    private void constructFirewallAccessTable(List<FirewallRule> firewallRules, Set<CloudAssetTag> targetTags)
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

            log.info("\t Constructing Firewall Access Table entry... [({} -> {})]", allowedSourceTags, targetTag.tag());
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

    private Set<CloudAssetTag> getAllowedTrafficSourcesForDestinationTag(CloudAssetTag targetTag)
    {
        return
            firewallAccessMap.containsKey(targetTag) ?
            firewallAccessMap.get(targetTag) :
            Collections.emptySet();
    }

    private static Set<CloudAssetTag> resolveAllTrafficDestinationTags(List<FirewallRule> fwRules)
    {
        return
            fwRules.stream()
            .map(fwRule -> fwRule.getTargetTag())
            .collect(Collectors.toSet());
    }

    private static Set<CloudAssetTag> resolveTrafficSourceTagsForDestination(List<FirewallRule> fwRules, CloudAssetTag targetTag)
    {
        return
            fwRules.stream()
            .filter(fwRule -> targetTag.tag().equals(fwRule.getTargetTag().tag()))
            .map(fwRule -> fwRule.getSourceTag())
            .collect(Collectors.toSet());
    }
}

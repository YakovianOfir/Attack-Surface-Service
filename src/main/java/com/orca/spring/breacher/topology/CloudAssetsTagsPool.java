package com.orca.spring.breacher.topology;

import com.orca.spring.breacher.model.FwRule;
import com.orca.spring.breacher.model.Vm;
import com.orca.spring.breacher.settings.AttackSurfaceServiceSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class CloudAssetsTagsPool
{
    // region Fields

    private final Set<CloudAssetTag> tagsPool = new HashSet<>();

    // endregion

    @Autowired
    public CloudAssetsTagsPool(AttackSurfaceServiceSettings settings)
    {
        var environment = settings.getCloudEnvironment();

        imbueVirtualMachinesTags(environment.getVms());
        imbueFirewallRulesTags(environment.getFwRules());
    }

    private void imbueVirtualMachinesTags(List<Vm> vms)
    {
        var vmTags = vms.stream()
                .map(vm -> vm.getTags())
                .flatMap(List::stream)
                .map(tag -> new CloudAssetTag(tag))
                .collect(Collectors.toSet());

        log.debug("Resolved {} VMs Tags -> {}", vmTags.size(), vmTags);

        tagsPool.addAll(vmTags);
    }

    private void imbueFirewallRulesTags(List<FwRule> fwRules)
    {
        var fwRuleTags = fwRules.stream()
                .flatMap(fwr -> Stream.of(fwr.getSourceTag(), fwr.getDestTag()))
                .map(tag -> new CloudAssetTag(tag))
                .collect(Collectors.toSet());

        log.debug("Resolved {} FW Rules Tags -> {}", fwRuleTags.size(), fwRuleTags);

        tagsPool.addAll(fwRuleTags);
    }

    public Set<CloudAssetTag> get()
    {
        return tagsPool;
    }
}

package com.orca.spring.breacher.topology;

import com.orca.spring.breacher.model.Vm;
import com.orca.spring.breacher.settings.AttackSurfaceServiceSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CloudAssetsTopology
{
    // region Dependencies

    @Autowired
    private FirewallAccessTable accessTable;

    @Autowired
    private AttackSurfaceServiceSettings serviceSettings;

    // endregion

    public Set<Vm> analyzeAttackSurface(CloudAssetIdentifier targetAsset)
    {
        var environment = serviceSettings.getCloudEnvironment();
        var vms = environment.getVms();
        var aaa = vms.stream().filter(cloudAsset -> cloudAsset.getVmId().equals(targetAsset.id()));
        var bbb = aaa.collect(Collectors.toList());

        return analyzeAttackSurface(bbb.get(0));

//
//        return
//            analyzeAttackSurface(
//                environment.getVms().stream()
//                .filter(cloudAsset -> cloudAsset.getVmId() == targetAsset.id())
//                .findFirst()
//                .get());
    }

    public Set<Vm> analyzeAttackSurface(Vm targetAsset)
    {
        var environment = serviceSettings.getCloudEnvironment();

        return
            environment.getVms().stream()
            .filter(sourceAsset -> hasAccess(sourceAsset, targetAsset))
            .collect(Collectors.toSet());
    }

    public boolean hasAccess(Vm sourceAsset, Vm targetAsset)
    {
        return
            targetAsset.getTags().stream()
            .map(targetTag -> new CloudAssetTag(targetTag))
            .anyMatch(targetTag -> hasAccess(sourceAsset, targetTag));
    }

    private boolean hasAccess(Vm sourceAsset, CloudAssetTag targetTag)
    {
        var allowedSourceTags = accessTable.getAllowedSourcesForTarget(targetTag);

        if (allowedSourceTags == null || allowedSourceTags.isEmpty())
        {
            return false;
        }

        return hasAccess(sourceAsset, allowedSourceTags);
    }

    private boolean hasAccess(Vm sourceAsset, Set<CloudAssetTag> allowedSourceTags)
    {
        return
            sourceAsset.getTags().stream()
            .map(sourceTag -> new CloudAssetTag(sourceTag))
            .anyMatch(sourceTag -> allowedSourceTags.contains(sourceTag));
    }
}

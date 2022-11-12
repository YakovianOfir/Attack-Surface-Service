package com.orca.spring.breacher.topology;

import com.orca.spring.breacher.model.FwRule;
import com.orca.spring.breacher.settings.AttackSurfaceServiceSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FirewallAccessTable
{
    // region Fields

    private final Map<CloudAssetTag, Set<CloudAssetTag>> accessTable = new HashMap<>();

    // endregion

    @Autowired
    public FirewallAccessTable(AttackSurfaceServiceSettings settings, CloudAssetsTagsPool tagsPool)
    {
        var environment = settings.getCloudEnvironment();

        imbueExternalTrafficSourcesForTags(environment.getFwRules(), tagsPool.get());
    }

    public Set<CloudAssetTag> getAllowedSourcesForTarget(CloudAssetTag targetTag)
    {
        return accessTable.get(targetTag);
    }

    private void imbueExternalTrafficSourcesForTags(List<FwRule> fwRules, Set<CloudAssetTag> tagsPool)
    {
        for (var targetTag : tagsPool)
        {
            imbueExternalTrafficSourcesForTag(fwRules, targetTag);
        }
    }

    private void imbueExternalTrafficSourcesForTag(List<FwRule> fwRules, CloudAssetTag targetTag)
    {
        var sourceTags = fwRules.stream()
                .filter(fwr -> targetTag.tag().equals(fwr.getDestTag()))
                .map(fwr -> fwr.getSourceTag())
                .map(tag -> new CloudAssetTag(tag))
                .collect(Collectors.toSet());

        if (!sourceTags.isEmpty())
        {
            log.debug("Resolved {} source tags for target {} -> {}", sourceTags.size(), targetTag, sourceTags);

            accessTable.put(targetTag, sourceTags);
        }
    }
}

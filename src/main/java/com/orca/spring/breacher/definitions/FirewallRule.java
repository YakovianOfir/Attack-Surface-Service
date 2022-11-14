package com.orca.spring.breacher.definitions;

import com.orca.spring.breacher.model.FwRule;
import lombok.Getter;

public class FirewallRule
{
    // region Fields

    private final FwRule rule;

    @Getter
    private final CloudAssetTag sourceTag;

    @Getter
    private final CloudAssetTag targetTag;

    // endregion

    public FirewallRule(FwRule rule)
    {
        this.rule = rule;
        this.targetTag = new CloudAssetTag(rule.getDestTag());
        this.sourceTag = new CloudAssetTag(rule.getSourceTag());
    }

    @Override
    public String toString()
    {
        return String.format("[FW] -> (%s))", rule.toString());
    }
}

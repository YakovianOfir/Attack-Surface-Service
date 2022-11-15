package com.orca.spring.breacher.utils;

import com.orca.spring.breacher.definitions.CloudAssetIdentifier;
import com.orca.spring.breacher.definitions.VirtualMachine;
import com.orca.spring.breacher.model.FwRule;
import com.orca.spring.breacher.model.Vm;
import com.orca.spring.breacher.topology.VirtualMachineAccessTable;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.orca.spring.breacher.utils.ServiceTestRandomUtils.*;

@Slf4j
public class ServiceTestAccessTableUtils
{
    private ServiceTestAccessTableUtils()
    {}

    public static Map.Entry<CloudAssetIdentifier, Set<VirtualMachine>> randomizeAccessibleAsset(VirtualMachineAccessTable accessTable)
    {
        var accessibleAssets = accessTable.getMachineAccessMap()
                .entrySet()
                .stream()
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toList());

        log.info("[Test-Access-Table]: Found ({}) accessible Virtual Machine assets", accessibleAssets.size());

        Collections.shuffle(accessibleAssets);

        var randomizedAccessibleAsset = accessibleAssets.get(0);

        log.info("[Test-Access-Table]: Returning the following (randomly chosen) asset: ({})", randomizedAccessibleAsset.getKey().id());

        return randomizedAccessibleAsset;
    }

    public static String randomizeFwIdentifier()
    {
        return String.format("fw-%s", randomizeHexString());
    }

    public static String randomizeVmIdentifier()
    {
        return String.format("vm-%s", randomizeHexString());
    }

    public static List<String> randomizeVmIdentifierList()
    {
        return randomizeVmIdentifierList(randomizeLowInteger());
    }

    public static List<String> randomizeVmIdentifierList(Integer size)
    {
        var assetIdentifiers = new ArrayList<String>();

        for (var i = 0; i < size; ++i)
        {
            assetIdentifiers.add(randomizeVmIdentifier());
        }

        return assetIdentifiers;
    }

    public static List<Vm> randomizeVmsWithTags(List<String> tags)
    {
        return randomizeVmsWithTags(randomizeLowInteger(), tags);
    }

    public static List<Vm> randomizeVmsWithTags(Integer size, List<String> tags)
    {
        var vms = new ArrayList<Vm>();

        for (var i = 0; i < size; ++i)
        {
            vms.add(randomizeVmWithTags(tags));
        }

        return vms;
    }

    public static Vm randomizeVmWithTags(List<String> tags)
    {
        var vm = new Vm();

        vm.setTags(tags);
        vm.setVmId(randomizeVmIdentifier());
        vm.setName(randomizeAlphaNumericString());

        return vm;
    }

    public static FwRule randomizeFwRuleWithTags(String sourceTag, String targetTag)
    {
        var fwRule = new FwRule();

        fwRule.setFwId(randomizeFwIdentifier());
        fwRule.setSourceTag(sourceTag);
        fwRule.setDestTag(targetTag);

        return fwRule;
    }
}

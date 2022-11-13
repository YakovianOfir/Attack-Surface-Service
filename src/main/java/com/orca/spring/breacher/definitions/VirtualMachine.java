package com.orca.spring.breacher.definitions;

import com.orca.spring.breacher.model.Vm;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VirtualMachine
{
    // region Fields

    private final Vm machine;

    @Getter
    private final CloudAssetIdentifier identifier;

    @Getter
    private final List<CloudAssetTag> tags;

    // endregion

    public VirtualMachine(Vm machine)
    {
        this.machine = machine;
        this.identifier = new CloudAssetIdentifier(machine.getVmId());
        this.tags = machine.getTags().stream().map(t -> new CloudAssetTag(t)).collect(Collectors.toList());
    }

    public boolean hasTag(Set<CloudAssetTag> candidateTags)
    {
        return !Collections.disjoint(tags, candidateTags);
    }

    @Override
    public String toString()
    {
        return String.format("[VM] -> (%s))", machine.toString());
    }
}

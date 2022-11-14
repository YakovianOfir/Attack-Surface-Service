package com.orca.spring.breacher.environment;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CloudEnvironmentProviderType
{
    FileSystemProvider(0),
    SystemPropertyProvider(1);

    public Integer value;
}

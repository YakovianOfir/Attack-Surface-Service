package com.orca.spring.breacher.environment;

import com.orca.spring.breacher.model.CloudEnvironment;

public interface ICloudEnvironmentProvider
{
    CloudEnvironment get();
    CloudEnvironmentProviderType type();
}

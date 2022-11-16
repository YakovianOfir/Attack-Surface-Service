package com.orca.spring.breacher.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orca.spring.breacher.model.CloudEnvironment;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CloudEnvironmentSystemPropertyProvider implements ICloudEnvironmentProvider
{
    // region Fields

    private CloudEnvironment cloudEnvironment;

    // endregion

    public CloudEnvironmentSystemPropertyProvider(String systemPropertyName, ObjectMapper objectMapper) throws IOException
    {
        log.debug("[System-Property-Provider] Accessing the System Property. ({})", systemPropertyName);
        var systemPropertyValue = System.getProperty(systemPropertyName, null);

        if (systemPropertyValue == null)
        {
            log.error("[System-Property-Provider] Cannot find the System Property. ({})", systemPropertyName);
            throw new IllegalArgumentException("Cannot load the Cloud Environment.");
        }

        log.debug("[System-Property-Provider] Deserializing the Cloud Environment document. ({})", systemPropertyValue);
        this.cloudEnvironment = objectMapper.readValue(systemPropertyValue, CloudEnvironment.class);

        log.info("[System-Property-Provider] Done. (OK) ({})", cloudEnvironment);
    }

    @Override
    public CloudEnvironment get()
    {
        return cloudEnvironment;
    }

    @Override
    public CloudEnvironmentProviderType type()
    {
        return CloudEnvironmentProviderType.SystemPropertyProvider;
    }
}

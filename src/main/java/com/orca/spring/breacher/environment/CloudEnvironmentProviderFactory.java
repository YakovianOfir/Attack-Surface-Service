package com.orca.spring.breacher.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CloudEnvironmentProviderFactory
{
    private CloudEnvironmentProviderFactory()
    {}

    public static ICloudEnvironmentProvider create(CloudEnvironmentProviderType type, String argument, ObjectMapper mapper) throws IOException
    {
        switch (type)
        {
            case FileSystemProvider ->
            {
                log.debug("[Cloud-Environment-Provider] Creating a file-based provider. ({})", argument);
                return new CloudEnvironmentFileSystemProvider(argument, mapper);
            }

            case SystemPropertyProvider ->
            {
                log.debug("[Cloud-Environment-Provider] Creating a property-based provider. ({})", argument);
                return new CloudEnvironmentSystemPropertyProvider(argument, mapper);
            }

            default ->
            {
                log.error("[Cloud-Environment-Provider] Invalid provider type. ({})", type.value);
                throw new IllegalArgumentException();
            }
        }
    }
}

package com.orca.spring.breacher.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orca.spring.breacher.model.CloudEnvironment;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class CloudEnvironmentFileSystemProvider implements ICloudEnvironmentProvider
{
    // region Fields

    private CloudEnvironment cloudEnvironment;

    // endregion

    public CloudEnvironmentFileSystemProvider(String inputFilePath, ObjectMapper objectMapper) throws IOException
    {
        log.debug("[File-System-Provider] Accessing the Cloud Environment file. ({})", inputFilePath);
        var cloudEnvironmentInputFile = new File(inputFilePath);

        if (!cloudEnvironmentInputFile.exists())
        {
            log.error("[File-System-Provider] Cannot find the Cloud Environment file. ({})", inputFilePath);
            throw new IllegalArgumentException(String.format("Cannot find the Cloud Environment file -> [%s]", inputFilePath));
        }

        log.debug("[File-System-Provider] Deserializing the Cloud Environment document. ({})", cloudEnvironmentInputFile);
        this.cloudEnvironment = objectMapper.readValue(cloudEnvironmentInputFile, CloudEnvironment.class);

        log.info("[File-System-Provider] Done. (OK) ({})", cloudEnvironment);
    }

    @Override
    public CloudEnvironment get()
    {
        return cloudEnvironment;
    }

    @Override
    public CloudEnvironmentProviderType type()
    {
        return CloudEnvironmentProviderType.FileSystemProvider;
    }
}

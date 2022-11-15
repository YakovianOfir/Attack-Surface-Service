package com.orca.spring.breacher.environment;

import lombok.SneakyThrows;
import com.google.common.io.Resources;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;

public class CloudEnvironmentTestSample
{
    // region Fields

    private final Resource resourceSample;

    // endregion

    public CloudEnvironmentTestSample(Resource resourceSample)
    {
        this.resourceSample = resourceSample;
    }

    public String name()
    {
        return resourceSample.getFilename();
    }

    @SneakyThrows
    public String contents()
    {
        return Resources.toString(resourceSample.getURL(), StandardCharsets.UTF_8);
    }
}

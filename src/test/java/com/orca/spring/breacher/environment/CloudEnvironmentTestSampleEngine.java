package com.orca.spring.breacher.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orca.spring.breacher.model.CloudEnvironment;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloudEnvironmentTestSampleEngine
{
    // region Fields

    private final CloudEnvironmentTestSampleProvider sampleProvider;

    // endregion

    // region Statics

    public static CloudEnvironmentTestSampleEngine Instance = new CloudEnvironmentTestSampleEngine();

    // endregion

    private CloudEnvironmentTestSampleEngine()
    {
        this.sampleProvider = new CloudEnvironmentTestSampleProvider();
    }

    public void loadRandom()
    {
        log.info("[Test-Sample-Engine]: Loading a random test.");
        CloudEnvironmentSystemProperty.set(sampleProvider.getRandom());
    }

    @SneakyThrows
    public void load(CloudEnvironment cloudEnvironment)
    {
        log.info("[Test-Sample-Engine]: Loading custom cloud environment sample -> ({})", cloudEnvironment.toString());
        CloudEnvironmentSystemProperty.set(new ObjectMapper().writeValueAsString(cloudEnvironment));
    }

    public void load(String sampleName)
    {
        log.info("[Test-Sample-Engine]: Loading test sample -> ({})", sampleName);
        CloudEnvironmentSystemProperty.set(sampleProvider.get(sampleName));
    }

    public void unload()
    {
        log.info("[Test-Sample-Engine]: Unloading current test sample.");
        CloudEnvironmentSystemProperty.clear();
    }

    public void trace()
    {
        var loadedSample = CloudEnvironmentSystemProperty.get();
        log.info("[Test-Sample-Engine]: Current test sample -> ({})", loadedSample);
    }
}

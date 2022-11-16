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
        log.info("[Test-Sample-Engine]: Loading a random test sample.");
        CloudEnvironmentSystemProperty.set(sampleProvider.getRandom());
        trace();
    }

    @SneakyThrows
    public void load(CloudEnvironment cloudEnvironment)
    {
        log.info("[Test-Sample-Engine]: Loading a custom cloud environment sample -> ({})", cloudEnvironment.toString());
        CloudEnvironmentSystemProperty.set(new ObjectMapper().writeValueAsString(cloudEnvironment));
        trace();
    }

    public void load(String sampleName)
    {
        log.info("[Test-Sample-Engine]: Loading a test sample -> ({})", sampleName);
        CloudEnvironmentSystemProperty.set(sampleProvider.get(sampleName));
        trace();
    }

    public void unload()
    {
        log.info("[Test-Sample-Engine]: Unloading current test sample.");
        CloudEnvironmentSystemProperty.clear();
    }

    private void trace()
    {
        var loadedSample = CloudEnvironmentSystemProperty.get();
        log.info("[Test-Sample-Engine]: Current test sample -> ({})", loadedSample);
    }
}

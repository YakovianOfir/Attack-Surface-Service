package com.orca.spring.breacher.environment;

import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsConstants.CloudEnvironmentTestSampleLocation;
import static com.orca.spring.breacher.utils.ServiceTestRandomUtils.randomizeElementInList;

public class CloudEnvironmentTestSampleProvider
{
    // region Fields

    private final List<Resource> testResources;
    private final List<CloudEnvironmentTestSample> resourceSamples;

    // endregion

    // region Constants



    // endregion

    @SneakyThrows
    public CloudEnvironmentTestSampleProvider()
    {
        var classLoader = MethodHandles.lookup().getClass().getClassLoader();
        var resourceResolver = new PathMatchingResourcePatternResolver(classLoader);

        this.testResources = List.of(resourceResolver.getResources(CloudEnvironmentTestSampleLocation));
        this.resourceSamples = loadResourceSamples(testResources);
    }

    private static List<CloudEnvironmentTestSample> loadResourceSamples(List<Resource> testResources)
    {
        return
            testResources.stream()
            .map(r -> new CloudEnvironmentTestSample(r))
            .collect(Collectors.toList());
    }

    public CloudEnvironmentTestSample get(String sampleName)
    {
        return
            resourceSamples.stream()
            .filter(r -> r.name().equals(sampleName))
            .findFirst()
            .get();
    }

    public CloudEnvironmentTestSample getRandom()
    {
        return
            new CloudEnvironmentTestSample(
                randomizeElementInList(testResources));
    }
}

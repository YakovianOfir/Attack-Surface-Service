package com.orca.spring.breacher.environment;

import com.google.common.io.Resources;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CloudEnvironmentTestSamplesProvider
{
    // region Fields

    private final ClassLoader classLoader;
    private final List<Resource> environmentSamples;

    // endregion

    // region Constants

    private static String EnvironmentInputSamplesLocationPattern = "classpath*:CloudEnvironmentSamples/*.json";

    // endregion

    // region Statics

    public static CloudEnvironmentTestSamplesProvider Instance = new CloudEnvironmentTestSamplesProvider();

    // endregion

    private CloudEnvironmentTestSamplesProvider()
    {
        this.classLoader = MethodHandles.lookup().getClass().getClassLoader();
        this.environmentSamples = loadEnvironmentInputSamples();
    }

    @SneakyThrows
    private List<Resource> loadEnvironmentInputSamples()
    {
        var resolver = new PathMatchingResourcePatternResolver(classLoader);
        return List.of(resolver.getResources(EnvironmentInputSamplesLocationPattern));
    }

    @SneakyThrows
    public String readResourceContents(String resourceName)
    {
        return Resources.toString(getResourceByName(resourceName).getURL(), StandardCharsets.UTF_8);
    }

    private Resource getResourceByName(String resourceName)
    {
        return environmentSamples.stream().filter(r -> r.getFilename().equals(resourceName)).findFirst().get();
    }
}

package com.orca.spring.breacher.environment;

import lombok.extern.slf4j.Slf4j;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.ServiceCloudEnvironmentSystemPropertyName;

@Slf4j
public class CloudEnvironmentSystemProperty
{
    private CloudEnvironmentSystemProperty()
    {}

    public static void set(String customSample)
    {
        log.info("[Environment-System-Property]: Loading test sample -> [({})=({})]", ServiceCloudEnvironmentSystemPropertyName, customSample);
        System.setProperty(ServiceCloudEnvironmentSystemPropertyName, customSample);
    }

    public static void set(CloudEnvironmentTestSample formalSample)
    {
        log.info("[Environment-System-Property]: Loading test sample -> [({})=({})]", ServiceCloudEnvironmentSystemPropertyName, formalSample.name());
        System.setProperty(ServiceCloudEnvironmentSystemPropertyName, formalSample.contents());
    }

    public static String get()
    {
        log.info("[Environment-System-Property]: Reading test sample -> ({})", ServiceCloudEnvironmentSystemPropertyName);
        return System.getProperty(ServiceCloudEnvironmentSystemPropertyName);
    }

    public static void clear()
    {
        log.info("[Environment-System-Property]: Clearing test sample -> ({})", ServiceCloudEnvironmentSystemPropertyName);
        System.clearProperty(ServiceCloudEnvironmentSystemPropertyName);
    }
}

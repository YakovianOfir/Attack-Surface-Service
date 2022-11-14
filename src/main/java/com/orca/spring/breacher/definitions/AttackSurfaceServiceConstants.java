package com.orca.spring.breacher.definitions;

public final class AttackSurfaceServiceConstants
{
    private AttackSurfaceServiceConstants()
    {}

    public static final String RestControllerServiceApiPath = "/api/v1";
    public static final String RestControllerEndpointMappingStats = "/stats";
    public static final String RestControllerEndpointMappingAttack = "/attack";
    public static final String RestControllerEndpointAttackQueryParam = "vm_id";
    public static final String ActuatorEndpointMeasurementsMetricTag = "uri:";
    public static final String ActuatorEndpointMeasurementsMetricName = "http.server.requests";
    public static final String ServiceCloudEnvironmentSystemPropertyName = "breacher.cloud.environment";
}

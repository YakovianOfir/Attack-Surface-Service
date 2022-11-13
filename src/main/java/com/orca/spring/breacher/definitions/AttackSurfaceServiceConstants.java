package com.orca.spring.breacher.definitions;

public final class AttackSurfaceServiceConstants
{
    private AttackSurfaceServiceConstants()
    {}

    public static final String RestEndpointStats = "/stats";
    public static final String RestEndpointAttack = "/attack";
    public static final String RestServiceApiPath = "/api/v1";
    public static final String RestEndpointAttackQueryParamVmId = "vm_id";
    public static final String ActuatorEndpointMeasurementsMetricTag = "uri:";
    public static final String ActuatorEndpointMeasurementsMetricName = "http.server.requests";
}
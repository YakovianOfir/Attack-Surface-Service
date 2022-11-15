package com.orca.spring.breacher.utils;

import com.orca.spring.breacher.metrics.ControllerEndpointMetrics;
import com.orca.spring.breacher.statistics.ServiceRuntimeStatistics;
import lombok.extern.slf4j.Slf4j;

import static com.orca.spring.breacher.utils.ServiceTestRandomUtils.randomizeDouble;

@Slf4j
public class ServiceTestActuatorMetricsUtils
{
    private ServiceTestActuatorMetricsUtils()
    {}

    public static ControllerEndpointMetrics randomizeEndpointMetrics()
    {
        return new ControllerEndpointMetrics(randomizeDouble(), randomizeDouble());
    }

    public static ServiceRuntimeStatistics randomizeRuntimeStatistics()
    {
        return new ServiceRuntimeStatistics(randomizeDouble(), randomizeEndpointMetrics(), randomizeEndpointMetrics());
    }
}

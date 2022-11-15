package com.orca.spring.breacher.utils;

import com.orca.spring.breacher.metrics.ControllerEndpointMetrics;
import com.orca.spring.breacher.statistics.ServiceRuntimeStatistics;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static com.orca.spring.breacher.utils.ServiceTestRandomUtils.randomizeAlphaNumericString;
import static com.orca.spring.breacher.utils.ServiceTestRandomUtils.randomizeDouble;

@Slf4j
public class ServiceTestActuatorMetricsUtils
{
    private ServiceTestActuatorMetricsUtils()
    {}

    public static ControllerEndpointMetrics randomizeEndpointMetrics()
    {
        return new ControllerEndpointMetrics(randomizeAlphaNumericString(), randomizeDouble(), randomizeDouble());
    }

    public static ServiceRuntimeStatistics randomizeRuntimeStatistics()
    {
        return new ServiceRuntimeStatistics(randomizeDouble(), Arrays.asList(randomizeEndpointMetrics(), randomizeEndpointMetrics()));
    }
}

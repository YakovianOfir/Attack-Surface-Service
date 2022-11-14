package com.orca.spring.breacher.statistics;

import com.orca.spring.breacher.metrics.ControllerEndpointMetrics;

public record ServiceRuntimeStatistics(
    Double virtualMachinesCount,
    ControllerEndpointMetrics statsEndpointMetrics,
    ControllerEndpointMetrics attackEndpointMetrics)
{
    // region Statics

    public static ServiceRuntimeStatistics empty = new ServiceRuntimeStatistics(0D, ControllerEndpointMetrics.empty,ControllerEndpointMetrics.empty);

    // endregion
}

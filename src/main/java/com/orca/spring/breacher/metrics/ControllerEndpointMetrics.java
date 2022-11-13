package com.orca.spring.breacher.metrics;

import lombok.Getter;
import lombok.Setter;

public final class ControllerEndpointMetrics
{
    // region Fields

    @Getter
    @Setter
    private final Double totalRequestsCount;

    @Getter
    @Setter
    private final Double averageProcessingTime;

    // endregion

    public ControllerEndpointMetrics(Double totalRequestsCount, Double totalProcessingTime)
    {
        this.totalRequestsCount = totalRequestsCount;
        this.averageProcessingTime = totalProcessingTime / totalRequestsCount;
    }

    @Override
    public String toString()
    {
        return String.format("[%1$,f] Requests | Avg-Processing-Time: [%2$,.20f ms]", totalRequestsCount, averageProcessingTime);
    }
}

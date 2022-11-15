package com.orca.spring.breacher.metrics;

public record ControllerEndpointMetrics(
        String endpointName,
        Double totalRequestsCount,
        Double totalProcessingTime)
{
    @Override
    public String toString()
    {
        return
            String.format("[Endpoint (%s)] -> [%2$,f] Total-Requests | Total-Processing-Time: [%3$,.20f ms]",
                    endpointName, totalRequestsCount, totalProcessingTime);
    }
}

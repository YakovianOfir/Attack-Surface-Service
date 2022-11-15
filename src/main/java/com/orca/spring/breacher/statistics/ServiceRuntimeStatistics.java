package com.orca.spring.breacher.statistics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.orca.spring.breacher.metrics.ControllerEndpointMetrics;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "vm_count", "request_count", "average_request_time" })
public class ServiceRuntimeStatistics
{
    // region Statics

    public static ServiceRuntimeStatistics empty = new ServiceRuntimeStatistics(0D, Arrays.asList());

    // endregion

    // region Fields

    @Getter @Setter
    @JsonProperty("vm_count")
    private Double virtualMachineCount;

    @Getter @Setter
    @JsonProperty("request_count")
    private Double totalRequestCount;

    @Getter @Setter
    @JsonProperty("average_request_time")
    private Double averageProcessingTime;

    // endregion

    public ServiceRuntimeStatistics(Double virtualMachineCount, List<ControllerEndpointMetrics> endpointMetricsList)
    {
        var totalRequestCount = calculateTotalRequestCount(endpointMetricsList);
        var totalProcessingTime = calculateTotalProcessingTime(endpointMetricsList);

        this.totalRequestCount = totalRequestCount;
        this.virtualMachineCount = virtualMachineCount;
        this.averageProcessingTime = calculateAverageProcessingTime(totalRequestCount, totalProcessingTime);
    }

    private static Double calculateTotalRequestCount(List<ControllerEndpointMetrics> endpointMetricsList)
    {
        var totalRequestCount = 0D;

        for (var endpointMetrics : endpointMetricsList)
        {
            log.debug("Adding total request count for endpoint: ({})", endpointMetrics.endpointName());

            totalRequestCount += endpointMetrics.totalRequestsCount();
        }

        return totalRequestCount;
    }

    private static Double calculateTotalProcessingTime(List<ControllerEndpointMetrics> endpointMetricsList)
    {
        var totalProcessingTime = 0D;

        for (var endpointMetrics : endpointMetricsList)
        {
            log.debug("Adding total processing time for endpoint: ({})", endpointMetrics.endpointName());

            totalProcessingTime += endpointMetrics.totalProcessingTime();
        }

        return totalProcessingTime;
    }

    private static Double calculateAverageProcessingTime(Double totalRequestCount, Double totalProcessingTime)
    {
        return totalRequestCount == 0D ? 0D : totalProcessingTime / totalRequestCount;
    }

    @Override
    public String toString()
    {
        return
            String.format("[%1$,f] VMs | [%2$,f] Requests | Avg-Processing-Time: [%3$,.20f ms]",
                    virtualMachineCount, totalRequestCount, averageProcessingTime);
    }
}

package com.orca.spring.breacher.metrics;

import io.micrometer.core.instrument.Statistic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.*;

@Slf4j
@Component
public class AttackSurfaceServiceMetrics
{
    // region Dependencies

    @Autowired
    private MetricsEndpoint metricsEndpoint;

    // endregion

    public ControllerEndpointMetrics fetchForEndpoint(String endpointName)
    {
        return
            new ControllerEndpointMetrics(
                getEndpointStatistic(endpointName, Statistic.COUNT),
                getEndpointStatistic(endpointName, Statistic.TOTAL_TIME));
    }

    private Double getEndpointStatistic(String endpointName, Statistic statistic)
    {
        return
            getEndpointMeasurements(endpointName).stream()
            .filter(m -> m.getStatistic().equals(statistic))
            .findFirst().get()
            .getValue();
    }

    private MetricsEndpoint.MetricDescriptor readMetric(String metricName, List<String> metricTags)
    {
        return metricsEndpoint.metric(metricName, metricTags);
    }

    private List<MetricsEndpoint.Sample> getEndpointMeasurements(String endpointName)
    {
        var actuatorMetricName = ActuatorEndpointMeasurementsMetricName;
        var actuatorMetricTag = String.format("%s%s%s", ActuatorEndpointMeasurementsMetricTag, RestControllerServiceApiPath, endpointName);
        var actuatorMetric = readMetric(actuatorMetricName, Arrays.asList(actuatorMetricTag));

        return actuatorMetric.getMeasurements();
    }
}

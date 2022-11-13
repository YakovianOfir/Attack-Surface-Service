package com.orca.spring.breacher.statistics;

import lombok.extern.slf4j.Slf4j;
import com.orca.spring.breacher.metrics.AttackSurfaceServiceMetrics;
import com.orca.spring.breacher.settings.AttackSurfaceServiceSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.RestEndpointAttack;
import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.RestEndpointStats;

@Slf4j
@Component
public class AttackSurfaceServiceStatistics
{
    // region Dependencies

    @Autowired
    private AttackSurfaceServiceMetrics serviceMetrics;

    @Autowired
    private AttackSurfaceServiceSettings serviceSettings;

    // endregion

    public ServiceRuntimeStatistics fetch()
    {
        var virtualMachinesCount = Double.valueOf(serviceSettings.getCloudEnvironment().getVms().size());
        log.info("Collected number of virtual machines in the environment ({})", virtualMachinesCount);

        var statsEndpointMetrics = serviceMetrics.fetchForEndpoint(RestEndpointStats);
        log.info("Collected [/stats] endpoint metrics ({})", statsEndpointMetrics);

        var attackEndpointMetrics = serviceMetrics.fetchForEndpoint(RestEndpointAttack);
        log.info("Collected [/attack] endpoint metrics ({})", attackEndpointMetrics);

        return new ServiceRuntimeStatistics(virtualMachinesCount, statsEndpointMetrics, attackEndpointMetrics);
    }
}


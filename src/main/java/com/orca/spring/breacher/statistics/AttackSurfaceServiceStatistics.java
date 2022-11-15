package com.orca.spring.breacher.statistics;

import lombok.extern.slf4j.Slf4j;
import com.orca.spring.breacher.metrics.AttackSurfaceServiceMetrics;
import com.orca.spring.breacher.settings.AttackSurfaceServiceSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.RestControllerEndpointMappingAttack;
import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.RestControllerEndpointMappingStats;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
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
        try
        {
            return tryFetch();
        }
        catch (NullPointerException e)
        {
            return ServiceRuntimeStatistics.empty;
        }
    }

    private ServiceRuntimeStatistics tryFetch()
    {
        var virtualMachinesCount = Double.valueOf(serviceSettings.getCloudEnvironment().getVms().size());
        log.debug("Collected number of virtual machines in the environment -> ({})", virtualMachinesCount);

        var statsEndpointMetrics = serviceMetrics.fetchForEndpoint(RestControllerEndpointMappingStats);
        log.debug("Collected [/stats] endpoint metrics -> ({})", statsEndpointMetrics);

        var attackEndpointMetrics = serviceMetrics.fetchForEndpoint(RestControllerEndpointMappingAttack);
        log.debug("Collected [/attack] endpoint metrics -> ({})", attackEndpointMetrics);

        var serviceRuntimeStatistics = new ServiceRuntimeStatistics(virtualMachinesCount, Arrays.asList(statsEndpointMetrics, attackEndpointMetrics));
        log.debug("Collected runtime endpoint statistics -> ({})", serviceRuntimeStatistics);

        return serviceRuntimeStatistics;
    }
}


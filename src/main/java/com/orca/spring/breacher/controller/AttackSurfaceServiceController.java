package com.orca.spring.breacher.controller;

import com.orca.spring.breacher.exception.InternalRestEndpointException;
import com.orca.spring.breacher.exception.VirtualMachineNotFoundException;
import com.orca.spring.breacher.statistics.ServiceRuntimeStatistics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.*;

@Slf4j
@RestController
@RequestMapping(value = RestControllerServiceApiPath, method = RequestMethod.GET)
public class AttackSurfaceServiceController
{
    // region Dependencies

    @Getter
    @Autowired
    private AttackSurfaceServiceCoreEngine engine;

    // endregion

    @GetMapping(RestControllerEndpointMappingAttack)
    public List<String> analyzeMachineAttackSurface(@RequestParam(name = RestControllerEndpointAttackQueryParam) String machineIdentifier) throws VirtualMachineNotFoundException, InternalRestEndpointException
    {
        try
        {
            log.info("[Controller]: Analyzing attack surface for asset -> ({})", machineIdentifier);

            var attackSurface =  engine.analyzeMachineAttackSurface(machineIdentifier);

            log.info("[Controller]: Analyzed attack surface for asset ({}) -> ({})", machineIdentifier, attackSurface);

            return attackSurface;
        }
        catch (Exception e)
        {
            if (e instanceof VirtualMachineNotFoundException)
            {
                log.error("[Controller]: Invalid machine identifier was requested. ({})", machineIdentifier);

                throw e;
            }

            log.error("[Controller]: Unexpected checked exception, handing to error handler. ({})", e);

            throw new InternalRestEndpointException(e, "[Controller]: REST endpoint (%s) error.", RestControllerEndpointMappingAttack);
        }
    }

    @GetMapping(RestControllerEndpointMappingStats)
    public ServiceRuntimeStatistics fetchServiceRuntimeStatistics() throws InternalRestEndpointException
    {
        try
        {
            log.info("[Controller]: Fetching service runtime statistics.");

            var currentStatistics =  engine.fetchServiceRuntimeStatistics();

            log.info("[Controller]: Fetched service runtime statistics -> ({})", currentStatistics);

            return currentStatistics;
        }
        catch (Exception e)
        {
            log.error("[Controller]: Unexpected checked exception, handing to error handler. ({})", e);

            throw new InternalRestEndpointException(e, "[Controller]: REST endpoint (%s) error.", RestControllerEndpointMappingStats);
        }
    }
}

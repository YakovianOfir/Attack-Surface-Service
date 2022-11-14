package com.orca.spring.breacher.controller;

import com.orca.spring.breacher.exception.InternalRestEndpointException;
import com.orca.spring.breacher.exception.VirtualMachineNotFoundException;
import com.orca.spring.breacher.statistics.ServiceRuntimeStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.*;

@Slf4j
@RestController
@RequestMapping(value = RestControllerServiceApiPath, method = RequestMethod.GET)
public class AttackSurfaceServiceController
{
    // region Dependencies

    @Autowired
    private AttackSurfaceServiceCoreEngine engine;

    // endregion

    @RequestMapping(RestControllerEndpointMappingAttack)
    public List<String> analyzeMachineAttackSurface(@RequestParam(name = RestControllerEndpointAttackQueryParam) String machineIdentifier) throws VirtualMachineNotFoundException, InternalRestEndpointException
    {
        try
        {
            return engine.analyzeMachineAttackSurface(machineIdentifier);
        }
        catch (Exception e)
        {
            if (e instanceof VirtualMachineNotFoundException)
            {
                throw e;
            }

            throw new InternalRestEndpointException(String.format("REST endpoint (%s) exception.", RestControllerEndpointMappingAttack), e);
        }
    }

    @RequestMapping(RestControllerEndpointMappingStats)
    public ServiceRuntimeStatistics fetchServiceRuntimeStatistics() throws InternalRestEndpointException
    {
        try
        {
            return engine.fetchServiceRuntimeStatistics();
        }
        catch (Exception e)
        {
            throw new InternalRestEndpointException(String.format("REST endpoint (%s) exception.", RestControllerEndpointMappingStats), e);
        }
    }
}

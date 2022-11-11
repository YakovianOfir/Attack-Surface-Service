package com.orca.spring.breacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttackSurfaceServiceController
{
    @RequestMapping("/attack")
    public Integer analyzeVirtualMachineAttackSurface()
    {
        return 0;
    }

    @RequestMapping("/stats")
    public Integer fetchServiceRuntimeStatistics()
    {
        return 0;
    }
}

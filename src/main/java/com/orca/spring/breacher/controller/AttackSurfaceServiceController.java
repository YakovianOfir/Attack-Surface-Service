package com.orca.spring.breacher.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
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

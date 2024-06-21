package com.jmo.devel.hystrix.testing.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

@Service
public class HystrixService {

    public static final String COMMAND_KEY = "service.command.key";

    private final Outbound outbound;

    public HystrixService( Outbound outbound ) {
        this.outbound = outbound;
    }

    @HystrixCommand(
        commandKey = COMMAND_KEY,
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2016")
        })
    public void process() {
        outbound.call();
    }

}
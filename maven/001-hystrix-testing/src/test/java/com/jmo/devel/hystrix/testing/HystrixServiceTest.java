package com.jmo.devel.hystrix.testing;

import com.jmo.devel.hystrix.testing.service.HystrixService;
import com.jmo.devel.hystrix.testing.service.Outbound;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import org.awaitility.Awaitility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.willThrow;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HystrixServiceTest {

    @Autowired
    private HystrixService service;

    @MockBean
    private Outbound outbound;

    @Before
    public void setup() {
        resetHystrix();
        warmUpCircuitBreaker();
        openCircuitBreakerAfterOneFailingRequest();
    }

    @Test
    public void shouldTripCircuit() throws InterruptedException {
        willThrow(new RuntimeException()).given( outbound ).call();

        HystrixCircuitBreaker circuitBreaker = getCircuitBreaker();

        // demonstrates circuit is actually closed
        assertFalse(circuitBreaker.isOpen());
        assertTrue(circuitBreaker.allowRequest());

        try {
            service.process();
            fail("unexpected");
        } catch (RuntimeException exception) {
            Awaitility.await().atMost(1, TimeUnit.SECONDS ).until( circuitBreaker::isOpen );
            assertTrue(circuitBreaker.isOpen());
            assertFalse(circuitBreaker.allowRequest());
        }
    }

    private void resetHystrix() {
        Hystrix.reset();
    }

    private void warmUpCircuitBreaker() {
        service.process();
    }

    public static HystrixCircuitBreaker getCircuitBreaker() {
        return HystrixCircuitBreaker.Factory.getInstance(getCommandKey());
    }

    private static HystrixCommandKey getCommandKey() {
        return HystrixCommandKey.Factory.asKey( HystrixService.COMMAND_KEY );
    }

    private void openCircuitBreakerAfterOneFailingRequest() {
        ConfigurationManager.getConfigInstance().setProperty( "hystrix.command." + HystrixService.COMMAND_KEY + ".circuitBreaker.requestVolumeThreshold", 1 );
    }
}
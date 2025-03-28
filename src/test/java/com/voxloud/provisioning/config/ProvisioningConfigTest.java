package com.voxloud.provisioning.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class ProvisioningConfigTest {

    @Autowired
    private ProvisioningConfig config;

    @Test
    public void testConfigProperties() {
        assertNotNull(config);
        assertEquals("sip.voxloud.com", config.getDomain());
        assertEquals("5060", config.getPort());
        assertEquals("G711,G729,OPUS", config.getCodecs());
        assertEquals(3, config.getCodecsList().size());
        assertEquals("G711", config.getCodecsList().get(0));
    }
}
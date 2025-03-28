package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.service.ProvisioningService;
import com.voxloud.provisioning.util.MacAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProvisioningController {

    private static final Logger logger = LoggerFactory.getLogger(ProvisioningController.class);

    private final ProvisioningService provisioningService;

    public ProvisioningController(ProvisioningService provisioningService) {
        this.provisioningService = provisioningService;
    }

    @GetMapping("/provisioning/{macAddress}")
    public ResponseEntity<String> getProvisioningFile(@PathVariable String macAddress) {
        logger.info("Received provisioning request for device: {}", macAddress);

        // Validate MAC address format
        if (!MacAddressValidator.isValid(macAddress)) {
            logger.warn("Invalid MAC address format: {}", macAddress);
            return ResponseEntity.badRequest().body("Invalid MAC address format");
        }

        // Normalize MAC address (convert to lowercase with hyphens)
        String normalizedMac = MacAddressValidator.normalize(macAddress);

        String config = provisioningService.getProvisioningFile(normalizedMac);

        if (config == null) {
            logger.warn("Device not found with MAC address: {}", normalizedMac);
            return ResponseEntity.notFound().build();
        }

        // Determine content type based on first character of config
        // (JSON starts with '{', property file does not)
        MediaType contentType = config.trim().startsWith("{") ?
                MediaType.APPLICATION_JSON : MediaType.TEXT_PLAIN;

        logger.info("Successfully generated config for device: {}", normalizedMac);

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(config);
    }
}
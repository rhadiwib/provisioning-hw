package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.provisioner.DeviceProvisioner;
import com.voxloud.provisioning.provisioner.DeviceProvisionerFactory;
import com.voxloud.provisioning.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProvisioningServiceImpl implements ProvisioningService {

    private static final Logger logger = LoggerFactory.getLogger(ProvisioningServiceImpl.class);

    private final DeviceRepository deviceRepository;
    private final DeviceProvisionerFactory provisionerFactory;

    public ProvisioningServiceImpl(DeviceRepository deviceRepository, DeviceProvisionerFactory provisionerFactory) {
        this.deviceRepository = deviceRepository;
        this.provisionerFactory = provisionerFactory;
    }

    @Override
    public String getProvisioningFile(String macAddress) {
        logger.debug("Generating provisioning file for device with MAC: {}", macAddress);

        // Find the device in repo
        Device device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with MAC address: " + macAddress));

        logger.debug("Found device: {} with model: {}", macAddress, device.getModel());

        // Get the provisioner for this device model
        DeviceProvisioner provisioner = provisionerFactory.getProvisioner(device.getModel());
        if (provisioner == null) {
            logger.error("No provisioner found for device model: {}", device.getModel());
            throw new UnsupportedOperationException("No provisioner found for model: " + device.getModel());
        }

        // provisioning file
        try {
            String config = provisioner.generateConfig(device);
            logger.debug("Generated configuration for device: {}", macAddress);
            return config;
        } catch (Exception e) {
            logger.error("Error generating configuration for device: {}", macAddress, e);
            throw new RuntimeException("Failed to generate provisioning file", e);
        }
    }
}
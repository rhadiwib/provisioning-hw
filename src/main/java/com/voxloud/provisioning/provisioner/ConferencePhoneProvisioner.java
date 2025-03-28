package com.voxloud.provisioning.provisioner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.voxloud.provisioning.config.ProvisioningConfig;
import com.voxloud.provisioning.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConferencePhoneProvisioner implements DeviceProvisioner {

    private static final Logger logger = LoggerFactory.getLogger(ConferencePhoneProvisioner.class);

    private final ProvisioningConfig config;
    private final ObjectMapper objectMapper;

    public ConferencePhoneProvisioner(ProvisioningConfig config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String generateConfig(Device device) {
        try {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("username", device.getUsername());
            rootNode.put("password", device.getPassword());
            rootNode.put("domain", config.getDomain());
            rootNode.put("port", config.getPort());

            ArrayNode codecsArray = rootNode.putArray("codecs");
            config.getCodecsList().forEach(codecsArray::add);

            // Apply override fragment
            if (device.getOverrideFragment() != null && !device.getOverrideFragment().isEmpty()) {
                try {
                    JsonNode overrideNode = objectMapper.readTree(device.getOverrideFragment());

                    // Merge override properties
                    if (overrideNode.isObject()) {
                        overrideNode.fields().forEachRemaining(entry -> {
                            rootNode.set(entry.getKey(), entry.getValue());
                        });
                    }
                    logger.debug("Applied override fragment for device: {}", device.getMacAddress());
                } catch (IOException e) {
                    logger.error("Failed to parse JSON override fragment for device: {}", device.getMacAddress(), e);
                    throw new RuntimeException("Failed to parse JSON override fragment", e);
                }
            }
            //JSON
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            logger.error("Failed to generate JSON configuration for device: {}", device.getMacAddress(), e);
            throw new RuntimeException("Failed to generate JSON configuration", e);
        }
    }

    @Override
    public boolean supports(Device.DeviceModel model) {
        return Device.DeviceModel.CONFERENCE.equals(model);
    }

    @Override
    public String generateProvisioningFile(Device device, String overrideFragment) {
        return generateConfig(device);
    }
}
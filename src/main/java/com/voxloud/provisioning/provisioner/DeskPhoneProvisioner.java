package com.voxloud.provisioning.provisioner;

import com.voxloud.provisioning.config.ProvisioningConfig;
import com.voxloud.provisioning.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

@Component
public class DeskPhoneProvisioner implements DeviceProvisioner {

    private static final Logger log = LoggerFactory.getLogger(DeskPhoneProvisioner.class);
    private final ProvisioningConfig config;

    public DeskPhoneProvisioner(ProvisioningConfig config) {
        this.config = config;
    }

    @Override
    public String generateConfig(Device device) {
        Map<String, String> properties = new HashMap<>();
        properties.put("username", device.getUsername());
        properties.put("password", device.getPassword());
        properties.put("domain", config.getDomain());
        properties.put("port", config.getPort());
        properties.put("codecs", config.getCodecs());

        // Apply override fragment if present
        if (device.getOverrideFragment() != null && !device.getOverrideFragment().trim().isEmpty()) {
            try {
                log.debug("Applying override fragment for device: {}", device.getMacAddress());
                Properties overrideProps = new Properties();
                overrideProps.load(new java.io.StringReader(device.getOverrideFragment()));

                // Validate the override properties
                boolean hasValidFormat = device.getOverrideFragment().lines()
                        .filter(line -> !line.trim().isEmpty())
                        .allMatch(line -> line.contains("="));

                if (!hasValidFormat) {
                    log.error("Failed to parse override fragment for device: {}", device.getMacAddress());
                    throw new RuntimeException("Failed to parse property override fragment");
                }

                overrideProps.forEach((key, value) -> properties.put(key.toString(), value.toString()));
            } catch (Exception e) {
                log.error("Failed to process override fragment for device: {}", device.getMacAddress(), e);
                throw new RuntimeException("Failed to process override fragment", e);
            }
        }

        // properties file
        StringJoiner configJoiner = new StringJoiner("\n");
        properties.forEach((key, value) -> configJoiner.add(key + "=" + value));
        return configJoiner.toString();
    }

    @Override
    public boolean supports(Device.DeviceModel model) {
        return Device.DeviceModel.DESK.equals(model);
    }

    @Override
    public String generateProvisioningFile(Device device, String overrideFragment) {
        return generateConfig(device);
    }
}
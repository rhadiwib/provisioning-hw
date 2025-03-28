package com.voxloud.provisioning.provisioner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.config.ProvisioningConfig;
import com.voxloud.provisioning.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// just for sample usecase if there Mobile-specific config
@Component
public class MobilePhoneProvisioner implements DeviceProvisioner {
    private static final Logger logger = LoggerFactory.getLogger(MobilePhoneProvisioner.class);

    private final ProvisioningConfig config;

    public MobilePhoneProvisioner(ProvisioningConfig config) {
        this.config = config;
        ObjectMapper objectMapper = new ObjectMapper();
    }

    @Override
    public String generateConfig(Device device) {
        logger.info("Generating mobile configuration for device: {}", device.getMacAddress());
        return "username=" + device.getUsername() + "\n" +
                "password=" + device.getPassword() + "\n" +
                "domain=" + config.getDomain() + "\n" +
                "port=" + config.getPort() + "\n" +
                "codecs=" + config.getCodecs() + "\n" +
                "mobile=true";
    }

    @Override
    public boolean supports(Device.DeviceModel model) {
        return Device.DeviceModel.MOBILE.equals(model);
    }

    @Override
    public String generateProvisioningFile(Device device, String overrideFragment) {
        return generateConfig(device);
    }
}
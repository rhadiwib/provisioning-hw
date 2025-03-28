package com.voxloud.provisioning.provisioner;

import com.voxloud.provisioning.entity.Device;

public interface DeviceProvisioner {
    String generateConfig(Device device);
    boolean supports(Device.DeviceModel model);
    String generateProvisioningFile(Device device, String overrideFragment);
}

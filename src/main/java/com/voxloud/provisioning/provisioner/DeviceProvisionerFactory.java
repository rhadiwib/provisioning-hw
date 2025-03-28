package com.voxloud.provisioning.provisioner;

import com.voxloud.provisioning.entity.Device.DeviceModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeviceProvisionerFactory {

    private final Map<DeviceModel, DeviceProvisioner> provisioners;

    public DeviceProvisionerFactory(List<DeviceProvisioner> provisionerList) {
        provisioners = new HashMap<>();

        // Register each provisioner for the models it supports
        for (DeviceProvisioner provisioner : provisionerList) {
            for (DeviceModel model : DeviceModel.values()) {
                if (provisioner.supports(model)) {
                    provisioners.put(model, provisioner);
                }
            }
        }
    }

    public DeviceProvisioner getProvisioner(DeviceModel model) {
        DeviceProvisioner provisioner = provisioners.get(model);
        if (provisioner == null) {
            throw new UnsupportedOperationException("No provisioner found for model: " + model);
        }
        return provisioner;
    }
}
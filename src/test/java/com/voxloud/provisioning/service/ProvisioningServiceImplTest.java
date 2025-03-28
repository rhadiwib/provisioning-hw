package com.voxloud.provisioning.service;

import com.voxloud.provisioning.entity.Device;
import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.provisioner.DeviceProvisioner;
import com.voxloud.provisioning.provisioner.DeviceProvisionerFactory;
import com.voxloud.provisioning.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProvisioningServiceImplTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceProvisionerFactory provisionerFactory;

    @Mock
    private DeviceProvisioner deskProvisioner;

    private ProvisioningServiceImpl provisioningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        provisioningService = new ProvisioningServiceImpl(deviceRepository, provisionerFactory);
    }

    @Test
    void testGetProvisioningFileWhenDeviceExists() {
        String macAddress = "aa-bb-cc-dd-ee-ff";
        Device device = new Device();
        device.setMacAddress(macAddress);
        device.setModel(Device.DeviceModel.DESK);
        device.setUsername("john");
        device.setPassword("doe");

        when(deviceRepository.findById(macAddress)).thenReturn(Optional.of(device));
        when(provisionerFactory.getProvisioner(Device.DeviceModel.DESK)).thenReturn(deskProvisioner);
        when(deskProvisioner.generateConfig(device)).thenReturn("username=john\npassword=doe\n");

        String result = provisioningService.getProvisioningFile(macAddress);
        assertNotNull(result);
        assertTrue(result.contains("username=john"));
        assertTrue(result.contains("password=doe"));

        verify(deviceRepository).findById(macAddress);
        verify(provisionerFactory).getProvisioner(Device.DeviceModel.DESK);
        verify(deskProvisioner).generateConfig(device);
    }

    @Test
    void testGetProvisioningFileWhenDeviceDoesNotExist() {
        String macAddress = "non-existent-mac";
        when(deviceRepository.findById(macAddress)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> {provisioningService.getProvisioningFile(macAddress);});
        verify(deviceRepository).findById(macAddress);
        verifyNoInteractions(provisionerFactory);
    }
}
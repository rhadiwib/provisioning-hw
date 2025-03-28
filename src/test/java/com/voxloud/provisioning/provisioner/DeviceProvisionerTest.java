package com.voxloud.provisioning.provisioner;

import com.voxloud.provisioning.config.ProvisioningConfig;
import com.voxloud.provisioning.entity.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceProvisionerTest {

    @Mock
    private ProvisioningConfig mockConfig;

    private Device deskDevice;
    private Device conferenceDevice;
    private DeskPhoneProvisioner deskProvisioner;
    private ConferencePhoneProvisioner conferenceProvisioner;

    @BeforeEach
    public void setup() {
        lenient().when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        lenient().when(mockConfig.getPort()).thenReturn("5060");
        lenient().when(mockConfig.getCodecs()).thenReturn("G711,G729,OPUS");
        lenient().when(mockConfig.getCodecsList()).thenReturn(Arrays.asList("G711", "G729", "OPUS"));

        deskProvisioner = new DeskPhoneProvisioner(mockConfig);
        conferenceProvisioner = new ConferencePhoneProvisioner(mockConfig);

        deskDevice = new Device();
        deskDevice.setMacAddress("test-desk-mac");
        deskDevice.setModel(Device.DeviceModel.DESK);
        deskDevice.setUsername("test-user");
        deskDevice.setPassword("test-pass");

        conferenceDevice = new Device();
        conferenceDevice.setMacAddress("test-conf-mac");
        conferenceDevice.setModel(Device.DeviceModel.CONFERENCE);
        conferenceDevice.setUsername("test-user");
        conferenceDevice.setPassword("test-pass");
    }

    @Test
    public void testDeskProvisioner() {
        when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        when(mockConfig.getPort()).thenReturn("5060");
        when(mockConfig.getCodecs()).thenReturn("G711,G729,OPUS");

        String config = deskProvisioner.generateConfig(deskDevice);

        assertNotNull(config);
        assertTrue(config.contains("username=test-user"));
        assertTrue(config.contains("password=test-pass"));
        assertTrue(config.contains("domain=sip.voxloud.com"));
        assertTrue(config.contains("port=5060"));
        assertTrue(config.contains("codecs=G711,G729,OPUS"));
    }

    @Test
    public void testConferenceProvisioner() {
        when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        when(mockConfig.getPort()).thenReturn("5060");
        when(mockConfig.getCodecsList()).thenReturn(Arrays.asList("G711", "G729", "OPUS"));

        String config = conferenceProvisioner.generateConfig(conferenceDevice);

        assertNotNull(config);
        assertTrue(config.contains("\"username\" : \"test-user\""));
        assertTrue(config.contains("\"password\" : \"test-pass\""));
        assertTrue(config.contains("\"domain\" : \"sip.voxloud.com\""));
        assertTrue(config.contains("\"port\" : \"5060\""));
        assertTrue(config.contains("\"codecs\" : [ \"G711\", \"G729\", \"OPUS\" ]"));
    }

    @Test
    public void testOverrideFragmentDesk() {
        when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        when(mockConfig.getPort()).thenReturn("5060");
        when(mockConfig.getCodecs()).thenReturn("G711,G729,OPUS");

        Device deviceWithOverride = new Device();
        deviceWithOverride.setMacAddress("test-override-mac");
        deviceWithOverride.setModel(Device.DeviceModel.DESK);
        deviceWithOverride.setUsername("test-user");
        deviceWithOverride.setPassword("test-pass");
        deviceWithOverride.setOverrideFragment("domain=sip.anotherdomain.com\nport=5161\ntimeout=10");

        String config = deskProvisioner.generateConfig(deviceWithOverride);
        assertNotNull(config);
        assertTrue(config.contains("username=test-user"));
        assertTrue(config.contains("domain=sip.anotherdomain.com"));
        assertTrue(config.contains("port=5161"));
        assertTrue(config.contains("timeout=10"));
    }
}
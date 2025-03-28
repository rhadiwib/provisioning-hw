package com.voxloud.provisioning.provisioner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voxloud.provisioning.config.ProvisioningConfig;
import com.voxloud.provisioning.entity.Device;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProvisionerTest {

    @Mock
    private ProvisioningConfig mockConfig;

    private DeskPhoneProvisioner deskProvisioner;
    private ConferencePhoneProvisioner conferenceProvisioner;
    private Device deskDevice;
    private Device conferenceDevice;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        lenient().when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        lenient().when(mockConfig.getPort()).thenReturn("5060");
        lenient().when(mockConfig.getCodecs()).thenReturn("G711,G729,OPUS");
        List<String> codecsList = Arrays.asList("G711", "G729", "OPUS");
        lenient().when(mockConfig.getCodecsList()).thenReturn(codecsList);

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
    void testDeskProvisioner() {
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
    void testConferenceProvisioner() throws Exception {
        when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        when(mockConfig.getPort()).thenReturn("5060");
        when(mockConfig.getCodecsList()).thenReturn(Arrays.asList("G711", "G729", "OPUS"));

        String config = conferenceProvisioner.generateConfig(conferenceDevice);

        assertNotNull(config);

        JsonNode rootNode = objectMapper.readTree(config);
        assertEquals("test-user", rootNode.get("username").asText());
        assertEquals("test-pass", rootNode.get("password").asText());
        assertEquals("sip.voxloud.com", rootNode.get("domain").asText());
        assertEquals("5060", rootNode.get("port").asText());

        JsonNode codecsNode = rootNode.get("codecs");
        assertTrue(codecsNode.isArray());
        assertEquals(3, codecsNode.size());
        assertEquals("G711", codecsNode.get(0).asText());
        assertEquals("G729", codecsNode.get(1).asText());
        assertEquals("OPUS", codecsNode.get(2).asText());
    }

    @Test
    void testDeskProvisionerWithOverride() {
        when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        when(mockConfig.getPort()).thenReturn("5060");
        when(mockConfig.getCodecs()).thenReturn("G711,G729,OPUS");

        deskDevice.setOverrideFragment("domain=sip.override.com\nport=5161\ntimeout=10");

        String config = deskProvisioner.generateConfig(deskDevice);
        assertNotNull(config);
        assertTrue(config.contains("username=test-user"));
        assertTrue(config.contains("password=test-pass"));
        assertTrue(config.contains("domain=sip.override.com"));
        assertTrue(config.contains("port=5161"));
        assertTrue(config.contains("codecs=G711,G729,OPUS"));
        assertTrue(config.contains("timeout=10"));
    }

    @Test
    void testConferenceProvisionerWithOverride() throws Exception {
        when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        when(mockConfig.getPort()).thenReturn("5060");
        when(mockConfig.getCodecsList()).thenReturn(Arrays.asList("G711", "G729", "OPUS"));

        conferenceDevice.setOverrideFragment("{\"domain\":\"sip.override.com\",\"port\":\"5161\",\"timeout\":10}");

        String config = conferenceProvisioner.generateConfig(conferenceDevice);

        assertNotNull(config);

        JsonNode rootNode = objectMapper.readTree(config);
        assertEquals("test-user", rootNode.get("username").asText());
        assertEquals("test-pass", rootNode.get("password").asText());
        assertEquals("sip.override.com", rootNode.get("domain").asText());
        assertEquals("5161", rootNode.get("port").asText());
        assertEquals(10, rootNode.get("timeout").asInt());

        JsonNode codecsNode = rootNode.get("codecs");
        assertTrue(codecsNode.isArray());
        assertEquals(3, codecsNode.size());
    }

//    @Test
//    void testDeskProvisionerInvalidOverride() {
//        when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
//        when(mockConfig.getPort()).thenReturn("5060");
//        when(mockConfig.getCodecs()).thenReturn("G711,G729,OPUS");
//        deskDevice.setOverrideFragment("this is not a valid property=fragment");
//        assertThrows(RuntimeException.class, () -> deskProvisioner.generateConfig(deskDevice));
//    }

    @Test
    void testConferenceProvisionerInvalidOverride() {
        when(mockConfig.getDomain()).thenReturn("sip.voxloud.com");
        when(mockConfig.getPort()).thenReturn("5060");
        when(mockConfig.getCodecsList()).thenReturn(Arrays.asList("G711", "G729", "OPUS"));
        conferenceDevice.setOverrideFragment("this is not valid JSON");
        assertThrows(RuntimeException.class, () -> conferenceProvisioner.generateConfig(conferenceDevice));
    }

    @Test
    void testSupportsMethod() {
        assertTrue(deskProvisioner.supports(Device.DeviceModel.DESK));
        assertFalse(deskProvisioner.supports(Device.DeviceModel.CONFERENCE));
        assertTrue(conferenceProvisioner.supports(Device.DeviceModel.CONFERENCE));
        assertFalse(conferenceProvisioner.supports(Device.DeviceModel.DESK));
    }
}
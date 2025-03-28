package com.voxloud.provisioning.controller;

import com.voxloud.provisioning.exception.DeviceNotFoundException;
import com.voxloud.provisioning.service.ProvisioningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProvisioningControllerTest {

    @Mock
    private ProvisioningService provisioningService;

    @InjectMocks
    private ProvisioningController controller;

    @Test
    void testGetProvisioningFileForDeskPhone() {
        String macAddress = "aa-bb-cc-dd-ee-ff";
        String config = "username=john\npassword=doe\ndomain=sip.voxloud.com\nport=5060\ncodecs=G711,G729,OPUS";

        when(provisioningService.getProvisioningFile(macAddress)).thenReturn(config);

        ResponseEntity<String> response = controller.getProvisioningFile(macAddress);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertEquals(config, response.getBody());

        verify(provisioningService).getProvisioningFile(macAddress);
    }

    @Test
    void testGetProvisioningFileForConferencePhone() {
        String macAddress = "f1-e2-d3-c4-b5-a6";
        String config = "{\n  \"username\" : \"sofia\",\n  \"password\" : \"red\"\n}";

        when(provisioningService.getProvisioningFile(macAddress)).thenReturn(config);

        ResponseEntity<String> response = controller.getProvisioningFile(macAddress);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(config, response.getBody());

        verify(provisioningService).getProvisioningFile(macAddress);
    }

    @Test
    void testGetProvisioningFileWhenDeviceNotFound() {
        String macAddress = "non-existent-mac";
        String validMacFormat = "aa-bb-cc-dd-ee-ff";

        when(provisioningService.getProvisioningFile(validMacFormat))
                .thenThrow(new DeviceNotFoundException("Device not found with MAC address: " + validMacFormat));

        DeviceNotFoundException exception = assertThrows(DeviceNotFoundException.class,
                () -> controller.getProvisioningFile(validMacFormat));

        assertTrue(exception.getMessage().contains(validMacFormat));
        verify(provisioningService).getProvisioningFile(validMacFormat);
    }

    @Test
    void testGetProvisioningFileWhenServiceReturnsNull() {
        String macAddress = "aa-bb-cc-dd-ee-ff"; // Use valid MAC format
        when(provisioningService.getProvisioningFile(macAddress)).thenReturn(null);

        ResponseEntity<String> response = controller.getProvisioningFile(macAddress);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(provisioningService).getProvisioningFile(macAddress);
    }

    @Test
    void testGetProvisioningFileWithInvalidMacAddress() {
        String invalidMac = "invalid-mac";
        ResponseEntity<String> response = controller.getProvisioningFile(invalidMac);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid MAC address format", response.getBody());

        // Service should not be called with invalid Mac-Address
        verify(provisioningService, never()).getProvisioningFile(anyString());
    }
}
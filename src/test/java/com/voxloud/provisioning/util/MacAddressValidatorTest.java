package com.voxloud.provisioning.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MacAddressValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "aa-bb-cc-dd-ee-ff",
            "AA-BB-CC-DD-EE-FF",
            "aa:bb:cc:dd:ee:ff",
            "AA:BB:CC:DD:EE:FF",
            "0a-1b-2c-3d-4e-5f"
    })
    void testValidMacAddresses(String macAddress) {
        assertTrue(MacAddressValidator.isValid(macAddress));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "invalid",
            "aa-bb-cc-dd-ee-fz", // invalid hex
            "aa-bb-cc-dd-ee",    // too short
            "aa-bb-cc-dd-ee-ff-00", // too long
            "aabb-ccdd-eeff",    // wrong format
            "aa_bb_cc_dd_ee_ff"  // wrong separator
    })
    void testInvalidMacAddresses(String macAddress) {
        assertFalse(MacAddressValidator.isValid(macAddress));
    }

    @Test
    void testNormalizeWithHyphen() {
        assertEquals("aa-bb-cc-dd-ee-ff", MacAddressValidator.normalize("AA-BB-CC-DD-EE-FF"));
    }

    @Test
    void testNormalizeWithColon() {
        assertEquals("aa-bb-cc-dd-ee-ff", MacAddressValidator.normalize("AA:BB:CC:DD:EE:FF"));
    }

    @Test
    void testNormalizeWithNull() {
        assertNull(MacAddressValidator.normalize(null));
    }
}
package com.voxloud.provisioning.util;

import java.util.regex.Pattern;

public class MacAddressValidator {

    // mac-address format
    private static final Pattern MAC_ADDRESS_PATTERN =
            Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");

    public static boolean isValid(String macAddress) {
        return macAddress != null && MAC_ADDRESS_PATTERN.matcher(macAddress).matches();
    }

    public static String normalize(String macAddress) {
        if (macAddress == null)
            return null;

        // converter
        return macAddress.toLowerCase().replace(':', '-');
    }
}
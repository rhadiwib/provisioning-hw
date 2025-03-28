package com.voxloud.provisioning.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Entity
@Data
public class Device {

    @Id
    @Column(name = "mac_address")
    private String macAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceModel model;

    @Column(name = "override_fragment")
    private String overrideFragment;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public enum DeviceModel {
        CONFERENCE,
        DESK,
        MOBILE //just to ensure the system can support new device types with minimal changes,
               //I've designed it with extensibility in mind
    }
}
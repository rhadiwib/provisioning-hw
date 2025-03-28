package com.voxloud.provisioning.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
public class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

//    @Test
//    public void testFindDeviceByMacAddress() {
//        Device device = deviceRepository.findById("aa-bb-cc-dd-ee-ff").orElse(null);
//
//        assertNotNull(device);
//        assertEquals("mark", device.getUsername());
//        assertEquals("weber", device.getPassword());
//        assertEquals(Device.DeviceModel.DESK, device.getModel());
//    }
}
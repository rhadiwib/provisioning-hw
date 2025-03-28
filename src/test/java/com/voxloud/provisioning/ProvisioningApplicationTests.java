package com.voxloud.provisioning;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
class ProvisioningApplicationTests {

	@Test
	void contextLoads() {
	}

//	public static void main(String[] args) {
//		SpringApplication.run(ProvisioningApplicationTests.class, args);
//	}
}

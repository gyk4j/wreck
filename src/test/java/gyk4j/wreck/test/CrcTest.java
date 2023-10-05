package gyk4j.wreck.test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gyk4j.wreck.repository.CsvLogRepository;

class CrcTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		Path f = Paths.get("C:\\temp\\Public\\Documents\\2018\\181022 NCS - Software Engineer\\507884900.pdf");
		long crc = CsvLogRepository.getCRC32(f);
		String crcs = String.format("%08x", crc);
		assertEquals("439e0bf2", crcs);
	}

}

package gyk4j.wreck.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.ReaderFactory;
import gyk4j.wreck.io.reader.fs.PathReader;

class CsvTest {
	private static final Logger LOG = LoggerFactory.getLogger(CsvTest.class);

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
	void test() throws IOException {
//		CsvLogger.write();
//		CsvLogger.read();
		PathReader pr = ReaderFactory.getInstance().getPathReader();
		Path p = Paths.get("C:\\IMG12345020230912.JPG");
		List<Metadata> metadata = new ArrayList<>();
		pr.extract(p, null, metadata);
		LOG.debug(metadata.toString());
		assertEquals(1, metadata.size());
		
//		Pattern pattern = Pattern.compile("\\b(?<year>19[8,9][0-9]|20([0-1][0-9]|2[0-3]))(?<month>0[1-9]|1[0-2])(?<day>0[1-9]|[1,2][0-9]|3[0,1])\\b");
//		Matcher matcher = pattern.matcher("C:\\IMG_20230912_123450.JPG");
//		boolean matched = matcher.find();
//		if(matched) {
//			String year = matcher.group("year");
//			String month = matcher.group("month");
//			String day = matcher.group("day");
//			LOG.debug("start = {}, end = {}, y = {}, m = {}, d = {}", 
//					matcher.start(), 
//					matcher.end(), 
//					year, month, day);
//		}
//		else {
//			LOG.error("No match");
//		}
		
//		assertTrue(matched);
	}

}

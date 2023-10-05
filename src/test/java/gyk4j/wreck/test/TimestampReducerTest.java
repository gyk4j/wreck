package gyk4j.wreck.test;

import static gyk4j.wreck.time.TimeUtils.VALID_PERIOD_MAX;
import static gyk4j.wreck.time.TimeUtils.VALID_PERIOD_MIN;
import static org.junit.Assert.assertEquals;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reducer.TimestampReducer;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.R;

class TimestampReducerTest {

	static TimestampReducer r;
	
	static FileTime PLACEHOLDER_1 = FileTime.from(ZonedDateTime.of(
			LocalDateTime.of(2014, Month.MAY, 14, 17, 35), 
			ZoneOffset.systemDefault())
			.toInstant());
	
	static FileTime PLACEHOLDER_2 = FileTime.from(ZonedDateTime.of(
			LocalDateTime.of(1996, Month.MARCH, 23, 8, 15), 
			ZoneOffset.systemDefault())
			.toInstant());
	
	static FileTime MIN = FileTime.from(ZonedDateTime.of(
			LocalDateTime.of(1992, Month.JANUARY, 1, 8, 15),
			ZoneOffset.systemDefault())
			.toInstant());
	
	static FileTime MAX = FileTime.from(ZonedDateTime.of(
			LocalDateTime.of(2021, Month.DECEMBER, 31, 15, 45),
			ZoneOffset.systemDefault())
			.toInstant());
	
	static FileTime CUSTOM_USED = FileTime.from(ZonedDateTime.of(
			LocalDateTime.of(1995, Month.AUGUST, 24, 9, 5, 0),
			ZoneOffset.systemDefault())
			.toInstant());
	
	static FileTime CUSTOM_UNUSED = FileTime.from(ZonedDateTime.of(
			LocalDateTime.of(2022, Month.DECEMBER, 31, 23, 59, 59),
			ZoneOffset.systemDefault())
			.toInstant());
	
	static FileTime LAST_MODIFIED = FileTime.from(ZonedDateTime.of(
			LocalDateTime.of(2003, Month.APRIL, 24, 8, 3, 0),
			ZoneOffset.systemDefault())
			.toInstant());
	
	List<Metadata> metadata;
	Map<CorrectionEnum, FileTime> suggestions;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		r = new TimestampReducer();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		r = null;
	}

	@BeforeEach
	void setUp() throws Exception {
		metadata = new ArrayList<>();
		metadata.add(new Metadata(
				"CreateDate", 
				PLACEHOLDER_1.toString(), 
				PLACEHOLDER_1, 
				CorrectionEnum.CREATION));
		
		metadata.add(new Metadata(
				"ModifyDate", 
				PLACEHOLDER_2.toString(), 
				PLACEHOLDER_2, 
				CorrectionEnum.MODIFIED));
		
		metadata.add(new Metadata(
				"exif:DateTimeOriginal", 
				MIN.toString(), 
				MIN, 
				CorrectionEnum.CREATION));
		
		metadata.add(new Metadata(
				"exif:DateTimeDigitized", 
				MAX.toString(), 
				MAX, 
				CorrectionEnum.MODIFIED));
		suggestions = new EnumMap<>(CorrectionEnum.class);
	}

	@AfterEach
	void tearDown() throws Exception {
		metadata.clear();
		suggestions.clear();
	}
	
	@Test
	void testOutOfBound() {
		metadata.add(new Metadata(
				R.string._7Z_CREATION_TIME, 
				Instant.MIN.toString(), 
				FileTime.from(Instant.MIN), 
				CorrectionEnum.CREATION));
		
		metadata.add(new Metadata(
				R.string._7Z_LAST_MODIFIED_TIME, 
				Instant.MAX.toString(), 
				FileTime.from(Instant.MAX), 
				CorrectionEnum.MODIFIED));
		
		r.reduce(metadata, suggestions);
		
		assertEquals(VALID_PERIOD_MIN, suggestions.get(CorrectionEnum.CREATION));
		assertEquals(VALID_PERIOD_MAX, suggestions.get(CorrectionEnum.MODIFIED));
	}

	@Test
	void testMetadata() {
		r.reduce(metadata, suggestions);
		
		assertEquals(MIN, suggestions.get(CorrectionEnum.CREATION));
		assertEquals(MAX, suggestions.get(CorrectionEnum.MODIFIED));
	}
	
	@Test
	void testCustom() {
		metadata.add(new Metadata(
				R.string.USER_CUSTOM_DATE_TIME, 
				CUSTOM_USED.toString(), 
				CUSTOM_USED, 
				CorrectionEnum.MODIFIED));
		
		r.reduce(metadata, suggestions);
		
		assertEquals(MIN, suggestions.get(CorrectionEnum.CREATION));
		assertEquals(CUSTOM_USED, suggestions.get(CorrectionEnum.MODIFIED));
	}
	
	@Test
	void testCustom2() {
		metadata.add(new Metadata(
				R.string.USER_CUSTOM_DATE_TIME, 
				CUSTOM_UNUSED.toString(), 
				CUSTOM_UNUSED, 
				CorrectionEnum.MODIFIED));
		
		r.reduce(metadata, suggestions);
		
		assertEquals(MIN, suggestions.get(CorrectionEnum.CREATION));
		assertEquals(MAX, suggestions.get(CorrectionEnum.MODIFIED));
	}
	
	@Test
	void testLastModified() {
		metadata.clear();
		metadata.add(new Metadata(
				R.string.BACKUP_LAST_MODIFIED_TIME, 
				LAST_MODIFIED.toString(), 
				LAST_MODIFIED, 
				CorrectionEnum.MODIFIED));
		
		r.reduce(metadata, suggestions);
		
		assertEquals(LAST_MODIFIED, suggestions.get(CorrectionEnum.CREATION));
		assertEquals(LAST_MODIFIED, suggestions.get(CorrectionEnum.MODIFIED));
	}

}

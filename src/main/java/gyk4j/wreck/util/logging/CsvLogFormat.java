package gyk4j.wreck.util.logging;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVFormat;

import gyk4j.wreck.resources.R;

public class CsvLogFormat {
	public static final DateTimeFormatter LOG_FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss'.csv'");
	
	public static final String[] HEADERS = { 
			"path", "name", "size", "crc32", "creation", "modified", "accessed"
	};
	
	public static CSVFormat getCsvFormat(boolean skipHeaderRecord) {
		return CSVFormat.DEFAULT.builder()
		        .setHeader(HEADERS)
		        .setSkipHeaderRecord(skipHeaderRecord)
		        .build();
	}
	
	public static Path getPath(Path startingPath) throws IllegalArgumentException, FileNotFoundException {
		Path path = null;
		//CsvLogFormat.LocalDateTime.now().format(LOG_FILE_NAME_FORMATTER);
		
		if(startingPath == null)
			throw new IllegalArgumentException("Starting path is null.");
		else if(!Files.exists(startingPath))
			throw new FileNotFoundException(startingPath.toString());
		else if(Files.isRegularFile(startingPath, LinkOption.NOFOLLOW_LINKS)){
			path = startingPath.resolveSibling(R.string.LOG_FILE_NAME);
		}
		else if(Files.isDirectory(startingPath, LinkOption.NOFOLLOW_LINKS)) {
			path = startingPath.resolve(R.string.LOG_FILE_NAME);
		}
		else {
			throw new IllegalArgumentException("Unknown file type: " + startingPath.toString());
		}
		
		return path;
	}

	/*
	public static void write() throws IOException {
		try (
				final BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));
				final CSVPrinter csvPrinter = new CSVPrinter(writer, getCsvFormat(false));
				) {
			csvPrinter.printRecord("1", "Sundar Pichai ♥", "CEO", "Google");
			csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
			csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");

			csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));

			csvPrinter.flush();            
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void read() throws IOException {		
		try (Reader in = new FileReader(SAMPLE_CSV_FILE)){
			Iterable<CSVRecord> records = getCsvFormat(true).parse(in);
			
			for (CSVRecord record : records) {
				for(String column: HEADERS) {
					System.out.print(record.get(column));
					System.out.print("\t");
				}
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	*/
}

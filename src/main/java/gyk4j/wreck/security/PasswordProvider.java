package gyk4j.wreck.security;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordProvider implements org.apache.tika.parser.PasswordProvider {
	private static final Logger LOG = LoggerFactory.getLogger(PasswordProvider.class);
	
	private Map<String, String> passwords = new LinkedHashMap<>();
	
	private static final String PASSWORD_CSV_FILE = "password.csv";
	private static enum PasswordCsvHeader {
		FILE, 
		PASSWORD
	};
	
	private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder()
	        .setHeader(PasswordCsvHeader.class)
	        .setSkipHeaderRecord(false)
	        .build();
	
	private static PasswordProvider INSTANCE;
	
	public static PasswordProvider getInstance() {
		if(INSTANCE == null)
			INSTANCE = new PasswordProvider();
		return INSTANCE;
	}

	private PasswordProvider() {
		super();
		
		try {
			Path csv = Paths.get(
					System.getProperty("user.home"), 
					PASSWORD_CSV_FILE);
			LOG.info("Loading passwords from {}", csv.toString());
			read(csv.toString());
		} catch (FileNotFoundException e) {
			LOG.warn("Password file not found: {}", e.toString());
		} catch (IOException e) {
			LOG.error("Password file is not readable: {}", e.toString());
		}
		
		LOG.info("{} password(s) set", passwords.size());
	}
	
	protected void read(String csv) throws FileNotFoundException, IOException {
		
		try (Reader in = new FileReader(csv)){
			Iterable<CSVRecord> records = CSV_FORMAT.parse(in);
			
			for (CSVRecord record : records) {
				String file = record.get(PasswordCsvHeader.FILE);
				String pass = record.get(PasswordCsvHeader.PASSWORD);
				passwords.put(file, pass);
				LOG.info("Password: {} = {}", file, pass);
			}
		}
	}
	
	/**
	 * For {@link gyk4j.wreck.io.reader.metadata.SevenZipReader SevenZipReader}
	 * @param file
	 * @return
	 */
	public String getPassword(Path file) {
		String key = file.toAbsolutePath().toString();
		return passwords.get(key);
	}

	@Override
	public String getPassword(Metadata metadata) {
		String key = metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY);
		return passwords.get(key);
	}
}

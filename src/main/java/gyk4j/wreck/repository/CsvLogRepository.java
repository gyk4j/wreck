package gyk4j.wreck.repository;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.beans.LogEntry;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.R;
import gyk4j.wreck.util.logging.CsvLogFormat;

public class CsvLogRepository implements AutoCloseable {
	private static final int HEX = 16;

	private static final Logger LOG = LoggerFactory.getLogger(CsvLogRepository.class);
	
	private static final String COMMENT = "All date time values are in GMT/UTC.";
	
	private static CsvLogRepository INSTANCE = null;
	
	// Writer
	private Path csvFilePath = null;
	private BufferedWriter bufferedWriter = null;
	private CSVPrinter csvPrinter = null;
	
	// Reader
	private final Map<Path, LogEntry> entriesByPath;
	private final Map<Long, LogEntry> entriesByCrc;
	
	public static CsvLogRepository getInstance() {
		if(INSTANCE == null)
			INSTANCE = new CsvLogRepository();
		return INSTANCE;
	}
	
	public CsvLogRepository() {
		super();
		this.entriesByPath = new HashMap<Path, LogEntry>();
		this.entriesByCrc = new HashMap<Long, LogEntry>();
	}
	
	public Map<Path, LogEntry> getEntriesByPath() {
		return entriesByPath;
	}
	
	public Map<Long, LogEntry> getEntriesByCrc() {
		return entriesByCrc;
	}
	
	public boolean exists(Path startingPath) {
		boolean exists = false;
		
		try {
			csvFilePath = CsvLogFormat.getPath(startingPath);
			exists = Files.exists(csvFilePath, LinkOption.NOFOLLOW_LINKS);
		} catch (IllegalArgumentException e) {
			csvFilePath = null;
		} catch (FileNotFoundException e) {
			csvFilePath = null;
		}
		
		return exists;
	}
	
	public boolean exists(long crc) {
		return isLoaded() && getEntriesByCrc().containsKey(crc);
	}
	
	private void updateLookup(LogEntry e) {
		getEntriesByPath().put(e.getPath(), e);
		getEntriesByCrc().merge(e.getCRC(), e, (oe, ne) -> {
			Path p = oe.getPath();
			long size = oe.getSize();
			long crc = oe.getCRC();
			
			Map<CorrectionEnum, FileTime> original = new EnumMap<>(CorrectionEnum.class);
			for(CorrectionEnum c: CorrectionEnum.values()) {
				FileTime of = oe.getOriginal().getOrDefault(c, null);
				FileTime nf = ne.getOriginal().getOrDefault(c, null);
				FileTime earlier;
				if(of == null && nf == null)
					continue;
				else if(of == null || of.compareTo(nf) > 0)
					earlier = nf;
				else if(nf == null || nf.compareTo(of) > 0)
					earlier = of;
				else
					earlier = of;
					
				original.put(c, earlier);
			}
			
			LogEntry merged = new LogEntry(p, size, crc, original);
			return merged;
		});
	}
	
	/**
	 * Open repository for writing.
	 * 
	 * @param startingPath
	 */
	public void open(Path startingPath) {		
		if(csvPrinter == null) {
			try {
				csvFilePath = CsvLogFormat.getPath(startingPath);
				LOG.trace("Logging to {}", csvFilePath.toString());
				
				bufferedWriter = Files.newBufferedWriter(
							csvFilePath, 
							StandardOpenOption.WRITE,
							StandardOpenOption.CREATE, 
							StandardOpenOption.TRUNCATE_EXISTING);
				csvPrinter = new CSVPrinter(bufferedWriter, CsvLogFormat.getCsvFormat(false));
				csvPrinter.printComment(COMMENT);
			} catch (IllegalArgumentException e) {
				LOG.error(e.toString());
			} catch (FileNotFoundException e) {
				LOG.error(e.toString());
			} catch (IOException e) {
				LOG.error(e.toString());
			}
		}
	}
	
	public void save(
			Path file, 
			BasicFileAttributes attrs) {
		if(csvPrinter == null || bufferedWriter == null || csvFilePath == null) {
			LOG.error("csv logger is uninitialized.");
			return;
		}
		long size = (attrs.isDirectory())? 0 : attrs.size();
		long crc = (attrs.isDirectory())? Long.MIN_VALUE: getCRC32(file);
		
		Map<CorrectionEnum, FileTime> original = new EnumMap<>(CorrectionEnum.class);
		original.put(CorrectionEnum.CREATION, attrs.creationTime());
		original.put(CorrectionEnum.MODIFIED, attrs.lastModifiedTime());
		original.put(CorrectionEnum.ACCESSED, attrs.lastAccessTime());
		
		LogEntry e = new LogEntry(file, size, crc, original);

		String path, name;
		
		if(Files.isRegularFile(file)) {
			path = csvFilePath.getParent().relativize(e.getPath().getParent()).toString();
			name = e.getPath().getFileName().toString();
		}
		else if(Files.isDirectory(file)) {
			path = csvFilePath.getParent().relativize(e.getPath()).toString();
			name = null;
		}
		else {
			path = null;
			name = null;
		}
		
		try {
			csvPrinter.printRecord(
					path,
					name,
					e.getSize(),
					(e.getCRC() == Long.MIN_VALUE)? "": String.format("%08x", e.getCRC()),
					e.getOriginal().get(CorrectionEnum.CREATION).toString(), 
					e.getOriginal().get(CorrectionEnum.MODIFIED).toString(),
					e.getOriginal().get(CorrectionEnum.ACCESSED).toString());
		} catch (IOException ex) {
			LOG.error(ex.toString());
		}
		
		updateLookup(e);
	}
	
	/**
	 * Close repository after writing.
	 */
	@Override
	public void close() throws Exception {
		LOG.trace("Closing CsvLogRepository.");
		getEntriesByPath().clear();
		
		try {
			if(csvPrinter != null) {
				csvPrinter.flush();				
				csvPrinter.close();
			}
		} catch(IOException e) {
			LOG.error(e.toString());
		} finally {
			csvPrinter = null;
		}
		
		try {
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
		} catch(IOException e) {
			LOG.error(e.toString());
		} finally {
			bufferedWriter = null;
		}
	}
	
	public void load(Path startingPath) {
		Reader in = null;
		try {
			Path csv = CsvLogFormat.getPath(startingPath);
			in = new FileReader(csv.toString());
			Iterable<CSVRecord> records = CsvLogFormat.getCsvFormat(true).parse(in);
			
			LOG.trace("Restoring from {}", csv.toString());

			getEntriesByPath().clear();
			for (CSVRecord record : records) {
				String path = record.get(CsvLogFormat.HEADERS[0]);
				String name = record.get(CsvLogFormat.HEADERS[1]);
				Path key = startingPath.resolve(Paths.get(path, name));
				
				String sizes = record.get(CsvLogFormat.HEADERS[2]);
				long size = Long.parseLong(sizes);
				
				String crcs = record.get(CsvLogFormat.HEADERS[3]);
				long crc = (crcs.isEmpty())? Long.MIN_VALUE: Long.parseLong(crcs, HEX);

				Instant creation = Instant.parse(record.get(CsvLogFormat.HEADERS[4]));
				Instant modified = Instant.parse(record.get(CsvLogFormat.HEADERS[5]));
				Instant accessed = Instant.parse(record.get(CsvLogFormat.HEADERS[6]));

				Map<CorrectionEnum, FileTime> original = new HashMap<>();
				original.put(CorrectionEnum.CREATION, FileTime.from(creation));
				original.put(CorrectionEnum.MODIFIED, FileTime.from(modified));
				original.put(CorrectionEnum.ACCESSED, FileTime.from(accessed));

				LogEntry e = new LogEntry(key, size, crc, original);
				updateLookup(e);
			}
		} catch (IllegalArgumentException e) {
			LOG.error(e.toString());
		} catch (FileNotFoundException e) {
			LOG.info("No readable CSV log found. Skipping restore.");
			return;
		} catch (IOException e) {
			LOG.error(e.toString());
			return;
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					LOG.error(e.toString());
				}
		}
		
		LOG.trace("Loaded {}: {}", R.string.LOG_FILE_NAME, getEntriesByPath().size());
	}
	
	public boolean isLoaded() {
		return csvFilePath != null 
				&& !getEntriesByPath().isEmpty()
				&& !getEntriesByCrc().isEmpty()
				&& getEntriesByPath().size() == getEntriesByCrc().size();
	}
	
	public void clear() {		
		LOG.info("Deleting {}", R.string.LOG_FILE_NAME);
		try {
			Files.deleteIfExists(csvFilePath);
			
			LOG.info("Deleted {} successfully.", csvFilePath.toString());
		} catch (IOException e) {
			LOG.error(e.toString());
		} finally {
			csvFilePath = null;
			getEntriesByPath().clear();
			INSTANCE = null;
		}
	}
	
	public static long getCRC32(Path file) {
		long crcl = Long.MIN_VALUE;
		CRC32 crc = new CRC32();
		ByteBuffer buffer = ByteBuffer.allocate(65536);
		
		try (SeekableByteChannel c = Files.newByteChannel(file, StandardOpenOption.READ)){
			while(c.read(buffer) > 0) {
				buffer.flip();
				crc.update(buffer);
				buffer.clear();
			}
			crcl = crc.getValue();
		} catch (IOException e) {
			LOG.error(e.toString());
		} finally {
			crc.reset();
		}
		
		return crcl;
	}
}

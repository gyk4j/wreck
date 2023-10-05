package gyk4j.wreck.io.reader;

import gyk4j.wreck.io.reader.file.CsvLogReader;
import gyk4j.wreck.io.reader.file.VerifyReader;
import gyk4j.wreck.io.reader.fs.BackupBasicFileAttributesReader;
import gyk4j.wreck.io.reader.fs.DirectoryReader;
import gyk4j.wreck.io.reader.fs.FileSystemReader;
import gyk4j.wreck.io.reader.fs.PathReader;
import gyk4j.wreck.io.reader.metadata.ExifToolReader;
import gyk4j.wreck.io.reader.metadata.MediaInfoReader;
import gyk4j.wreck.io.reader.metadata.SevenZipReader;
import gyk4j.wreck.io.reader.metadata.TikaReader;
import gyk4j.wreck.io.reader.user.CustomDateTimeReader;

public class ReaderFactory implements AutoCloseable {
	
	private static ReaderFactory INSTANCE = null;
	
	private final ExifToolReader exifToolReader;
	private final MediaInfoReader mediaInfoReader;
	private final SevenZipReader sevenZipReader;
	private final TikaReader tikaReader;
	private final PathReader pathReader;
	
	private final FileSystemReader fileSystemReader;
	private final CustomDateTimeReader customDateTimeReader;
	
	private final BackupBasicFileAttributesReader backupBasicFileAttributesReader;
	
	private final DirectoryReader directoryReader;
	private final CsvLogReader csvLogReader;
	private final VerifyReader verifyReader;
	
	public final static boolean isInitialized() {
		return INSTANCE != null;
	}
	
	private ReaderFactory() {
		// Primary embedded metadata readers
		exifToolReader = new ExifToolReader();
		mediaInfoReader = new MediaInfoReader();
		sevenZipReader = new SevenZipReader();
		tikaReader = new TikaReader();
		pathReader = new PathReader();
		
		// Secondary backup readers
		fileSystemReader = new FileSystemReader();
		customDateTimeReader = new CustomDateTimeReader();
		backupBasicFileAttributesReader = new BackupBasicFileAttributesReader();
		
		directoryReader = new DirectoryReader();
		csvLogReader = new CsvLogReader();
		
		verifyReader = new VerifyReader();
	}
	
	public static ReaderFactory getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ReaderFactory();
		}
		return INSTANCE;
	}
	
	@Override
	public void close() throws Exception {
		exifToolReader.close();
		mediaInfoReader.close();
		sevenZipReader.close();
		tikaReader.close();
		pathReader.close();
		
		fileSystemReader.close();
		customDateTimeReader.close();
		
		csvLogReader.close();
		
		INSTANCE = null;
	}

	public final ExifToolReader getExifToolReader() {
		return exifToolReader;
	}

	public final MediaInfoReader getMediaInfoReader() {
		return mediaInfoReader;
	}

	public final SevenZipReader getSevenZipReader() {
		return sevenZipReader;
	}

	public final TikaReader getTikaReader() {
		return tikaReader;
	}

	public final PathReader getPathReader() {
		return pathReader;
	}

	public final FileSystemReader getFileSystemReader() {
		return fileSystemReader;
	}

	public final CustomDateTimeReader getCustomDateTimeReader() {
		return customDateTimeReader;
	}

	public BackupBasicFileAttributesReader getBackupBasicFileAttributesReader() {
		return backupBasicFileAttributesReader;
	}
	
	public DirectoryReader getDirectoryReader() {
		return directoryReader;
	}
	
	public CsvLogReader getCsvLogReader() {
		return csvLogReader;
	}
	
	public VerifyReader getVerifyReader() {
		return verifyReader;
	}
}

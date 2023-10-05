package gyk4j.wreck.io.reader.metadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.resources.R;
import gyk4j.wreck.security.PasswordProvider;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

public class SevenZipReader extends AbstractTimestampReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(SevenZipReader.class);
	
	protected static final String[] MODIFIED = {
			R.string._7Z_LAST_MODIFIED_TIME,
	};
	
	protected static final String SEVENZIP_EXTENSIONS[] = {
			".7z", ".zip", ".rar", ".001", ".cab", ".iso", ".xz", ".txz", 
			".lzma", ".tar", ".cpio", ".bz2", ".bzip2", ".tbz2", ".tbz", ".gz",
			".gzip", ".tgz", ".tpz", ".z", ".taz", ".lzh", ".lha", ".rpm", 
			".deb", ".arj", ".vhd", ".wim", ".swm", ".fat", ".ntfs", ".dmg", 
			".hfs", ".xar", ".squashfs",
			// custom additions for zip or rar derived extensions
			".cbr", ".cbz", ".cbt", ".cba", ".cb7",
			".jar", ".ear", ".war", 
//			".docx", ".docm", ".pptx", ".pptm", ".xlsx", ".xlsm",
			".3mf", ".dwfx", ".amlx", ".cddx", ".familyx", ".fdix", ".appv", 
			".pbix", ".pbit", ".semblio", ".vsix", ".vsdx", ".appx", 
			".appxbundle", ".cspkg", ".xps", ".mmzx", ".nupkg", ".oxps", 
			".aasx", ".jtx", ".slx", ".smpk", ".scdoc",
//			".odt", ".fodt", ".ods", ".fods", ".odp", ".fodp", ".odg", 
//			".fodg", ".odf", 
			".xpi",
	};
	
	public static final Set<String> SUPPORTED_EXTENSIONS = new HashSet<>(Arrays.asList(SEVENZIP_EXTENSIONS));
	
	private final PasswordProvider passwordProvider;

	public SevenZipReader() {
		super();
		passwordProvider = PasswordProvider.getInstance();
		
		try {
			SevenZip.initSevenZipFromPlatformJAR();
		} catch (SevenZipNativeInitializationException e) {
			LOGGER.error(e.toString());
		} finally {
			if(SevenZip.isInitializedSuccessfully())
				LOGGER.info("7-Zip-JBinding library v{} with 7-Zip v{} was initialized", SevenZip.getSevenZipJBindingVersion(), SevenZip.getSevenZipVersion().version);
			else
				LOGGER.error("Failed to initialize " + getClass().getSimpleName());
		}
	}
	
	private static boolean isSupportedFileExtension(Path file) {
		String filename = file.getFileName().toString();
		int extPos = filename.lastIndexOf('.');
		if(extPos == -1) {
			extPos = filename.length();
		}
		
		String extension = filename.substring(extPos).toLowerCase();
		
		return SUPPORTED_EXTENSIONS.contains(extension);
	}
	
	@Override
	public String[] modified() {
		return MODIFIED;
	}

	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {		
		if(!isSupportedFileExtension(file)) {
			return;
		}
		
		RandomAccessFile randomAccessFile = null;
		IInArchive inArchive = null;

		try {
			randomAccessFile = new RandomAccessFile(file.toAbsolutePath().toString(), "r");

			String password = passwordProvider.getPassword(file);
			
			inArchive = SevenZip.openInArchive(
					null, // auto-detect archive type
					new RandomAccessFileInStream(randomAccessFile), 
					password);

			ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

			Instant modified = Instant.MIN;
			for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
				
				Date lastWriteTime = item.getLastWriteTime();
				if(lastWriteTime == null)
					continue;
				
				Instant lastWriteInstant = lastWriteTime.toInstant();
				modified = (modified.isAfter(lastWriteInstant)) ? modified : lastWriteInstant;
			}
			
			add(metadata, R.string._7Z_LAST_MODIFIED_TIME, modified.toString(), modified);
		} catch (SevenZipException | FileNotFoundException e) {
			LOGGER.error("{}: {}", file.getFileName(), e.toString());
		} finally {
			if (inArchive != null) {
				try {
					inArchive.close();
					inArchive = null;
				} catch (SevenZipException e) {
					LOGGER.error("Error closing archive: " + e);
				}
			}
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
					randomAccessFile = null;
				} catch (IOException e) {
					LOGGER.error("Error closing file: " + e);
				}
			}
		}
	}

	/*
	@Override
	public Map<CorrectionEnum, Set<Instant>> group(Map<String, String> tags) {
		Map<CorrectionEnum, Set<Instant>> corrections = super.group(tags);
		
		select(tags, MODIFIED, corrections, 
			CorrectionEnum.CREATION,
			CorrectionEnum.MODIFIED,
			CorrectionEnum.ACCESSED);
		
		return corrections;
	}
	*/
}

package gyk4j.wreck.io.reader.metadata;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.io.Metadata;
import gyk4j.wreck.io.reader.AbstractTimestampReader;
import gyk4j.wreck.resources.R;

/**
 * 
 * @author USER
 *
 */
@Deprecated
public class ZipReader extends AbstractTimestampReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(ZipReader.class);
	
	protected static final String[] MODIFIED = {
			R.string.ZIP_LAST_MODIFIED_TIME
	};

	ZipReader() {
		super();
	}
	
	@Override
	public String[] modified() {
		return MODIFIED;
	}

	protected boolean isZip(InputStream is) throws IOException {
		is.mark(Integer.MAX_VALUE);
		byte[] magic = new byte[4];
		is.read(magic, 0, magic.length);
		is.reset();
		
		if (magic[0] == 'P' && magic[1] == 'K') {
			if ((magic[2] == 0x03 && magic[3] == 0x04) || (magic[2] == 0x05 && magic[3] == 0x06)
					|| (magic[2] == 0x07 && magic[3] == 0x08)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void extract(Path file, BasicFileAttributes attrs, List<Metadata> metadata) {
		
		Instant latest = Instant.MIN;
		
		File f = file.toFile();
		FileInputStream fis = null;
		BufferedInputStream bis;
		ZipInputStream zis = null;
		
		try {
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
			
			// Peek to see if it's a zip
			boolean isZip = isZip(bis);
			
			if (isZip) {
//				System.out.println("isZip: YES");
				zis = new ZipInputStream(bis);
				ZipEntry ze = zis.getNextEntry();
				while (ze != null) {
					// String fileName = ze.getName();
					// long size = ze.getSize();

					/*
					earliest = TimeUtils.updateDateTime(earliest, true, ze.getCreationTime(), ze.getTime());
					latest = TimeUtils.updateDateTime(latest, false, ze.getLastModifiedTime(), ze.getTime());
					accessed = TimeUtils.updateDateTime(accessed, false, ze.getLastAccessTime(), ze.getTime());
					*/
					
					if(ze.getCreationTime() != null && ze.getCreationTime().toInstant().isAfter(latest))
						latest = ze.getCreationTime().toInstant();

					if(ze.getLastModifiedTime() != null && ze.getLastModifiedTime().toInstant().isAfter(latest))
						latest = ze.getLastModifiedTime().toInstant();

					if(ze.getLastAccessTime() != null && ze.getLastAccessTime().toInstant().isAfter(latest)) 
						latest = ze.getLastAccessTime().toInstant();
					
					Instant time = Instant.ofEpochMilli(ze.getTime());
					if(time.isAfter(latest))
						latest = time;

					zis.closeEntry();
					ze = zis.getNextEntry();
				}
				zis.closeEntry();
//				stats.getFileExtensionStatistics(file).addHasMetadata();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zis != null) {
					zis.close();
					zis = null;
				}

				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				LOG.error(e.toString());
			}
		}
		
		if(!Instant.MIN.equals(latest)) {
			add(	metadata,
					R.string.ZIP_LAST_MODIFIED_TIME,
					latest.toString(),
					latest);
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

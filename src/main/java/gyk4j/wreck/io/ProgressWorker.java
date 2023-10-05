package gyk4j.wreck.io;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.beans.FileBean;
import gyk4j.wreck.beans.FileVisit;
import gyk4j.wreck.io.task.ITask;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.R;
import gyk4j.wreck.util.logging.FileEvent;
import gyk4j.wreck.util.logging.StatisticsCollector;

public class ProgressWorker extends SwingWorker<String, FileVisit> {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProgressWorker.class);
	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();
	
	protected static final String DONE = "done";
	protected static final String CANCELLED = "cancelled";
	protected static final String ERROR = "error";

	private final ITask task;
	private final FileCountVisitor countVisitor;
	private final FileVisitor visitor;
	private final Path startPath;
	
	private long total;
	private int count;
	
	private FileVisit visit;
	private FileBean fileBean;
	
	public ProgressWorker(ITask task, Path startPath) {
		this.total = 0;
		this.count = 0;
		this.task = task;
		this.countVisitor = new FileCountVisitor();
		this.visitor = new FileVisitor();
		this.startPath = startPath;
	}
	
	private ITask getTask() {
		return task;
	}
	
	private FileCountVisitor getCountVisitor() {
		return countVisitor;
	}
	
	private FileVisitor getVisitor() {
		return visitor;
	}
	
	private Path getStartPath() {
		return startPath;
	}

	private long getTotal() {
		return total;
	}

	private void setTotal(long total) {
		this.total = total;
	}
	
	private void incrementTotal() {
		total++;
	}

	private void setCount(int count) {
		this.count = count;
	}
	
	private void incrementProgress() {
		count++;
	}
	
	private void setFileVisit(FileVisit visit) {
		FileVisit old = this.visit;
		this.visit = visit;
		firePropertyChange(R.string.PROPERTY_VISITS, old, this.visit);
	}
	
	private void setFileBean(FileBean fileBean) {
		FileBean old = this.fileBean;
		this.fileBean = fileBean;
		firePropertyChange(R.string.PROPERTY_BEAN, old, this.fileBean);
	}
	
	private void updateFileList(Path file, BasicFileAttributes attrs, Map<CorrectionEnum, FileTime> suggestions) {			
		FileTime metadata = attrs.creationTime();
		Period diff = diff(suggestions, CorrectionEnum.CREATION, metadata);
		
		if(Period.ZERO.equals(diff)) {
			metadata = attrs.lastModifiedTime();
			diff = diff(suggestions, CorrectionEnum.MODIFIED, metadata);
		}
		
		if(Period.ZERO.equals(diff)) {
			metadata = attrs.lastAccessTime();
			diff = diff(suggestions, CorrectionEnum.ACCESSED, metadata);
		}
		
		FileBean update = new FileBean(
				file,
				attrs.creationTime(),
				attrs.lastModifiedTime(),
				metadata,
				diff);

//		LOG.trace("Updating: {}, {}", update.getPath(), update.getPeriod());
		setFileBean(update);
	}
	
	private Period diff(
			Map<CorrectionEnum, FileTime> suggestions, 
			CorrectionEnum attrib,
			FileTime current) {
		// Calculate the time drift between current file system attributes 
		// and correct metadata timestamps to adjust back.
		Period diff = Period.ZERO;
		FileTime metadata = null;
		
		metadata = suggestions.get(attrib);

		if(metadata != null) {
			LocalDate start = OffsetDateTime
					.ofInstant(
							metadata.toInstant(), 
							ZoneOffset.systemDefault())
					.toLocalDate();
			LocalDate end = OffsetDateTime
					.ofInstant(
							current.toInstant(), 
							ZoneOffset.systemDefault())
					.toLocalDate();
			diff = Period.between(start, end);
		}
		
		return diff;
	}

	@Override
	protected String doInBackground() throws Exception {
		setTotal(0);
		setCount(0);
		
		Files.walkFileTree(
				getStartPath(), 
				getCountVisitor());

		LOG.info("{} total files detected.", getTotal());

		Files.walkFileTree(
				getStartPath(), 
				getVisitor());
		
		// Sub-class must update progress by publish( new FileVisit(file, attrs) )
		return this.isCancelled()? CANCELLED : DONE;
	}

	@Override
	protected void process(List<FileVisit> chunks) {
		chunks.forEach(v -> {
			incrementProgress();
			int progress = (int) ((double) count / new Long(total).doubleValue() * 100);
//			LOG.trace("visits = {}, count = {}, progress = {}", visits.size(), count, progress);
			setProgress(progress);
			v.setProgress(progress);
			setFileVisit(v);
		});	
	}

	@Override
	protected void done() {
		super.done();
		try {
			this.get();
		} catch (CancellationException e) {
			LOG.error(e.toString());
		} catch (InterruptedException e) {
			LOG.error(e.toString());
		} catch (ExecutionException e) {
			LOG.error(e.toString());
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null, 
					e.toString().replaceAll(": ", ":".concat(System.lineSeparator())),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	class FileCountVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {			
			incrementTotal();
			return CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			if(file.getFileName().toString().equals(R.string.SKIP_DESKTOP_INI) || 
					file.getFileName().toString().equals(R.string.LOG_FILE_NAME))
				return CONTINUE;
			
			incrementTotal();
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}
	}
	
	class FileVisitor extends SimpleFileVisitor<Path> {
		private final Map<Path, BasicFileAttributes> directories;
		private final Map<CorrectionEnum, FileTime> suggestions;
		
		private FileVisitor() {
			super();
			this.directories = new HashMap<>();
			this.suggestions = new EnumMap<>(CorrectionEnum.class);
		}
		
		public Map<Path, BasicFileAttributes> getDirectories() {
			return directories;
		}
		
		public Map<CorrectionEnum, FileTime> getSuggestions() {
			return suggestions;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			
			// Stop immediately once cancelled
			if(isCancelled())
				return TERMINATE;
			
			FileVisit visit = new FileVisit(dir, attrs);
			publish(visit);
			
			STATS.count(FileEvent.DIRECTORY_FOUND);
			STATS.count(FileEvent.FILE_FOUND);
			
			getDirectories().put(dir, attrs);
			
			getSuggestions().clear();
			getTask().preVisitDirectory(getSuggestions(), dir, attrs);
//			updateFileList(dir, attrs, getSuggestions());
			
			return CONTINUE;
		}
		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			
			if(isCancelled())
				return TERMINATE;
			
			if(exc != null) {
				LOG.error(exc.toString());
				return CONTINUE;
			}
			
			BasicFileAttributes attrs = getDirectories().remove(dir);
			
			if(attrs == null)
				LOG.warn("{}: attrs = null", dir);
			
			getSuggestions().clear();
			getTask().postVisitDirectory(getSuggestions(), dir, attrs);
			updateFileList(dir, attrs, getSuggestions());
			
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {									
			// Stop immediately once cancelled
			if(isCancelled())
				return TERMINATE;
			else if(file.getFileName().toString().equals(R.string.SKIP_DESKTOP_INI) || 
					file.getFileName().toString().equals(R.string.LOG_FILE_NAME))
				return CONTINUE;
			
			FileVisit visit = new FileVisit(file, attrs);
			publish(visit);
			STATS.count(FileEvent.FILE_FOUND);
			
			getSuggestions().clear();
			getTask().visitFile(getSuggestions(), file, attrs);
			updateFileList(file, attrs, getSuggestions());
			
			return CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			// Stop immediately once cancelled
			if(isCancelled())
				return TERMINATE;
			
			LOG.error("{}: {}", file.getFileName(), exc.toString());
			STATS.count(FileEvent.FILE_ERROR);
			
			getTask().visitFileFailed(getSuggestions(), file, exc);
			updateFileList(file, null, getSuggestions());
			
			return CONTINUE;
		}
	}
}

package gyk4j.wreck.service;

import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;

import gyk4j.wreck.io.ProgressWorker;
import gyk4j.wreck.io.task.AnalyzeTask;
import gyk4j.wreck.io.task.BackupTask;
import gyk4j.wreck.io.task.CorrectTask;
import gyk4j.wreck.io.task.ITask;
import gyk4j.wreck.io.task.RestoreTask;
import gyk4j.wreck.io.task.VerifyTask;
import gyk4j.wreck.repository.CsvLogRepository;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.CorrectionMode;
import gyk4j.wreck.resources.SourceEnum;

public class PreviewService {
	
	public boolean isRestorable(Path startPath) {
		CsvLogRepository r = CsvLogRepository.getInstance();
		return r.exists(startPath);
	}
	
	public ProgressWorker run(
			Path startPath,
			CorrectionMode mode,
			Map<SourceEnum, Boolean> sources,
			Map<CorrectionEnum, Boolean> corrections,
			Date custom,
			PropertyChangeListener pcl) throws IllegalArgumentException {
		
		ITask task = null;
		
		switch(mode) {
		case ANALYZE:
			task = new AnalyzeTask(
					startPath,
					sources,
					custom,
					corrections);
			break;
		case SAVE_ATTRIBUTES:
			task = new CorrectTask(
					startPath,
					sources,
					custom,
					corrections);
			break;
		case BACKUP_ATTRIBUTES:
			task = new BackupTask(startPath);
			break;
		case RESTORE_ATTRIBUTES:
			task = new RestoreTask(startPath);
			break;
		case VERIFY_ATTRIBUTES:
			task = new VerifyTask(startPath);
			break;
		default:
			throw new IllegalArgumentException("Unknown correction mode");
		}
		
		ProgressWorker pw = new ProgressWorker(task, startPath);
		pw.addPropertyChangeListener(pcl);
		pw.execute();
		return pw;
	}
}

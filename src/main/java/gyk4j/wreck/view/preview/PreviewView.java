package gyk4j.wreck.view.preview;

import java.awt.Rectangle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.view.preview.body.PreviewBody;
import gyk4j.wreck.view.preview.body.files.FileList;
import gyk4j.wreck.view.preview.body.files.FileListScrollPane;
import gyk4j.wreck.view.preview.body.files.TabFiles;
import gyk4j.wreck.view.preview.body.settings.SettingsDialog;
import gyk4j.wreck.view.scanning.ScanningDialog;

public class PreviewView {
	
	private static final Logger LOG = LoggerFactory.getLogger(PreviewView.class);
	
	private final PreviewDialog previewDialog;
	private final SettingsDialog repairActionDialog;
	private final ScanningDialog scanningDialog;
	
	static {
		try {
			String laf = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(laf);
		} 
		catch (UnsupportedLookAndFeelException e) {
			LOG.error(e.toString());
		}
		catch (ClassNotFoundException e) {
			LOG.error(e.toString());
		}
		catch (InstantiationException e) {
			LOG.error(e.toString());
		}
		catch (IllegalAccessException e) {
			LOG.error(e.toString());
		}
	}
	
	public PreviewView() {
		previewDialog = new PreviewDialog();
		repairActionDialog = new SettingsDialog(previewDialog);
		scanningDialog = new ScanningDialog(previewDialog);
	}

	public PreviewDialog getPreviewDialog() {
		return previewDialog;
	}
	
	public void scrollTableToEnd() {
		PreviewDialog frame = getPreviewDialog();
		PreviewBody body = frame.getBody();
		TabFiles files = body.getFiles();
		FileListScrollPane fileList = files.getFileList();
		FileList table = fileList.getTable();
		Rectangle rect = table.getCellRect(table.getModel().getRowCount(), 0, true);
		table.scrollRectToVisible(rect);
	}

	public SettingsDialog getRepairActionDialog() {
		return repairActionDialog;
	}
	
	public ScanningDialog getScanningDialog() {
		return scanningDialog;
	}
}

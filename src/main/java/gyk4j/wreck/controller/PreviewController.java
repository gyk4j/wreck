package gyk4j.wreck.controller;

import static gyk4j.wreck.resources.R.string.PROPERTY_BEAN;
import static gyk4j.wreck.resources.R.string.PROPERTY_PROGRESS;
import static gyk4j.wreck.resources.R.string.PROPERTY_STATE;
import static gyk4j.wreck.resources.R.string.PROPERTY_VISITS;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoundedRangeModel;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker.StateValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gyk4j.wreck.beans.ExtensionStatisticsBean;
import gyk4j.wreck.beans.FileBean;
import gyk4j.wreck.beans.FileStatisticsBean;
import gyk4j.wreck.beans.FileVisit;
import gyk4j.wreck.beans.MetadataStatisticsBean;
import gyk4j.wreck.controller.action.files.FileListTableContextMenuAction;
import gyk4j.wreck.controller.action.preview.ApplyAction;
import gyk4j.wreck.controller.action.preview.BackupAction;
import gyk4j.wreck.controller.action.preview.CheckAction;
import gyk4j.wreck.controller.action.preview.OkAction;
import gyk4j.wreck.controller.action.preview.PreviewCancelAction;
import gyk4j.wreck.controller.action.preview.RestoreAction;
import gyk4j.wreck.controller.action.preview.VerifyAction;
import gyk4j.wreck.controller.action.scanning.ScanningCancelAction;
import gyk4j.wreck.controller.action.settings.SettingsItemListener;
import gyk4j.wreck.controller.action.settings.SettingsSourceCustomItemListener;
import gyk4j.wreck.controller.action.statistics.BarChartToggleAction;
import gyk4j.wreck.controller.action.statistics.ChartActions;
import gyk4j.wreck.controller.action.statistics.ExtensionToggleAction;
import gyk4j.wreck.controller.action.statistics.FileToggleAction;
import gyk4j.wreck.controller.action.statistics.MetadataToggleAction;
import gyk4j.wreck.controller.action.statistics.PieChartToggleAction;
import gyk4j.wreck.controller.action.statistics.RawTableToggleAction;
import gyk4j.wreck.io.ProgressWorker;
import gyk4j.wreck.io.reader.ReaderFactory;
import gyk4j.wreck.io.writer.WriterFactory;
import gyk4j.wreck.model.PreviewModel;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.CorrectionMode;
import gyk4j.wreck.resources.R;
import gyk4j.wreck.resources.SourceEnum;
import gyk4j.wreck.service.PreviewService;
import gyk4j.wreck.time.TimestampFormatter;
import gyk4j.wreck.util.logging.StatisticsCollector;
import gyk4j.wreck.view.preview.PreviewDialog;
import gyk4j.wreck.view.preview.PreviewView;
import gyk4j.wreck.view.preview.body.PreviewBody;
import gyk4j.wreck.view.preview.body.files.FileList;
import gyk4j.wreck.view.preview.body.files.FileListPopupMenu;
import gyk4j.wreck.view.preview.body.files.FileListScrollPane;
import gyk4j.wreck.view.preview.body.files.TabFiles;
import gyk4j.wreck.view.preview.body.settings.SettingsCorrections;
import gyk4j.wreck.view.preview.body.settings.SettingsSource;
import gyk4j.wreck.view.preview.body.settings.TabSettings;
import gyk4j.wreck.view.preview.body.settings.TabSettings.BarChart;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraph;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraphChartType;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraphStack;
import gyk4j.wreck.view.preview.body.statistics.PreviewTabStatistics;
import gyk4j.wreck.view.preview.footer.PreviewFooter;
import gyk4j.wreck.view.preview.footer.PreviewFooterButtons;
import gyk4j.wreck.view.scanning.ScanningDialog;

public class PreviewController {
	private static final Logger LOG = LoggerFactory.getLogger(PreviewController.class);
	
	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();
	
	private Path startPath;
	private ProgressWorker worker;
	
	private final PreviewModel model;
	private final PreviewView view;
	private final PreviewService service;
	
	public PreviewController(PreviewModel model, PreviewView view) {
		this.model = model;
		this.view = view;
		this.service = new PreviewService();
	}
	
	public PreviewModel getModel() {
		return model;
	}

	public PreviewView getView() {
		return view;
	}

	public Path getStartPath() {
		return startPath;
	}
	
	private ProgressWorker getWorker() {
		return worker;
	}
	
	private void setWorker(ProgressWorker worker) {
		this.worker = worker;
	}

	public void start(String startPath) {
		try {
			this.startPath = TimestampFormatter.isValidPath(startPath);
			init();
			getView().getPreviewDialog().open();
		} catch(IllegalArgumentException e) {
			JOptionPane.showMessageDialog(
					getView().getPreviewDialog(), 
					e.getMessage(), 
					"Invalid path", 
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}
	
	public void stop() {
		LOG.info("Stopping and cleaning up...");
		try {
			if(WriterFactory.isInitialized()) {
				LOG.info("Closing writers...");
				WriterFactory.getInstance().close();
			}
			
			if(ReaderFactory.isInitialized()) {
				LOG.info("Closing readers...");
				ReaderFactory.getInstance().close();
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}
	
	private void init() {
		String title = startPath.toString().concat(" - ").concat(R.string.APP_TITLE);
		initPreviewDialog(title);
		initTabSettings();
		initTabFiles();
		initTabStatistics();
		initTabAbout();
		initFooter();
		initScanningDialog(title);
	}
	
	private void initPreviewDialog(String title) {
		PreviewDialog preview = view.getPreviewDialog();
		preview.setTitle(title);
	}
	
	/**
	 * Settings tab
	 */
	private void initTabSettings() {
		PreviewBody body = view.getPreviewDialog().getBody();
		
//		RepairActionDialog repairActionDialog = view.getRepairActionDialog();
		TabSettings actions = body.getSettings(); //repairActionDialog.getActions();
		
		SettingsSource sources = actions.getSources();
		JCheckBox[] actionsGroup = sources.getSources();
		
		for(int i=0; i < actionsGroup.length; i++) {
			JCheckBox action = actionsGroup[i];
			
			SourceEnum repair = SourceEnum.valueOf(action.getName());
			
			ButtonModel sourceModel = model.getSourceModel().get(repair);
			
//			SettingsSourceAction repairAction = new SettingsSourceAction(sources, repair);
//			sourceModel.addActionListener(repairAction);
//			repairModel.addChangeListener(new FileRepairFallbackChangeListener());
			
			if(SourceEnum.CUSTOM == repair) {
				sourceModel.addItemListener(new SettingsSourceCustomItemListener(sources.getCustomDateTime().getDateTime()));
			}
			action.setModel(sourceModel);	
		}
		
		model.getSourceModel().get(SourceEnum.METADATA).setSelected(true);
		model.getSourceModel().get(SourceEnum.FILE_SYSTEM).setSelected(false);
		model.getSourceModel().get(SourceEnum.CUSTOM).setSelected(false);
		
//		JTextField customDateTime = actionButtons.getCustomDateTime();
//		customDateTime.setDocument(model.getDateTimeDocument());
//		try {
//			model.getDateTimeDocument().insertString(0, "999999", null);
//		} catch (BadLocationException e1) {
//			LOG.error(e1.toString());
//		}
//		SwingUtilities.invokeLater(() -> customDateTime.requestFocusInWindow());
//		actionButtons.getCustomDateTime().getYear().setModel(model.getCustomDateTimeYearModel());
//		actionButtons.getCustomDateTime().getMonth().setModel(model.getCustomDateTimeMonthModel());
//		actionButtons.getCustomDateTime().getDay().setModel(model.getCustomDateTimeDayModel());
		sources.getCustomDateTime().getDateTime().setModel(model.getCustomDateTimeModel());
		sources.getCustomDateTime().getDateTime().setEditor(
				new JSpinner.DateEditor(sources.getCustomDateTime().getDateTime(), "yyyy-MM-dd HH:mm:ss"));
		sources.getCustomDateTime().getDateTime().setEnabled(model.getSourceModel().get(SourceEnum.CUSTOM).isSelected());
		
		SettingsCorrections corrections = actions.getCorrections();
		for(CorrectionEnum c: CorrectionEnum.values()) {
			ButtonModel m = model.getCorrectionModel().get(c);
			corrections.getCorrections().get(c).setModel(m);
			m.setSelected(true);
		}
		
		JButton backup = actions.getBackup();
		backup.addActionListener(new BackupAction(
				R.string.ACTION_BACKUP, 
				R.string.ACTION_BACKUP, 
        		KeyEvent.VK_B,
        		KeyStroke.getKeyStroke((char)KeyEvent.VK_B),
        		this));
		backup.setModel(model.getBackupModel());
		
		JButton restore = actions.getRestore();
		restore.addActionListener(new RestoreAction(
				R.string.ACTION_RESTORE, 
				R.string.ACTION_RESTORE, 
        		KeyEvent.VK_R,
        		KeyStroke.getKeyStroke((char)KeyEvent.VK_R),
        		this));
		restore.setModel(model.getRestoreModel());
		updateRestoreState();
		
		JButton verify = actions.getVerify();
		verify.addActionListener(new VerifyAction(
				R.string.ACTION_VERIFY,
				R.string.ACTION_VERIFY,
				KeyEvent.VK_V,
				KeyStroke.getKeyStroke((char)KeyEvent.VK_V),
        		this));
		verify.setModel(model.getVerifyModel());
				
		JButton analyze = actions.getAnalyze();
		analyze.addActionListener(new CheckAction(
				R.string.ACTION_ANALYZE, 
				R.string.ACTION_ANALYZE, 
        		KeyEvent.VK_A,
        		KeyStroke.getKeyStroke((char)KeyEvent.VK_A),
        		this));
		
//		RepairActionEnum selectedAction = RepairActionEnum.valueOf(actionsGroup.getSelection().getActionCommand());
//		actions.getDescription().setText(selectedAction.getDescription());
		
		PreviewFooterButtons footerButtons = view.getPreviewDialog().getFooter().getButtons();
		JCheckBox[] correctionsGroup = corrections.getCorrections().values().toArray(new JCheckBox[0]);
		SettingsItemListener settingsItemListener = new SettingsItemListener(
				actionsGroup, 
				correctionsGroup,
				footerButtons.getOk(),
				footerButtons.getApply());
		
		for(JCheckBox cb : actionsGroup) {
			cb.addChangeListener(settingsItemListener);
		}
		
		for(JCheckBox cb : correctionsGroup) {
			cb.addChangeListener(settingsItemListener);
		}
		
	}
	
	/**
	 * Files tab
	 */
	private void initTabFiles() {
		PreviewBody body = view.getPreviewDialog().getBody();
		
		// Files tab
		TabFiles files = body.getFiles();
		FileListScrollPane fileList = files.getFileList();
		FileList table = fileList.getTable();
		table.setModel(this.model.getTableModel());
		table.resizeColumns();
		
		// Table popup context menu
		FileListPopupMenu popupMenu = table.getPopupMenu();
		ActionListener rightClickListener = new FileListTableContextMenuAction(model, table, popupMenu);
		
		popupMenu.getMenuItemAdd().addActionListener(rightClickListener);
		popupMenu.getMenuItemRemove().addActionListener(rightClickListener);
		popupMenu.getMenuItemRemoveAll().addActionListener(rightClickListener);
	}
	
	/**
	 * Statistics tab
	 */
	private void initTabStatistics() {
		PreviewBody body = view.getPreviewDialog().getBody();
		PreviewTabStatistics statistics = body.getStatistics();

		PreviewStatisticsGraph graph = statistics.getGraph();
		PreviewStatisticsGraphChartType chartType = graph.getChartType();
		PreviewStatisticsGraphStack graphStack = graph.getGraphStack();
		chartType.getDataFile().setAction(new FileToggleAction(graphStack, model.getFileStatisticsTableModel()));
		chartType.getDataMetadata().setAction(new MetadataToggleAction(graphStack, model.getMetadataStatisticsTableModel()));
		chartType.getDataExtension().setAction(new ExtensionToggleAction(graphStack, model.getExtensionStatisticsTableModel()));
		chartType.getButtonTable().setAction(new RawTableToggleAction(graphStack));
		chartType.getButtonBar().setAction(new BarChartToggleAction(graphStack));
		chartType.getButtonPie().setAction(new PieChartToggleAction(graphStack));

		chartType.getDataFile().setSelected(true);
		chartType.getButtonTable().setSelected(true);
		graphStack.getRawTable().getScrollTable().getTable().setModel(model.getFileStatisticsTableModel());
		ChartActions.updateChartsFiles(graphStack.getBarChart(), graphStack.getPieChart());

		//	ChartActions.fillRandomData(graphStack.getBarChart().getDataset());
		//	ChartActions.fillRandomData(graphStack.getPieChart().getDataset());

		//	PreviewStatisticsText text = statistics.getText();

		//	text.getFiles().getTable().setModel(model.getFileStatisticsTableModel());
		//	text.getMetadata().getTable().setModel(model.getMetadataStatisticsTableModel());
		//	text.getExtension().getTable().setModel(model.getExtensionStatisticsTableModel());
		//	StatisticsTabChangeAction statisticsTabOnChange = new StatisticsTabChangeAction(graphStack, text);
		//	text.getStatistics().addChangeListener(statisticsTabOnChange);
	}
	
	/**
	 * Statistics tab
	 */
	private void initTabAbout() {
		
	}
	
	/**
	 * Footer
	 */
	private void initFooter() {
//		RepairFooter repairFooter = repairActionDialog.getFooter();
//		repairFooter.getButtons().getOk().addActionListener(new RepairOkAction(
//				"OK", 
//        		"OK", 
//        		KeyEvent.VK_ENTER,
//        		KeyStroke.getKeyStroke((char)KeyEvent.VK_ENTER),
//        		view.getRepairActionDialog()));
//		repairFooter.getButtons().getCancel().addActionListener(new RepairCancelAction(
//				"Cancel", 
//        		"Cancel", 
//        		KeyEvent.VK_ENTER,
//        		KeyStroke.getKeyStroke((char)KeyEvent.VK_ENTER),
//        		view.getRepairActionDialog()));
		
		
		
		PreviewFooter footer = view.getPreviewDialog().getFooter();
		PreviewFooterButtons footerButtons = footer.getButtons();
		footerButtons.getOk().setAction(new OkAction(
        		"OK", 
        		"OK", 
        		KeyEvent.VK_ENTER,
        		KeyStroke.getKeyStroke((char)KeyEvent.VK_ENTER),
        		this));
		footerButtons.getCancel().setAction(new PreviewCancelAction(
        		"Cancel",
        		"Cancel",
        		KeyEvent.VK_ESCAPE,
        		KeyStroke.getKeyStroke((char)KeyEvent.VK_ESCAPE),
        		this));
		footerButtons.getApply().setAction(new ApplyAction(
        		"Apply",
        		"Apply",
        		KeyEvent.VK_A,
        		KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK),
        		this));
	}
	
	private void initScanningDialog(String title) {
		ScanningDialog scanning = view.getScanningDialog();
		scanning.setTitle(title);
		scanning.getProgress().setModel(getModel().getScanningProgressModel());
		scanning.getCancel().setAction(new ScanningCancelAction(
				"Cancel",
				"Cancel",
				KeyEvent.VK_ESCAPE,
				KeyStroke.getKeyStroke((char)KeyEvent.VK_ESCAPE),
				scanning,
				this));
	}
	/*
	private String formatPathAsTruncatedString(Path p) {
		final int MAX = 64;
		final String PLACEHOLDER = "...";
		
		String o = p.toString();
		
		StringBuilder sb = new StringBuilder();
		String b;
		if(o.length() > MAX) {
			sb.append(PLACEHOLDER);
			int s = Math.max(0, o.length()-MAX+PLACEHOLDER.length());
			b = o.substring(s, o.length());
		}
		else {
			b = o;
		}
		
		sb.append(b);
		
		String file = p.getFileName().toString(); //sb.toString();
		LOG.trace("Setting dialog file = {}", file);
		return file;
	}
	*/
	
	public void backup() {
		run(CorrectionMode.BACKUP_ATTRIBUTES);
	}
	
	public void restore() {		
		run(CorrectionMode.RESTORE_ATTRIBUTES);
	}
	
	public void verify() {
		run(CorrectionMode.VERIFY_ATTRIBUTES);
	}
	
	public void analyze() {
		run(CorrectionMode.ANALYZE);
	}
	
	public void repair() {
		run(CorrectionMode.SAVE_ATTRIBUTES);
	}
	
	private void run(CorrectionMode mode) {		
		getModel().getTableModel().clear();
		getView().getScanningDialog().open();
		
		STATS.reset();
		getModel().getExtensionStatisticsTableModel().clear();
		getModel().getMetadataStatisticsTableModel().clear();
		getModel().getFileStatisticsTableModel().clear();
		
		Map<SourceEnum, ButtonModel> sm = getModel().getSourceModel();
		Map<CorrectionEnum, ButtonModel> cm = getModel().getCorrectionModel();
		
		Map<SourceEnum, Boolean> sources = new HashMap<>();
		for(SourceEnum s : SourceEnum.values()) {
			sources.put(s, Boolean.valueOf(sm.get(s).isSelected()));
		}
		
		Map<CorrectionEnum, Boolean> corrections = new HashMap<>();
		for(CorrectionEnum c : CorrectionEnum.values()) {
			corrections.put(c, Boolean.valueOf(cm.get(c).isSelected()));
		}
		
		Date custom = getModel().getCustomDateTimeModel().getDate();
		
		// Hook the progress bar to the worker.
		getModel().getScanningProgressModel().setMinimum(0);
		getModel().getScanningProgressModel().setMaximum(100);
		
		PropertyChangeListener propertyChangeListener = new ProgressPropertyChangeListener();
		
		ProgressWorker worker = service.run(
				getStartPath(),
				mode, 
				sources, 
				corrections, 
				custom,
				propertyChangeListener);
		
		setWorker(worker);
	}
	
	private class ProgressPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if(PROPERTY_STATE.equals(evt.getPropertyName())) {
				StateValue state = (StateValue) evt.getNewValue();
				if(StateValue.DONE.equals(state)) {
					if(getWorker() != null && getWorker().isDone())
						done();
				}
			}
			else if (PROPERTY_PROGRESS.equals(evt.getPropertyName())) {
				int progress = (Integer)evt.getNewValue();
				getModel().getScanningProgressModel().setValue(progress);
			}
			else if (PROPERTY_VISITS.equals(evt.getPropertyName())) {
				FileVisit visit = (FileVisit) evt.getNewValue();
				
				getView().getScanningDialog().setProgress(visit.getProgress());
				getView().getScanningDialog().getAction().setText(visit.getFile().getFileName().toString());
			}
			else if(PROPERTY_BEAN.equals(evt.getPropertyName())) {
				FileBean update = (FileBean) evt.getNewValue();
				getModel().getTableModel().addRow(update);
			}
		}
	}
	
	public boolean cancelScanning() {
		boolean cancelled = false;
		if(getWorker() != null && !getWorker().isCancelled() && !getWorker().isDone()) {
			getWorker().cancel(false);
			cancelled = true;
		}
		return cancelled;
	}
	
	// Update statistics
	
	private void updateStatistics() {
		STATS.getStatistics().forEach((name, count) -> {
//			printKeyValueColumns(name, count);
			model.getFileStatisticsTableModel().addRow(
					new FileStatisticsBean(name.toString(), count));
		});
		
		
//		printKeyValueColumns("Dir  Found", STATS.get(FileEvent.DIRECTORY_FOUND));
//		printKeyValueColumns("File Found", STATS.get(FileEvent.FILE_FOUND));
//		printKeyValueColumns("File Error", STATS.get(FileEvent.FILE_ERROR));
//		printKeyValueColumns("Fixed Creation", STATS.get(FileEvent.CORRECTED_CREATION));
//		printKeyValueColumns("Fixed Modified", STATS.get(FileEvent.CORRECTED_MODIFIED));
//		printKeyValueColumns("Fixed Accessed", STATS.get(FileEvent.CORRECTED_ACCESSED));
		
		STATS.getMetadataKeys().forEach((name, count) -> {
//			printKeyValueColumns(name, count);
			String parser, tag;
			String[] pair = name.split(":", 2);
			parser = pair[0];
			tag = pair[1];
			model.getMetadataStatisticsTableModel().addRow(
					new MetadataStatisticsBean(parser, tag, count));
		});
		
		STATS.getFileExtensions().forEach((extension, count) -> {
//			printKeyValueColumns(extension, count);
			model.getExtensionStatisticsTableModel().addRow(
					new ExtensionStatisticsBean(extension, count.getTotal(), count.getHasMetadata()));
		});
		
		PreviewStatisticsGraphStack graphStack = view.getPreviewDialog().getBody().getStatistics().getGraph().getGraphStack();
		ChartActions.updateChartsFiles(graphStack.getBarChart(), graphStack.getPieChart());
	}
	
	private void updateForecastStatistics() {
		TabSettings settings = this.getView().getPreviewDialog().getBody().getSettings();
		BarChart barChart = settings.getStatistics();
		ChartActions.updateForecastedCorrections(barChart);
	}
	
	private void updateRestoreState() {
		boolean restorable = service.isRestorable(getStartPath());
		
		DefaultButtonModel backup = model.getBackupModel();
		backup.setEnabled(!restorable);
		
		DefaultButtonModel restore = model.getRestoreModel();
		restore.setEnabled(restorable);
		
		DefaultButtonModel verify = model.getVerifyModel();
		verify.setEnabled(restorable);
	}
	
	public void done() {		
		if(getWorker().isDone()) {
			BoundedRangeModel m = getModel().getScanningProgressModel();
			
			if(!m.getValueIsAdjusting())
				m.setValue(m.getMaximum());
			else
				LOG.warn("Progress bar value is adjusting.");
			
			getView().getScanningDialog().close();
			
			stop();
			setWorker(null);
			updateRestoreState();
		}
		
		//controller.getView().getPreviewDialog().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		updateStatistics();
		updateForecastStatistics();

		/*
		LOG.info("Metadata: {}, Last Modified: {}, Custom: {}",
				STATS.get(SelectionEvent.METADATA),
				STATS.get(SelectionEvent.LAST_MODIFIED),
				STATS.get(SelectionEvent.CUSTOM));
		*/
	}
}

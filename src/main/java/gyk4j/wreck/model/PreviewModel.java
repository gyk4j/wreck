package gyk4j.wreck.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoundedRangeModel;
import javax.swing.ButtonModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.JToggleButton;
import javax.swing.SpinnerDateModel;

import gyk4j.wreck.beans.AboutBean;
import gyk4j.wreck.beans.ExtensionStatisticsBean;
import gyk4j.wreck.beans.FileBean;
import gyk4j.wreck.beans.FileStatisticsBean;
import gyk4j.wreck.beans.MetadataStatisticsBean;
import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.SourceEnum;

public class PreviewModel {
	
//	private static final Pattern DIGITS = Pattern.compile("^[0-9]+$");
	
	private final SampleTableModel<FileBean> tableModel;
	private final ButtonModel metadataModel;
	private final Map<SourceEnum, ButtonModel> sourceModel;
//	private final Document dateTimeDocument;
//	private final SpinnerNumberModel customDateTimeYearModel;
//	private final SpinnerNumberModel customDateTimeMonthModel;
//	private final SpinnerNumberModel customDateTimeDayModel;
	private final SpinnerDateModel customDateTimeModel;
	
	private final Map<CorrectionEnum, ButtonModel> correctionModel;
	
	private final DefaultButtonModel backupModel;
	private final DefaultButtonModel restoreModel;
	private final DefaultButtonModel verifyModel;
	
	private final SampleTableModel<FileStatisticsBean> fileStatisticsTableModel;
	private final SampleTableModel<MetadataStatisticsBean> metadataStatisticsTableModel;
	private final SampleTableModel<ExtensionStatisticsBean> extensionStatisticsTableModel;
	
	private final DefaultListModel<AboutBean> aboutModel;
	
	private final BoundedRangeModel scanningProgressModel;
	
	public PreviewModel() {
		this.tableModel = new SampleTableModel<FileBean>(FileBean.class);
		this.metadataModel = new JToggleButton.ToggleButtonModel();
		this.sourceModel = new HashMap<SourceEnum, ButtonModel>();
		this.correctionModel = new EnumMap<CorrectionEnum, ButtonModel>(CorrectionEnum.class);

		for(SourceEnum repair : SourceEnum.values()) {
			DefaultButtonModel m = new JToggleButton.ToggleButtonModel();
			m.setActionCommand(repair.toString());
			this.sourceModel.put(repair, m);
		}
		
//		this.dateTimeDocument = new PlainDocument();
//		this.dateTimeDocument.putProperty("name", "dateTimeDocument");
//		((AbstractDocument) this.dateTimeDocument).setDocumentFilter(new DocumentFilter() {
//
//			@Override
//			public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
//				LOG.trace("Removed: " + fb.getDocument().getText(offset, length));
//				super.remove(fb, offset, length);
//			}
//
//			@Override
//			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
//					throws BadLocationException {
//				if(DIGITS.matcher(string).matches()) {
//					LOG.trace("Added: " + string);
//					super.insertString(fb, offset, string, attr);
//				}
//			}
//
//			@Override
//			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
//					throws BadLocationException {
//				if(DIGITS.matcher(text).matches()) {
//					if (offset >= fb.getDocument().getLength()) {
//						LOG.trace("Added: " + text);
//					} else {
//						String old = fb.getDocument().getText(offset, length);
//						LOG.trace("Replaced " + old + " with " + text);
//					}
//					super.replace(fb, offset, length, text, attrs);
//				}
//			}
//			
//		});
//		LocalDate today = LocalDate.now();
//		this.customDateTimeYearModel = new SpinnerNumberModel(
//				today.getYear(),
//				today.minusYears(100).getYear(),
//				today.plusYears(100).getYear(),
//				1);
//		this.customDateTimeMonthModel = new SpinnerNumberModel(
//				today.getMonthValue(),
//				1,
//				12,
//				1);
//		this.customDateTimeDayModel = new SpinnerNumberModel(
//				today.getDayOfMonth(),
//				1,
//				31,
//				1);
		
		
		ZonedDateTime zdt = LocalDateTime
				.now()
				.atZone(ZoneOffset.systemDefault());

		Date now = Date.from(zdt.toInstant());
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -100);
		Date min = calendar.getTime();
		
		calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 100);
		Date max = calendar.getTime();
		
		
		this.customDateTimeModel = new SpinnerDateModel(now, min, max, Calendar.DAY_OF_MONTH);
		
		for(CorrectionEnum c : CorrectionEnum.values()) {
			correctionModel.put(c, new JToggleButton.ToggleButtonModel());
		}
		
		this.backupModel = new DefaultButtonModel();
		this.restoreModel = new DefaultButtonModel();
		this.verifyModel = new DefaultButtonModel();
		
		this.fileStatisticsTableModel = new SampleTableModel<FileStatisticsBean>(FileStatisticsBean.class);
		this.metadataStatisticsTableModel = new SampleTableModel<MetadataStatisticsBean>(MetadataStatisticsBean.class);
		this.extensionStatisticsTableModel = new SampleTableModel<ExtensionStatisticsBean>(ExtensionStatisticsBean.class);
		
		this.aboutModel = new DefaultListModel<>();
		this.scanningProgressModel = new DefaultBoundedRangeModel();
		
		/*
		this.dateTimeDocument.addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateLog(e, "inserted into");
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateLog(e, "removed from");
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				//Plain text components do not fire these events
			}
			
			String newline = System.lineSeparator();
			public void updateLog(DocumentEvent e, String action) {
		        Document doc = (Document)e.getDocument();
		        int changeLength = e.getLength();
		        LOG.trace(
		            changeLength + " character" +
		            ((changeLength == 1) ? " " : "s ") +
		            action + " " + doc.getProperty("name") + "." + newline +
		            "  Text length = " + doc.getLength() + newline);
		    }
			
		});
		*/
	}

	public SampleTableModel<FileBean> getTableModel() {
		return tableModel;
	}

//	public Document getDateTimeDocument() {
//		return dateTimeDocument;
//	}

	public SampleTableModel<FileStatisticsBean> getFileStatisticsTableModel() {
		return fileStatisticsTableModel;
	}

	public SampleTableModel<MetadataStatisticsBean> getMetadataStatisticsTableModel() {
		return metadataStatisticsTableModel;
	}
	
	public SampleTableModel<ExtensionStatisticsBean> getExtensionStatisticsTableModel() {
		return extensionStatisticsTableModel;
	}

	public ButtonModel getMetadataModel() {
		return metadataModel;
	}

	public Map<SourceEnum, ButtonModel> getSourceModel() {
		return sourceModel;
	}

//	public SpinnerNumberModel getCustomDateTimeYearModel() {
//		return customDateTimeYearModel;
//	}
//
//	public SpinnerNumberModel getCustomDateTimeMonthModel() {
//		return customDateTimeMonthModel;
//	}
//
//	public SpinnerNumberModel getCustomDateTimeDayModel() {
//		return customDateTimeDayModel;
//	}
	
	public SpinnerDateModel getCustomDateTimeModel() {
		return customDateTimeModel;
	}
	
	public Map<CorrectionEnum, ButtonModel> getCorrectionModel() {
		return correctionModel;
	}
	
	public DefaultButtonModel getBackupModel() {
		return backupModel;
	}
	
	public DefaultButtonModel getRestoreModel() {
		return restoreModel;
	}
	
	public DefaultButtonModel getVerifyModel() {
		return verifyModel;
	}

	public DefaultListModel<AboutBean> getAboutModel() {
		return aboutModel;
	}
	
	public BoundedRangeModel getScanningProgressModel() {
		return scanningProgressModel;
	}
}

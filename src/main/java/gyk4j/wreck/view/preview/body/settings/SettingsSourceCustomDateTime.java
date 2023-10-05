package gyk4j.wreck.view.preview.body.settings;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;

public class SettingsSourceCustomDateTime extends JPanel {

	private static final long serialVersionUID = 1L;
	
//	private final JSpinner year;
//	private final JSpinner month;
//	private final JSpinner day;
	
	private final JSpinner dateTime;

	public SettingsSourceCustomDateTime() {
		super();
//		this.year = new JSpinner();
//		this.year.setToolTipText("Year in yyyy format.");
//		this.month = new JSpinner();
//		this.month.setToolTipText("Month in MM format.");
//		this.day = new JSpinner();
//		this.day.setToolTipText("Month in dd format.");
		dateTime = new JSpinner();
		dateTime.setToolTipText("Date time");
		
		setLayout(new FlowLayout(FlowLayout.LEADING));
//		setBorder(R.style.BORDER_EMPTY_8_LEFT);
		setMaximumSize(new Dimension(200, 32));
		
//		add(year);
//		add(month);
//		add(day);
		
		add(dateTime);
	}

//	public JSpinner getYear() {
//		return year;
//	}
//
//	public JSpinner getMonth() {
//		return month;
//	}
//
//	public JSpinner getDay() {
//		return day;
//	}
	
	public JSpinner getDateTime() {
		return dateTime;
	}
}

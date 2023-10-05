package gyk4j.wreck.view.preview.renderer;

import java.awt.Color;
import java.awt.Component;
import java.time.Period;

import javax.swing.JTable;
import javax.swing.SwingConstants;

public class PeriodRenderer extends GenericRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		Period period = (Period) value;
		
		String text = (period.isZero())? "": 
			String.format("%02d-%02d-%02d", 
					Math.abs(period.getYears()), 
					Math.abs(period.getMonths()), 
					Math.abs(period.getDays()));
		setText(text);
		
		if(!period.isZero())
			this.setForeground((period.isNegative())? Color.RED: Color.ORANGE);
		
		setHorizontalAlignment(SwingConstants.LEADING);
		return this;
	}
}

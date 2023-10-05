package gyk4j.wreck.view.preview.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public abstract class GenericRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	
	private static final int BORDER_SIZE = 4;
	private static final Border BORDER = BorderFactory.createEmptyBorder(
			BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setBorder(BORDER);
		
		setOpaque(true);
		setForeground((isSelected)? table.getSelectionForeground(): Color.BLACK);
		setBackground((isSelected)? table.getSelectionBackground(): Color.WHITE);
		setHorizontalAlignment(SwingConstants.LEADING);
		return this;
	}

}

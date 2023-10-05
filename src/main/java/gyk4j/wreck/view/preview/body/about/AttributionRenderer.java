package gyk4j.wreck.view.preview.body.about;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import gyk4j.wreck.beans.AboutBean;
import gyk4j.wreck.resources.R;
import gyk4j.wreck.util.ResourceLoader;

class AttributionRenderer extends JLabel implements ListCellRenderer<AboutBean> {

	private static final long serialVersionUID = 1L;

	// This is the only method defined by ListCellRenderer.
	// We just reconfigure the JLabel each time we're called.

	public Component getListCellRendererComponent(
			JList<? extends AboutBean> list,           // the list
			AboutBean value,            // value to display
			int index,               // cell index
			boolean isSelected,      // is the cell selected
			boolean cellHasFocus)    // does the cell have focus
	{
		setToolTipText(value.getTooltip());
		setText(value.getAttribution());
		
		if(value.getIcon() != null) {
			Icon icon = ResourceLoader.getIcon(value.getIcon());
			setIcon(icon);
		}
		
//		if (isSelected) {
//			setBackground(list.getSelectionBackground());
//			setForeground(list.getSelectionForeground());
//		} 
//		else {
//			setBackground(list.getBackground());
//			setForeground(list.getForeground());
//		}
		
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(false);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setBorder(R.style.BORDER_EMPTY_4);
		
		return this;
	}
}

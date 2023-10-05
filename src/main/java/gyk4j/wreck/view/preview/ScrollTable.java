package gyk4j.wreck.view.preview;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import gyk4j.wreck.resources.R;

public class ScrollTable extends JScrollPane {
	
	private static final long serialVersionUID = 1L;
	
	private final JTable table;

	public ScrollTable() {
		super();
		
//		setBorder(R.style.BORDER_BEVEL_LOWER_SOFT);
		table = new SampleTable();
		setViewportView(table);
	}
	
	public ScrollTable(int border) {
		this();
		
		setBorder(R.style.BORDER_BEVEL_LOWER_EMPTY_8);
	}

	public JTable getTable() {
		return table;
	}
}

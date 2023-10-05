package gyk4j.wreck.view.preview.body.files;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

public class FileListPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	private final JMenuItem menuItemAdd = new JMenuItem("Add New Row");
	private final JMenuItem menuItemRemove = new JMenuItem("Remove Current Row");
	private final JMenuItem menuItemRemoveAll = new JMenuItem("Remove All Rows");

	public FileListPopupMenu(JTable parent) {
		super();

		add(menuItemAdd);
		add(menuItemRemove);
		add(menuItemRemoveAll);
	}

	public JMenuItem getMenuItemAdd() {
		return menuItemAdd;
	}

	public JMenuItem getMenuItemRemove() {
		return menuItemRemove;
	}

	public JMenuItem getMenuItemRemoveAll() {
		return menuItemRemoveAll;
	}
}

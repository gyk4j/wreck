package gyk4j.wreck.controller.action.files;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import gyk4j.wreck.model.PreviewModel;
import gyk4j.wreck.view.preview.body.files.FileList;
import gyk4j.wreck.view.preview.body.files.FileListPopupMenu;

public class FileListTableContextMenuAction implements ActionListener {
	private final PreviewModel model;
	private final FileList table;
	private final FileListPopupMenu popupMenu;
	
	public FileListTableContextMenuAction(PreviewModel model, FileList table, FileListPopupMenu popupMenu) {
		this.model = model;
		this.table = table; 
		this.popupMenu = popupMenu;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem menu = (JMenuItem) e.getSource();
        if (menu == popupMenu.getMenuItemAdd()) {
        	ContextMenuActions.addNewRow(model.getTableModel());
        } else if (menu == popupMenu.getMenuItemRemove()) {
        	ContextMenuActions.removeCurrentRow(model.getTableModel(), table.getSelectedRow());
        } else if (menu == popupMenu.getMenuItemRemoveAll()) {
        	ContextMenuActions.removeAllRows(model.getTableModel());
        }		
	}
}

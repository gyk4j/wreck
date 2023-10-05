package gyk4j.wreck.controller.action.files;

import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import gyk4j.wreck.beans.FileBean;
import gyk4j.wreck.model.SampleTableModel;

public class ContextMenuActions {
	
	public static void addNewRow(SampleTableModel<FileBean> tableModel) {
//		Random r = new Random();
		
//		Date now = new Date();
		
		FileBean row = new FileBean(
				Paths.get("C:\\Windows\\notepad.exe"),
				FileTime.from(Instant.EPOCH),
				FileTime.from(Instant.now()),
				FileTime.from(Instant.now().plus(1, ChronoUnit.DAYS)),
				Period.ZERO
//				Long.toString(Math.abs(r.nextLong()), Character.MAX_RADIX),
//				Math.abs(r.nextInt()),
//				Math.abs(r.nextLong()),
//				new Date(Math.abs(r.nextLong()) % (now.getTime() * 2)),
//				r.nextBoolean()
		);
		
        tableModel.addRow(row);
    }
     
    public static void removeCurrentRow(SampleTableModel<FileBean> tableModel, int selectedRow) {
//        int selectedRow = table.getSelectedRow();
        tableModel.removeRow(selectedRow);
    }
     
    public static void removeAllRows(SampleTableModel<FileBean> tableModel) {
        int rowCount = tableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            tableModel.removeRow(0);
        }
    }
}

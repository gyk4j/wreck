package gyk4j.wreck.resources;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class R {
	public static class string {
		public static final String APP_TITLE = "WRECK";
		public static final String PROPERTY_STATE = "state";
		public static final String PROPERTY_PROGRESS = "progress";
		public static final String PROPERTY_VISITS = "visits";
		public static final String PROPERTY_BEAN = "bean";
		public static final String SKIP_DESKTOP_INI = "desktop.ini";
		public static final String _7Z_LAST_MODIFIED_TIME = "7z:lastModifiedTime";
		public static final String _7Z_CREATION_TIME = "7z:creationTime";
		public static final String PATH_NAME = "path:name";
		public static final String FILES= "Files";
		public static final String STATISTICS = "Statistics";
		public static final String ERROR = "Error";
		public static final String OK = "OK";
		public static final String STARTING = "Starting";
		public static final String EMPTY = "";
		public static final String FORECASTED_CORRECTIONS = "Forecasted Corrections";
		public static final String TIMESTAMPS = "Timestamps";
		public static final String FILE_COUNT = "File Count";
		public static final String CREATION = "Creation";
		public static final String LAST_MODIFIED = "Last Modified";
		public static final String LAST_ACCESSED = "Last Accessed";
		public static final String BACKUP_CREATION_TIME = "backup:creationTime";
		public static final String BACKUP_LAST_MODIFIED_TIME = "backup:lastModifiedTime";
		public static final String BACKUP_LAST_ACCESSED_TIME = "backup:lastAccessedTime";
		public static final String FS_CREATION = "fs:creation";
		public static final String FS_MODIFIED = "fs:modified";
		public static final String USER_CUSTOM_DATE_TIME = "user:customDateTime";
		public static final String ZIP_LAST_MODIFIED_TIME = "zip:lastModifiedTime";
		public static final String CSV_CREATION = "csv:creation";
		public static final String CSV_MODIFIED = "csv:modified";
		public static final String CSV_ACCESSED = "csv:accessed";
		public static final String DIR_EARLIEST = "dir:earliest";
		public static final String DIR_LATEST = "dir:latest";
		public static final String LOG_FILE_NAME = "META_INF.CSV";
		public static final String ACTION_ANALYZE = "Analyze";
		public static final String ACTION_BACKUP = "Backup";
		public static final String ACTION_RESTORE = "Restore";
		public static final String ACTION_VERIFY = "Verify";
	}
	
	public static class integer {
		public static final int BORDER_4 = 4;
		public static final int BORDER_8 = 8;
		public static final int BORDER_16 = 16;
		public static final int BORDER_32 = 32;
	}
	
	public static class icon {
		public static final String APP = "app.png";
	}
	
	public static class style {		
		public static final Border BORDER_DEBUG = BorderFactory.createLineBorder(Color.CYAN, 2);
		public static final Border BORDER_BEVEL_LOWER_SOFT = BorderFactory.createLoweredSoftBevelBorder();
		public static final Border BORDER_EMPTY_4 = BorderFactory.createEmptyBorder(
				R.integer.BORDER_4, R.integer.BORDER_4, R.integer.BORDER_4, R.integer.BORDER_4);
		public static final Border BORDER_EMPTY_8 = BorderFactory.createEmptyBorder(
				R.integer.BORDER_8, R.integer.BORDER_8, R.integer.BORDER_8, R.integer.BORDER_8);
		public static final Border BORDER_FORECAST = BorderFactory.createEmptyBorder(
				R.integer.BORDER_32, 0, R.integer.BORDER_32, 0);
		public static final Border BORDER_EMPTY_8_LEFT = BorderFactory.createEmptyBorder(
				0, R.integer.BORDER_8, 0, 0);
		public static final Border BORDER_BEVEL_LOWER_EMPTY_8 = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(
						R.integer.BORDER_8, R.integer.BORDER_8, R.integer.BORDER_8, R.integer.BORDER_8),
				BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
	
	public static class dimen {
		public static final int FRAME_WIDTH = 480;
		public static final int FRAME_HEIGHT = 640;
		public static final int DIALOG_WIDTH = 360;
		public static final int DIALOG_HEIGHT = 512;
		public static final int TABLE_ROW_HEIGHT = 20;
	}
	
	public static class color {
		public static final Color TABLE_GRID_COLOR = new Color(0xF0, 0xF0, 0xF0);
		public static final Color CORRECTION_REQUIRED = new Color(210, 202, 0);
		public static final Color CORRECTION_NON_REQUIRED = new Color(0, 211, 40);
		public static final Color CORRECTION_NO_METADATA = new Color(210, 0, 0);
	}
}

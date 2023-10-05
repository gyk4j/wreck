package gyk4j.wreck.io.writer;

public class WriterFactory implements AutoCloseable {
	
	private static WriterFactory INSTANCE = null;
	
	private final AnalyzeWriter analyzeWriter;
	private final BasicFileAttributesWriter basicFileAttributesWriter;
	private final CsvLogWriter csvLogWriter;
	private final RestoreBasicFileAttributesWriter restoreBasicFileAttributesWriter;
	
	public final static boolean isInitialized() {
		return INSTANCE != null;
	}
	
	private WriterFactory() {
		analyzeWriter = new AnalyzeWriter();
		basicFileAttributesWriter = new BasicFileAttributesWriter();
		csvLogWriter = new CsvLogWriter();
		restoreBasicFileAttributesWriter = new RestoreBasicFileAttributesWriter();
	}
	
	public static WriterFactory getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new WriterFactory();
		}
		return INSTANCE;
	}
	
	@Override
	public void close() throws Exception {
		csvLogWriter.close();
		
		INSTANCE = null;
	}
	
	public AnalyzeWriter getAnalyzeWriter() {
		return analyzeWriter;
	}
	
	public BasicFileAttributesWriter getBasicFileAttributesWriter() {
		return basicFileAttributesWriter;
	}

	public CsvLogWriter getCsvLogWriter() {
		return csvLogWriter;
	}
	
	public RestoreBasicFileAttributesWriter getRestoreBasicFileAttributesWriter() {
		return restoreBasicFileAttributesWriter;
	}
}

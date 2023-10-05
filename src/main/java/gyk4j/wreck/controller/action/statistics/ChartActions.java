package gyk4j.wreck.controller.action.statistics;

import java.awt.EventQueue;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.util.SortOrder;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import gyk4j.wreck.resources.CorrectionEnum;
import gyk4j.wreck.resources.CorrectionsChartSeriesEnum;
import gyk4j.wreck.resources.R;
import gyk4j.wreck.util.logging.ExtensionStatistics;
import gyk4j.wreck.util.logging.FileEvent;
import gyk4j.wreck.util.logging.StatisticsCollector;
import gyk4j.wreck.view.preview.body.statistics.ChartSeriesEnum;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraphStack.BarChart;
import gyk4j.wreck.view.preview.body.statistics.PreviewStatisticsGraphStack.PieChart;

public class ChartActions {
	private static final StatisticsCollector STATS = StatisticsCollector.getInstance();
	
	public static void fillRandomData(DefaultCategoryDataset dataset) {
		final String column = "count";
		
		Random random = new Random();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				String row = Integer.toString(dataset.getRowCount());
//				LOG.trace("Update: {}", row);
				dataset.addValue(Math.round(random.nextDouble() * 5.0), row, column);
			}
		};
		new Timer().scheduleAtFixedRate(task, 0, 1000);
	}
	
	public static void fillRandomData(DefaultPieDataset<String> dataset) {
		Random random = new Random();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				String row = Integer.toString(dataset.getItemCount());
//				LOG.trace("Update: {}", row);
				dataset.setValue(row, Math.round(random.nextDouble() * 5.0));
			}
		};
		new Timer().scheduleAtFixedRate(task, 0, 1000);
	}
	
	public static void updateChartsFiles(BarChart bar, PieChart pie) {
		bar.getChart().getChart().setTitle(R.string.FILES);
		CategoryAxis categoryAxis = bar.getChart().getChart().getCategoryPlot().getDomainAxis();
		categoryAxis.setLabel(R.string.STATISTICS);
		pie.getChart().getChart().setTitle(R.string.FILES);
		
		bar.getDataset().clear();
		pie.getDataset().clear();
		
		/*
		data.entrySet().stream().sorted(
				(Map.Entry<FileEvent, Integer> o1, Map.Entry<FileEvent, Integer> o2) -> {
					if(o1.getValue() > o2.getValue())
						return -1;
					else if(o1.getValue() == o2.getValue())
						return 0;
					else
						return 1;
				})
		.forEach(o -> {
			bar.getDataset().addValue(o.getValue(), "Files", o.getKey().toString());
			pie.getDataset().setValue(o.getKey().toString(), o.getValue());
		});
		*/
				
		bar.getDataset().addValue(STATS.get(FileEvent.FILE_FOUND) - STATS.get(FileEvent.FILE_ERROR), ChartSeriesEnum.DONE.getName(), "Files");
		bar.getDataset().addValue(STATS.get(FileEvent.FILE_ERROR), ChartSeriesEnum.SKIPPED.getName(), "Files");

		bar.getDataset().addValue(STATS.get(FileEvent.CORRECTIBLE_CREATION), ChartSeriesEnum.DONE.getName(), "Creation");
		bar.getDataset().addValue(STATS.get(FileEvent.FILE_FOUND) - STATS.get(FileEvent.CORRECTIBLE_CREATION), ChartSeriesEnum.SKIPPED.getName(), "Creation");

		bar.getDataset().addValue(STATS.get(FileEvent.CORRECTIBLE_MODIFIED), ChartSeriesEnum.DONE.getName(), "Last Modified");
		bar.getDataset().addValue(STATS.get(FileEvent.FILE_FOUND) - STATS.get(FileEvent.CORRECTIBLE_MODIFIED), ChartSeriesEnum.SKIPPED.getName(), "Last Modified");

		bar.getDataset().addValue(STATS.get(FileEvent.CORRECTIBLE_ACCESSED), ChartSeriesEnum.DONE.getName(), "Last Accessed");
		bar.getDataset().addValue(STATS.get(FileEvent.FILE_FOUND) - STATS.get(FileEvent.CORRECTIBLE_ACCESSED), ChartSeriesEnum.SKIPPED.getName(), "Last Accessed");

		bar.getDataset().addValue(STATS.get(FileEvent.DIRECTORY_FOUND), ChartSeriesEnum.DONE.getName(), "Directories");

		pie.getDataset().setValue(R.string.ERROR, STATS.get(FileEvent.FILE_ERROR));
		pie.getDataset().setValue(R.string.OK, STATS.get(FileEvent.FILE_FOUND) - STATS.get(FileEvent.FILE_ERROR));

		pie.getDataset().sortByValues(SortOrder.ASCENDING);
	}
	
	public static void updateChartsMetadata(BarChart bar, PieChart pie) {
		Map<String, Integer> data = STATS.getMetadataKeys();
		
		bar.getChart().getChart().setTitle("Metadata");
		CategoryAxis categoryAxis = bar.getChart().getChart().getCategoryPlot().getDomainAxis();
		categoryAxis.setLabel("Tags");
		pie.getChart().getChart().setTitle("Metadata");
		
		bar.getDataset().clear();
		pie.getDataset().clear();
		
		data.entrySet()
		.stream().sorted(
				(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> {
					if(o1.getValue() > o2.getValue())
						return -1;
					else if(o1.getValue() == o2.getValue())
						return 0;
					else
						return 1;
				})
		.forEach((o) -> {
			String[] pair = o.getKey().split(":", 2);
			bar.getDataset().addValue(o.getValue(), "Tags", pair[1]);
			pie.getDataset().setValue(pair[1], o.getValue());
		});
//		pie.getDataset().sortByValues(SortOrder.DESCENDING);
	}
	
	public static void updateChartsExtension(BarChart bar, PieChart pie) {
		Map<String, ExtensionStatistics> data = STATS.getFileExtensions();
		
		bar.getChart().getChart().setTitle("Extension");
		CategoryAxis categoryAxis = bar.getChart().getChart().getCategoryPlot().getDomainAxis();
		categoryAxis.setLabel("Extension");
		pie.getChart().getChart().setTitle("Extension");
		
		bar.getDataset().clear();
		pie.getDataset().clear();
		
		data.values()
		.stream().sorted((ExtensionStatistics o1, ExtensionStatistics o2) -> {
			if(o1.getTotal() > o2.getTotal())
				return -1;
			else if(o1.getTotal() == o2.getTotal())
				return 0;
			else
				return 1;
		})
		.forEach(e -> {
			bar.getDataset().addValue(e.getHasMetadata(), "With Metadata", e.getId());
			bar.getDataset().addValue(e.getTotal() - e.getHasMetadata(), "Missing Metadata", e.getId());
			pie.getDataset().setValue(e.getId(), e.getTotal());
		});
//		pie.getDataset().sortByValues(SortOrder.DESCENDING);
	}
	
	public static void fillForecastedCorrectionsRandomData(DefaultCategoryDataset dataset) {
		TimerTask t = new TimerTask() {
			private final int MAX = 100;
			Random r = new Random();

			@Override
			public void run() {
				EventQueue.invokeLater(() -> {
					dataset.clear();
					
					int required = r.nextInt(MAX);
					int noMetadata = r.nextInt(MAX-required);
					int nonRequired = MAX-required-noMetadata;
					
					dataset.addValue(required, CorrectionsChartSeriesEnum.REQUIRED.getName(), R.string.CREATION);
					dataset.addValue(noMetadata, CorrectionsChartSeriesEnum.NO_METADATA.getName(), R.string.CREATION);
					dataset.addValue(nonRequired, CorrectionsChartSeriesEnum.NON_REQUIRED.getName(), R.string.CREATION);
					
					required = r.nextInt(MAX);
					noMetadata = r.nextInt(MAX-required);
					nonRequired = MAX-required-noMetadata;
					dataset.addValue(required, CorrectionsChartSeriesEnum.REQUIRED.getName(), R.string.LAST_MODIFIED);
					dataset.addValue(noMetadata, CorrectionsChartSeriesEnum.NO_METADATA.getName(), R.string.LAST_MODIFIED);
					dataset.addValue(nonRequired, CorrectionsChartSeriesEnum.NON_REQUIRED.getName(), R.string.LAST_MODIFIED);
					
					required = r.nextInt(MAX);
					noMetadata = r.nextInt(MAX-required);
					nonRequired = MAX-required-noMetadata;
					dataset.addValue(required, CorrectionsChartSeriesEnum.REQUIRED.getName(), R.string.LAST_ACCESSED);
					dataset.addValue(noMetadata, CorrectionsChartSeriesEnum.NO_METADATA.getName(), R.string.LAST_ACCESSED);
					dataset.addValue(nonRequired, CorrectionsChartSeriesEnum.NON_REQUIRED.getName(), R.string.LAST_ACCESSED);
				});
				
			}
			
		};
		
		Timer timer = new Timer();
		timer.schedule(t, 2000, 2000);
	}
	
	public static void updateForecastedCorrections(
			gyk4j.wreck.view.preview.body.settings.TabSettings.BarChart bar) {
		for(CorrectionEnum c : CorrectionEnum.values()) {
			for(CorrectionsChartSeriesEnum s: CorrectionsChartSeriesEnum.values()) {
				int value;
				
				if(CorrectionsChartSeriesEnum.NO_METADATA.equals(s)) {
					switch(c) {
					case CREATION:
						value = STATS.get(FileEvent.UNCORRECTIBLE_CREATION);
						break;
					case MODIFIED:
						value = STATS.get(FileEvent.UNCORRECTIBLE_MODIFIED);
						break;
					case ACCESSED:
						value = STATS.get(FileEvent.UNCORRECTIBLE_ACCESSED);
						break;
					default:
						value = 0;
						break;
					}	
				}
				else if(CorrectionsChartSeriesEnum.REQUIRED.equals(s)){
					switch(c) {
					case CREATION:
						value = STATS.get(FileEvent.CORRECTIBLE_CREATION);
						break;
					case MODIFIED:
						value = STATS.get(FileEvent.CORRECTIBLE_MODIFIED);
						break;
					case ACCESSED:
						value = STATS.get(FileEvent.CORRECTIBLE_ACCESSED);
						break;
					default:
						value = 0;
						break;
					}					
				}
				else if(CorrectionsChartSeriesEnum.NON_REQUIRED.equals(s)) {
					int total = STATS.get(FileEvent.FILE_FOUND);// - STATS.get(FileEvent.FILE_ERROR);
					switch(c) {
					case CREATION:
						value = total
						- STATS.get(FileEvent.CORRECTIBLE_CREATION)
						- STATS.get(FileEvent.UNCORRECTIBLE_CREATION);
						break;
					case MODIFIED:
						value = total 
						- STATS.get(FileEvent.CORRECTIBLE_MODIFIED)
						- STATS.get(FileEvent.UNCORRECTIBLE_MODIFIED);
						break;
					case ACCESSED:
						value = total 
						- STATS.get(FileEvent.CORRECTIBLE_ACCESSED)
						- STATS.get(FileEvent.UNCORRECTIBLE_ACCESSED);
						break;
					default:
						value = 0;
						break;
					}	
				}
				else {
					value = 0;
				}
				
				bar.getDataset().addValue(value, s.getName(), c.getName());
			}
		}	
	}
}

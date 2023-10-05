package gyk4j.wreck.view.preview.body.statistics;

import java.awt.CardLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import gyk4j.wreck.view.preview.ScrollTable;

public class PreviewStatisticsGraphStack extends JPanel {

	private static final long serialVersionUID = 1L;
	
//	private static final Logger LOG = LoggerFactory.getLogger(PreviewStatisticsGraphStack.class);
	
	public static final String CARD_TEXT = "text";
	public static final String CARD_BAR = "bar";
	public static final String CARD_PIE = "pie";
	
	private final RawTable rawTable;
	private final BarChart barChart;
	private final PieChart pieChart;
	
	public PreviewStatisticsGraphStack() {
		super();
		
		rawTable = new RawTable();
		barChart = new BarChart();
		pieChart = new PieChart();
		
		setLayout(new CardLayout());
		add(rawTable, CARD_TEXT);
		add(barChart.getChart(), CARD_BAR);
		add(pieChart.getChart(), CARD_PIE);
	}
	
	public class RawTable extends JPanel {

		private static final long serialVersionUID = 1L;
		
		private final ScrollTable scrollTable;

		public RawTable() {
			super();
			scrollTable = new ScrollTable();
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			add(scrollTable);
		}

		public ScrollTable getScrollTable() {
			return scrollTable;
		}
	}
	
	public class BarChart {
		private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		private final ChartPanel chart;
		
		public BarChart() {
			JFreeChart barChart = ChartFactory.createStackedBarChart(
					"File Statistics",	// title
					"Files",            // category
					"Count",            // value
					dataset,
					PlotOrientation.HORIZONTAL,	// orientation    
					false,				// legends 
					true, 				// tooltips
					false);				// urls
			
			barChart.getCategoryPlot().getRenderer().setSeriesPaint(ChartSeriesEnum.DONE.getIndex(), ChartSeriesEnum.DONE.getColor());
			barChart.getCategoryPlot().getRenderer().setSeriesPaint(ChartSeriesEnum.SKIPPED.getIndex(), ChartSeriesEnum.SKIPPED.getColor());
			
			chart = new ChartPanel( barChart );
		}

		public DefaultCategoryDataset getDataset() {
			return dataset;
		}

		public ChartPanel getChart() {
			return chart;
		}
		
	}
	
	public class PieChart {
		private final DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		private final ChartPanel chart;
		
		public PieChart() {
			JFreeChart pieChart = ChartFactory.createPieChart(      
					"Files",	// chart title 
					dataset,	// data    
					false,		// include legend   
					true,		// tooltips
					false);		// urls			
			
			chart = new ChartPanel( pieChart );
		}

		public DefaultPieDataset<String> getDataset() {
			return dataset;
		}

		public ChartPanel getChart() {
			return chart;
		}
	}
	
	public RawTable getRawTable() {
		return rawTable;
	}

	public BarChart getBarChart() {
		return barChart;
	}

	public PieChart getPieChart() {
		return pieChart;
	}
}

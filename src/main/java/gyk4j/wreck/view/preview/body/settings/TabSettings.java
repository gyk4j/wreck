package gyk4j.wreck.view.preview.body.settings;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import gyk4j.wreck.resources.CorrectionsChartSeriesEnum;
import gyk4j.wreck.resources.R;

public class TabSettings extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final SettingsSource sources;
	private final SettingsCorrections corrections;
	
	private final BarChart statistics;
	private final JButton backup;
	private final JButton restore;
	private final JButton verify;
	private final JButton analyze;

	public TabSettings() {
		super();
		
		sources = new SettingsSource();
		corrections = new SettingsCorrections();
		statistics = new BarChart();
		backup = new JButton("Backup");
		restore = new JButton("Restore");
		verify = new JButton("Verify");
		analyze = new JButton("Analyze");
		
		setBorder(R.style.BORDER_EMPTY_4);
		
//		BoxLayout l = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		
		setLayout(new BorderLayout());
		
		JPanel actions = new JPanel();
		actions.setOpaque(false);
		actions.setLayout(new GridLayout(0, 2));
		actions.add(sources);
		actions.add(corrections);
		
		add(actions, BorderLayout.PAGE_START);
		add(statistics.getChart(), BorderLayout.CENTER);
		
		JPanel p = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		p.setOpaque(false);
		p.add(backup);
		p.add(restore);
		p.add(verify);
		p.add(analyze);
		add(p, BorderLayout.PAGE_END);
	}

	public SettingsSource getSources() {
		return sources;
	}

	public SettingsCorrections getCorrections() {
		return corrections;
	}

	public BarChart getStatistics() {
		return statistics;
	}
	
	public JButton getBackup() {
		return backup;
	}
	
	public JButton getRestore() {
		return restore;
	}
	
	public JButton getVerify() {
		return verify;
	}
	
	public JButton getAnalyze() {
		return analyze;
	}

	public class BarChart {
		private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		private final ChartPanel chart;
		
		public BarChart() {
			JFreeChart barChart = ChartFactory.createStackedBarChart(
					R.string.FORECASTED_CORRECTIONS,	// title
					R.string.TIMESTAMPS,            	// category
					R.string.FILE_COUNT,            	// value
					dataset,
					PlotOrientation.VERTICAL,			// orientation    
					false,								// legends 
					true, 								// tooltips
					false);								// urls
			
			for(CorrectionsChartSeriesEnum c: CorrectionsChartSeriesEnum.values()) {
				barChart.getCategoryPlot().getRenderer().setSeriesPaint(c.getIndex(), c.getColor());
			}
			
			chart = new ChartPanel( barChart );
			chart.setOpaque(false);
			chart.setBorder(R.style.BORDER_FORECAST);
		}

		public DefaultCategoryDataset getDataset() {
			return dataset;
		}

		public ChartPanel getChart() {
			return chart;
		}
		
	}
}

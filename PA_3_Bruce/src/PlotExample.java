//import java.awt.Color;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
//Could not locate: import org.jfree.chart.ui.RefineryUtilities;
//import org.jfree.chart.ui.*;
//import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.chart.plot.XYPlot;  
import org.jfree.data.xy.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.chart.axis.*;

import Jama.Matrix;

public class PlotExample extends ApplicationFrame {

	private XYDataset test_dataset_line;

	private DefaultCategoryDataset dataset_line; 
	private XYSeriesCollection dataset_points; 
	private JFreeChart lineChart;
	private JFreeChart scatterPlot;
	private ChartPanel chartPanel_line;
	private ChartPanel chartPanel_plot;
	private JFreeChart master_chart; 
	private ChartPanel chartPanel_master;

	private double const_b;
	private double coeff_m;

	public PlotExample(Matrix x_mat, Matrix y_mat, double const_b, double coeff_m, 
			int largest_x_val, int dimensions, String line_eq, String file_name) {
		super("Graph for file: " + file_name);

		this.const_b = const_b;
		this.coeff_m = coeff_m;

		test_dataset_line = load_test_data_line(largest_x_val, line_eq);

		lineChart = ChartFactory.createLineChart(
				"Least Squares Regression Line",
				"Price",
				"Monthly Sales",
				createDataset_bestfit(x_mat.get(x_mat.getRowDimension() - 1, 1)));

		scatterPlot = ChartFactory.createScatterPlot(
				"Least Squares Regression Points",
				"Price",
				"Monthly Sales",
				createDataset_scatterplot(x_mat, y_mat));

		chartPanel_line = new ChartPanel( lineChart );
		chartPanel_line.setPreferredSize( new java.awt.Dimension(dimensions, dimensions) );
		setContentPane( chartPanel_line );

		chartPanel_plot = new ChartPanel( scatterPlot );
		chartPanel_plot.setPreferredSize( new java.awt.Dimension(dimensions,dimensions) );
		setContentPane( chartPanel_plot );

		combine_sets();
		
		chartPanel_master = new ChartPanel( master_chart );
		chartPanel_master.setPreferredSize( new java.awt.Dimension(dimensions, dimensions) );
		setContentPane( chartPanel_master );
	}//end constructor

	public void combine_sets(){
		XYPlot master_plot = new XYPlot();

		/* SETUP SCATTER */

		// Create the scatter data, renderer, and axis
		XYDataset collection1 = dataset_points;
		XYItemRenderer renderer1 = new XYLineAndShapeRenderer(false, true);	// Shapes only
		ValueAxis domain1 = new NumberAxis("Price (Dollars)");
		ValueAxis range1 = new NumberAxis("Monthly sales");

		// Set the scatter data, renderer, and axis into plot
		master_plot.setDataset(0, collection1);
		master_plot.setRenderer(0, renderer1);
		master_plot.setDomainAxis(0, domain1);
		master_plot.setRangeAxis(0, range1);

		// Map the scatter to the first Domain and first Range
		master_plot.mapDatasetToDomainAxis(0, 0);
		master_plot.mapDatasetToRangeAxis(0, 0);

		/* SETUP LINE */

		// Create the line data, renderer, and axis
		XYDataset collection2 = test_dataset_line;
		XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);	// Lines only
//		ValueAxis domain2 = new NumberAxis("Domain2");
//		ValueAxis range2 = new NumberAxis("Range2");
		
//		domain2.setAxisLineVisible(false);
//		range2.setAxisLineVisible(false);
		
		// Set the line data, renderer, and axis into plot
		master_plot.setDataset(1, collection2);
		master_plot.setRenderer(1, renderer2);
//		master_plot.setDomainAxis(1, domain1);
//		master_plot.setRangeAxis(1, range1);

		// Map the line to the second Domain and second Range
		master_plot.mapDatasetToDomainAxis(0, 0);
		master_plot.mapDatasetToRangeAxis(0, 0);
		
		// Map the line to the FIRST Domain and second Range
		master_plot.mapDatasetToDomainAxis(0, 0);
		master_plot.mapDatasetToRangeAxis(0, 0);

		// Create the chart with the plot and a legend
		master_chart = new JFreeChart(
				"Monthly Sales by Price with Least Squares Regression Line", 
				JFreeChart.DEFAULT_TITLE_FONT, 
				master_plot, 
				true);
		
	}//end combine_sets() method

	public XYDataset load_test_data_line(double x_limit, String line_eq){
		XYSeriesCollection collection = new XYSeriesCollection();
		XYSeries data = new XYSeries("Regression Line: " + line_eq);

		//Iterate to x_limit and draw the line
		for(int x = 0; x < x_limit; x++) {
			double temp_y_val = const_b + (coeff_m * x);
			data.add(x, temp_y_val);
		}
		
		collection.addSeries(data);

		return collection;
	}

	public DefaultCategoryDataset createDataset_bestfit(double x_limit) {
		dataset_line = new DefaultCategoryDataset( );

		//Iterate to x_limit and draw the line
		for(int x = 0; x < x_limit; x++) {
			double temp_y_val = const_b + (coeff_m * x);
			dataset_line.addValue(temp_y_val,
					"Best fit line",
					x + "");//(y, line_name,x) --weird parameters, but that's how it is.
		}

		return dataset_line;
	}//end bestfit line dataset method

	public XYSeriesCollection createDataset_scatterplot(Matrix x_coords, Matrix y_coords) {
		dataset_points = new XYSeriesCollection();

		XYSeries series = new XYSeries("Monthly Sale Points");
		//Iterate through x_coords (column 1) and y_coords to create new points
		for(int r = 0; r < x_coords.getRowDimension(); r++) {
			series.add((int)x_coords.get(r, 1), y_coords.get(r, 0));//(y, line_name,x) --weird parameters, but that's how it is.
		}

		dataset_points.addSeries(series);

		return dataset_points;
	}//end scatterplot dataset method

	public JFreeChart get_JFreeChart_line() {
		return lineChart;
	}

	public JFreeChart get_JFreeChart_point() {
		return lineChart;
	}

	//	public void print_dataset_point() {
	//		String temp = "Points dataset:";
	//
	//		for(int r = 0; r < dataset_points.getRangeUpperBound(true); r++) {
	//			for(int c = 0; c < dataset_points.getDomainUpperBound(true); c++) {
	//				temp += "\n\trow: " + r 
	//						+ ", col: " + c 
	//						+ ":\t"
	//						+ String.format("%.1f", dataset_points.getValue(r, c).doubleValue());
	//			}//end inner loop
	//		}//end outer loop
	//
	//		System.out.println(temp+"\n");
	//	}

	public void print_dataset_line() {
		String temp = "Lines dataset:";

		for(int r = 0; r < dataset_line.getRowCount(); r++) {
			for(int c = 0; c < dataset_line.getColumnCount(); c++) {
				temp += "\n\trow: " + r 
						+ ", col: " + c 
						+ ":\t"
						+ String.format("%.1f", dataset_line.getValue(r, c).doubleValue());
			}//end inner loop
		}//end outer loop

		System.out.println(temp+"\n");
	}//end print_dataset method
}//end class

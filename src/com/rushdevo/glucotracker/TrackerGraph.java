package com.rushdevo.glucotracker;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidplot.Plot;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.rushdevo.glucotracker.data.GlucoseRecord;


/**
 * @author jasonrush
 * Activity for displaying the graph
 */
public class TrackerGraph extends Activity implements GlucoseRecordListable {
	private GlucoseRecordList glucoseRecordList;
	private XYPlot graph;
	private TextView noDataView;
	private SimpleXYSeries series;
	private SimpleXYSeries highSeries;
	private SimpleXYSeries lowSeries;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracker_graph);
		
		Long startDate = null;
		Long stopDate = null;
		if (savedInstanceState != null) {
			startDate = savedInstanceState.getLong("startDate");
			stopDate = savedInstanceState.getLong("stopDate");
		}
		
		this.glucoseRecordList = new GlucoseRecordList(this, startDate, stopDate);
		this.series = new SimpleXYSeries(getTitle().toString());
		this.highSeries = new SimpleXYSeries("");
		this.lowSeries = new SimpleXYSeries("");
		
		graph = (XYPlot)findViewById(R.id.graph);
		noDataView = (TextView)findViewById(R.id.no_data);
		
		initGraph();
		updateSeries();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putLong("startDate", this.glucoseRecordList.getStartDate().getTimeInMillis());
	    outState.putLong("stopDate", this.glucoseRecordList.getStopDate().getTimeInMillis());
	}
	
	@Override
    public void onPause() {
		super.onPause();
    	if (this.glucoseRecordList != null) this.glucoseRecordList.close();
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.graph_menu, menu);
    	return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.settings:
    		startActivity(new Intent(this, Settings.class));
    		return true;
    	case R.id.change_dates:
    		return glucoseRecordList.displayDateRangeDialog();
    	case R.id.list:
    		startActivity(new Intent(this, TrackerList.class));
    		return true;
    	}
    	return false;
    }
	
	////////////HELPERS ///////////////
	/**
	 * @return The records from the list
	 */
	private List<GlucoseRecord> getRecords() {
		return glucoseRecordList.getRecords();
	}
	
	/**
	 * @return true if their are one or more records
	 */
	private Boolean isEmpty() {
		return getRecords().isEmpty();
	}
	
	/**
	 * Implementation - See GlucoseRecordListable
	 * Update the list and set the footer text
	 */
	public void afterDateChange() {
		updateSeries();
    	graph.redraw();
	}
	
	/**
	 * Initialize the graph
	 */
	private void initGraph() {
		if (isEmpty()) {
			// No data, no graph
			graph.setVisibility(View.GONE);
			noDataView.setVisibility(View.VISIBLE);
		} else {
			noDataView.setVisibility(View.GONE);
			graph.setVisibility(View.VISIBLE);
			
			graph.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
	        graph.getGraphWidget().getGridLinePaint().setColor(Color.BLACK);
	        graph.getGraphWidget().getGridLinePaint().setPathEffect(new DashPathEffect(new float[]{1,1}, 1));
	        graph.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
	        graph.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
	 
	        graph.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
	        graph.getBorderPaint().setStrokeWidth(1);
	        graph.getBorderPaint().setAntiAlias(false);
	        graph.getBorderPaint().setColor(Color.WHITE);
	 
	        graph.getGraphWidget().setPaddingRight(2);
	        graph.getGraphWidget().setPaddingBottom(10);
	        
	        // draw a domain tick for each year:
	        graph.setDomainStep(XYStepMode.SUBDIVIDE, getNumberOfTickmarks());
	 
	        // customize our domain/range labels
	        graph.setDomainLabel("Date");
	        graph.setRangeLabel("Blood Sugar");
	 
	        // get rid of decimal points in our range labels:
	        graph.setRangeValueFormat(new DecimalFormat("0"));
	 
	        graph.setDomainValueFormat(new GraphDateFormat());
	 
	        // by default, AndroidPlot displays developer guides to aid in laying out your plot.
	        // To get rid of them call disableAllMarkup():
	        graph.disableAllMarkup();
		}
	}
	
	/**
	 * Add series to the graph
	 */
	private void addSeriesToGraph() {
		Paint lineFill = new Paint();
        lineFill.setAlpha(0);
        
		LineAndPointFormatter formatter  = new LineAndPointFormatter(Color.BLACK, Color.BLACK, Color.WHITE);
        formatter.setFillPaint(lineFill);
        graph.addSeries(series, formatter);
        
        LineAndPointFormatter boundaryFormatter  = new LineAndPointFormatter(Color.RED, Color.RED, Color.WHITE);
        boundaryFormatter.setFillPaint(lineFill);
        graph.addSeries(highSeries, boundaryFormatter);
        graph.addSeries(lowSeries, boundaryFormatter);
	}
	
	/**
	 * Update the SimpleXYSeries for the graph
	 */
	private void updateSeries() {
		if (!isEmpty()) {
			List<GlucoseRecord> records = getRecords();
			// Clear the series
			clearSeries(series);
			// And add back to it
			for (GlucoseRecord record : records) {
				Long timestamp = record.getBloodSugarTimestamp();
				Integer bloodSugar = record.getBloodSugar();
				// Skip it if either value is null...
				if (timestamp != null && bloodSugar != null) {
					series.addLast(timestamp, bloodSugar);
				}
			}
			updateHighRangeSeries();
			updateLowRangeSeries();
			addSeriesToGraph();
		}		
	}
	/**
	 * Get a two-element series drawing a line across the high end of the ideal range
	 */
	private void updateHighRangeSeries() {
		if (!isEmpty()) {
			clearSeries(highSeries);
			Integer high = Settings.getHigh(this);
			List<GlucoseRecord> records = getRecords();
			
			GlucoseRecord record = records.get(0);
			highSeries.addLast(record.getBloodSugarTimestamp(), high);
			
			record = records.get(records.size()-1);
			highSeries.addLast(record.getBloodSugarTimestamp(), high);
		}
	}
	
	/**
	 * Get a two-element series drawing a line across the low end of the ideal range
	 */
	private void updateLowRangeSeries() {
		if (!isEmpty()) {
			clearSeries(lowSeries);
			Integer low = Settings.getLow(this);
			List<GlucoseRecord> records = getRecords();
			
			GlucoseRecord record = records.get(0);
			lowSeries.addLast(record.getBloodSugarTimestamp(), low);
			
			record = records.get(records.size()-1);
			lowSeries.addLast(record.getBloodSugarTimestamp(), low);
		}
	}
	
	private void clearSeries(SimpleXYSeries xySeries) {
		while (xySeries.size() > 0) xySeries.removeFirst();
	}
	
	/**
	 * 
	 * @return Number of tickmarks for graph to use
	 * 
	 * Right now defaults to 4 for portrait and 8 for landscape.
	 * Should probably be more discerning than that... (TODO) 
	 */
	private Integer getNumberOfTickmarks() {
		int numberOfDays = glucoseRecordList.getNumberOfDays();
		int maxTickmarks;
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			maxTickmarks = 8;
		} else {
			maxTickmarks = 4;
		}
		if (numberOfDays <= maxTickmarks) {
			return numberOfDays;
		} else {
			return maxTickmarks;
		}
	}
}

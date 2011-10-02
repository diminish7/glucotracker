package com.rushdevo.glucotracker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import com.androidplot.series.XYSeries;
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracker_graph);
		this.glucoseRecordList = new GlucoseRecordList(this);
		
		graph = (XYPlot)findViewById(R.id.graph);
		noDataView = (TextView)findViewById(R.id.no_data);
		
		updateGraph();
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
	 * Implementation - See GlucoseRecordListable
	 * Update the list and set the footer text
	 */
	public void afterDateChange() {
    	updateGraph();
	}
	
	/**
	 * Initialize the graph
	 */
	private void updateGraph() {
		XYSeries series = getSeries();
		
		if (series == null) {
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
	 
	        // setup our line fill paint to be a slightly transparent gradient:
	        Paint lineFill = new Paint();
	        lineFill.setAlpha(0);
	 
	        LineAndPointFormatter formatter  = new LineAndPointFormatter(Color.BLACK, Color.BLACK, Color.WHITE);
	        formatter.setFillPaint(lineFill);
	        
	        graph.getGraphWidget().setPaddingRight(2);
	        graph.getGraphWidget().setPaddingBottom(10);
	        
	        graph.addSeries(series, formatter);
	 
	        // draw a domain tick for each year:
	        graph.setDomainStep(XYStepMode.SUBDIVIDE, 4);
	 
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
	 * Get the record data as a SimpleXYSeries for the graph
	 */
	private XYSeries getSeries() {
		List<GlucoseRecord> records = glucoseRecordList.getRecords();
		if (records == null || records.isEmpty()) return null;
		List<Number> x = new ArrayList<Number>();
		List<Number> y = new ArrayList<Number>();
		Boolean addedAny = false;
		for (GlucoseRecord record : records) {
			Long timestamp = record.getBloodSugarTimestamp();
			Integer bloodSugar = record.getBloodSugar();
			// Skip it if either value is null...
			if (timestamp != null && bloodSugar != null) {
				addedAny = true;
				// Timestamp goes in x
				x.add(timestamp);
				// Blood sugar goes in y
				y.add(bloodSugar);
			}
		}
		if (addedAny) {
			return new SimpleXYSeries(x, y, getTitle().toString());
		} else {
			// No valid data, no graph
			return null;
		}
	}
}

package com.rushdevo.glucotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/**
 * @author jasonrush
 * Activity for displaying the graph
 */
public class TrackerGraph extends Activity implements GlucoseRecordListable {
	
	private GlucoseRecordList glucoseRecordList;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracker_graph);
		this.glucoseRecordList = new GlucoseRecordList(this);
		// TODO: Display graph
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
	 * Update the graph after a date change
	 */
	private void updateGraph() {
		// TODO
	}
}

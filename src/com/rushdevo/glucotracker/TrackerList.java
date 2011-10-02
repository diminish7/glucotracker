package com.rushdevo.glucotracker;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;


/**
 * @author jasonrush
 * List of glucose records
 */
public class TrackerList extends ListActivity implements GlucoseRecordListable {
	private ListView listView;
	private TextView footer;
	private GlucoseRecordAdapter listAdapter;
	
	private GlucoseRecordList glucoseRecordList;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tracker_list);
		this.glucoseRecordList = new GlucoseRecordList(this);
		this.listView = getListView();
        footer = new TextView(this);
        setFooterText();
        footer.setGravity(Gravity.CENTER);
        listView.addFooterView(footer);
        listAdapter = new GlucoseRecordAdapter(this, android.R.layout.simple_list_item_1, glucoseRecordList.getRecords());
        setListAdapter(listAdapter);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.list_menu, menu);
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
    	case R.id.graph:
    		startActivity(new Intent(this, TrackerGraph.class));
    		return true;
    	}
    	return false;
    }
	
	//////////// HELPERS ///////////////
	/**
	 * Implementation - See GlucoseRecordListable
	 * Update the list and set the footer text
	 */
	public void afterDateChange() {
    	updateList();
    	setFooterText();
	}
	
	/**
	 * Update the list view with whatever records stores right now
	 */
	private void updateList() {
    	listAdapter.setRecords(glucoseRecordList.getRecords());
    	listAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Set the footer text to the current average
	 */
	private void setFooterText() {
		footer.setText("Average: " + glucoseRecordList.getAverage());
	}
}

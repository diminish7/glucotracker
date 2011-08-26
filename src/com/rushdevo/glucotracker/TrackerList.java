package com.rushdevo.glucotracker;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author jasonrush
 * List of glucose records
 */
public class TrackerList extends Activity {
	private Date startDate;
	private Date endDate;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_list);
        initializeDates();
	}

	/////// GETTERS AND SETTERS ////////////
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	//////////// HELPERS ///////////////
	
	/**
	 * Initialize the dates for the query. Default to current month
	 */
	private void initializeDates() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_MONTH, 1);
		this.startDate = c.getTime();
		c.set(Calendar.DAY_OF_MONTH, c.getMaximum(Calendar.DAY_OF_MONTH));
		this.endDate = c.getTime();
	}
}

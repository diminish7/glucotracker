package com.rushdevo.glucotracker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

import com.rushdevo.glucotracker.data.GlucoseRecord;
import com.rushdevo.glucotracker.data.GlucotrackerData;

/**
 * @author jasonrush
 * List of glucose records
 */
public class TrackerList extends ListActivity {
	private Date startDate;
	private Date endDate;
	
	private GlucotrackerData dataDelegate;
	private List<GlucoseRecord> records;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.dataDelegate = new GlucotrackerData(this);
        initializeDates();
        queryRecords();
        setListAdapter(new GlucoseRecordAdapter(this, android.R.layout.simple_list_item_1, this.records));
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
	
	/**
	 * Query the records between this.startDate and this.endDate
	 */
	private void queryRecords() {
		this.records = dataDelegate.getGlucoseRecords(startDate, endDate);
	}
}

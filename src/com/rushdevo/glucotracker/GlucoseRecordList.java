/**
 * 
 */
package com.rushdevo.glucotracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.rushdevo.glucotracker.data.GlucoseRecord;
import com.rushdevo.glucotracker.data.GlucotrackerData;

/**
 * @author jasonrush
 * Encapsulates the data needed for both the TrackerList activity and the TrackerGraph activity
 */
public class GlucoseRecordList {
	private GlucotrackerData dataDelegate;
	private Calendar startDateCal;
	private Calendar stopDateCal;
	private Integer average;
	private SimpleDateFormat formatter;
	private Integer numberOfDays;
	
	private List<GlucoseRecord> records;
	private Activity activity;
	
	public GlucoseRecordList(Context context) {
		this.dataDelegate = new GlucotrackerData(context);
		this.activity = (Activity)context;
		this.formatter = new SimpleDateFormat("M/d/yyyy");
		initializeDates();
        resetTitle();
        queryRecords();
        calculateAverage();
        determineNumberOfDays();
	}
	
	/////// GETTERS AND SETTERS ////////////
	public void setStartDate(Calendar startDate) {
		this.startDateCal = startDate;
	}

	public Calendar getStartDate() {
		return startDateCal;
	}

	public void setStopDate(Calendar stopDate) {
		this.stopDateCal = stopDate;
	}

	public Calendar getStopDate() {
		return stopDateCal;
	}

	public GlucotrackerData getDataDelegate() {
		return this.dataDelegate;
	}
	
	public List<GlucoseRecord> getRecords() {
		return this.records;
	}
	
	public Activity getActivity() {
		return this.activity;
	}
	
	public Integer getAverage() {
		return this.average;
	}
	
	/**
	 * @return The number of days represented in the dataset
	 */
	public Integer getNumberOfDays() {
		return this.numberOfDays;
	}
	
	/////// HELPERS //////////////
	
	/**
	 * Display the dialog for changing the date range
	 */
	public Boolean displayDateRangeDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
		
		dialog.setTitle(R.string.change_dates_title);
		
		LayoutInflater factory = LayoutInflater.from(activity);
		final View dateChangerView = factory.inflate(R.layout.date_changer, null);
		dialog.setView(dateChangerView);
		
		final DatePicker startPicker = (DatePicker)dateChangerView.findViewById(R.id.start_date);
		startPicker.updateDate(startDateCal.get(Calendar.YEAR), startDateCal.get(Calendar.MONTH), startDateCal.get(Calendar.DAY_OF_MONTH));
		final DatePicker stopPicker = (DatePicker)dateChangerView.findViewById(R.id.stop_date);
		stopPicker.updateDate(stopDateCal.get(Calendar.YEAR), stopDateCal.get(Calendar.MONTH), stopDateCal.get(Calendar.DAY_OF_MONTH));
		
		dialog.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	// Update the start and stop dates
            	startDateCal.set(Calendar.YEAR, startPicker.getYear());
            	startDateCal.set(Calendar.MONTH, startPicker.getMonth());
            	startDateCal.set(Calendar.DAY_OF_MONTH, startPicker.getDayOfMonth());
            	
            	stopDateCal.set(Calendar.YEAR, stopPicker.getYear());
            	stopDateCal.set(Calendar.MONTH, stopPicker.getMonth());
            	stopDateCal.set(Calendar.DAY_OF_MONTH, stopPicker.getDayOfMonth());
            	
            	// Query records within the start and stop date
            	queryRecords();
            	// Update the title to include the new date range
            	resetTitle();
            	// Update the average
            	calculateAverage();
            	
            	if (getActivity() instanceof GlucoseRecordListable) {
            		GlucoseRecordListable activity = (GlucoseRecordListable)getActivity();
            		activity.afterDateChange();
            	}
            }
        });
		
		dialog.setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled, NOOP (default behavior)
			}
		});
		
		dialog.show();
		return true;
	}
	
	/**
	 * Initialize the dates for the query. Default to current month
	 */
	private void initializeDates() {
		startDateCal = Calendar.getInstance();
		startDateCal.setTime(new Date());
		startDateCal.set(Calendar.DAY_OF_MONTH, 1);
		
		stopDateCal = Calendar.getInstance();
		stopDateCal.setTime(new Date());
		stopDateCal.set(Calendar.DAY_OF_MONTH, stopDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
	
	/**
	 * Query the records between this.startDate and this.endDate
	 */
	private void queryRecords() {
		this.records = dataDelegate.getGlucoseRecords(startDateCal.getTime(), stopDateCal.getTime());
	}
	
	/**
	 * Calculate the average for the set of GlucoseRecords
	 */
	private void calculateAverage() {
		if (records.isEmpty()) {
			this.average = 0;
		} else {
			Integer total = 0;
			for (GlucoseRecord record : records) {
				total += record.getBloodSugar();
			}
			this.average = Math.round(total / new Float(records.size()));
		}
	}
	
	/**
	 * Reset the title to include the dates
	 */
	private void resetTitle() {
		StringBuilder title = new StringBuilder(this.activity.getResources().getString(R.string.app_name));
		if (startDateCal != null && stopDateCal != null) {
			title.append(" (");
			title.append(formatter.format(startDateCal.getTime()));
			title.append(" - ");
			title.append(formatter.format(stopDateCal.getTime()));
			title.append(")");
		}
		
		activity.setTitle(title);
	}
	
	/**
	 * Determines the number of days represented in the dataset and set this.numberOfDays
	 */
	private void determineNumberOfDays() {
		Set<String> dates = new HashSet<String>();
		for (GlucoseRecord record : getRecords()) {
			dates.add(record.getBloodSugarDate());
		}
		this.numberOfDays = dates.size();
	}
}

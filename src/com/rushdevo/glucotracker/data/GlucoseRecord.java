package com.rushdevo.glucotracker.data;

import static android.provider.BaseColumns._ID;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;

import com.rushdevo.glucotracker.Settings;

/**
 * @author jasonrush
 * Glucose record model
 */
public class GlucoseRecord {
	public static final String TABLE_NAME = "glucose_records";
	public static final String BLOOD_SUGAR = "blood_sugar";
	public static final String BLOOD_SUGAR_DATE = "blood_sugar_date";
	public static final String BLOOD_SUGAR_TIME = "blood_sugar_time";
	public static final String BLOOD_SUGAR_MEAL = "blood_sugar_meal";
	public static final String BLOOD_SUGAR_CORRECTION = "blood_sugar_correction";
	public static final String[] COLUMNS = { _ID, BLOOD_SUGAR, BLOOD_SUGAR_DATE, BLOOD_SUGAR_TIME, BLOOD_SUGAR_MEAL, BLOOD_SUGAR_CORRECTION };
	
	//////////// PROPERTIES ////////////////////
	private Long id;
	private Integer bloodSugar;
	private String bloodSugarDate;
	private String bloodSugarTime;
	private Boolean bloodSugarMeal;
	private Boolean bloodSugarCorrection;
	
	private Boolean flag;
	
	private GlucotrackerData delegate;
	private String[] errors = {};
	
	private SimpleDateFormat dateParser;
	
	/////////// CONSTRUCTORS ///////////////////
	public GlucoseRecord(GlucotrackerData delegate, Cursor cursor) {
		this.delegate = delegate;
		this.dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		refreshFromCursor(cursor);
	}
	
	public GlucoseRecord(GlucotrackerData delegate, Integer bloodSugar, String date, String time, Boolean isMeal, Boolean isCorrection) {
		this.delegate = delegate;
		this.bloodSugar = bloodSugar;
		this.bloodSugarDate = date;
		this.bloodSugarTime = time;
		this.bloodSugarMeal = isMeal;
		this.bloodSugarCorrection = isCorrection;
		if (!this.create()) {
			this.errors[0] = "Unable to create record!";
		}
	}
	
	public GlucoseRecord(Context ctx, Integer bloodSugar, String date, String time, Boolean isMeal, Boolean isCorrection) {
		this(new GlucotrackerData(ctx), bloodSugar, date, time, isMeal, isCorrection);
	}
	
	/////////// GETTERS AND SETTERS ////////////
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public void setBloodSugar(Integer bloodSugar) {
		this.bloodSugar = bloodSugar;
	}

	public Integer getBloodSugar() {
		return bloodSugar;
	}

	public void setBloodSugarDate(String bloodSugarDate) {
		this.bloodSugarDate = bloodSugarDate;
	}

	public String getBloodSugarDate() {
		return bloodSugarDate;
	}

	public void setBloodSugarTime(String bloodSugarTime) {
		this.bloodSugarTime = bloodSugarTime;
	}

	public String getBloodSugarTime() {
		return bloodSugarTime;
	}
	
	// Return the epoch time
	public Long getBloodSugarTimestamp() {
		String timestampString = this.bloodSugarDate + " " + this.bloodSugarTime;
		try {
			Date timestamp = dateParser.parse(timestampString);
			return timestamp.getTime();
		} catch (ParseException e) {
			return null;
		}
	}

	public void setBloodSugarMeal(Boolean bloodSugarMeal) {
		this.bloodSugarMeal = bloodSugarMeal;
	}

	public Boolean getBloodSugarMeal() {
		return bloodSugarMeal;
	}

	public void setBloodSugarCorrection(Boolean bloodSugarCorrection) {
		this.bloodSugarCorrection = bloodSugarCorrection;
	}

	public Boolean getBloodSugarCorrection() {
		return bloodSugarCorrection;
	}
	
	public void setDelegate(GlucotrackerData delegate) {
		this.delegate = delegate;
	}

	public GlucotrackerData getDelegate() {
		return delegate;
	}

	public String[] getErrors() {
		return errors;
	}
	
	public Boolean hasErrors() {
		return (this.errors != null && this.errors.length > 0);
	}
	
	/**
	 * 
	 * @param low The low end of the target
	 * @param high
	 * @return
	 */
	public Boolean shouldFlag() {
		if (this.flag == null) {
			Context context = this.delegate.getContext();
			int low = Settings.getLow(context);
			int high = Settings.getHigh(context);
			this.flag = (this.bloodSugar < low || this.bloodSugar > high);
		}
		return this.flag;
	}
	
	public String getErrorMessage() {
		if (hasErrors()) {
			if (this.errors.length == 1) {
				return this.errors[0];
			} else {
				StringBuilder msg = new StringBuilder();
				msg.append(this.errors[0]);
				for (int i=1; i< this.errors.length; i++) {
					msg.append(", ");
					msg.append(this.errors[i]);
				}
				return msg.toString();
			}
		} else {
			return "";
		}
	}
	
	//////// DATA ACCESS METHODS ////////////////
	public void save() {
		if (this.id == null) create();
		else update();
	}
	
	public boolean create() {
		return delegate.create(this);
	}
	
	public boolean update() {
		return delegate.update(this);
	}
	
///////////// CURSOR HELPERS //////////////
	public void refreshFromCursor(Cursor cursor) {
		idFromCursor(cursor);
		bloodSugarFromCursor(cursor);
		dateFromCursor(cursor);
		timeFromCursor(cursor);
		mealFromCursor(cursor);
		correctionFromCursor(cursor);
	}
	private void idFromCursor(Cursor cursor) {
		setId(cursor.getLong(0));
	}
	
	private void bloodSugarFromCursor(Cursor cursor) {
		setBloodSugar(cursor.getInt(1));
	}
	
	private void dateFromCursor(Cursor cursor) {
		setBloodSugarDate(cursor.getString(2));
	}
	
	private void timeFromCursor(Cursor cursor) {
		setBloodSugarTime(cursor.getString(3));
	}
	
	private void mealFromCursor(Cursor cursor) {
		Integer val = cursor.getInt(4);
		setBloodSugarMeal(!(val == null || val == 0));
	}
	
	private void correctionFromCursor(Cursor cursor) {
		Integer val = cursor.getInt(5);
		setBloodSugarCorrection(!(val == null || val == 0));
	}
}

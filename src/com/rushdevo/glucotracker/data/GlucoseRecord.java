package com.rushdevo.glucotracker.data;

import static android.provider.BaseColumns._ID;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author jasonrush
 * Represents a blood sugar record in the database
 */
public class GlucoseRecord extends SQLiteOpenHelper {
	//////////// CONSTANTS /////////////////////
	public static final String DATABASE_NAME = "glucotracker.db";
	public static final int DATABASE_VERSION = 1;
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
	private String bloodSugarDate;	// TODO: Bother converting this to a Date object?
	private String bloodSugarTime;	// TODO: Bother converting this to a Time object?
	private Boolean bloodSugarMeal;
	private Boolean bloodSugarCorrection;
	
	private String[] errors = {};
	
	//////////// CONSTRUCTORS ////////////////////
	/**
	 * Basic constructor, delegates to SQLiteOpenHelper
	 * @param ctx
	 * @param factory
	 */
	public GlucoseRecord(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * Creates a GlucoseRecord and refreshes its properties from the cursor values
	 * @param ctx
	 * @param factory
	 * @param cursor
	 */
	public GlucoseRecord(Context ctx, Cursor cursor) {
		this(ctx);
		refreshFromCursor(cursor);
	}
	
	public GlucoseRecord(Context ctx, Integer bloodSugar, String date, String time, Boolean isMeal, Boolean isCorrection) {
		this(ctx);
		this.bloodSugar = bloodSugar;
		this.bloodSugarDate = date;
		this.bloodSugarTime = time;
		this.bloodSugarMeal = isMeal;
		this.bloodSugarCorrection = isCorrection;
		if (!this.create()) {
			this.errors[0] = "Unable to create record!";
		}
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

	public String[] getErrors() {
		return errors;
	}
	
	public Boolean hasErrors() {
		return (this.errors != null && this.errors.length > 0);
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

	//////////// SQLiteOpenHelper STUFF ////////
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + TABLE_NAME + " (");
		sql.append(_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sql.append(BLOOD_SUGAR + " INTEGER, ");
		sql.append(BLOOD_SUGAR_DATE + " TEXT,");
		sql.append(BLOOD_SUGAR_TIME + " TEXT,");
		sql.append(BLOOD_SUGAR_MEAL + " INTEGER,");
		sql.append(BLOOD_SUGAR_CORRECTION + " INTEGER");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO This is just stubbed out for now, need to implement a version-by-version upgrade
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	///////////// DB OPERATIONS ///////////////
	public void save() {
		if (this.id == null) create();
		else update();
	}
	
	private Boolean create() {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BLOOD_SUGAR, getBloodSugar());
		values.put(BLOOD_SUGAR_DATE, getBloodSugarDate());
		values.put(BLOOD_SUGAR_TIME, getBloodSugarTime());
		values.put(BLOOD_SUGAR_MEAL, getBloodSugarMeal());
		values.put(BLOOD_SUGAR_CORRECTION, getBloodSugarCorrection());
		try {
			Long newId = db.insertOrThrow(TABLE_NAME, null, values);
			if (newId == null || newId <= 0) {
				return false;
			} else {
				setId(newId);
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	private void update() {
		// TODO
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

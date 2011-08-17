package com.rushdevo.glucotracker.data;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
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
	private Integer id;
	private Integer bloodSugar;
	private String bloodSugarDate;	// TODO: Bother converting this to a Date object?
	private String bloodSugarTime;	// TODO: Bother converting this to a Time object?
	private Boolean bloodSugarMeal;
	private Boolean bloodSugarCorrection;
	
	//////////// CONSTRUCTORS ////////////////////
	/**
	 * Basic constructor, delegates to SQLiteOpenHelper
	 * @param ctx
	 * @param factory
	 */
	public GlucoseRecord(Context ctx, CursorFactory factory) {
		super(ctx, DATABASE_NAME, factory, DATABASE_VERSION);
	}
	
	/**
	 * Creates a GlucoseRecord and refreshes its properties from the cursor values
	 * @param ctx
	 * @param factory
	 * @param cursor
	 */
	public GlucoseRecord(Context ctx, CursorFactory factory, Cursor cursor) {
		this(ctx, factory);
		refreshFromCursor(cursor);
	}
	
	/////////// GETTERS AND SETTERS ////////////
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
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
		sql.append(BLOOD_SUGAR_CORRECTION + " INTEGER,");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO This is just stubbed out for now, need to implement a version-by-version upgrade
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
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
		setId(cursor.getInt(0));
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

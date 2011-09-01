package com.rushdevo.glucotracker.data;

import static android.provider.BaseColumns._ID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author jasonrush
 * Data delegate for glucotracker.db
 */
public class GlucotrackerData extends SQLiteOpenHelper {
	//////////// CONSTANTS /////////////////////
	public static final String DATABASE_NAME = "glucotracker.db";
	public static final int DATABASE_VERSION = 1;
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	/////////// PROPERTIES /////////////////////
	private Context context;
	
	//////////// CONSTRUCTORS ////////////////////
	/**
	 * Basic constructor, delegates to SQLiteOpenHelper
	 * @param ctx
	 * @param factory
	 */
	public GlucotrackerData(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = ctx;
	}
	
	//////////// GETTERS AND SETTERS////////////
	public void setContext(Context ctx) {
		this.context = ctx;
	}
	
	public Context getContext() {
		return this.context;
	}
	
	//////////// SQLiteOpenHelper STUFF ////////
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE " + GlucoseRecord.TABLE_NAME + " (");
		sql.append(_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sql.append(GlucoseRecord.BLOOD_SUGAR + " INTEGER, ");
		sql.append(GlucoseRecord.BLOOD_SUGAR_DATE + " TEXT,");
		sql.append(GlucoseRecord.BLOOD_SUGAR_TIME + " TEXT,");
		sql.append(GlucoseRecord.BLOOD_SUGAR_MEAL + " INTEGER,");
		sql.append(GlucoseRecord.BLOOD_SUGAR_CORRECTION + " INTEGER");
		sql.append(");");
		db.execSQL(sql.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO This is just stubbed out for now, need to implement a version-by-version upgrade
		db.execSQL("DROP TABLE IF EXISTS " + GlucoseRecord.TABLE_NAME);
		onCreate(db);
	}
	
	///////////// DB OPERATIONS ///////////////
	public Boolean create(GlucoseRecord record) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GlucoseRecord.BLOOD_SUGAR, record.getBloodSugar());
		values.put(GlucoseRecord.BLOOD_SUGAR_DATE, record.getBloodSugarDate());
		values.put(GlucoseRecord.BLOOD_SUGAR_TIME, record.getBloodSugarTime());
		values.put(GlucoseRecord.BLOOD_SUGAR_MEAL, record.getBloodSugarMeal());
		values.put(GlucoseRecord.BLOOD_SUGAR_CORRECTION, record.getBloodSugarCorrection());
		try {
			Long newId = db.insertOrThrow(GlucoseRecord.TABLE_NAME, null, values);
			if (newId == null || newId <= 0) {
				return false;
			} else {
				record.setId(newId);
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	public Boolean update(GlucoseRecord record) {
		// TODO
		return true;
	}
	
	public List<GlucoseRecord> getGlucoseRecords(Date startDate, Date endDate) {
		List<GlucoseRecord> list = new ArrayList<GlucoseRecord>();
		SQLiteDatabase db = getReadableDatabase();
		String selection = GlucoseRecord.BLOOD_SUGAR_DATE + " BETWEEN ? AND ?";
		String[] selectionArgs = { dateFormatter.format(startDate), dateFormatter.format(endDate) };
		Cursor cursor = db.query(GlucoseRecord.TABLE_NAME, GlucoseRecord.COLUMNS, selection, selectionArgs, null, null, GlucoseRecord.BLOOD_SUGAR_DATE + ", " + GlucoseRecord.BLOOD_SUGAR_TIME + " ASC");
		while (cursor.moveToNext()) {
			list.add(new GlucoseRecord(this, cursor));
		}
		cursor.close();
		return list;
	}
}

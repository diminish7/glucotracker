/**
 * 
 */
package com.rushdevo.glucotracker;

import com.rushdevo.glucotracker.data.GlucoseRecord;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author jasonrush
 *
 */
public class GlucoseRecordView extends LinearLayout {

	private GlucoseRecord record;
	private TextView bloodSugarView;
	private TextView dateView;
	private int dip;
	
	public GlucoseRecordView(Context context, GlucoseRecord record) {
		super(context);
		this.setOrientation(HORIZONTAL);
		this.setRecord(record);
		this.dip = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)1, getResources().getDisplayMetrics());
		buildSubViews(context);
	}
	
	//////////GETTERS AND SETTERS//////////////
	public void setRecord(GlucoseRecord record) {
		this.record = record;
	}

	public GlucoseRecord getRecord() {
		return record;
	}
	
	/////////////HELPERS///////////////////////
	private void buildSubViews(Context context) {
		int sidePadding = 10*dip;
		int topPadding = 5*dip;
		int width = 50*dip;
		
		if (this.record.shouldFlag()) {
			setBackgroundColor(Color.RED);
		}
		
		bloodSugarView = new TextView(context);
		bloodSugarView.setText(this.record.getBloodSugar().toString());
		bloodSugarView.setPadding(sidePadding, topPadding, sidePadding, topPadding);
		bloodSugarView.setGravity(Gravity.RIGHT);
		addView(bloodSugarView, new LinearLayout.LayoutParams(width, LayoutParams.FILL_PARENT));
		
		dateView = new TextView(context);
		dateView.setText(formattedTimestamp(record.getBloodSugarDate(), record.getBloodSugarTime()));
		dateView.setPadding(sidePadding, topPadding, sidePadding, topPadding);
		addView(dateView, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
	}
	
	private String formattedTimestamp(String date, String time) {
		// TODO: Do some better formatting on this. This will render it as yyyy-mm-dd hh:mm
		return date + " " + time;
	}
}

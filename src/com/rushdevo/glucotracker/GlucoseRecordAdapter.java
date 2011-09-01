package com.rushdevo.glucotracker;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rushdevo.glucotracker.data.GlucoseRecord;

/**
 * @author jasonrush
 * Custom adapter for rendering GlucoseRecords in a list
 */
public class GlucoseRecordAdapter extends ArrayAdapter<GlucoseRecord> {
	private List<GlucoseRecord> records;
	private Context context;
	
	public GlucoseRecordAdapter(Context context, int textViewResourceId, List<GlucoseRecord> records) {
		super(context, textViewResourceId, records);
		this.records = records;
		this.context = context;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		GlucoseRecordView view;
		GlucoseRecord record = records.get(position);
		if (convertView == null) {
			view = new GlucoseRecordView(context, record);
		} else {
			view = (GlucoseRecordView)convertView;
			view.setRecord(record);
		}
		return view;
    }
}

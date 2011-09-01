package com.rushdevo.glucotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rushdevo.glucotracker.data.GlucoseRecord;
import com.rushdevo.glucotracker.data.GlucotrackerData;

/**
 * @author jasonrush
 * Main form for adding glucose records to the database
 */
public class TrackerForm extends Activity implements OnClickListener {
	private ScrollView formScrollView;
	private Button formButton;
	private EditText bloodSugarField;
	private DatePicker bloodSugarDateField;
	private TimePicker bloodSugarTimeField;
	private CheckBox bloodSugarMealField;
	private CheckBox bloodSugarCorrectionField;
	private TextView messageView;
	private GlucotrackerData dataDelegate;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_form);
        this.formScrollView = (ScrollView)findViewById(R.id.form_scroll_view);
        this.formButton = (Button)findViewById(R.id.form_button);
        this.formButton.setOnClickListener(this);
        this.bloodSugarField = (EditText)findViewById(R.id.blood_sugar_field);
        this.bloodSugarDateField = (DatePicker)findViewById(R.id.blood_sugar_date_field);
        this.bloodSugarTimeField = (TimePicker)findViewById(R.id.blood_sugar_time_field);
        this.bloodSugarMealField = (CheckBox)findViewById(R.id.blood_sugar_meal_field);
        this.bloodSugarCorrectionField = (CheckBox)findViewById(R.id.blood_sugar_correction_field);
        this.messageView = (TextView)findViewById(R.id.message_view);
        this.dataDelegate = new GlucotrackerData(this);
    }


	@Override
	public void onClick(View v) {
		if (bloodSugarField.getText().length() == 0) {
			showMessage("Blood Sugar can't be blank.", true);
		} else {
			Integer bloodSugar = new Integer(bloodSugarField.getText().toString());
			String bloodSugarDate = getDbDateFromDatePicker(bloodSugarDateField);
			String bloodSugarTime = getDbTimeFromTimePicker(bloodSugarTimeField);
			Boolean isMeal = bloodSugarMealField.isChecked();
			Boolean isCorrection = bloodSugarCorrectionField.isChecked();
			GlucoseRecord record = new GlucoseRecord(dataDelegate, bloodSugar, bloodSugarDate, bloodSugarTime, isMeal, isCorrection); 
			if (record.hasErrors()) {
				showMessage(record.getErrorMessage(), true);
			} else {
				showMessage("Successfully saved blood sugar.", false);
				clearForm();
			}
		}
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.settings:
    		// TODO
    		return true;
    	case R.id.list:
    		startActivity(new Intent(this, TrackerList.class));
    		return true;
    	case R.id.graph:
    		// TODO
    		return true;
    	// TODO: Addtl menu items go here
    	}
    	return false;
    }
	
	private String getDbDateFromDatePicker(DatePicker picker) {
		return picker.getYear() + "-" +
				String.format("%02d", (picker.getMonth() + 1)) + "-" +
				String.format("%02d", picker.getDayOfMonth());
	}
	
	private String getDbTimeFromTimePicker(TimePicker picker) {
		return String.format("%02d", picker.getCurrentHour()) + ":" +
				String.format("%02d", picker.getCurrentMinute()) + ":00";
	}
	
	private void showMessage(String msg, Boolean isError) {
		messageView.setVisibility(View.VISIBLE);
		messageView.setText(msg);
		if (isError) {
			messageView.setTextColor(getResources().getColor(R.color.error));
		} else {
			messageView.setTextColor(getResources().getColor(R.color.notice));
		}
		formScrollView.fullScroll(ScrollView.FOCUS_UP);
	}
	
	private void clearForm() {
        this.bloodSugarField.setText("");
        this.bloodSugarMealField.setChecked(false);
        this.bloodSugarCorrectionField.setChecked(false);
	}
}
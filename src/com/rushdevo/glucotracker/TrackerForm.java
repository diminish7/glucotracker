package com.rushdevo.glucotracker;

import java.util.ArrayList;
import java.util.List;

import com.rushdevo.glucotracker.data.GlucoseRecord;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class TrackerForm extends Activity implements OnClickListener {
	private Button formButton;
	private EditText bloodSugarField;
	private DatePicker bloodSugarDateField;
	private TimePicker bloodSugarTimeField;
	private CheckBox bloodSugarMealField;
	private CheckBox bloodSugarCorrectionField;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_form);
        this.formButton = (Button)findViewById(R.id.form_button);
        this.formButton.setOnClickListener(this);
        this.bloodSugarField = (EditText)findViewById(R.id.blood_sugar_field);
        this.bloodSugarDateField = (DatePicker)findViewById(R.id.blood_sugar_date_field);
        this.bloodSugarTimeField = (TimePicker)findViewById(R.id.blood_sugar_time_field);
        this.bloodSugarMealField = (CheckBox)findViewById(R.id.blood_sugar_meal_field);
        this.bloodSugarCorrectionField = (CheckBox)findViewById(R.id.blood_sugar_correction_field);
    }


	@Override
	public void onClick(View v) {
		if (bloodSugarField.getText().length() == 0) {
			// TODO: Show error dialog
		} else {
			Integer bloodSugar = new Integer(bloodSugarField.getText().toString());
			String bloodSugarDate = getDbDateFromDatePicker(bloodSugarDateField);
			String bloodSugarTime = getDbTimeFromTimePicker(bloodSugarTimeField);
			Boolean isMeal = bloodSugarMealField.isChecked();
			Boolean isCorrection = bloodSugarCorrectionField.isChecked();
			GlucoseRecord record = new GlucoseRecord(this, bloodSugar, bloodSugarDate, bloodSugarTime, isMeal, isCorrection);
			if (record.hasErrors()) {
				// TODO: Show error dialog
			} else {
				// TODO: Show success message
			}
		}
		
	}
	
	private String getDbDateFromDatePicker(DatePicker picker) {
		return picker.getYear() + "-" +
				String.format("%02d", picker.getMonth()) + "-" +
				String.format("%02d", picker.getDayOfMonth());
	}
	
	private String getDbTimeFromTimePicker(TimePicker picker) {
		return String.format("%02d", picker.getCurrentHour()) + ":" +
				String.format("%02d", picker.getCurrentMinute()) + ":00";
	}
}
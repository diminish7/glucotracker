/**
 * 
 */
package com.rushdevo.glucotracker;

/**
 * @author jasonrush
 * Interface that activities using the GlucoseRecordList must implement
 */
public interface GlucoseRecordListable {
	/**
	 * Called after the date change dialog's positive button is pressed
	 */
	public void afterDateChange();
}

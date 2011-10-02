package com.rushdevo.glucotracker;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jasonrush
 * Formatter for date labels on the graph view
 */
public class GraphDateFormat extends Format {
	private static final long serialVersionUID = 2681644514341572458L;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
	
	/* (non-Javadoc)
	 * @see java.text.Format#format(java.lang.Object, java.lang.StringBuffer, java.text.FieldPosition)
	 */
	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        long timestamp = ((Number) obj).longValue();
        Date date = new Date(timestamp);
        return dateFormat.format(date, toAppendTo, pos);
    }

	/* (non-Javadoc)
	 * @see java.text.Format#parseObject(java.lang.String, java.text.ParsePosition)
	 */
	@Override
	public Object parseObject(String string, ParsePosition position) {
		return null;
	}

}

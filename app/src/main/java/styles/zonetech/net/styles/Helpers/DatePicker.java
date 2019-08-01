package styles.zonetech.net.styles.Helpers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import styles.zonetech.net.styles.R;

public class DatePicker {

    public final Calendar myCalendar = Calendar.getInstance();
    TextView textView;
    Context mContext;
    public static  boolean isDateSet;
    public static  boolean  isTimeSet;
    public boolean isCreditCard;
    public boolean isDate;

    public DatePicker(Context mContext,TextView textView,boolean isCreditCard,boolean isDate) {
        this.textView=textView;
        this.mContext=mContext;
        this.isCreditCard=isCreditCard;
        this.isDate=isDate;
        if(this.isDate){

          DatePickerDialog datePickerDialog=  new DatePickerDialog(mContext, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
//          datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
          datePickerDialog.show();
        }
        else {

            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = myCalendar.get(Calendar.MINUTE);
          new TimePickerDialog(mContext,time,hour,minute,DateFormat.is24HourFormat(mContext)).show();
        }
    }

    public DatePicker() {
    }

    private void setDate(TextView textView,boolean isCreditCard) {
        String myFormat;
        if(isDate){
            if(isCreditCard){
                myFormat = "MM/yyyy";
            }
            else{
                myFormat="yyyy-MM-dd";
            }

        }
        else{
            myFormat="hh:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA_FRENCH);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        textView.setText(sdf.format(myCalendar.getTime()));
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setDate(textView,isCreditCard);
            isDateSet=true;



        }

    };

    TimePickerDialog.OnTimeSetListener time =new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            setDate(textView,false);
            isTimeSet=true;

        }
    };

}

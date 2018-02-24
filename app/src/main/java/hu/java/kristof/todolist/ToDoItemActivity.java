package hu.java.kristof.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import hu.java.kristof.todolist.model.ToDoItem;

/**
 * Created by Kristof Toth on 2018. 02. 16..
 */

public class ToDoItemActivity extends AppCompatActivity {
    private Intent intent;
    private ToDoItem doItem;

    private EditText etName;
    private EditText etVerbose;

    private RadioGroup rgGroup;
    private RadioButton rbTopprio;
    private RadioButton rbImportant;
    private RadioButton rbNotimp;

    private TextView tvDate;
    private TextView tvHour;

    private CheckBox cbDone;

    private Button btnHour;

    private Button btnTomorrow;
    private Button btnOther;
    private int year_x, month_x, day_x;
    private int hour_x, minute_x;
    static final int DIALOG_ID_DATEPICKER = 0;
    static final int DIALOG_ID_TIMEPICKER = 1;
    private Button btnSave;
    private Button btnCancel;
    private boolean doneChanged = false;
    private boolean isDoneInput;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_todoitem_view);
        intent = getIntent();
        etName = findViewById(R.id.etName);
        etVerbose = findViewById(R.id.etName);
        rgGroup = findViewById(R.id.rgGroup);
        rbTopprio = findViewById(R.id.rbTopprio);
        rbImportant = findViewById(R.id.rbImportant);
        rbNotimp = findViewById(R.id.rbImportant);
        tvDate = findViewById(R.id.tvDate);
        tvHour = findViewById(R.id.tvHour);
        cbDone = findViewById(R.id.cbDone);
        btnHour = findViewById(R.id.btnHour);

        btnTomorrow = findViewById(R.id.btnTomorrow);
        btnOther = findViewById(R.id.btnOther);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);


        doItem = (ToDoItem) intent.getSerializableExtra("doItem");
        if (doItem != null) {
            etName.setText(doItem.getItemName());
            etVerbose.setText(doItem.getItemVerboseText());
            switch (doItem.getItemPriority()) {
                case 1:
                    rgGroup.check(R.id.rbTopprio);
                    break;
                case 2:
                    rgGroup.check(R.id.rbImportant);
                    break;
                case 3:
                    rgGroup.check(R.id.rbNotimp);
                    break;
                case 4:
                    break;
            }
            if (doItem.isDone()) {
                cbDone.setChecked(true);
                isDoneInput = true;
            } else {
                isDoneInput = false;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
            String dateString = dateFormat.format(doItem.getItemDate());
            String hourString = hourFormat.format(doItem.getItemDate());
            tvDate.setText(dateString);
            tvHour.setText(hourString);

            Date tempDate = doItem.getItemDate();

            Calendar cal = Calendar.getInstance();
            cal.setTime(tempDate);
            year_x = cal.get(Calendar.YEAR);
            month_x = cal.get(Calendar.MONTH)+1;
            day_x = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            final Calendar cal = Calendar.getInstance();
            year_x = cal.get(Calendar.YEAR);
            month_x = cal.get(Calendar.MONTH)+1;
            day_x = cal.get(Calendar.DAY_OF_MONTH);
            String dateString = formatDate(year_x + "." + month_x + "." + day_x);
            tvDate.setText(dateString);
            tvHour.setText("24:00");
        }
    }

    public void showTimePickerDialog(View view) {
        btnHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID_TIMEPICKER);
            }
        });
    }

    public void showDatePickerDialog(View view) {
        btnOther.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(DIALOG_ID_DATEPICKER);
                    }
                }

        );
    }

    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID_DATEPICKER) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x-1, day_x);
        } else if (id == DIALOG_ID_TIMEPICKER) {
            return new TimePickerDialog(this, kTimePickerListener, hour_x, minute_x, true);
        } else {
            return null;
        }
    }

    private TimePickerDialog.OnTimeSetListener kTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            hour_x = hour;
            minute_x = minute;
            tvHour.setText(hour_x + ":" + minute_x);
        }
    };
    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            year_x = year;
            month_x = month + 1;
            day_x = day;
            tvDate.setText(formatDate(year_x + "." + month_x + "." + day_x));

        }
    };

    public void SaveItem(View view) {
        if (doItem == null) {
            doItem = new ToDoItem();
        }

        doItem.setItemName(etName.getText().toString());
        doItem.setItemVerboseText(etVerbose.getText().toString());
        int selectedPriority = -1;
        int checkedRadioButtonId = rgGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rbTopprio) {
            selectedPriority = 1;
        } else if (checkedRadioButtonId == R.id.rbImportant) {
            selectedPriority = 2;
        } else if (checkedRadioButtonId == R.id.rbNotimp) {
            selectedPriority = 3;
        }

        doItem.setItemPriority(selectedPriority);
        String tempDate = tvDate.getText() + " " + tvHour.getText();
        DateFormat sv = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date date = null;
        try {
            date = sv.parse(tempDate);
        } catch (ParseException e) {
        }
        doItem.setItemDate(date);
        if (cbDone.isChecked()) {

            doItem.setDone(true);
            doItem.setItemPriority(4);
            if (!isDoneInput) {
                doneChanged = true;
            }
        } else {
            doItem.setDone(false);
            if (isDoneInput) {
                doneChanged = true;
            }

        }
        if (doItem.getItemName().length() < 3) {

            Toast.makeText(ToDoItemActivity.this, "The item's name has to be at least 3 characters long", Toast.LENGTH_LONG).show();
        } else {
            intent.putExtra("doItem", doItem);
            intent.putExtra("isDoneChanged", doneChanged);
            setResult(RESULT_OK, intent);
            finish();

        }
    }

    public void Cancel(View view) {
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void setDateTomorrow(View view) {
        String tempDate = tvDate.getText() + "";
        DateFormat sv = new SimpleDateFormat("yyyy.MM.dd");
        Calendar c = Calendar.getInstance();

        try {

            c.setTime(sv.parse(tempDate));
            c.add(Calendar.DATE, 1);
            tempDate = sv.format(c.getTime());
            tvDate.setText(tempDate);
        } catch (ParseException e) {
            Log.e("TODOAPP", "PARSEEXCEPTION: " + e);
        }


    }

    public String formatDate(String dateString) {
        String tempDate = dateString;
        DateFormat sv = new SimpleDateFormat("yyyy.MM.dd");
        Calendar c = Calendar.getInstance();

        try {

            c.setTime(sv.parse(tempDate));

            tempDate = sv.format(c.getTime());
            tvDate.setText(tempDate);


        } catch (ParseException e) {
            Log.e("TODOAPP", "PARSEEXCEPTION: " + e);
        }
        return tempDate;
    }


}

package com.eliassilva.sportagenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewEditEvent extends AppCompatActivity {
    @BindView(R.id.done_fab)
    FloatingActionButton mDoneFab;
    @BindView(R.id.sport_input_et)
    EditText mSportInput;
    @BindView(R.id.local_input_et)
    EditText mLocalInput;
    @BindView(R.id.date_input_et)
    EditText mDateInpute;
    @BindView(R.id.time_input_et)
    EditText mTimeInput;
    @BindView(R.id.participants_input_et)
    EditText mParticipantsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_event);
        ButterKnife.bind(this);

        mDateInpute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEditEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String dateFormat = getString(R.string.date_format);
                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

                        mDateInpute.setText(sdf.format(c.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        mTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewEditEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        String timeFormat = getString(R.string.time_format);
                        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);

                        mTimeInput.setText(sdf.format(c.getTime()));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        mDoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setEmailIntent() != null) {
                    startActivity(setEmailIntent());
                }
            }
        });
    }

    private Intent setEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, setEmailBody());
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            if (mSportInput.getText().toString().equals("")) {
                Toast.makeText(NewEditEvent.this, getString(R.string.sport_field_empty), Toast.LENGTH_LONG).show();
                return null;
            }
            return emailIntent;
        }
        return null;
    }

    private Spanned setEmailBody() {
        return Html.fromHtml(new StringBuilder()
                .append("<h1 style=\"color: #5e9ca0;\">SportAgenda <span style=\"color: #2b2301;\">")
                .append("<p>Hello Sportsman. I would like to invite you to participate in my event.<p>")
                .append("<h4 style=\"color: #2e6c80;\">Event details:</h4>")
                .append("<ol style=\"list-style: none; font-size: 14px; line-height: 32px; font-weight: bold;\">\n" +
                        "<li style=\"clear: both;\">Sport: " + mSportInput.getText().toString() + "</li>\n" +
                        "  <li style=\"clear: both;\">Date: " + mDateInpute.getText().toString() + "</li>\n" +
                        "  <li style=\"clear: both;\">Time: " + mTimeInput.getText().toString() + "</li>\n" +
                        "  <li style=\"clear: both;\">Local: " + mLocalInput.getText().toString() + "</li>\n" +
                        "  <li style=\"clear: both;\">Nº of Participants: " + mParticipantsInput.getText().toString() + "</li>\n" +
                        "</ol>")
                .toString());
    }
}

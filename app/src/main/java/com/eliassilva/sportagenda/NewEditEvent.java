package com.eliassilva.sportagenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewEditEvent extends AppCompatActivity {
    @BindView(R.id.done_fab)
    FloatingActionButton mDoneFab;
    @BindView(R.id.delete_fab)
    FloatingActionButton mDeleteFab;
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
    private Event mCurrentEvent;
    private FirebaseUser mUser;
    private String mCurrentEventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_event);
        ButterKnife.bind(this);

        mLocalInput.setHint(getString(R.string.default_local_value));
        mParticipantsInput.setHint(getString(R.string.default_participants_value));

        Intent intent = getIntent();
        mCurrentEvent = intent.getParcelableExtra(getString(R.string.event));
        mCurrentEventKey = intent.getStringExtra(getString(R.string.eventKey));

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mCurrentEvent == null) {
            mDeleteFab.setVisibility(View.GONE);
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
                    DatabaseReference userEvents = getDatabaseReference();
                    DatabaseReference eventKey = userEvents.push();

                    if (mSportInput.getText().toString().equals("") || mDateInpute.getText().toString().equals("") || mTimeInput.getText().toString().equals("")) {
                        Toast.makeText(NewEditEvent.this, getString(R.string.empty_field_error), Toast.LENGTH_LONG).show();
                    } else {
                        eventKey.child(getString(R.string.db_sports)).setValue(mSportInput.getText().toString());
                        eventKey.child(getString(R.string.db_date)).setValue(mDateInpute.getText().toString());
                        eventKey.child(getString(R.string.db_time)).setValue(mTimeInput.getText().toString());

                        String dateAndTime = mDateInpute.getText().toString() + " " + mTimeInput.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_format) + " " + getString(R.string.time_format));
                        Date dateFormated = null;
                        try {
                            dateFormated = format.parse(dateAndTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long milliseconds = dateFormated.getTime();
                        eventKey.child(getString(R.string.db_date_in_milliseconds)).setValue(milliseconds);

                        if (mLocalInput.getText().toString().equals("")) {
                            mLocalInput.setText(getString(R.string.default_local_value));
                        }
                        eventKey.child(getString(R.string.db_local)).setValue(mLocalInput.getText().toString());

                        if (mParticipantsInput.getText().toString().equals("")) {
                            mParticipantsInput.setText(getString(R.string.default_participants_value));
                        }
                        eventKey.child(getString(R.string.db_participants)).setValue(mParticipantsInput.getText().toString());

                        Intent intent = new Intent(NewEditEvent.this, MainActivity.class);
                        startActivity(intent);

                        if (setEmailIntent() != null) {
                            startActivity(setEmailIntent());
                        }
                    }
                }
            });
        } else {
            mDoneFab.setVisibility(View.GONE);

            mSportInput.setText(mCurrentEvent.sports);
            mDateInpute.setText(mCurrentEvent.date);
            mTimeInput.setText(mCurrentEvent.time);
            mLocalInput.setText(mCurrentEvent.local);
            mParticipantsInput.setText(mCurrentEvent.participants);

            disableEditText(mSportInput);
            disableEditText(mDateInpute);
            disableEditText(mTimeInput);
            disableEditText(mLocalInput);
            disableEditText(mParticipantsInput);

            mDeleteFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference userEvents = getDatabaseReference();
                    DatabaseReference eventToDelete = userEvents.child(mCurrentEventKey);
                    eventToDelete.removeValue();
                    Toast.makeText(NewEditEvent.this, getString(R.string.event_deleted), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewEditEvent.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setClickable(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private DatabaseReference getDatabaseReference() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference events = database.getReference(getString(R.string.db_events));
        DatabaseReference userEvents = events.child(mUser.getUid());
        return userEvents;
    }

    private Intent setEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, setEmailBody());
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            return emailIntent;
        }
        return null;
    }

    private Spanned setEmailBody() {
        String emailContent = getString(R.string.email_content, new String[]{
                mSportInput.getText().toString(),
                mDateInpute.getText().toString(),
                mTimeInput.getText().toString(),
                mLocalInput.getText().toString(),
                mParticipantsInput.getText().toString()
        });
        return Html.fromHtml(emailContent);
    }
}

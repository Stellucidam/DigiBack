package ch.heigvd.digiback.ui.fragment.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.utils.Day;
import ch.heigvd.digiback.business.utils.Month;

public class ActivityFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "ActivityFragment";
    private TextView stepCountTextView;
    private Date selectedDay;
    private TextView currentDate;

    private TableLayout table;

    private float totalSteps = 0;
    private float previousStepCount = 0;
    private boolean running = false;
    private SensorManager sensorManager;

    private ScheduledExecutorService scheduler;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        saveData();
        loadData();

        View root = inflater.inflate(R.layout.fragment_activity, container, false);

        sensorManager = (SensorManager) getActivity().getSystemService(getContext().SENSOR_SERVICE);

        stepCountTextView = root.findViewById(R.id.steps_total);
        currentDate = root.findViewById(R.id.text_date);

        table = root.findViewById(R.id.calendar_table);

        Calendar calendar = Calendar.getInstance();
        selectedDay = calendar.getTime();
        setCalendar(calendar);

        // Set scheduled data sending
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::saveSteps, 0, 24, TimeUnit.SECONDS);

        return root;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running) {
            totalSteps = sensorEvent.values[0];
            float current = totalSteps - previousStepCount;

            stepCountTextView.setText((int)current + "");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null) {
            Toast.makeText(getContext(), "No sensor detected oon this device.", Toast.LENGTH_LONG).show();
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // Not implemented
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("key1", previousStepCount);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        float savedNumber = sharedPreferences.getFloat("key1", 0);
        Log.d(TAG, "savedNumber : " + savedNumber);
        previousStepCount = savedNumber;
    }

    private void saveSteps() {
        // TODO
        Log.d(TAG, "Save steps " + totalSteps);
    }

    private void setCalendar(Calendar calendar) {
        // set the current date on top of the calendar
        calendar.setTime(selectedDay);
        String language = Locale.getDefault().getDisplayLanguage();
        if (language.equals("français")) {
            currentDate.setText(Day.getDay(selectedDay.getDay()).french() + " " +
                    selectedDay.getDate() + " " +
                    Month.getMonth(selectedDay.getMonth()).french());
        } else {
            currentDate.setText(Day.getDay(selectedDay.getDay()).english() + " " +
                    selectedDay.getDate() + " " +
                    Month.getMonth(selectedDay.getMonth()).english());
        }

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        TableRow rowDate = new TableRow(getContext());
        TableRow rowDay = new TableRow(getContext());

        for (int i = 0; i < 7; i++) {
            Date dateTime = calendar.getTime();

            TextView date = new TextView(getContext());
            date.setText("" + dateTime.getDate());
            date.setGravity(Gravity.CENTER);
            date.setLayoutParams(new TableRow.LayoutParams(i));
            date.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

            TextView day = new TextView(getContext());
            day.setLayoutParams(new TableRow.LayoutParams(i));
            day.setGravity(Gravity.CENTER);
            day.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            if (language.equals("français")) {
                day.setText(Day.getDay(calendar.getTime().getDay()).frenchAbbreviation());
            } else {
                day.setText(Day.getDay(calendar.getTime().getDay()).englishAbbreviation());
            }

            if (calendar.getTime().getDay() == selectedDay.getDay()) {
                day.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                date.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            }

            date.setOnClickListener(view -> {
                selectedDay = dateTime;
                table.removeAllViews();
                setCalendar(calendar);
            });
            day.setOnClickListener(view -> {
                selectedDay = dateTime;
                table.removeAllViews();
                setCalendar(calendar);
            });

            rowDay.addView(day);
            rowDate.addView(date);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        table.addView(rowDay);
        table.addView(rowDate);
    }
}

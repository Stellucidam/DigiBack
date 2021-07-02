package ch.heigvd.digiback.ui.fragment.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.activity.GetActivity;
import ch.heigvd.digiback.business.api.activity.PostStep;
import ch.heigvd.digiback.business.api.activity.iOnActivityFetched;
import ch.heigvd.digiback.business.api.activity.iOnStepFetched;
import ch.heigvd.digiback.business.model.activity.Activity;
import ch.heigvd.digiback.business.model.activity.Step;
import ch.heigvd.digiback.business.utils.Day;
import ch.heigvd.digiback.business.utils.Month;

public class ActivityFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "ActivityFragment";
    private TextView currentDate;
    private TextView stepCountTextView;
    private TextView quizCountTextView;
    private TextView exerciseCountTextView;

    private Date selectedDay;

    private TableLayout table;

    private float currentSteps = 0;
    private float previousStepCount = 0;
    private boolean running = false;
    private SensorManager sensorManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        saveData();
        loadData();

        View root = inflater.inflate(R.layout.fragment_activity, container, false);

        sensorManager = (SensorManager) getActivity().getSystemService(getContext().SENSOR_SERVICE);

        stepCountTextView = root.findViewById(R.id.steps_total);
        exerciseCountTextView = root.findViewById(R.id.exercise_total);
        quizCountTextView = root.findViewById(R.id.quiz_total);
        currentDate = root.findViewById(R.id.text_date);

        table = root.findViewById(R.id.calendar_table);

        Calendar calendar = Calendar.getInstance();
        selectedDay = calendar.getTime();
        setCalendar(calendar);

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running) {
            float totalSteps = sensorEvent.values[0];

            this.currentSteps = totalSteps - previousStepCount;

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Timestamp(cal.getTime().getTime()));

            PostStep postStep = new PostStep(
                    new Step(new java.sql.Date(cal.getTime().getTime()), (long) this.currentSteps),
                    new iOnStepFetched() {
                        @Override
                        public void showProgressBar() {

                        }

                        @Override
                        public void hideProgressBar() {

                        }

                        @Override
                        public void setDataInPageWithResult(Step step) {
                            // TODO do something with the data
                        }
                    });
            try {
                final TaskRunner taskRunner = new TaskRunner();
                taskRunner.executeAsync(postStep);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

            setActivities();
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
        previousStepCount = sharedPreferences.getFloat("key1", 0);
    }


    private void setCalendar(Calendar calendar) {
        setActivities();

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

    private void setActivities() {
        Calendar calendar = Calendar.getInstance();
        Log.d(TAG, selectedDay.toString() + " == " + calendar.getTime().toString() + " ? ");
        if (selectedDay.getDate() == calendar.getTime().getDate()) {
            stepCountTextView.setText((int) currentSteps);
        } else {
            stepCountTextView.setText("0");
            exerciseCountTextView.setText("0");
            quizCountTextView.setText("0");
        }

        try {
            final TaskRunner taskRunner = new TaskRunner();
            taskRunner.executeAsync(new GetActivity(selectedDay.toString(), new iOnActivityFetched() {
                @Override
                public void showProgressBar() {

                }

                @Override
                public void hideProgressBar() {

                }

                @Override
                public void setDataInPageWithResult(Activity activity) {
                    // TODO
                    Log.d(TAG, activity.toString());

                    stepCountTextView.setText(activity.getNbrSteps().intValue());
                    exerciseCountTextView.setText(activity.getNbrExercises().intValue());
                    quizCountTextView.setText(activity.getNbrQuiz().intValue());
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }
}

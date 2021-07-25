package ch.heigvd.digiback.ui.fragment.calendar;

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
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.activity.GetActivity;
import ch.heigvd.digiback.business.api.activity.PostStep;
import ch.heigvd.digiback.business.api.activity.iOnActivityFetched;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.model.Activity;
import ch.heigvd.digiback.business.model.Status;
import ch.heigvd.digiback.business.model.Step;
import ch.heigvd.digiback.ui.fragment.exercise.ExerciseFragment;
import ch.heigvd.digiback.ui.fragment.quiz.QuizFragment;

public class CalendarFragment extends Fragment implements SensorEventListener {
    private static final String TAG = "CalendarFragment";
    private TextView currentDate;
    private TextView stepCountTextView;
    private TextView quizCountTextView;
    private TextView exerciseCountTextView;

    private CardView exercise, quiz;

    private Button addMobility;

    private Date selectedDay;

    private TableLayout table;

    private float currentSteps = 0;
    private float previousStepCount = 0;
    private boolean running = false;
    private SensorManager sensorManager;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        saveData();
        loadData();

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        sensorManager = (SensorManager) getActivity().getSystemService(getContext().SENSOR_SERVICE);

        exercise = root.findViewById(R.id.activity_card_exercise);
        quiz = root.findViewById(R.id.activity_card_quiz);
        setCardViewsOnClickListener();

        stepCountTextView = root.findViewById(R.id.steps_total);
        exerciseCountTextView = root.findViewById(R.id.exercise_total);
        quizCountTextView = root.findViewById(R.id.quiz_total);
        currentDate = root.findViewById(R.id.text_date);
        addMobility = root.findViewById(R.id.add_pain);

        table = root.findViewById(R.id.calendar_table);

        Calendar calendar = Calendar.getInstance();
        selectedDay = calendar.getTime();
        setCalendar(calendar);

        setAddMobilityOnClickListener();

        return root;
    }

    private void setCardViewsOnClickListener() {
        exercise.setOnClickListener(view -> {
            FragmentTransaction transaction;
            if (getFragmentManager() != null) {
                transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                // TODO set title on fragment (navigation)
                transaction.replace(R.id.nav_host_fragment, new ExerciseFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        quiz.setOnClickListener(view -> {
            FragmentTransaction transaction;
            if (getFragmentManager() != null) {
                transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                // TODO set title on fragment (navigation)
                transaction.replace(R.id.nav_host_fragment, new QuizFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
    }

    private void setAddMobilityOnClickListener() {
        addMobility.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(getContext(), view);
            popup.setOnMenuItemClickListener(item -> {
                int painLevel = 0;
                Log.d(TAG, "Selected pain : " + item.getTitle() + " id " + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.pain_0:
                        painLevel = 0;
                        break;
                    case R.id.pain_1:
                        painLevel = 1;
                        break;
                    case R.id.pain_2:
                        painLevel = 2;
                        break;
                    case R.id.pain_3:
                        painLevel = 3;
                        break;
                    case R.id.pain_4:
                        painLevel = 4;
                        break;
                    case R.id.pain_5:
                        painLevel = 5;
                        break;
                    case R.id.pain_6:
                        painLevel = 6;
                        break;
                    case R.id.pain_7:
                        painLevel = 7;
                        break;
                    case R.id.pain_8:
                        painLevel = 8;
                        break;
                    case R.id.pain_9:
                        painLevel = 9;
                        break;
                    case R.id.pain_10:
                        painLevel = 10;
                        break;
                }

                return true;
            }
            );
            MenuInflater inflater1 = popup.getMenuInflater();
            inflater1.inflate(R.menu.add_pain, popup.getMenu());
            popup.show();
        });
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
                new iOnStatusFetched() {
                    @Override
                    public void showProgressBar() {

                    }

                    @Override
                    public void hideProgressBar() {

                    }

                    @Override
                    public void setDataInPageWithResult(Status status) {
                        // TODO do something with the data
                        Log.i(TAG, "Sent steps ");
                    }
                }
            );
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
            Toast.makeText(getContext(), "No sensor detected on this device.", Toast.LENGTH_LONG).show();
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setCalendar(Calendar calendar) {
        setActivities();

        // set the current date on top of the calendar
        calendar.setTime(selectedDay);
        SimpleDateFormat dayMonthYearFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat dayAbbreviationFormat = new SimpleDateFormat("EE");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

        currentDate.setText(dayMonthYearFormat.format(calendar.getTime()));

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        TableRow rowDate = new TableRow(getContext());
        TableRow rowDay = new TableRow(getContext());

        for (int i = 0; i < 7; i++) {
            Date dateTime = calendar.getTime();

            TextView date = new TextView(getContext());
            date.setText(dayFormat.format(calendar.getTime()));
            date.setGravity(Gravity.CENTER);
            date.setLayoutParams(new TableRow.LayoutParams(i));
            date.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

            TextView day = new TextView(getContext());
            day.setLayoutParams(new TableRow.LayoutParams(i));
            day.setGravity(Gravity.CENTER);
            day.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            day.setText(dayAbbreviationFormat.format(calendar.getTime()));

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
        exerciseCountTextView.setText("-");
        quizCountTextView.setText("-");

        Calendar calendar = Calendar.getInstance();
        if (selectedDay.getDate() == calendar.getTime().getDate()) {
            stepCountTextView.setText((int) currentSteps + "");
        } else {
            stepCountTextView.setText("-");
        }

        try {
            final TaskRunner taskRunner = new TaskRunner();
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            taskRunner.executeAsync(new GetActivity(simpleDateFormat.format(selectedDay), new iOnActivityFetched() {
                @Override
                public void showProgressBar() {

                }

                @Override
                public void hideProgressBar() {

                }

                @Override
                public void setDataInPageWithResult(Activity activity) {
                    Log.d(TAG, activity.toString());

                    if (activity != null) {
                        stepCountTextView.setText("" + activity.getNbrSteps().intValue());
                        exerciseCountTextView.setText("" + activity.getNbrExercises().intValue());
                        quizCountTextView.setText("" + activity.getNbrQuiz().intValue());
                    } else {
                        stepCountTextView.setText("Nan");
                        exerciseCountTextView.setText("Nan");
                        quizCountTextView.setText("Nan");
                    }
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }
}

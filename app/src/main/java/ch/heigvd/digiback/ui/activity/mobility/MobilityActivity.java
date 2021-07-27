package ch.heigvd.digiback.ui.activity.mobility;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.api.movement.PostMovement;
import ch.heigvd.digiback.business.model.Movement;
import ch.heigvd.digiback.business.model.MovementType;
import ch.heigvd.digiback.business.model.Status;

public class MobilityActivity extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "MobilityActivity";
    public static final float CST = 57.2957795f;

    private final TaskRunner runner = new TaskRunner();

    private PopupWindow
            helpPopUp,
            validateMovementPopup;
    private Sensor
            mAccelerometer,
            mMagField;
    private SensorManager mSensorManager;
    private Spinner spinner;

    private View movementPopView;
    private AnyChartView movementChartView;
    private FloatingActionButton back;
    private TextView angleInfo;
    private Button
            measureStarter,
            help,
            sendMeasures,
            cancelMeasures,
            addPain;

    private MovementType selectedMovementType;
    private LinkedList<Float> allAngles = new LinkedList<>();
    private boolean
            calculateAngles = false,
            firstMeasure = true;
    private float[]
            Rot = null,
            I = null,
            accels = new float[3],
            mags = new float[3],
            values = new float[3];
    private float
            pitch,
            roll,
            max,
            min;
    private int painLevel = -1;

    // Self timer part
    private Timer timer = new Timer();
    private View view;
    private TextView time;
    private MutableLiveData<Long> startTime = new MutableLiveData<>(0L);
    private final Handler h = new Handler(new Callback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean handleMessage(Message msg) {
            long millis = System.currentTimeMillis() - startTime.getValue();
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds     = seconds % 60;

            time.setText(String.format("%d:%02d", minutes, seconds));
            if (seconds == 3 && minutes == 0) {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                Log.d(TAG, "Should sound an alarm");
                r.play();

                // Start measures
                allAngles.clear();
                calculateAngles = true;
            }

            if (seconds == 8 && minutes == 0) {
                // Re-enable measure starter
                enable(measureStarter);
                enable(spinner);

                // Stop timer
                timer.cancel();
                timer.purge();

                // Stop measures
                calculateAngles = false;
                firstMeasure = true;

                movementPopView = getLayoutInflater()
                        .inflate(R.layout.popup_movement_validation, null, false);
                validateMovementPopup = new PopupWindow(
                        movementPopView,
                        100,100, true);

                // Validate the movement
                validateMovementPopup.setElevation(10);
                validateMovementPopup.showAtLocation(view, Gravity.CENTER, 10, 10);
                validateMovementPopup.update(800, 1500);

                movementChartView = movementPopView.findViewById(R.id.angles_graph);
                sendMeasures = movementPopView.findViewById(R.id.validate_measure);
                cancelMeasures = movementPopView.findViewById(R.id.cancel_measure);
                addPain = movementPopView.findViewById(R.id.add_pain);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setPopUpGraphAndOnClicks();
                } else {
                    Log.e(TAG, "The build version (" + Build.VERSION.SDK_INT + ") does not allow for a chart view !");
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // we need fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_mobility);

        view = findViewById(R.id.movement_spinner).getRootView();
        spinner = findViewById(R.id.movement_spinner);
        angleInfo = findViewById(R.id.angle);
        back = findViewById(R.id.floating_back_button);
        measureStarter = findViewById(R.id.start_measure);
        help = findViewById(R.id.help);
        helpPopUp = new PopupWindow(
                getLayoutInflater()
                        .inflate(R.layout.popup_movement_help, null, false),
                100,100, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOnClicks();
        } else {
            Log.e(TAG, "Wrong SDK version : " + Build.VERSION.SDK_INT +
                    " should be >= " + Build.VERSION_CODES.LOLLIPOP);
        }

        // Set the movements spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movements_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        selectedMovementType = MovementType.NONE;

        // Self timer part
        time = (TextView)findViewById(R.id.text_timer);
    }

    @Override
    public void onSensorChanged(@NotNull SensorEvent event) {
        // Source : https://www.ssaurel.com/blog/get-android-device-rotation-angles-with-accelerometer-and-geomagnetic-sensors/
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mags = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accels = event.values.clone();
                break;
        }

        if (mags != null && accels != null && calculateAngles) {
            Rot = new float[9];
            I = new float[9];
            SensorManager.getRotationMatrix(Rot, I, accels, mags);

            float[] outR = new float[9];
            SensorManager.remapCoordinateSystem(Rot, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
            SensorManager.getOrientation(outR, values);

            pitch = values[1] * CST;
            roll = values[2] * CST;

            mags = null;
            accels = null;

            switch (selectedMovementType) {
                case FRONT_TILT:
                    if (firstMeasure) {
                        max = pitch;
                        min = pitch;
                        firstMeasure = false;
                    } else {
                        max = Math.max(max, pitch);
                        min = Math.min(min, pitch);
                    }
                    allAngles.add(Math.abs(pitch));
                    break;
                case RIGHT_TILT:
                case LEFT_TILT:
                    if (firstMeasure) {
                        max = roll;
                        min = roll;
                        firstMeasure = false;
                    } else {
                        max = Math.max(max, roll);
                        min = Math.min(min, roll);
                    }
                    allAngles.add(Math.abs(roll));
                    break;
                case NONE:
                    break;
            }

            angleInfo.setText("Nbr d'angles : " + allAngles.size());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                Log.w(TAG, "Sensor accuracy changed to HIGH");
                return;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                Log.w(TAG, "Sensor accuracy changed to MEDIUM");
                return;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                Log.w(TAG, "Sensor accuracy changed to LOW");
                return;
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                Log.w(TAG, "Sensor accuracy changed to UNRELIABLE");
                return;
            default:
                Log.w(TAG, "No idea what happened");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.i(TAG, "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());
        switch (pos) {
            case 1:
                selectedMovementType = MovementType.FRONT_TILT;
                enable(measureStarter);
                break;
            case 2:
                selectedMovementType = MovementType.RIGHT_TILT;
                enable(measureStarter);
                break;
            case 3:
                selectedMovementType = MovementType.LEFT_TILT;
                enable(measureStarter);
                break;
            default:
                selectedMovementType = MovementType.NONE;
                disable(measureStarter);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager
                .registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager
                .registerListener(this, mMagField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        timer.cancel();
        timer.purge();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setOnClicks() {
        // Button to start the measure
        measureStarter.setOnClickListener(view -> {
            startTime.setValue(System.currentTimeMillis());
            timer = new Timer();
            timer.schedule(new firstTask(), 0,500);
            disable(measureStarter);
            disable(spinner);
        });

        // Button to see instructions
        help.setOnClickListener(view -> {
            helpPopUp.setElevation(10);
            helpPopUp.showAtLocation(view, Gravity.CENTER, 10, 10);
            helpPopUp.update(800, 1000);
        });

        back.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    /**
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPopUpGraphAndOnClicks() {
        // OnClicks
        // Validate measures
        sendMeasures.setOnClickListener(view -> {
            movementChartView.clear();
            // Send measures to backend
            runner.executeAsync(new PostMovement(painLevel,
                    new Movement(
                            selectedMovementType,
                            new Date(Calendar.getInstance().getTime().getTime()),
                            allAngles),
                    new iOnStatusFetched() {
                        @Override
                        public void showProgressBar() {

                        }

                        @Override
                        public void hideProgressBar() {

                        }

                        @Override
                        public void setDataInPageWithResult(Status result) {
                            allAngles.clear();
                            painLevel = -1;
                            validateMovementPopup.dismiss();
                            Toast.makeText(getApplicationContext(), result.getStatus(), Toast.LENGTH_LONG).show();
                        }
                    }
            ));

        });

        // Cancel measures
        cancelMeasures.setOnClickListener(view ->  {
            movementChartView.clear();
            allAngles.clear();
            validateMovementPopup.dismiss();
        });

        // Add pain level
        addPain.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(item -> {
                        painLevel = 0;
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

        // Chart
        setChart();
    }

    /**
     * Disables the given view
     * @param view view to disable
     */
    private void disable(View view) {
        view.setEnabled(false);
        view.setAlpha(.5f);
    }

    /**
     * Enables the given view
     * @param view view to enable
     */
    private void enable(View view) {
        view.setEnabled(true);
        view.setAlpha(1f);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setChart() {
        Log.d(TAG, "Set chart with new values...");
        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title(getString(R.string.movement_text));
        cartesian.yAxis(0).title(getString(R.string.values));
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> angleSeries = new ArrayList<>();
        for (int i = 0; i < allAngles.size(); ++i) {
            angleSeries.add(new ValueDataEntry(i, allAngles.get(i)));
        }

        Set anglesSet = Set.instantiate();
        anglesSet.data(angleSeries);
        Mapping seriesMapping = anglesSet.mapAs("{ x: 'x', value: 'value' }");

        Line angleLine = cartesian.line(seriesMapping);
        angleLine.name(getString(R.string.angles));
        angleLine.hovered().markers().enabled(true);
        angleLine.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        angleLine.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontFamily("Montserrat");
        cartesian.legend().fontSize(14d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        movementChartView.setChart(cartesian);
    }

    //tells handler to send a message
    class firstTask extends TimerTask {

        @Override
        public void run() {
            h.sendEmptyMessage(0);
        }
    };
}
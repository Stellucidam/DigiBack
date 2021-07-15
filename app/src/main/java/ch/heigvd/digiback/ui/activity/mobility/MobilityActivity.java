package ch.heigvd.digiback.ui.activity.mobility;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.movement.PostMovement;
import ch.heigvd.digiback.business.model.movement.Movement;
import ch.heigvd.digiback.business.model.movement.MovementType;

// TODO Add self-timer for the measures
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
    private GraphView graph;
    private FloatingActionButton back;
    private TextView angleInfo;
    private Button
            measureStarter,
            measureStopper,
            help,
            sendMeasures,
            cancelMeasures;

    private MovementType selectedMovementType;
    private LinkedList<Float> allAngles = new LinkedList<>();
    private boolean
            calculateAngles = false,
            firstMeasure = true;
    float[]
            Rot = null,
            I = null,
            accels = new float[3],
            mags = new float[3],
            values = new float[3];
    float
            azimuth,
            pitch,
            roll,
            max,
            min;

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

        spinner = findViewById(R.id.movement_spinner);
        angleInfo = findViewById(R.id.angle);
        back = findViewById(R.id.floating_back_button);
        measureStarter = findViewById(R.id.start_measure);
        measureStopper = findViewById(R.id.stop_measure);
        help = findViewById(R.id.help);
        helpPopUp = new PopupWindow(
                getLayoutInflater()
                        .inflate(R.layout.popup_movement_help, null, false),
                100,100, true);

        movementPopView = getLayoutInflater()
                .inflate(R.layout.popup_movement_validation, null, false);
        validateMovementPopup = new PopupWindow(
                movementPopView,
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

        selectedMovementType = MovementType.UNKNOWN;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
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

            azimuth = values[0] * CST;
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
                    allAngles.add(pitch);
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
                    allAngles.add(roll);
                    break;
                case UNKNOWN:
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
                disable(measureStopper);
                break;
            case 2:
                selectedMovementType = MovementType.RIGHT_TILT;
                enable(measureStarter);
                disable(measureStopper);
                break;
            case 3:
                selectedMovementType = MovementType.LEFT_TILT;
                enable(measureStarter);
                disable(measureStopper);
                break;
            default:
                selectedMovementType = MovementType.UNKNOWN;
                disable(measureStarter);
                disable(measureStopper);
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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setOnClicks() {
        // Button to start the measure
        measureStarter.setOnClickListener(view -> {
            this.allAngles.clear();
            this.calculateAngles = true;
            disable(measureStarter);
            disable(spinner);
            enable(measureStopper);
        });

        // Button to stop the measure
        measureStopper.setOnClickListener(view -> {
            this.calculateAngles = false;
            this.firstMeasure = true;
            enable(measureStarter);
            enable(spinner);
            disable(measureStopper);
            Log.i(TAG, "Got " + allAngles.size() + " measures angles.");

            // Validate the movement
            validateMovementPopup.setElevation(10);
            validateMovementPopup.showAtLocation(view, Gravity.CENTER, 10, 10);
            validateMovementPopup.update(800, 1200);
            graph = movementPopView.findViewById(R.id.angles_graph);
            sendMeasures = movementPopView.findViewById(R.id.validate_measure);
            cancelMeasures = movementPopView.findViewById(R.id.cancel_measure);
            setPopUpGraphAndOnClicks();
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
    private void setPopUpGraphAndOnClicks() {
        // OnClicks
        // Validate measures
        sendMeasures.setOnClickListener(view -> {
            // Send measures to backend
            runner.executeAsync(new PostMovement(
                    new Movement(
                            selectedMovementType,
                            new Date(Calendar.getInstance().getTime().getTime()),
                            allAngles)
            ));

            allAngles.clear();
            validateMovementPopup.dismiss();
        });

        // Cancel measures
        cancelMeasures.setOnClickListener(view ->  {
            allAngles.clear();
            validateMovementPopup.dismiss();
        });

        // Graph
        graph.removeAllSeries();

        final DataPoint[] points = new DataPoint[allAngles.size()];

        for (int i = 0; i < allAngles.size(); ++i) {
            points[i] = new DataPoint(i, allAngles.get(i));
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

        series.setValueDependentColor(data -> Color.WHITE);
        series.setSpacing(1);

        // set manual y bounds to have nice steps
        graph.getViewport().setMinY(min - 5);
        graph.getViewport().setMaxY(max + 5);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.addSeries(series);
        graph.setAlpha(1f);
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
}
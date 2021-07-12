package ch.heigvd.digiback.ui.activity.mobility;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.LinkedList;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.utils.Movement;

public class MobilityActivity extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "MobilityActivity";
    public static final float CST = 57.2957795f;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagField;
    private Spinner spinner;

    private Button measureStarter, measureStopper;
    private TextView angleInfo;

    private Movement selectedMovement;

    private boolean calculateAngles = false, firstMeasure = true;

    private LinkedList<Float> allAngles = new LinkedList<>();

    float Rot[] = null;
    float I[] = null;
    float accels[] = new float[3];
    float mags[] = new float[3];
    float values[] = new float[3];

    float azimuth, pitch, roll, max, min;

    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // we need fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_mobility);

        spinner = findViewById(R.id.movement_spinner);
        angleInfo = findViewById(R.id.angle);
        measureStarter = findViewById(R.id.start_measure);
        measureStopper = findViewById(R.id.stop_measure);
        graph = findViewById(R.id.angles_graph);

        setOnClicks();

        // Set the movements spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movements_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        selectedMovement = Movement.UNKNOWN;
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


            switch (selectedMovement) {
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

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void setOnClicks() {
        // Button to start the measure
        measureStarter.setOnClickListener(view -> {
            this.allAngles.clear();
            this.calculateAngles = true;
        });

        // Button to stop the measure
        measureStopper.setOnClickListener(view -> {
            this.calculateAngles = false;
            this.firstMeasure = true;
            Log.i(TAG, "Got " + allAngles.size() + " measures angles.");
            setGraph();
        });
    }

    private void setGraph() {
        // Graph
        graph.removeAllSeries();

        final DataPoint[] points = new DataPoint[allAngles.size()];

        for (int i = 0; i < allAngles.size(); ++i) {
            points[i] = new DataPoint(i, allAngles.get(i));
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);

        series.setValueDependentColor(data -> Color.rgb(124, 54, 54));
        series.setSpacing(1);

        // set manual y bounds to have nice steps
        graph.getViewport().setMinY(min - 5);
        graph.getViewport().setMaxY(max + 5);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.addSeries(series);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.i(TAG, "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString());
        switch (pos) {
            case 1:
                selectedMovement = Movement.FRONT_TILT;
                break;
            case 2:
                selectedMovement = Movement.RIGHT_TILT;
                break;
            case 3:
                selectedMovement = Movement.LEFT_TILT;
                break;
            default:
                selectedMovement = Movement.UNKNOWN;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
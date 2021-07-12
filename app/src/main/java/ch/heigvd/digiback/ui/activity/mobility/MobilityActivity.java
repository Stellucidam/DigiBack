package ch.heigvd.digiback.ui.activity.mobility;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.LinkedList;

import ch.heigvd.digiback.R;

public class MobilityActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MobilityActivity";
    public static final float CST = 57.2957795f;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagField;

    private Button measureStarter, measureStopper;
    private TextView angleInfo;

    private boolean calculateAngles = false;

    private LinkedList<float[]> allAngles = new LinkedList<>();

    float Rot[]=null;
    float I[]=null;
    float accels[]=new float[3];
    float mags[]=new float[3];
    float[] values = new float[3];

    float azimuth;
    float pitch;
    float roll;

    ///

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

        angleInfo = findViewById(R.id.angle);
        measureStarter = findViewById(R.id.start_measure);
        measureStopper = findViewById(R.id.stop_measure);

        setOnClicks();

        graph = findViewById(R.id.angles_graph);
    }

    private void setOnClicks() {
        measureStarter.setOnClickListener(view -> {
            this.allAngles.clear();
            this.calculateAngles = true;
        });
        measureStopper.setOnClickListener(view -> {
            this.calculateAngles = false;
            angleInfo.setText("Nbr d'angles : " + allAngles.size());
            Log.i(TAG, "Got " + allAngles.size() + " measures angles.");
            setGraph();
        });
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

            allAngles.add(new float[]{azimuth, pitch, roll});

            angleInfo.setText("Nbr d'angles : " + allAngles.size());
            //angleInfo.setText("azimuth = " + azimuth + "\npitch = " + pitch + "\nroll = " + roll);
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

    private void setGraph() {
        // Graph
        graph.removeAllSeries();

        final DataPoint[] azimuths = new DataPoint[allAngles.size()];
        final DataPoint[] pitch = new DataPoint[allAngles.size()];
        final DataPoint[] roll = new DataPoint[allAngles.size()];

        for (int i = 0; i < allAngles.size(); ++i) {
            azimuths[i] = new DataPoint(i, allAngles.get(i)[0]);
            pitch[i] = new DataPoint(i, allAngles.get(i)[1]);
            roll[i] = new DataPoint(i, allAngles.get(i)[2]);
        }

        BarGraphSeries<DataPoint> seriesAz = new BarGraphSeries<>(azimuths);
        BarGraphSeries<DataPoint> seriesPi = new BarGraphSeries<>(pitch);
        BarGraphSeries<DataPoint> seriesRo = new BarGraphSeries<>(roll);

        // Around center angle
        seriesAz.setValueDependentColor(data -> Color.rgb(0, 0, 169));
        seriesAz.setSpacing(1);

        // Front angle
        seriesPi.setValueDependentColor(data -> Color.rgb(169, 0, 0));
        seriesPi.setSpacing(1);

        // Side angle
        seriesRo.setValueDependentColor(data -> Color.rgb(0, 169, 0));
        seriesRo.setSpacing(1);

        graph.addSeries(seriesAz);
        graph.addSeries(seriesPi);
        graph.addSeries(seriesRo);
    }
}
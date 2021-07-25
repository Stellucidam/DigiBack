package ch.heigvd.digiback.ui.activity.mobility;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.digiback.R;
import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.iOnStatusFetched;
import ch.heigvd.digiback.business.api.movement.PostMovement;
import ch.heigvd.digiback.business.model.Movement;
import ch.heigvd.digiback.business.model.MovementType;
import ch.heigvd.digiback.business.model.Status;

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
    private AnyChartView movementChartView;
    private FloatingActionButton back;
    private TextView angleInfo;
    private Button
            measureStarter,
            measureStopper,
            help,
            sendMeasures,
            cancelMeasures,
            addPain;

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
    int painLevel = -1;

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

        selectedMovementType = MovementType.NONE;
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
                selectedMovementType = MovementType.NONE;
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
                            // TODO confirm (or not the reception of the movement)
                            allAngles.clear();
                            painLevel = -1;
                            validateMovementPopup.dismiss();
                            Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            ));

        });

        // Cancel measures
        cancelMeasures.setOnClickListener(view ->  {
            allAngles.clear();
            validateMovementPopup.dismiss();
        });

        // Add pain level
        addPain.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(item -> {
                        painLevel = 0;
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

        // Graph
        setChart();
    }

    private void setAddPainOnClickListener() {
        addPain.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this, view);
            popup.setOnMenuItemClickListener(item -> {
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
}
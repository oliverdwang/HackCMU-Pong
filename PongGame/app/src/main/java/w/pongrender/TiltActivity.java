package w.pongrender;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public abstract class TiltActivity  extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magneticSensor;

    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[];

    //Raw data storage
    private float accelerometerRaw[] = new float[3];
    private float magneticRaw[] = new float[3];
    private float[] values = new float[3];

    // azimuth, pitch and roll
    public float azimuth;
    public float pitch;
    public float roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        azimuth = -1;
        pitch = -1;
        roll = -1;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticRaw = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerRaw = event.values.clone();
                break;
        }

        if (magneticRaw != null && accelerometerRaw != null) {
            gravity = new float[9];
            magnetic = new float[9];
            SensorManager.getRotationMatrix(gravity, magnetic, accelerometerRaw, magneticRaw);
            float[] outGravity = new float[9];
            SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X,SensorManager.AXIS_Z, outGravity);
            SensorManager.getOrientation(outGravity, values);

            azimuth = values[0] * 57.2957795f;
            pitch = values[1] * 57.2957795f;
            roll = values[2] * 57.2957795f;
            Log.v("onSensorChanged","azimuth=" + Float.toString(azimuth));
            Log.v("onSensorChanged","pitch=" + Float.toString(pitch));
            Log.v("onSensorChanged","roll=" + Float.toString(roll));
            magneticRaw = null;
            accelerometerRaw = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor == accelerometerSensor) {
            switch (accuracy) {
                case 0:
                    Log.v("Accelerometer Accuracy","Unreliable");
                    break;
                case 1:
                    Log.v("Accelerometer Accuracy","Low Accuracy");
                    break;
                case 2:
                    Log.v("Accelerometer Accuracy","Medium Accuracy");
                    break;
                case 3:
                    Log.v("Accelerometer Accuracy","High Accuracy");
                    break;
            }
        } else if (sensor == magneticSensor) {
            switch (accuracy) {
                case 0:
                    Log.v("Magnetic Accuracy","Unreliable");
                    break;
                case 1:
                    Log.v("Magnetic Accuracy","Low Accuracy");
                    break;
                case 2:
                    Log.v("Magnetic Accuracy","Medium Accuracy");
                    break;
                case 3:
                    Log.v("Magnetic Accuracy","High Accuracy");
                    break;
            }
        }
    }
}

package sensors.graph.cpiekarski.com.graphsensors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.graphics.Color;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private final String TAG = "GraphSensors";
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private GraphView mGraphGyro;
    private GraphView mGraphAccel;
    private GraphView mGraphGravity;
    private LineGraphSeries<DataPoint> mSeriesGyroX, mSeriesGyroY, mSeriesGyroZ;
    private LineGraphSeries<DataPoint> mSeriesAccelX, mSeriesAccelY, mSeriesAccelZ;
    private LineGraphSeries<DataPoint> mSeriesGravX, mSeriesGravY, mSeriesGravZ;
    private double graphLastGyroXValue = 5d;
    private double graphLastAccelXValue = 5d;
    private double graphLastGravXValue = 5d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGraphGyro = initGraph(R.id.graphGyro, "Sensor.TYPE_GYROSCOPE");
        mGraphAccel = initGraph(R.id.graphAccel, "Sensor.TYPE_ACCELEROMETER");
        mGraphGravity = initGraph(R.id.graphGravity, "Sensor.TYPE_GRAVITY");

        //GYRO
        mSeriesGyroX = initSeries(Color.BLUE, "X");
        mSeriesGyroY = initSeries(Color.RED, "Y");
        mSeriesGyroZ = initSeries(Color.GREEN, "Z");

        mGraphGyro.addSeries(mSeriesGyroX);
        mGraphGyro.addSeries(mSeriesGyroY);
        mGraphGyro.addSeries(mSeriesGyroZ);

        startGyro();

        // ACCEL
        mSeriesAccelX = initSeries(Color.BLUE, "X");
        mSeriesAccelY = initSeries(Color.RED, "Y");
        mSeriesAccelZ = initSeries(Color.GREEN, "Z");

        mGraphAccel.addSeries(mSeriesAccelX);
        mGraphAccel.addSeries(mSeriesAccelY);
        mGraphAccel.addSeries(mSeriesAccelZ);

        startAccel();

        // GRAVITY
        mSeriesGravX = initSeries(Color.BLUE, "X");
        mSeriesGravY = initSeries(Color.RED, "Y");
        mSeriesGravZ = initSeries(Color.GREEN, "Z");

        mGraphGravity.addSeries(mSeriesGravX);
        mGraphGravity.addSeries(mSeriesGravY);
        mGraphGravity.addSeries(mSeriesGravZ);

        startGravity();
    }

    public GraphView initGraph(int id, String title) {
        GraphView graph = (GraphView) findViewById(id);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getGridLabelRenderer().setLabelVerticalWidth(100);
        graph.setTitle(title);
        return graph;
    }

    public LineGraphSeries<DataPoint> initSeries(int color, String title) {
        LineGraphSeries<DataPoint> series;
        series = new LineGraphSeries<>();
        series.setDrawDataPoints(true);
        series.setDrawBackground(false);
        series.setColor(color);
        series.setTitle(title);
        return series;
    }

    public void startGyro(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startAccel(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startGravity(){
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //event.values[x,y,z]
        if(event.sensor.getType() == 4) {
            graphLastGyroXValue += 0.15d;
            mSeriesGyroX.appendData(new DataPoint(graphLastGyroXValue, event.values[0]), true, 33);
            mSeriesGyroY.appendData(new DataPoint(graphLastGyroXValue, event.values[1]), true, 33);
            mSeriesGyroZ.appendData(new DataPoint(graphLastGyroXValue, event.values[2]), true, 33);
        } else if(event.sensor.getType() == 1) {
            graphLastAccelXValue += 0.15d;
            mSeriesAccelX.appendData(new DataPoint(graphLastAccelXValue, event.values[0]), true, 33);
            mSeriesAccelY.appendData(new DataPoint(graphLastAccelXValue, event.values[1]), true, 33);
            mSeriesAccelZ.appendData(new DataPoint(graphLastAccelXValue, event.values[2]), true, 33);
        } else if(event.sensor.getType() == 9) {
            graphLastGravXValue += 0.15d;
            mSeriesGravX.appendData(new DataPoint(graphLastGravXValue, event.values[0]), true, 33);
            mSeriesGravY.appendData(new DataPoint(graphLastGravXValue, event.values[1]), true, 33);
            mSeriesGravZ.appendData(new DataPoint(graphLastGravXValue, event.values[2]), true, 33);
        }
        String dataString = String.valueOf(event.accuracy) + "," + String.valueOf(event.timestamp) + "," + String.valueOf(event.sensor.getType()) + "\n";
        Log.d(TAG, "Data received: " + dataString);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged called for the gyro");
    }


}

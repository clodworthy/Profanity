package profanity.mofogames.org;
import android.util.Log;
import org.openintents.sensorsimulator.hardware.Sensor;
import org.openintents.sensorsimulator.hardware.SensorEvent;
import org.openintents.sensorsimulator.hardware.SensorEventListener;
import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;

public class CustomSensorEventListener implements SensorEventListener {
	
	private long now =0;
	private long timeDiff =0;
	private long lastUpdate =0;
	private long lastShake =0;
	
	private float x = 0;
	private float y = 0;
	private float z = 0;
	private float lastX = 0;
	private float lastY = 0;
	private float lastZ = 0;
	private float force = 0;
	private String TAG = "CustomerSensorEventListener";
	
	private int threshold;
	private int interval;
	private AccelerometerListener listener;

	public void onAccuracyChanged(Sensor sensor, int accuracy){}
	
	public void onSensorChanged(SensorEvent event){
		//use the event timestamp as a reference
		
		//<------------------------ Live Device Code STARTS--------------------------->
		//now = event.timestamp;
		//<------------------------ Live Device Code ENDS--------------------------->
		
		//<------------------------ SensorSimulator Code STARTS--------------------------->
		//Sensor Event doesn't implement the timestamp field
		//event timestamp as a reference
		//so we improvise by using the current system call time - this
		//is less accurate than using the SesnorEvent time
		now = System.currentTimeMillis();
		//<------------------------ SensorSimulator Code ENDS--------------------------->
		
		x = event.values[0];
		y = event.values[1];
		z = event.values[2];
		
		Log.d(TAG, "onSensorChanged at "+now +"s x: "+x+", y: "+y+", z: "+z);
		
		if(lastUpdate==0){
			lastUpdate = now;
			lastShake = now;
			lastX=x;
			lastY=y;
			lastZ=z;
		}else{
			timeDiff = now-lastUpdate;
			if(timeDiff>0){
				force = Math.abs(x+y+x-lastX-lastY-lastZ)/timeDiff;
				//force = Math.sqrt((x-lastX)^2 + ((y-lastY)^2)+ (z-lastZ)^2)/timeDiff;
				if(force>threshold){
					if(now-lastShake<=interval){
						//trigger shake event
						listener.onShake(force);
					}
					lastShake=now;
				}
				lastX=x;
				lastY=y;
				lastZ=z;
				lastUpdate=now;
			}
		}
		//trigger change event
		listener.onAccelerationChanged(x,y,z);
	}


}

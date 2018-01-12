package profanity.mofogames.org;

import java.util.List;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
//import android.util.Log;
//import org.openintents.sensorsimulator.hardware.Sensor;
//import org.openintents.sensorsimulator.hardware.SensorEvent;
//import org.openintents.sensorsimulator.hardware.SensorEventListener;
//import org.openintents.sensorsimulator.hardware.SensorManagerSimulator;




public class AccelerometerManager{
	
	
	private static final String TAG = "AccelerometerManager";
	
	/** Accuracy Configuration*/
	private static float threshold = 1;
	private static int interval = 3000;
	//Deployment mode type safe enumeration
	private static final int SIMULATOR = 0;
	private static final int LIVE =1;
	
	//Live accelerometer 
	private static Sensor sensor;
	private static SensorManager sensorManager;
	//OpenIntents emulated accelerometer
	private static org.openintents.sensorsimulator.hardware.SensorManagerSimulator sensorManagerSimulator;
	private static org.openintents.sensorsimulator.hardware.Sensor smsSensor;
	//private static org.openintents.sensorsimulator.hardware.SensorEventListener sensorSimEventListener;
	private static AccelerometerListener listener;
	private static final int deploymentMode = AccelerometerManager.LIVE;
	
	/** Indicates whether Accelerometer sensor is supported*/
	private static Boolean supported;
	/** Indicates whether Accelerometer is listening */
	private static boolean running = false;
	
	/**
	 * Returns true is the manager is listening to orientation changes
	 */
	public static boolean isListening(){
		return running;
	}
	
	
	/**
	 * Unregister Listeners
	 */
	public static void stopListening(){
		running = false;
		try{
				switch(deploymentMode){
					
				case(AccelerometerManager.LIVE):
					if(sensorManager!=null&&sensorEventListener!=null){
						sensorManager.unregisterListener(sensorEventListener);
					}
					break;
				case(AccelerometerManager.SIMULATOR):
					if(sensorManagerSimulator!=null&&sensorSimEventListener!=null){
						//sensorSimulator does not implement unrgisterListener!
						//sensorManagerSimulator.unregisterListener(sensorEventListener);
					}
					break;
				}
		}catch(Exception e){
			
		}
	}
	
	
	public static boolean isSupported(){
		
		if(supported==null){
			if(AcceleromterService.getContext()!=null){
				if(deploymentMode == AccelerometerManager.LIVE){
					/* 
					 * SensorSimulator Code/Live Device
					 *  Some methods need to be rewritten to work with the OpenIntents Sensor Simulator
					 *  As the OpenIntents Sensor Simulator is subtly different.
					 *  Comment in/out according to implementation environment
					 */
					
					//<---------------------------Live Device Starts------------------------------->
	 				sensorManager=(SensorManager)AcceleromterService.getContext().getSystemService(Context.SENSOR_SERVICE);
					List<Sensor>sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
					supported = Boolean.valueOf(sensors.size()>0);
					//supported = new Boolean(sensor!=null);
					//<------------------------Live Device Ends--------------------------------------->
					
					
				}	
				else{
					//<------------------------ SensorSimulator Code STARTS--------------------------->
					Context context = AcceleromterService.getContext();
					//sensorManagerSimulator = org.openintents.sensorsimulator.hardware.SensorManagerSimulator.getSystemService(context, Context.SENSOR_SERVICE);
					sensorManagerSimulator=    org.openintents.sensorsimulator.hardware.SensorManagerSimulator.getSystemService(AcceleromterService.getContext(), (AcceleromterService.getContext()).SENSOR_SERVICE);
					
					sensorManagerSimulator.connectSimulator();
					smsSensor = sensorManagerSimulator.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
					//<------------------------ SensorSimulator Code ENDS--------------------------->
					
					
					supported = Boolean.valueOf(smsSensor!=null);
				}
				
								
			}else {
				supported = Boolean.FALSE;
			}
				
				
		}
		return supported;
		
	}
	
	/**
	 * Configure the Listener for shaking
	 * @param threshold		min acceleration variation for shaking
	 * @param interval		min interval between shake events
	 */
	public static void configure(int threshold, int interval){
		AccelerometerManager.threshold = threshold;
		AccelerometerManager.interval = interval;
		
	}
	
	public static void configure(){
		AccelerometerManager.threshold = Preferences.getThresholdPreference(AcceleromterService.getContext());
		AccelerometerManager.interval = Preferences.getRefractoryPreference(AcceleromterService.getContext())*1000; //convert to ms
	}

	/**
	 * Registers a listener and starts listening
	 * @param	al	callback for accelerometer events
	 */
	public static void startListening(AccelerometerListener al){
		
		/* 
		 * SensorSimulator Code/Live Device
		 *  Some methods need to be rewritten to work with the OpenIntents Sensor Simulator
		 *  As the OpenIntents Sensor Simulator is subtly different.
		 *  Comment in/out according to implementation environment
		 */
		
		if(deploymentMode == AccelerometerManager.LIVE){
			//<---------------------------Live Device Starts---------------------------------->
			sensorManager=(SensorManager) AcceleromterService.getContext().getSystemService(Context.SENSOR_SERVICE);
			List<Sensor>sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
			listener = al;
			if(sensors.size()>0){
			sensor = sensors.get(0);
			running = sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
			}

			//<---------------------------Live Device Ends---------------------------------->
		}else{
			//<------------------------ SensorSimulator Code STARTS--------------------------->
			//Context context = AcceleromterService.getContext();
			//sensorManagerSimulator = org.openintents.sensorsimulator.hardware.SensorManagerSimulator.getSystemService(context, Context.SENSOR_SERVICE);
			sensorManagerSimulator=    org.openintents.sensorsimulator.hardware.SensorManagerSimulator.getSystemService(AcceleromterService.getContext(), Context.SENSOR_SERVICE);
			listener = al;
			sensorManagerSimulator.connectSimulator();
			smsSensor = sensorManagerSimulator.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			//sensorEventListener needs to be of type import org.openintents.sensorsimulator.hardware.SensorEventListener
			running = sensorManagerSimulator.registerListener(sensorSimEventListener, smsSensor, org.openintents.sensorsimulator.hardware.SensorManagerSimulator.SENSOR_DELAY_FASTEST);
			
			//<------------------------ SensorSimulator Code ENDS----------------------------->
		}
		
	
		
		//Log.d(TAG, "startListening() running:" + running);
	}
		
		/**
		 * Configures threshold and interval 
		 * and registers a listener and start listening
		 * @param threshold	minimum acceleration variation for shaking
		 * @param interval	minimum interval
		 */
		public static void startListening(AccelerometerListener al, int threshold, int interval){
			//configure(threshold, interval);
			//recommended settings Threshold 1, interval 3, sensitivity 1.
			configure(); //retrieve persisted preference values
			startListening(al);
			
		}
		
		/**
		 * The Listener that listens to events from the accelerometer listener
		 */
		private static SensorEventListener sensorEventListener = new SensorEventListener(){
		
			
			private long now =0;
			private long timeDiff =0;
			private long lastUpdate =0;
			private long lastShake =0;
			private long hdt =0; //holdDownTimer
			private double amax=0;
			
			private float x = 0;
			private float y = 0;
			private float z = 0;
			private float lastX = 0;
			private float lastY = 0;
			private float lastZ = 0;
			private double resultant = 0;
			
			
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy){}
			
			@Override
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
				
				AccelerometerManager.threshold = Preferences.getThresholdPreference(AcceleromterService.getContext());
				AccelerometerManager.interval = Preferences.getRefractoryPreference(AcceleromterService.getContext())*1000+500; //convert to ms
				
				x = event.values[0];
				y = event.values[1];
				z = event.values[2];
				
				//Log.d(TAG, "onSensorChanged at "+now +"s x: "+x+", y: "+y+", z: "+z);
				
				if(lastUpdate==0){
					lastUpdate = now;
					lastShake = now;
					lastX=x;
					lastY=y;
					lastZ=z;
				}else{
					timeDiff = now-lastUpdate;
					hdt+=timeDiff;
					
					double x_resid2 = squareDiff(x-lastX);
					double y_resid2 = squareDiff(y-lastY);
					double z_resid2 = squareDiff(z-lastZ);
					//resultant acceleration is the L2 norm of the 3-vector
					resultant =  Math.sqrt(x_resid2 + y_resid2 + z_resid2);
					if(resultant>amax){
						amax=resultant;
					}
					lastX=x;
					lastY=y;
					lastZ=z;
					lastUpdate=now;
					
					if(resultant>threshold){
						if(hdt>interval){
							listener.onShake(resultant);
							reinit();
						}
						lastShake=now;
					}
// <--------------Tutorial Code Starts---------->
// The accelerometer tutorial code resulted in a queue of updates to the UI
// Resulting in considerable latency between the shake stimulus and the
// resultant UI event. This was especially evident with a series of shakes.
// The method was therefore rewritten to invoke a callback in the registered
// AccelerometerListener class only if the peak acceleration in a quantization
// period exceeded a specified threshold. These values should be user customisable
// preferences. The holdDownTimer (hdt) implements this quantization period by summing
// the inter-event time differences.
//					if(timeDiff>0){
//						//resultant = Math.abs(x+y+x-lastX-lastY-lastZ)/timeDiff;
//						double x_resid2 = squareDiff(x-lastX);
//						double y_resid2 = squareDiff(y-lastY);
//						double z_resid2 = squareDiff(z-lastZ);
//						resultant =  Math.sqrt(x_resid2 + y_resid2 + z_resid2);
//						if(resultant>threshold){
//							if(now-lastShake<=interval){
//								//trigger shake event
//								listener.onShake(resultant);
//							}
//							lastShake=now;
//						}
//						
//						lastX=x;
//						lastY=y;
//						lastZ=z;
//						lastUpdate=now;
//					}
// <--------------Tutorial Code Ends---------->
				}
				//trigger change event
				listener.onAccelerationChanged(x,y,z);
			}
			
			//Private helper method for calculating the squared difference
			private double squareDiff(float e){
				double retVal = Math.pow(e,2);
				return retVal;
			}
			//Private helper method for reinitialising the holdDownTimer and maximum acceleration
			private void reinit(){
				hdt = 0;
				amax=0;
			}
		};
		
		
private static org.openintents.sensorsimulator.hardware.SensorEventListener sensorSimEventListener = new org.openintents.sensorsimulator.hardware.SensorEventListener(){
		
			
			private long now =0;
			private long timeDiff =0;
			private long lastUpdate =0;
			private long lastShake =0;
			private long hdt =0; //holdDownTimer
			private double amax=0;
			
			private float x = 0;
			private float y = 0;
			private float z = 0;
			private float lastX = 0;
			private float lastY = 0;
			private float lastZ = 0;
			private double resultant = 0;
			
			
			@Override
			public void onAccuracyChanged(org.openintents.sensorsimulator.hardware.Sensor sensor, int accuracy){}
			
			@Override
			public void onSensorChanged(org.openintents.sensorsimulator.hardware.SensorEvent event){
				
				now = System.currentTimeMillis();
				
				AccelerometerManager.threshold = Preferences.getThresholdPreference(AcceleromterService.getContext());
				AccelerometerManager.interval = Preferences.getRefractoryPreference(AcceleromterService.getContext())*1000; //convert to ms
				
				x = event.values[0];
				y = event.values[1];
				z = event.values[2];
				
				if(lastUpdate==0){
					lastUpdate = now;
					lastShake = now;
					lastX=x;
					lastY=y;
					lastZ=z;
				}else{
					timeDiff = now-lastUpdate;
					hdt+=timeDiff;
					
					double x_resid2 = squareDiff(x-lastX);
					double y_resid2 = squareDiff(y-lastY);
					double z_resid2 = squareDiff(z-lastZ);
					//resultant acceleration is the L2 norm of the 3-vector
					resultant =  Math.sqrt(x_resid2 + y_resid2 + z_resid2);
					if(resultant>amax){
						amax=resultant;
					}
					lastX=x;
					lastY=y;
					lastZ=z;
					lastUpdate=now;
					
					if(resultant>threshold){
						if(hdt>interval){
							listener.onShake(resultant);
							reinit();
						}
						lastShake=now;
					}

				}
				//trigger change event
				listener.onAccelerationChanged(x,y,z);
			}
			
			//Private helper method for calculating the squared difference
			private double squareDiff(float e){
				double retVal = Math.pow(e,2);
				return retVal;
			}
			//Private helper method for reinitialising the holdDownTimer and maximum acceleration
			private void reinit(){
				hdt = 0;
				amax=0;
			}
		};
}
	


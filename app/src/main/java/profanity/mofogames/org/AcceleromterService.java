package profanity.mofogames.org;
import java.io.IOException;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.graphics.drawable.*;


public class AcceleromterService extends Service{
	
	private static final String TAG = "AccelerometerService";
	private static Context CONTEXT;
	private static boolean running = false;
	private static int[] threshold = {0,5,10};
	private int intervals;
	private AssetFileDescriptor afd;
	private NotificationManager notificationManager;
	private String[] filePath, expletives;
	private static final int PROCESS_ID = 10251;
	private static final String SERVICE_IDENTIFIER = "Profanity!";
	private static final String SUBJECT = "is running in the background";
	private Toast pop; 
	private LinearLayout linearLayout;
	private ImageView imageView;
	private TextView textView;
	private Resources resources;
	private View toastView;
	private Thread thread;
	
	public void onCreate(){
		super.onCreate();
		running = true;
		CONTEXT = this;
		resources = CONTEXT.getResources();
		intervals = 3;
		//Layout & Notification initialisation
		notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		initialisePop();
		//displayNotificationMessage("Starting Accelerometer Service");
		thread = new Thread(null, new AccelerometerServiceThread(), "AccelerometerService");
		thread.start();
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		running=false;
		//displayNotificationMessage("Stopping Accelerometer Service");
		if(AccelerometerManager.isListening()){
			AccelerometerManager.stopListening();
		}
	}
	
	
	
	public static Context getContext(){
		return CONTEXT;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static boolean isRunning(){
		return running;
	}
	
	
	
	public class AccelerometerServiceThread implements Runnable, AccelerometerListener, SharedPreferences.OnSharedPreferenceChangeListener{
		
		private Handler handler;
		
		public void run(){
			//deprecated
			//Notification notification = new Notification(R.drawable.icon, SERVICE_IDENTIFIER, System.currentTimeMillis());
			Intent notificationIntent = new Intent(CONTEXT, ProfaneActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(CONTEXT,
					R.id.app_notification_id, notificationIntent,
			        PendingIntent.FLAG_UPDATE_CURRENT);
			
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
			Notification notification = new Notification.Builder(CONTEXT)
				.setContentTitle(SERVICE_IDENTIFIER)
				.setAutoCancel(true)
				.setContentText(SUBJECT)
				.setContentIntent(contentIntent)
				.setSmallIcon(R.drawable.icon)
				.setLargeIcon(bitmap)
				.build();
			
			
			SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			startForeground(PROCESS_ID, notification);
			sp.registerOnSharedPreferenceChangeListener(this);
			setProfanityPack();
			

			
			
			//Looper associated from the current thread is obtained via a static call to myLooper()
			//callback messaging is enabled (required to prevent runTime Exception).)
			Looper.prepare();
			//create a handler. This will bind automatically to this thread
			handler = new Handler();
			//executable code goes here
			if(AccelerometerManager.isSupported()&&!AccelerometerManager.isListening()){
				AccelerometerManager.startListening(this);
			}
			
	        //implement the message (queuing loop). Cessation may be requested by 
	        //calling the cessate() method. This enqueues a dummy runnable - allowing all 
	        //enqueued tasks to complete before thread cessation is executed.
	        Looper.loop();
        
		}
		
		
		
		/**
		 * OnShake	Asynchronous Callback
		 */
		public void onShake(double force){
			//Obtain References to selected expletives source & file paths 
			//(maybe delegate to initialisation method) which is called on initialistaion and if preferences are edited
			
			//Determine the length of the expletive array and divide into number of specified intervals
			int arrayLength = expletives.length;
			double intervalLength = arrayLength/intervals;
			int startIndex = (int)Math.floor(intervalLength*(intervals-1));
			//Set start index in array according to sensitivity thresholds
			//Toast.makeText(this, "Acceleration: " +String.format("%.2f", force) +"m/s^2", 2000).show();
			int sensitivity = Preferences.getSensitivityPreference(CONTEXT);
			int thresholdScaleFactor = Preferences.getThresholdPreference(CONTEXT);
			
			for(int i= 0;  i<(intervals); i++){
				threshold[i]= thresholdScaleFactor + i*(10-sensitivity);
				Log.d(TAG, "Threshold ["+i+"] =" + threshold[i]);
				if(force>=threshold[i]){
					startIndex = (int)Math.floor((intervals-(i+1))*intervalLength);
					//Log.d(TAG, "StartIndex: "+ startIndex + "\nInterval:  "+ (intervals-i-1)*intervalLength + "\nNumber of Intervals: "+intervals + "IntervalLength: "+intervalLength);
					}
			
			}
			Log.d(TAG, "Force: " + force);
			int index = (int)(startIndex+ intervalLength * Math.random());
			
			String expletive = expletives[index];
			String fp = filePath[index];
			try{
				afd = getAssets().openFd(fp);
			}catch(IOException ioe){
				
			}
			//FileDescriptor fd = afd.getFileDescriptor();
			Log.d(TAG, "File: "+ filePath[index]+ "\nFile Descriptor: "+afd.toString());
			//Music.play(CONTEXT,afd);
			//Check if Profanty is enabled before playing media content
			if(Preferences.getEnabledPreference(CONTEXT)){
				SfxPlayer.play(afd,CONTEXT);
				//if show text is enabled display expletive and explanatory text
				// The duration is either 2 seconds or (based on the average reading speed
				//of 4 words per second) a duration based on the number of words in the expletive
				if(Preferences.getShowTextPreference(CONTEXT)){
					int wordCount = wordCount(expletive);
					int duration = max(1000, wordCount*150);
					if(duration<700){
						pop.setDuration(Toast.LENGTH_SHORT);
					}else{
						pop.setDuration(Toast.LENGTH_LONG);
					}
									
					textView.setText(expletive, TextView.BufferType.NORMAL);
					pop.show();
				
				}
			}
			
			
			
		}
		
		
		/**
		 * onAccelerationChanged callback
		 */
		public void onAccelerationChanged(float x, float y, float z){
			
//			((TextView)ProfaneActivity.getActivityReference().findViewById(R.id.x_label)).setText("x: "+ String.format("%.2f",x));
//			((TextView)ProfaneActivity.getActivityReference().findViewById(R.id.y_label)).setText("y: " + String.format("%.2f",y));
//			((TextView)ProfaneActivity.getActivityReference().findViewById(R.id.z_label)).setText("z: " + String.format("%.2f",z));
		}
		
		public void onSharedPreferenceChanged(SharedPreferences sp, String key){
			Log.d(TAG, "Preference Changed "+key);
			//consider switch statement
			if(key.equals(getContext().getResources().getString(R.string.pack_key))){
				setProfanityPack();
			}
//			if(key.equals(getContext().getResources().getString(R.string.switch_key))){
//				//check swicth state & act accordingly
//			}
			
		 }
			
	};
	
	 private void displayNotificationMessage(String message){
			Notification notification = new Notification(R.drawable.note, message, System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ProfaneActivity.class),PendingIntent.FLAG_ONE_SHOT);
			notification.setLatestEventInfo(this, "Accelerometer Service", message, contentIntent);
			notificationManager.notify(R.id.app_notification_id, notification);
			
		}
	 
	 private int max(int int1, int int2){
		 if(int1> int2){
			 return int1;
		 }else{
			 return int2;
		 }
		 
	 }
	 
	 private int wordCount(String s){
		 int count =0;
		 int index = 0;
		 int SPACE =32;
		 if(s.length()==0){
			 return 0;
		 }
		 for(int i=0; i<s.length(); i++){
			 if(s.charAt(i)==SPACE){
				 count++;
			 }
		 }
		 Log.d(TAG , "Space Count =" +count);
		 return count;
	 }
	
	 private void initialisePop(){
		 	
			LayoutInflater inflater = (LayoutInflater)CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			toastView = inflater.inflate(R.layout.custom_toast,null);
			imageView = (ImageView)toastView.findViewById(R.id.toast_image);
			imageView.setImageResource(R.drawable.icon);
			textView = (TextView)toastView.findViewById(R.id.text);
			
			pop = new Toast(CONTEXT);
			pop.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			//pop.setView(linearLayout);
			pop.setView(toastView);
			
			PopUp.setPop(pop);
			PopUp.setTextView(textView);
			PopUp.setImageView(imageView);
			
	 }
	 
	 private void setProfanityPack(){
		 	int packSelection = Preferences.getPackPreference(getContext());
		 	Log.d(TAG,"PackSelectionIndex"+packSelection);
		 	//these should reference the xml array values
		 	LanguagePack lp = Utility.getPackFromIndex(CONTEXT, packSelection);
		 	expletives = lp.getExpletive();
		 	filePath=lp.getFilePath();
		 		 	
		 	
	 }
	 
	
}

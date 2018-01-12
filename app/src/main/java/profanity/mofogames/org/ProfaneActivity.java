package profanity.mofogames.org;




import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.content.Context;


public class ProfaneActivity extends Activity implements Constants, OnClickListener, DialogInterface.OnClickListener{
    /** Called when the activity is first created. */
	private static Activity ACTIVITY;
	private Intent intent;
	private int state;
	private int previousState;
	private static String TAG = "ProfaneActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ACTIVITY = this;
        if(AcceleromterService.isRunning()!=true){
        	startService(new Intent(this, AcceleromterService.class));
        }
        setContentView(R.layout.main);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		//actionBar.setDisplayShowTitleEnabled(false);
        state = R.integer.UI_MAIN;
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.background));
		//Set up click listeners for all the buttons
        View logo = findViewById(R.id.imageLogo);
        logo.setOnClickListener(this);
        View etymologyButton = findViewById(R.id.etymology_button);
        etymologyButton.setOnClickListener(this);
        View preferencesButton = findViewById(R.id.preferences_button);
        preferencesButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
       
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        
    }
    
    public static Activity getActivityReference(){
    	return ACTIVITY;
    }
    
    //override abstract method from OnClickListener
    public void onClick(View v){
    	
    	switch(v.getId()){
    		
    		case R.id.imageLogo:
    			intent = new Intent();
    	        intent.setAction(Intent.ACTION_VIEW);
    	        intent.addCategory(Intent.CATEGORY_BROWSABLE);
    	        intent.setData(Uri.parse("https://www.facebook.com/MofoStudioProfanity"));
    	        startActivity(intent);
    			break;
    	
    		case R.id.etymology_button:
    			intent = new Intent(this,DictionaryListView.class);
    			startActivity(intent);
    			break;
    		case R.id.about_button:
    			intent = new Intent(this, About.class);
    			startActivity(intent);
    			break;
    		
    		case R.id.preferences_button:
    			intent = new Intent(this, Preferences.class);
    			startActivity(intent);
    			break;
    		    		
    		case R.id.exit_button:
    			int type = R.integer.DIALOG_YES_NO;
    			String message = getResources().getString(R.string.exit_warning);
        		new CustomAlert(this, type, message, this);
        		break;
    		//handle other buttons here
    	
    	}
    	
    	
    }
    	
    public void onClick(DialogInterface diocl, int value){
    	
    	switch(value){
    		case DialogInterface.BUTTON_POSITIVE:
    			switch(state){
    			
					case (R.integer.UI_MAIN):
						finish();
					break;
    			}
	
    			
    		case DialogInterface.BUTTON_NEUTRAL:
    			break;
    		
    		case DialogInterface.BUTTON_NEGATIVE:
    			
    			break;
    	}
	}
    
   public static Context getContext(){
	   return getContext();
   }
}
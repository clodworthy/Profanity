package profanity.mofogames.org;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;


public class Preferences extends PreferenceActivity {
	
	
	
	private static final boolean PREF_SHOW_TEXT_DEFAULT_VALUE = true;
	private static final boolean PREF_START_AS_SERVICE_DEFAULT_VALUE = true;
	private static final boolean PREF_ENABLED_VALUE = true;
	private static final int PREF_SENSITIVITY_DEFAULT_VALUE =1;
	private static final int PREF_THRESHOLD_DEFAULT_VALUE =1;
	private static final int PREF_REFRACTORY_PERIOD_DEFAULT_VALUE =3;
	private static final int PREF_VOLUME_DEFAULT_VALUE =3;
	private static final String PREF_PACK_DEFAULT_VALUE ="1";
	private static final String TAG = "Preferences";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));
	}
	
	/**
	 * getShowText
	 * 
	 * @param context
	 * @return boolean
	 * 
	 * returns persisted application preferences for Showing Profanity Text
	 */
	public static boolean getShowTextPreference(Context context){
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(context.getResources().getString(R.string.show_text_key), PREF_SHOW_TEXT_DEFAULT_VALUE);
	}
	/**
	 * getStartAsService
	 * 
	 * @param context
	 * @return boolean
	 * 
	 * returns persisted application preferences for on Boot Broadcast listener
	 */
	public static boolean getStartAsServicePreference(Context context){
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(context.getResources().getString(R.string.start_as_service_key), PREF_START_AS_SERVICE_DEFAULT_VALUE);
	}
	
	public static boolean getEnabledPreference(Context context){
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(context.getResources().getString(R.string.switch_key), PREF_ENABLED_VALUE);
	}
	
	/**
	 * getFilterPreference
	 * 
	 * @param context
	 * @return boolean
	 * 
	 * returns persisted application preferences for Accelerometer
	 */
	public static int getSensitivityPreference(Context context){
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(context.getResources().getString(R.string.sensitivity_key), PREF_SENSITIVITY_DEFAULT_VALUE);
	}
	
	public static int getThresholdPreference(Context context){
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(context.getResources().getString(R.string.threshold_key), PREF_THRESHOLD_DEFAULT_VALUE);
	}
	
	public static int getRefractoryPreference(Context context){
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(context.getResources().getString(R.string.refractory_period_key), PREF_REFRACTORY_PERIOD_DEFAULT_VALUE);
	}
	
	public static int getVolumePreference(Context context){
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getInt(context.getResources().getString(R.string.volume_key), PREF_VOLUME_DEFAULT_VALUE);
	}
	public static int getPackPreference(Context context){
		SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(context);
		int retval =1;
		try{
			retval = Integer.parseInt(sp.getString(context.getResources().getString(R.string.pack_key), PREF_PACK_DEFAULT_VALUE));
			
		}catch(NumberFormatException nfe){
			Log.d(TAG, "Preferance Value:"+retval);
			retval=1;
		}
		return retval;
	}
	
	
	
}
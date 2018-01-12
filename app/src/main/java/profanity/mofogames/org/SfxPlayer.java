package profanity.mofogames.org;

import java.io.IOException;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.content.res.AssetFileDescriptor;
import android.content.Context;

public class SfxPlayer{
	
	private static final int STREAMS = 5;
	private static final int QUALITY_DEFAULT = 0;//API state currently no use - use 0 as default
	private static final int PRIORITY_DEFAULT=1; //API states included for prospective compatibility - use 1 as default
	private static SoundPool soundPool = new SoundPool(SfxPlayer.STREAMS, AudioManager.STREAM_MUSIC, SfxPlayer.QUALITY_DEFAULT);
	private static Context CONTEXT;
	private static AssetFileDescriptor afd;
	
	public static void play(String filePath, Context context){
		AssetFileDescriptor afdTemp = null;
		try{
			afdTemp = context.getAssets().openFd(filePath);
		}catch(IOException ioe){
		
		}
		SfxPlayer.play(afdTemp, context);
		
	}
	
	public synchronized static void play(AssetFileDescriptor afd, Context context){
		CONTEXT = context;
		int soundId = soundPool.load(afd, SfxPlayer.PRIORITY_DEFAULT);
		
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                    int status) {
            		AudioManager audioManager = (AudioManager) CONTEXT.getSystemService(CONTEXT.AUDIO_SERVICE);
            		//vsf=volume scale factor persisted from Preferences
            		float vsf = Preferences.getVolumePreference(CONTEXT)/10;
            		float actualVolume = (float) audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
            		float maxVolume = (float) audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            		float volume = actualVolume *vsf/ maxVolume;
            		float volume_left = volume;
            		float volume_right = volume;
            		int priority =8;
            		int loop = 0; //false
            		float rate = 1f;
            		soundPool.play(sampleId, volume_left, volume_right, priority, loop, rate);
            		}
			});
		
	}
	
	public synchronized static void play(Profanity profanity, Context context){
		String fileDescriptor = profanity.getFilePath();
		if(fileDescriptor!=null){
			play(fileDescriptor, context);
		}
	}
	
}

package profanity.mofogames.org;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import java.io.FileDescriptor;
import android.util.Log;
import android.content.res.AssetFileDescriptor;


public class Music {
	
	private static MediaPlayer mp = new MediaPlayer();
	private static String TAG = "MediaPLayer";
	
	/**stop old song and start new song
	*/
	public static void play(Context context, int resource){
		stop(context);
		//if(Prefs.getMusic(context)){
			mp = MediaPlayer.create(context, resource);
			mp.setLooping(false);
			mp.start();
		//}
	}
	/**stop old song and start new song
	*/
	public static void play(Context context, Uri resource){
		stop(context);
		
		//if(Prefs.getMusic(context)){
			mp = MediaPlayer.create(context, resource);
			mp.setLooping(false);
			mp.start();
			
		//}
	}
	
	public static void play(Context context, FileDescriptor fd){
			//stop(context);
			Log.d(TAG, "play: "+fd.toString());
			try{
				mp.reset();
				mp.setDataSource(fd);
				mp.setLooping(false);
				mp.prepare();
				mp.start();
				
			}catch(Exception ioe){
				
			}
			//if(Prefs.getMusic(context)){
			
			
		//}
	}
	
	public static void play(Context context, AssetFileDescriptor afd){
		//stop(context);
		Log.d(TAG, "play: "+afd.toString());
		try{
			mp.reset();
			mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			mp.setLooping(false);
			mp.prepare();
			mp.start();
			
		}catch(Exception ioe){
			
		}
		//if(Prefs.getMusic(context)){
		
		
	//}
}
	
	public static void stop(Context context){
		if(mp!=null){
			mp.stop();
			mp.release();
			mp = null;
		}
	}

}

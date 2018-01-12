package profanity.mofogames.org;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

public class BootBroadcastReceiver extends BroadcastReceiver{
	 @Override
	    public void onReceive(Context context, Intent intent) {
	        Intent startServiceIntent = new Intent(context, AcceleromterService.class);
	        context.startService(startServiceIntent);
	    }
}


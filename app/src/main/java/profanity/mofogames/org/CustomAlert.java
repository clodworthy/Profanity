package profanity.mofogames.org;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;

public class CustomAlert extends AlertDialog.Builder{
	
	private String buttonLabel;
	
	
	public CustomAlert(Context context, int type, String message, DialogInterface.OnClickListener diocl ){
		super(context);
		switch(type){
			case R.integer.DIALOG_OK:
				this.setMessage(message);
				buttonLabel = context.getResources().getString(R.string.ok_button);
				this.setNeutralButton(buttonLabel, diocl);
				this.show();
				break;
				
			case R.integer.DIALOG_YES_NO:
				
				this.setMessage(message);
				buttonLabel = context.getResources().getString(R.string.yes_button);
				this.setPositiveButton(buttonLabel, diocl);
				buttonLabel = context.getResources().getString(R.string.no_button);
				this.setNegativeButton(buttonLabel, diocl);
				this.show();
				break;
		}
	}

}

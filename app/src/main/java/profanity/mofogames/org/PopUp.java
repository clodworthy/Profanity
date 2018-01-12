package profanity.mofogames.org;
import profanity.mofogames.org.R;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

/**
 * 
 * @author Dil
 * PopUp is a static wrapper class holding an instance of a Toast Pop Up.
 * This allows application level access to the custom Toast created by the AccelerometerService
 * and thus allows text & image display to be requested from any class in the app.
 *
 */
public class PopUp {
	
	private static ImageView imageView = null;
	private static TextView textView = null;
	private static Toast pop = null; 
	
	
	public static void setImageView(ImageView iv){
		imageView = iv;
	}
	
	public  static  void setTextView(TextView tv){
		textView = tv;
	}
	
	public static  void setPop(Toast initPop){
		pop = initPop;
	}
	
	public static  void setDuration(int duration){
		if(pop!=null){
			pop.setDuration(duration);
		}
	}
		
	public synchronized static void setText(String text){
		if(text!=null){
			textView.setText(text, TextView.BufferType.NORMAL);
		}
	}
	
	public synchronized static  void setImage(int RImageID){
		if(imageView!=null){
			imageView.setImageResource(RImageID);
			//imageView.setImageResource(R.drawable.icon);
		}
	}
	
	public static synchronized void show(){
		if(pop!=null){
			pop.show();
		}
	}
	
	public static synchronized void show(String text){
		if(pop!=null&&textView!=null){
			int wordCount = Utility.wordCount(text);
			int duration = Utility.max(1000, wordCount*150);
			if(duration<700){
				pop.setDuration(Toast.LENGTH_SHORT);
			}else{
				pop.setDuration(Toast.LENGTH_LONG);
			}
						
		textView.setText(text, TextView.BufferType.NORMAL);
		pop.show();
		}
		
	}
	
	public static synchronized void show(Profanity profanity){
		String text = profanity.getText();
		String pImageName= profanity.getImageName();
		
		if(pImageName!=null){
		  //set imageview
			Context context = ProfaneActivity.getContext();
			if(context!=null){
				int resourceID = context.getResources().getIdentifier(pImageName, "drawable", context.getPackageName() );
				imageView.setImageResource(resourceID);
			}
		}else{
			imageView.setImageResource(R.drawable.icon);
		}
		show(text);
		
	}
	
	
	
		
	

}

package profanity.mofogames.org;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.content.Intent;
import android.net.Uri;

public class DictionaryActivity extends Activity implements OnItemClickListener, OnItemLongClickListener, Constants{
	
	private ListView listView;
	private int index;
	private LanguagePack pack;
	private String[] filePath;
	private String[] expletives;
	private String[] imageName;
	private Profanity[] data;
	private static String TAG = "DictionaryActivity";
	//private AssetFileDescriptor afd;
	
	
	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		
		setContentView(R.layout.language_dictionary_view);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));

		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			index = extras.getInt(INDEX_KEY, 0);
		}else{
			index=0;
		}
		
/*		pack = Utility.getPackFromIndex(this, index);
		expletives = pack.getExpletive();
		filePath = pack.getFilePath();
		imageName = pack.getImageName();
		data = new Profanity[expletives.length];
		
		for(int i=0;i<expletives.length;i++){
						
			data[i]=new Profanity(R.drawable.play_button,expletives[i],filePath[i]);
			//data[i]=new Profanity(imageName[i],expletives[i],filePath[i]);
			
		}*/
		data = Utility.getProfanityArray(this, index);
		
		ProfanityAdapter adapter = new ProfanityAdapter(this, R.layout.listview_item_row, data);
		listView = (ListView)findViewById(R.id.listview_dictionary);
		View header = (View)getLayoutInflater().inflate(R.layout.listview_header_item, null);
		TextView headerText = (TextView)header.findViewById(R.id.txt_header);
		String[] languagePacks = getResources().getStringArray(R.array.packArray);
		String t = (String)headerText.getText();
		//Log.d(TAG,t );
		//Log.d(TAG,languagePacks[index] );
		headerText.setText(languagePacks[index]);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		if(position>0){
			position-=1;
		}
		String fp = data[position].getFilePath();
		//Log.d(TAG, "Position: "+position +"\nFilePath: "+fp);
		
		SfxPlayer.play(fp,this);
		//PopUp.show(expletives[position]);
		PopUp.show(data[position]);
		
	}
	//enables media to be shared with other applications
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
		if(position>0){
			position-=1;
		}
		String fp = data[position].getFilePath();
		Uri uri = Uri.parse(fp);
		//Log.d(TAG, "Position: "+position +"\nFilePath: "+fp);
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.setType("audio/mpeg3");
		shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_title)));
		
		
		return true;
		
	}
}

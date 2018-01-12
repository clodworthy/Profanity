package profanity.mofogames.org;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

public class DictionaryListView extends ListActivity implements Constants{
	
	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_background)));
		ListAdapter adapter = createAdapter();
		setListAdapter(adapter);
			
	}
	
	protected ListAdapter createAdapter(){
		String[] languagePacks = getResources().getStringArray(R.array.packArray);
		ListAdapter la = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languagePacks );
		return la;
	}
	
	@Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	    //Toast.makeText(this, item + " selected at position "+position, Toast.LENGTH_LONG).show();
	    Intent intent = new Intent(this, DictionaryActivity.class);
	    intent.putExtra(INDEX_KEY, position);
		startActivity(intent);
	}

}

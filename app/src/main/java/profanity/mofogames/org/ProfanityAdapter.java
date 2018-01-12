package profanity.mofogames.org;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;


public class ProfanityAdapter extends ArrayAdapter{
	
	private Context context;
	private Profanity data[] = null;
	private int layoutResourceId;
	
	public ProfanityAdapter(Context context, int layoutResourceId, Profanity[] data){
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.data=data;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		ProfanityHolder holder = null;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater)((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new ProfanityHolder();
			holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
			holder.textTitle = (TextView)row.findViewById(R.id.txtTitle);
			row.setTag(holder);
		}else{
			holder = (ProfanityHolder)row.getTag();
		}
		Profanity profanity = data[position];
		holder.textTitle.setText(profanity.profanityText);
		holder.imgIcon.setImageResource(profanity.icon);
		return row;
	
	}
	
	static class ProfanityHolder{
		public ImageView imgIcon;
		public TextView textTitle;
	}

}

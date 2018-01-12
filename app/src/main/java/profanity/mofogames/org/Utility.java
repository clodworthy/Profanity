package profanity.mofogames.org;
import android.content.Context;
import 	android.content.res.Resources;
import android.util.Log;


public class Utility implements Constants{
	
	public static Profanity[] getProfanityArray(Context context, int index){
		Profanity[] data;
		LanguagePack pack = getPackFromIndex(context, index);
		
		String[] expletives = pack.getExpletive();
		String[] filePath = pack.getFilePath();
		String[] imageName = pack.getImageName();
		data = new Profanity[expletives.length];
		int resourceId; 
		for(int i=0;i<expletives.length;i++){
			
			resourceId = R.drawable.play_button;
			if(imageName!=null&&imageName[i]!=null){
			resourceId = context.getResources().getIdentifier(imageName[i], "drawable", 
			   context.getPackageName());
			}
			
			data[i]=new Profanity(resourceId,expletives[i],filePath[i]);
			//data[i]=new Profanity(imageName[i],expletives[i],filePath[i]);
			
		}
		
		return data;
	}
	
	
	public static LanguagePack getPackFromIndex(Context context, int index){
		
		Resources resources = context.getResources();
		String[] expletive;
		String[] filePath;
		String[] images;
		int e_R_id;
		int f_R_id;
		int i_R_id; 
		
		switch(index){
		case ANCIENT_EGYPTIAN_BONGO:
				e_R_id = R.array.AncientEgyptianProfaneArray;
				f_R_id = R.array.AncientEgyptianFilePathArray;
			break;
		case ARABIC_BONGO:
				e_R_id = R.array.ArabicProfaneArray;
				f_R_id = R.array.ArabicFilePathArray;
			break;
 		case ENGLISH_BONGO:
 				e_R_id = R.array.BongoProfaneArray;
 				f_R_id = R.array.BongoProfaneFilePathArray;
 			break;
 			
 		case ENGLISH_HAWKING:
				e_R_id = R.array.HawkingProfaneArray;
				f_R_id = R.array.HawkingFilePathArray;
			break;
 		case ENGLISH_SHAKESPEAREAN:
 				e_R_id = R.array.ShakespeareProfaneArray;
 				f_R_id = R.array.ShakespeareFilePathArray;
 			break;
 		case FIREFLY:
				e_R_id = R.array.FireflyProfaneArray;
				f_R_id = R.array.FireflyFilePathArray;
			break;	
 			
 		case GREEK_BONGO:
 				e_R_id = R.array.GreekProfaneArray;
 				f_R_id = R.array.GreekFilePathArray;
 			
 			break;
 			
 		case ITALIAN_BONGO:
				e_R_id = R.array.ItalianProfaneArray;
				f_R_id = R.array.ItalianFilePathArray;
			
			break;
			
 			
 		
 		case KLINGON_BONGO:
 				e_R_id = R.array.KlingonProfaneArray;
 				f_R_id = R.array.KlingonFilePathArray;
 			break;
 			
 		case LATIN_BONGO:
				e_R_id = R.array.LatinProfaneArray;
				f_R_id = R.array.LatinFilePathArray;
			
			break;
 			
 		case SPANISH_BONGO:
				e_R_id = R.array.SpanishProfaneArray;
				f_R_id = R.array.SpanishFilePathArray;
			break;
 		default:
 				e_R_id = R.array.BongoProfaneArray;
 				f_R_id = R.array.BongoProfaneFilePathArray; 	
 				
 				
		}
		expletive = resources.getStringArray(e_R_id);
		filePath = resources.getStringArray(f_R_id);
		String name = resources.getStringArray(R.array.packArray)[index];
		return new LanguagePack(index, name, expletive,filePath);
		//return new LanguagePack(index, name, expletive,filePath, images);
	}
	
	//given a context and a specific drawable name
	//return the R identifying integer.
	public static int getResourceIdFromString(String resourceName, Context context){
		return  context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
		
	}
	
	public static int max(int int1, int int2){
		 if(int1> int2){
			 return int1;
		 }else{
			 return int2;
		 }
		 
	 }
	
	public static int wordCount(String s){
		 int count =0;
		 int index = 0;
		 int SPACE =32;
		 if(s.length()==0){
			 return 0;
		 }
		 for(int i=0; i<s.length(); i++){
			 if(s.charAt(i)==SPACE){
				 count++;
			 }
		 }
		 
		 return count;
	 }
	

}

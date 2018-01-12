package profanity.mofogames.org;

public class Profanity {
	
	public int icon;
	public String profanityText;
	public String filePath;
	public int rank;
	public String imageName;
	
	public Profanity(){
		super();
	}
	
	public Profanity(int icon, String profanityText){
		super();
		this.icon=icon;
		this.profanityText = profanityText;
	}
	public Profanity(int icon, String profanityText, String filePath){
		super();
		this.icon=icon;
		this.profanityText = profanityText;
		this.filePath=filePath;
	}
	public Profanity(String imageName, String profanityText, String filePath){
		super();
		this.icon=icon;
		this.profanityText = profanityText;
		this.filePath=filePath;
		this.imageName = imageName;
	}
	
	public String getFilePath(){
		return filePath;
		
	}
		
	public String getText(){
		return profanityText;
	}
	
	public int getIcon(){
		return icon;
	
	}
	public String getImageName(){
		
		return imageName;
	
	}
}

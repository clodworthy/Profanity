package profanity.mofogames.org;

public class LanguagePack {
	
	private String name;
	private int index;
	private String[] expletive;
	private String[] filePath;
	private String[] imageName;

	public LanguagePack(int index, String name, String[] expletive, String[] filePath){
		this.index=index;
		this.name=name;
		this.expletive=expletive;
		this.filePath=filePath;
	}
	
	public LanguagePack(int index, String name, String[] expletive, String[] filePath, String[] imageName){
		this.index=index;
		this.name=name;
		this.expletive=expletive;
		this.filePath=filePath;
		this.imageName = imageName;
	}
	
	public String getName(){
		return name;
	}
	
	public String[] getExpletive(){
		return expletive;
	}

	public String[] getFilePath(){
		return filePath;
	}
	
	public String[] getImageName(){
		return imageName;
	}
	
	public int getIndex(){
		return index;
	}
}

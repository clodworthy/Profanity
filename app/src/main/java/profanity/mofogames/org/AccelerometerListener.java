package profanity.mofogames.org;

public interface AccelerometerListener {
	
	public void onAccelerationChanged(float x, float y, float z);
	public void onShake(double force);

}

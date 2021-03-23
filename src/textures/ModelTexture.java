package textures;

public class ModelTexture {
	private int textureID;

	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;

	private int numberOfRows = 1;
	
	public ModelTexture(int texture){
		this.textureID = texture;
	}
		
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public int getID(){
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}
}

package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0,2,0);
	private float pitch = 10;
	private float yaw ;

	private float distanceFromMovableCam = 0;
	private float angleAroundPlayer = 0;

	public Camera(){}

	public void move(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.z-=0.08f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.z+=0.08f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x+=0.08f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x-=0.08f;
		}
		float wheel = Mouse.getDWheel();
		if(wheel > 0){
			position.y+=0.1f;
		}
		if(wheel < 0){
			position.y-=0.1f;
		}

		calcAngleAround();
		calcPitch();

		float hor = calcHorizontalDistance();
		float ver = calcVerticalDistance();

		calcCamPos(hor, ver);
		this.yaw = angleAroundPlayer;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	private void calcPitch() {
		if (Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}

	private float calcHorizontalDistance() {
		return (float) (distanceFromMovableCam * Math.cos(Math.toRadians(pitch)));
	}

	private float calcVerticalDistance() {
		return (float) (distanceFromMovableCam * Math.sin(Math.toRadians(pitch)));
	}

	private void calcAngleAround() {
		if (Mouse.isButtonDown(2)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

	private void calcCamPos(float horizontalDist, float verticalDist) {
		float angleTheta = 0 + angleAroundPlayer;
		float offsetX = (float) (horizontalDist * Math.sin(Math.toRadians(angleTheta)));
		float offsetZ = (float) (horizontalDist * Math.cos(Math.toRadians(angleTheta)));

		position.x = position.x - offsetX;
		position.y = position.y + verticalDist;
		position.z = position.z - offsetZ;
	}
}

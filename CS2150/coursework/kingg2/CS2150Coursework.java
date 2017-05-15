/* CS2150Coursework.java - Copyright c George King 2015, username: kingg2
 *
 * Scene Graph for SCENE 1 (not in flight):
 *  Scene origin
 *  |
 *  +-- [T(0,-1,3.1) R(-90,1,0,0) S(11,6,1)] Floor plane
 *  |
 *  +-- [T(0,3.7,-20) S(31,12,1) Background plane
 *  |
 *  +-- [T(tardisMoveX,0,tardisMoveZ)] TARDIS draw
 *  	|
 *  	+-- [T(0,0,-0.5) R(-180,0,1,0)] Back side of TARDIS
 *  	|
 *  	+-- [T(0,0,-0.25)] Inside of TARDIS (only if doorsOpen == true)
 *  	|
 *  	+-- [T(-0.5,0,0) R(-90,0,1,0)] Left side of TARDIS
 *  	|
 *  	+-- [T(0.5,0,0) R(90,0,1,0)] Right side of TARDIS
 *  	|
 *  	+-- [T(-0.25,0,0.5) S(0.5,1,1)] Left door of TARDIS
 *  	|
 *  	+-- [T(0.25,0,0.5) S(0.5,1,1)] Right door of TARDIS
 *  	|
 *  	+-- [T(0,-0.8,0) S(1.1,0.1,1.1)] Bottom cube of TARDIS
 *  	|
 *  	+-- [T(0,0.8,0) S(1.05,0.1,1.05)] Outer top cube of TARDIS
 *  	|
 *  	+-- [T(0,0.87,0) S(0.95,0.05,0.95)] Inner top cube of TARDIS
 *  	|
 *  	+-- [T(0,0.95,0) S(0.95,0.6,0.95)] Triangle off of TARDIS
 *  	|
 *  	+-- [T(0,1.12,0) R(90,1,0,0)] Cylinder light of TARDIS
 *  	|
 *  	+-- [T(0,1.135,0) S(0.15,0.4,0.15)] Triangle on top of TARDIS
 *
 *
 * Scene Graph for SCENE 2 (in flight):
 *   Scene origin
 *   |
 *   +-- [T(0,0,14) S(38,19,1)] Vortex background plane
 *   |
 *   +-- [T(0,0,-13) S(24,12,1)] ************SECRET**************
 *   |
 *   +-- [R(-tardisSpinSpeed,0.5,-2,0) T(tardisMoveX, 0, tardisMoveZ)]
 *   	|
 *   	+-- .... as above for rest of TARDIS
 */
package coursework.kingg2;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
//import Random and TimeUnit packages to be used appropriately 
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;

import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * Hello The Doctor, the great exterminator!
 *
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin.
 * <li>Pressing "Space" will transport the TARDIS across the screen.
 * <li>Pressing "v" will transport the TARDIS into vortex mode.
 * <li>Pressing "r" will reset the TARDIS to its default location.
 * <li>Pressing "d" will open / close the TARDIS doors
 * <li>Pressing "l" will toggle the TARDIS light on top. Although this is automatic. For the fun though!  </ul>
 * 
 * <p>There are totally not any more keyboard commands. Totally. (hint hint)  
 *
 */
public class CS2150Coursework extends GraphicsLab
{
	//Initialises all necessary Textures, floats and booleans
	private Texture tardisSide;
	private Texture tardisLeftDoor;
	private Texture tardisRightDoor;
	private Texture tardisTopText;
	private Texture tardisWood;
	private Texture backBackdrop;
	private Texture bottomBackdrop;
	private Texture vortex;
	private Texture secretVortex;
	private Texture inside;
	private float tardisMoveX = 0f;
	private float tardisSpinSpeed = 2f;
	private float backgroundSpinSpeed = 1f;
	private float tardisMoveZ = 0f;
	private float tardisAlpha = 1f;
	private float tardisFadeInAlpha = 1f;
	private float tardisLightAlpha = 0.4f;
	private float testBackgroundAlpha = 0f;
	private float tardisFadeOutColour = 1.0f;
	private boolean tardisSpinKeyPress = false;
	private boolean tardisMove = false;
	private boolean tardisVortex = false;
	private boolean tardisDoors = false;
	private boolean tardisFadeOut = false;
	private boolean tardisFadeIn = false;
	private boolean tardisIsMoving = false;
	private boolean backgroundBlend = false;
	private boolean lightFlash = false;
	private boolean tardisLightIsWhite = false;
	private boolean tardisLightIsGray = true;
	private boolean startAnimation = true;


	/** For slower computers, change the animation scale "0.01f" higher to keep animations up to speed
	 */
	public static void main(String args[])
	{   new CS2150Coursework().run(WINDOWED,"TARDIS SIMULATOR",0.1f);
	}
	/** 
	 * This method initialises all of the used textures, global lighting and the 3 objects used in my scenes
	 */
	protected void initScene() throws Exception
	{
		//loads all necessary textures
		tardisSide = loadTexture("coursework/kingg2/textures/side.bmp");
		tardisLeftDoor = loadTexture("coursework/kingg2/textures/frontLeft.bmp");
		tardisRightDoor = loadTexture("coursework/kingg2/textures/frontRight.bmp");
		tardisTopText = loadTexture("coursework/kingg2/textures/top.bmp");
		tardisWood = loadTexture("coursework/kingg2/textures/wood.bmp");
		backBackdrop = loadTexture("coursework/kingg2/textures/astonBackdrop.bmp");
		bottomBackdrop = loadTexture("coursework/kingg2/textures/astonFloordrop.bmp");
		vortex = loadTexture("coursework/kingg2/textures/vortex.bmp");
		secretVortex = loadTexture("coursework/kingg2/textures/secretvortex.bmp");
		inside = loadTexture("coursework/kingg2/textures/inside.bmp");
		// Enables lighting for the scene (allows TARDIS light to appear)
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_NORMALIZE);

		// Global ambient is currently completely unnecessary since everything is textured
		//float globalAmbient[] = {tardisLightAlpha, tardisLightAlpha, tardisLightAlpha, 1.0f};
		//GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbient));

		//initialises New Lists for each objects (1 = plane, 2 = cube, 3 = square pyramid 
		GL11.glNewList(1, GL11.GL_COMPILE);
		{	
			drawUnitPlane(); 
		}
		GL11.glEndList();
		GL11.glNewList(2, GL11.GL_COMPILE);
		{	
			drawUnitCube();
		}
		GL11.glEndList();
		GL11.glNewList(3, GL11.GL_COMPILE);
		{
			drawUnitPyramid();
		}
		GL11.glEndList();
	}
	protected void checkSceneInput()
	{
		//R calls the reset method, which literally resets the program to original state
		if(Keyboard.isKeyDown(Keyboard.KEY_R))
		{   resetAnimations();
		}
		// V allows the tardis to go into vortex mode by first fading out 
		else if(Keyboard.isKeyDown(Keyboard.KEY_V))
		{
			if (tardisVortex != true) {
				tardisSpinKeyPress = true;
				tardisFadeOut = true;
				lightFlash = true;
			}
		}
		// Toggles open / close of doors
		else if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			if (tardisDoors != true && tardisIsMoving == false ) {
				tardisDoors = true;
				try {
					TimeUnit.MILLISECONDS.sleep(150);
				} catch (InterruptedException e)
				{}
			}
			else if (tardisIsMoving == false ){
				tardisDoors = false;
				try {
					TimeUnit.MILLISECONDS.sleep(150);
				} catch (InterruptedException e)
				{}
			}
		}
		//Allows the TARDIS to teleport across the screen (if its not in vortex mode and not already moving)
		else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			if (tardisVortex != true && tardisIsMoving == false) {
				tardisFadeOut = true;
				lightFlash = true;
				tardisIsMoving = true;
			}
		}
		//enables or disables the light flashing function
		else if(Keyboard.isKeyDown(Keyboard.KEY_L))
		{
			if (lightFlash == false && tardisVortex == false){
				lightFlash = true;
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e)
				{}
			}
			else if (lightFlash == true && tardisVortex == false) {
				lightFlash = false;
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e)
				{}
			}
		}
		//???????????????????????????????????????????????????????????????????? But what is this! ;)
		else if (Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			if (tardisVortex == true) {
				if (backgroundBlend == false) {
					backgroundBlend = true;
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e)
					{}
				}
				else {
					backgroundBlend = false;
					try {
						TimeUnit.MILLISECONDS.sleep(200);
					} catch (InterruptedException e)
					{}
				}
			}
		}
		//Sets up the animation (fades in / light flashes immediately)
		if (startAnimation == true) {
			lightFlash = true;
			resetAnimations();
			startAnimation = false;
		}
	}
	protected void updateScene()
	{
		// Calls the move TARDIS method to move across screen
		if (tardisMove == true) {
			moveTardis();
			tardisMove = false;
		}
		// Sets the tardis and background to spin relative to the animation scale
		if (tardisVortex == true) {
			tardisSpinSpeed += (-4f * getAnimationScale());
			backgroundSpinSpeed += (1.7f * getAnimationScale());
		}
		// Calls the materialise method
		tardisMaterialise();
		// Honestly who made this method hey? It's not like someone put in the time or effort
		// to put an Easter Egg in a bit of coursework right...
		if (backgroundBlend == true) {
			testBackgroundAlpha += 0.05f * getAnimationScale();
		}
		// Light flashing commences. This basically slowly increases the global ambient, then decreases it until
		// something else (either L key command or tardisMaterialise method) stops it
		if (lightFlash == true) {
			if (!(tardisLightAlpha >= 1) && tardisLightIsWhite == false) {
				tardisLightAlpha += 0.03 * getAnimationScale();
				tardisLightIsGray = false;
				if (tardisLightAlpha >=1) {
					tardisLightIsWhite = true;
				}
			}
			else { 
				if (tardisLightIsWhite == true) {
					tardisLightAlpha -= 0.03 * getAnimationScale();
					if (!(tardisLightAlpha >= 0.4)) {
						tardisLightIsWhite = false;
					}
				}
			}
		}
		else if (tardisLightIsGray != true && lightFlash == false) {
			tardisLightAlpha -= 0.03 * getAnimationScale();
			if (!(tardisLightAlpha >= 0.4)) {
				tardisLightIsGray = true;
			}
		}
		// This changes the Z value for the entire TARDIS in Vortex mode
		// in order for it to successfully spin around the Z axis
		if (tardisVortex == true) {
			tardisMoveX = 0;
			tardisMoveZ = 1.7f;
		}

	}
	protected void renderScene()
	{
		// The background must change to another texture / position if its in vortex mode
		if (tardisVortex == false) {
			//draw floor
			GL11.glPushMatrix();
			{
				setUpTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,bottomBackdrop.getTextureID());

				GL11.glTranslatef(0f, -1f, 3.1f);
				GL11.glRotated(-90, 1f, 0, 0);
				GL11.glScalef(11, 6, 1);
				GL11.glCallList(1);

				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();

			//draw background
			GL11.glPushMatrix();
			{
				setUpTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,backBackdrop.getTextureID());

				GL11.glTranslatef(0f, 3.7f, -20f);
				GL11.glScalef(31, 12, 1);
				GL11.glCallList(1);

				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();
		}
		else if (tardisVortex == true) {
			GL11.glPushMatrix();
			{
				setUpTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,vortex.getTextureID());

				backgroundSpin();

				GL11.glTranslatef(0f, 0f, -14f);
				GL11.glScalef(38, 19, 1);
				GL11.glCallList(1);

				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();
			// Again, no idea what this is. At all. No way. Nope.
			if (backgroundBlend == true) {
				GL11.glPushMatrix();
				{
					setUpTextures();
					setUpBackgroundBlending();
					GL11.glBindTexture(GL11.GL_TEXTURE_2D,secretVortex.getTextureID());

					setUpBackgroundBlending();

					GL11.glTranslatef(0f, 0f, -13f);
					GL11.glScalef(24, 12, 1);
					GL11.glCallList(1);
					turnOffTexturesAndBlending();
				}
				GL11.glPopMatrix();
			}
		}


		GL11.glPushMatrix();
		{
			// If the TARDIS is in vortex mode, it rotates clockwise on the X axis
			if (tardisVortex == true) {
				GL11.glRotatef(-tardisSpinSpeed, 0.5f, -2.0f, 0.0f);
			}
			// This allows all X and Z values of the TARDIS to be drawn at the same place
			GL11.glTranslatef(tardisMoveX, 0f, tardisMoveZ);
			//draw BACK of TARDIS
			GL11.glPushMatrix();
			{
				// Calls the methods it needs in order to set up appropriate colours / textures
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				// Bind the correct texture to the shape
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisSide.getTextureID());
				// Move the shape to the right position
				GL11.glTranslatef(0f,0f,-0.5f);
				GL11.glRotatef(-180.0f, 0.0f, 1.0f, 0.0f);
				GL11.glCallList(1);
				//Turn off appropriate method calls (blending, textures)
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();

			//draw INSIDE of TARDIS
			if (tardisDoors == true && tardisFadeOut == false && tardisFadeIn == false) {
				GL11.glPushMatrix();
				{
					setUpTextures();
					setUpFadeOutBlending();
					setUpFadeInBlending();
					GL11.glBindTexture(GL11.GL_TEXTURE_2D,inside.getTextureID());

					GL11.glTranslatef(0f,0f,-0.25f);
					GL11.glCallList(1);
					turnOffTexturesAndBlending();
				}
				GL11.glPopMatrix();
			}

			//draw LEFT SIDE of TARDIS
			GL11.glPushMatrix();
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisSide.getTextureID());

				GL11.glTranslatef(-0.5f,0f,0f);
				GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
				GL11.glCallList(1);
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix(); 

			//draw RIGHT SIDE of TARDIS
			GL11.glPushMatrix();
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisSide.getTextureID());
				//move side to correct position
				GL11.glTranslatef(0.5f, 0f, 0f);
				GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
				GL11.glCallList(1);
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix(); 

			//draw LEFT DOOR of TARDIS
			GL11.glPushMatrix();
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisLeftDoor.getTextureID());
				GL11.glTranslatef(-0.25f, 0f, 0.5f);
				GL11.glScalef(0.5f, 1.0f, 1.0f);
				// All conditions must be satisfied for the doors to open
				if (tardisDoors == true && tardisFadeOut == false && tardisFadeIn == false) {
					GL11.glRotatef(45.0f, 0f, 1.0f, 0.0f);
					GL11.glTranslatef(+0.25f, 0f, -0.5f);
				}
				GL11.glCallList(1);
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix(); 

			//draw RIGHT DOOR of TARDIS
			GL11.glPushMatrix();
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisRightDoor.getTextureID());
				GL11.glTranslatef(0.25f, 0f, 0.5f);
				GL11.glScalef(0.5f, 1.0f, 1.0f);
				if (tardisDoors == true && tardisFadeOut == false && tardisFadeIn == false) {
					GL11.glRotatef(-45.0f, 0f, 1.0f, 0.0f);
					GL11.glTranslatef(-0.25f, 0f, -0.5f);
				}
				GL11.glCallList(1);
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();

			// Draw BOTTOM of TARDIS
			GL11.glPushMatrix(); 
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisWood.getTextureID());
				GL11.glTranslatef(0f, -0.8f, 0f);
				GL11.glScalef(1.1f, 0.1f, 1.1f);
				GL11.glCallList(2);
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();

			//Draw OUTER TOP of TARDIS
			GL11.glPushMatrix(); 
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisTopText.getTextureID());
				GL11.glTranslatef(0f, 0.8f, 0f);
				GL11.glScalef(1.05f, 0.1f, 1.05f);
				GL11.glCallList(2);
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();

			//Draw INNER TOP of TARDIS
			GL11.glPushMatrix(); 
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisWood.getTextureID());
				GL11.glTranslatef(0f, 0.87f, 0f);
				GL11.glScalef(0.95f, 0.05f, 0.95f);
				GL11.glCallList(2);
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();

			//draw TRIANGLE off TARDIS
			GL11.glPushMatrix();
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisWood.getTextureID());
				GL11.glTranslatef(0f, 0.95f, 0f);
				GL11.glScalef(0.95f, 0.6f, 0.95f);
				GL11.glCallList(3);
				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();

			//draw LIGHT Cylinder
			GL11.glPushMatrix();
			{
				// Creates an emission for the light object which is wrapped around it
				// Sets this emission's colour to tardisLightAlpha which is grey until changed
				float tardisLightEmission[] = {tardisLightAlpha, tardisLightAlpha, tardisLightAlpha, 1.0f};
				GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(tardisLightEmission));
				GL11.glTranslatef(0f, 1.12f, 0f);
				GL11.glRotated(90f, 1, 0, 0);
				// Creates the cylinder object needed for the light and sets normals
				Cylinder c = new Cylinder();
				c.setOrientation(GLU.GLU_OUTSIDE);
				c.draw(0.05f, 0.05f, 0.3f, 50, 10);
			}
			GL11.glPopMatrix();

			//draw TRIANGLE on TOP
			GL11.glPushMatrix();
			{
				setUpTextures();
				setUpFadeOutBlending();
				setUpFadeInBlending();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,tardisWood.getTextureID());

				GL11.glTranslatef(0f, 1.135f, 0f);
				GL11.glScalef(0.15f, 0.4f, 0.15f);
				GL11.glCallList(3);

				turnOffTexturesAndBlending();
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}
	protected void setSceneCamera()
	{
		// call the default behaviour defined in GraphicsLab. This will set a default perspective projection
		// and default camera settings ready for some custom camera positioning below...  
		super.setSceneCamera();

		// The camera changes depending on what scene you are in
		if (tardisVortex == false) {
			GLU.gluLookAt(0, 1.5f, 8, 0, 1.5f, 0, 0, 1, 0);
		}
		else if (tardisVortex == true) {
			GLU.gluLookAt(0, 0f, 8, 0, 0f, 0, 0, 1, 0);
		}
	}

	protected void cleanupScene()
	{ // No cleanup needed
	}

	private void drawUnitPlane() {
		// Vertices for creating a TARDIS side-shaped plane
		Vertex v1 = new Vertex(-0.5f, -0.8f,  0f);
		Vertex v2 = new Vertex(-0.5f,  0.8f,  0f);
		Vertex v3 = new Vertex( 0.5f,  0.8f,  0f);
		Vertex v4 = new Vertex( 0.5f, -0.8f,  0f);

		//draw the plane
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Normal n = new Normal(v4.toVector(), v3.toVector(), v2.toVector(), v1.toVector()); 
			n.submit();
			//Submit textures correctly (0,0 being bottom left of object, 1,1 being top right)
			GL11.glTexCoord2f(1.0f,0.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f,1.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f,1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v1.submit();
		}
		GL11.glEnd(); 
	}
	// Vertices for creating a cube
	private void drawUnitCube() {
		Vertex v1 = new Vertex(-0.5f, -0.5f,  0.5f);
		Vertex v2 = new Vertex(-0.5f,  0.5f,  0.5f);
		Vertex v3 = new Vertex( 0.5f,  0.5f,  0.5f);
		Vertex v4 = new Vertex( 0.5f, -0.5f,  0.5f);
		Vertex v5 = new Vertex(-0.5f, -0.5f, -0.5f);
		Vertex v6 = new Vertex(-0.5f,  0.5f, -0.5f);
		Vertex v7 = new Vertex( 0.5f,  0.5f, -0.5f);
		Vertex v8 = new Vertex( 0.5f, -0.5f, -0.5f);

		//draw near face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Normal n = new Normal(v4.toVector(), v3.toVector(), v2.toVector(), v1.toVector()); 
			n.submit();
			GL11.glTexCoord2f(1.0f,0.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f,1.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f,1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v1.submit();
		}
		GL11.glEnd(); 
		//draw left face
		GL11.glBegin(GL11.GL_POLYGON); 
		{
			Normal n = new Normal(v5.toVector(), v1.toVector(), v2.toVector(), v6.toVector()); 
			n.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f,0.0f);
			v1.submit();
			GL11.glTexCoord2f(1.0f,1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f,1.0f);
			v6.submit();
		}
		GL11.glEnd();
		//draw right face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Normal n = new Normal(v8.toVector(), v7.toVector(), v3.toVector(), v4.toVector()); 
			n.submit();
			GL11.glTexCoord2f(1.0f,0.0f);
			v8.submit();
			GL11.glTexCoord2f(1.0f,1.0f);
			v7.submit();
			GL11.glTexCoord2f(0.0f,1.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v4.submit();
		}
		GL11.glEnd();
		//draw top face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Normal n = new Normal(v7.toVector(), v6.toVector(), v2.toVector(), v3.toVector()); 
			n.submit();
			v7.submit();
			v6.submit();
			v2.submit();
			v3.submit();
		}
		GL11.glEnd();
		//draw bottom face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Normal n = new Normal(v4.toVector(), v1.toVector(), v5.toVector(), v8.toVector()); 
			n.submit();
			v4.submit();
			v1.submit();
			v5.submit();
			v8.submit();
		}
		GL11.glEnd();
		//draw far face
		GL11.glBegin(GL11.GL_POLYGON);
		{
			Normal n = new Normal(v8.toVector(), v5.toVector(), v6.toVector(), v7.toVector()); 
			n.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v8.submit();
			GL11.glTexCoord2f(1.0f,0.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f,1.0f);
			v6.submit();
			GL11.glTexCoord2f(0.0f,1.0f);
			v7.submit();
		}
		GL11.glEnd(); 
	}

	private void drawUnitPyramid() {
		// Vertices for creating a Square based pyramid which is used twice in my model
		Vertex v1 = new Vertex(0.5f, -0.1f,  0.5f);
		Vertex v2 = new Vertex(0f,  0.1f,  0f);
		Vertex v3 = new Vertex(-0.5f, -0.1f,  0.5f);
		Vertex v4 = new Vertex(0.5f, -0.1f,  -0.5f);
		Vertex v5 = new Vertex(-0.5f, -0.1f,  -0.5f);

		//draw front face
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			Normal n = new Normal(v1.toVector(), v2.toVector(), v3.toVector());
			n.submit();
			GL11.glTexCoord2f(1.0f,0.0f);
			v1.submit();
			GL11.glTexCoord2f(0.5f,0.5f);
			v2.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v3.submit();
		}
		GL11.glEnd();

		//draw right face
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			Normal n = new Normal(v4.toVector(), v2.toVector(), v1.toVector());
			n.submit();
			GL11.glTexCoord2f(1.0f,0.0f);
			v4.submit();
			GL11.glTexCoord2f(0.5f,0.5f);
			v2.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v1.submit();
		}
		GL11.glEnd();

		//draw back face
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			Normal n = new Normal(v4.toVector(), v5.toVector(), v2.toVector());
			n.submit();
			GL11.glTexCoord2f(1.0f,0.0f);
			v4.submit();
			GL11.glTexCoord2f(0.5f,0.5f);
			v5.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v2.submit();
		}
		GL11.glEnd();

		//draw left face
		GL11.glBegin(GL11.GL_TRIANGLES);
		{
			Normal n = new Normal(v5.toVector(), v3.toVector(), v2.toVector());
			n.submit();
			GL11.glTexCoord2f(1.0f,0.0f);
			v5.submit();
			GL11.glTexCoord2f(0.5f,0.5f);
			v3.submit();
			GL11.glTexCoord2f(0.0f,0.0f);
			v2.submit();
		}
		GL11.glEnd();
	}
	// This method sets up appropriate textures and submits the colour white in order to see details of textures
	private void setUpTextures() {
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);
		Colour.WHITE.submit();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	// Disables textures ready for the next object to be made
	private void turnOffTexturesAndBlending() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopAttrib();
	}
	// Generates a random int between -3,3 which isn't the same as tardisMoveX's original value
	private void moveTardis() {
		int original = (int) tardisMoveX;
		while (tardisMoveX == original) {
			int testInt;  
			testInt = randInt(3, -3);
			if (testInt != tardisMoveX) {
				tardisMoveX = testInt;
			}
		}
	}
	// Resets all booleans and scenes
	private void resetAnimations() {
		tardisIsMoving = true;
		tardisMoveX = 0f;
		tardisMoveZ = 0f;
		tardisVortex = false;
		tardisDoors = false;
		backgroundBlend = false;
		counter = 3;
		tardisSpinKeyPress = false;
		tardisFadeOut = false;
		lightFlash = true;
		tardisFadeIn = true;
	}
	// Random number generator between a max and min value
	private static int randInt(int max, int min) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	// Rotates background around the X axis
	private void backgroundSpin() {
		if (tardisVortex == true) {
			GL11.glRotatef(backgroundSpinSpeed, 0, 0, 1);
		}
	}
	// Sets up Fading Out blending using White (tardisFadeColour = 1)
	private void setUpFadeOutBlending() {
		if (tardisFadeOut == true) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(tardisFadeOutColour, tardisFadeOutColour, tardisFadeOutColour, tardisAlpha);
		}
	}
	// Sets Up Fading In blending using White (tardisFadeColour = 1)
	private void setUpFadeInBlending() {
		if (tardisFadeIn == true) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_SRC_ALPHA);
			GL11.glColor4f(tardisFadeOutColour, tardisFadeOutColour, tardisFadeOutColour, tardisFadeInAlpha);
		}
	}
	// Completely useless.... You think. ;)
	private void setUpBackgroundBlending() {
		if (backgroundBlend == true) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, testBackgroundAlpha);

		}
	}

	// Creates a very long method allowing TARDIS to "blink" in and out of transparency. 
	// It Fades Out a total of 3 times before it fades back in. This is the job of the counter
	// If the counter = 2 (0+1+1+1) then the TARDIS will move
	int counter = 0;
	private void tardisMaterialise() {
		if (tardisFadeOut == true) {
			tardisAlpha -= 0.05f * getAnimationScale();
			if (tardisAlpha <= 0f) {
				if (counter == 2 && tardisSpinKeyPress == false) {
					tardisMove = true;
				}
				else if (counter == 2 && tardisSpinKeyPress == true) {
					tardisVortex = true;
					lightFlash = true;
				}
				if (tardisVortex == false) {
					tardisAlpha = 1f;
					tardisFadeIn = true;
					tardisFadeOut = false;
					tardisDoors = false;
					counter++;
				}
				else if (tardisVortex == true) {
					tardisAlpha = 1f;
					tardisFadeOut = false;
				}

			}
		}
		// When the counter = 3 then the TARDIS can stop blinking because it would have 
		// moved in the previous if statement
		if (tardisFadeIn == true) {
			tardisFadeInAlpha -= 0.05f * getAnimationScale();
			if (tardisFadeInAlpha <= 0) {
				tardisFadeIn = false;
				tardisFadeInAlpha = 1;
				if (counter != 3) {
					tardisFadeOut = true;
				}
				else {
					counter = 0;
					lightFlash = false;
					tardisIsMoving = false;
				}
			}
		}
	}
}

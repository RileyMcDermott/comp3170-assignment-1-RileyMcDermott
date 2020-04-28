package comp3170.ass1;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.GLException;
import comp3170.InputManager;
import comp3170.Shader;
import comp3170.ass1.sceneobjects.Helicopter;
import comp3170.ass1.sceneobjects.Helipad;
import comp3170.ass1.sceneobjects.House;
import comp3170.ass1.sceneobjects.Hud;
import comp3170.ass1.sceneobjects.River;
import comp3170.ass1.sceneobjects.Rotor;
import comp3170.ass1.sceneobjects.Tree;
import comp3170.ass1.sceneobjects.SceneObject;
import comp3170.ass1.sceneobjects.Speedo;

public class Assignment1 extends JFrame implements GLEventListener {

	private GLCanvas canvas;
	private Shader shader;
	
	final private File DIRECTORY = new File("src/comp3170/ass1"); 
	final private String VERTEX_SHADER = "vertex.glsl";
	final private String FRAGMENT_SHADER = "fragment.glsl";

	private InputManager input;
	
	private SceneObject root;	

	private Matrix4f worldMatrix;
	private Animator animator;
	
	Helicopter helicopter;
	Rotor rotor1;
	Rotor rotor2;
	
	Speedo speedo;
	
	boolean moving = false;
	float speed = 0;
	
	boolean landed = true;
	boolean takeoff = false;
	boolean landing = false;
	float heliSize = 0.5f;
	
	public Assignment1() {
		super("COMP3170 Assignment 1");
		
		// create an OpenGL 4 canvas and add this as a listener
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// create an input manager to listen for keypresses and mouse events
		
		this.input = new InputManager();
		input.addListener(this);
		input.addListener(this.canvas);

		// set up the JFrame		
		
		this.setSize(1000, 1000);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	
		
		//adding an animator
		animator = new Animator(canvas);
		animator.start();
		
	}

	@Override
	/**
	 * Initialise the GLCanvas
	 */
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// Compile the shader
		try {
			File vertexShader = new File(DIRECTORY, VERTEX_SHADER);
			File fragementShader = new File(DIRECTORY, FRAGMENT_SHADER);
			this.shader = new Shader(vertexShader, fragementShader);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// allocate matrices
		
		this.worldMatrix = new Matrix4f();
		
		// construct objects and attach to the scene-graph
		this.root = new SceneObject();
		
		River river = new River(this.shader);
		river.localMatrix.scale(1f, 0.8f, 1);
		river.setParent(this.root);
		
		House house1 = new House(this.shader);
		house1.localMatrix.translate(0.3f, -0.2f, 0);
		house1.localMatrix.scale(0.04f, 0.04f, 1);
		house1.setParent(this.root);
		
		House house2 = new House(this.shader);
		house2.localMatrix.translate(0.1f, 0.15f, 0);
		house2.localMatrix.scale(0.04f, 0.04f, 1);
		house2.setParent(this.root);
		
		House house3 = new House(this.shader);
		house3.localMatrix.translate(0.4f, 0.15f, 0);
		house3.localMatrix.scale(0.04f, 0.04f, 1);
		house3.setParent(this.root);
		
		Tree tree1 = new Tree(this.shader);
		tree1.localMatrix.translate(-0.3f, -0.4f, 0);
		tree1.localMatrix.scale(0.08f, 0.08f, 1);
		tree1.setParent(this.root);
		
		Tree tree2 = new Tree(this.shader);
		tree2.localMatrix.translate(-0.2f, -0.3f, 0);
		tree2.localMatrix.scale(0.08f, 0.08f, 1);
		tree2.setParent(this.root);
		
		Tree tree3 = new Tree(this.shader);
		tree3.localMatrix.translate(-0.2f, -0.5f, 0);
		tree3.localMatrix.scale(0.08f, 0.08f, 1);
		tree3.setParent(this.root);
		
		Helipad helipad = new Helipad(this.shader);
		helipad.localMatrix.translate(-0.4f, 0.5f, 0);
		helipad.localMatrix.scale(0.1f, 0.1f, 1);
		helipad.setParent(this.root);
		
		helicopter = new Helicopter(this.shader);
		helicopter.localMatrix.translate(-0.4f, 0.5f, 0);
		helicopter.localMatrix.rotateZ((float)Math.PI);
		helicopter.localMatrix.scale(0.25f, 0.25f, 1);
		helicopter.setParent(this.root);
		
		rotor1 = new Rotor(this.shader);
		rotor1.localMatrix.translate(0, 0.1f, 0);
		rotor1.localMatrix.rotateZ((float)Math.PI/4);
		rotor1.localMatrix.scale(0.7f, 0.7f, 1);
		rotor1.setParent(helicopter);
		
		rotor2 = new Rotor(this.shader);
		rotor2.localMatrix.translate(0, -0.1f, 0);
		rotor2.localMatrix.scale(0.7f, 0.7f, 1);
		rotor2.setParent(helicopter);
		
		Hud hud = new Hud(this.shader);
		hud.localMatrix.translate(-0.75f, -0.9f, 0);
		hud.localMatrix.scale(0.2f, 0.2f, 1);
		hud.setParent(this.root);
		
		speedo = new Speedo(this.shader);
		speedo.localMatrix.translate(0, 0.05f, 0);
		speedo.localMatrix.rotateZ((float)Math.PI/2);
		speedo.setParent(hud);
	}

	@Override
	/**
	 * Called when the canvas is redrawn
	 */
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// set the background colour to dark green
		gl.glClearColor(0.1f, 0.6f, 0.1f, 1.0f);
		gl.glClear(GL_COLOR_BUFFER_BIT);
		
		//adding key press functionality
		if(input.isKeyDown(KeyEvent.VK_UP) && !landed) {
			helicopter.localMatrix.translate(0f, 0.01f, 0);
			moving = true;
		} else {
			moving = false;
		}
		
		//increasing the speedometer 
		if(moving && speed < 1) {
			speedo.localMatrix.rotateZ((float)-Math.PI/50);
			speed += 0.02;
		}
		
		if(speed > 1) {
			speed = 1;
		}
		
		//decreasing the speedometer
		if(speed > 0 && !moving) {
			speedo.localMatrix.rotateZ((float)Math.PI/50);
			speed -= 0.02;
		}
		
		if(speed < 0) {
			speed = 0;
		}
		
		if(input.isKeyDown(KeyEvent.VK_LEFT) && !landed){
		helicopter.localMatrix.rotateZ((float)Math.PI/144);
		}
		
		if(input.isKeyDown(KeyEvent.VK_RIGHT) && !landed){
			helicopter.localMatrix.rotateZ((float)-Math.PI/144);
		}
		
		//if the helicopter is around the helipad it can start landing
		if(input.isKeyDown(KeyEvent.VK_L) && !landed &&
				helicopter.localMatrix.m30() < -0.32f && helicopter.localMatrix.m30() > -0.53f &&
				helicopter.localMatrix.m31() > 0.47f && helicopter.localMatrix.m31() < 0.63f) {
			landing = true;
			landed = true;
		}
		
		if(landing && heliSize > 0.5f) {
			helicopter.localMatrix.scale(0.99f, 0.99f, 1);
			heliSize -= 0.01f;
		}
		
		if(heliSize <= 0.5f) {
			heliSize = 0.5f;
			landing = false;
		}
		
		//allowing the helicopter to take off
		if(input.isKeyDown(KeyEvent.VK_T) && landed) {
			takeoff = true;
		}
		
		if(takeoff && heliSize < 1) {
			helicopter.localMatrix.scale(1.01f, 1.01f, 1);
			heliSize += 0.01f;
		}
		
		if(heliSize >= 1) {
			heliSize = 1;
			landed = false;
			takeoff = false;
		}
		
		rotor1.localMatrix.rotateZ((float)Math.PI/40);
		rotor2.localMatrix.rotateZ((float)-Math.PI/40);
		
		this.shader.enable();
		
		this.worldMatrix.identity();
		this.root.draw(shader, worldMatrix);

	}

	@Override
	/**
	 * Called when the canvas is resized
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

	}

	@Override
	/**
	 * Called when we dispose of the canvas 
	 */
	public void dispose(GLAutoDrawable drawable) {

	}

	public static void main(String[] args) throws IOException, GLException {
		new Assignment1();
	}


}

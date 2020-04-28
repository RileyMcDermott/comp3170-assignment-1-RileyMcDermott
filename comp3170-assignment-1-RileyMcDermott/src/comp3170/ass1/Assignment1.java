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
import comp3170.ass1.sceneobjects.Rotor;
import comp3170.ass1.sceneobjects.Tree;
import comp3170.ass1.sceneobjects.SceneObject;

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
		
		House house1 = new House(this.shader);
		house1.localMatrix.translate(0.3f, -0.2f, 0);
		//house1.localMatrix.rotateZ((float) Math.PI / 6);
		house1.localMatrix.scale(0.05f, 0.05f, 1);
		house1.setParent(this.root);
		
		Tree tree1 = new Tree(this.shader);
		tree1.localMatrix.scale(0.1f, 0.1f, 1);
		tree1.setParent(this.root);
		
		Helipad helipad = new Helipad(this.shader);
		helipad.localMatrix.translate(-0.2f, 0.3f, 0);
		helipad.localMatrix.scale(0.1f, 0.1f, 1);
		helipad.setParent(this.root);
		
		helicopter = new Helicopter(this.shader);
		helicopter.localMatrix.translate(0, 0.5f, 0);
		helicopter.localMatrix.scale(0.5f, 0.5f, 1);
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
		
		if(input.isKeyDown(KeyEvent.VK_UP)) {
			helicopter.localMatrix.translate(0f, 0.01f, 0);
		}
		if(input.isKeyDown(KeyEvent.VK_LEFT)){
		helicopter.localMatrix.rotateZ((float)Math.PI/144);
		}
		
		if(input.isKeyDown(KeyEvent.VK_RIGHT)){
			helicopter.localMatrix.rotateZ((float)-Math.PI/144);
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

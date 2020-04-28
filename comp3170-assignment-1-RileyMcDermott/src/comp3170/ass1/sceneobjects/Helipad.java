package comp3170.ass1.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Helipad extends SceneObject{
	public static float TAU = (float) (Math.PI * 2);	// https://tauday.com/tau-manifesto
	public static int NSIDES = 50; 
	
	float[] padVertices = new float[NSIDES * 3 * 2];
	
	private int padVertexBuffer;
	
	float[] HVertices = new float[] {
			0.3f, 0.1f,
			0.3f, -0.1f,
			-0.3f, -0.1f,
			
			-0.3f, -0.1f,
			-0.3f, 0.1f,
			0.3f, 0.1f,
			
			0.3f, 0.5f,
			0.5f, 0.5f,
			0.5f, -0.5f,
			
			0.5f, -0.5f,
			0.3f, -0.5f,
			0.3f, 0.5f,
			
			-0.3f, 0.5f,
			-0.5f, 0.5f,
			-0.5f, -0.5f,
			
			-0.5f, -0.5f,
			-0.3f, -0.5f,
			-0.3f, 0.5f,
	};
	
	private int HVertexBuffer;

	private float[] padColour = {0.62f, 0.62f, 0.62f}; // grey
	private float[] HColour = {1f, 1f, 1f,}; //white
	
	
	public Helipad(Shader shader) {
		//creating a circle for the helipad.
		int j = 0;
		for (int i = 0; i < NSIDES; i++) {
			
			float angle0 = i * TAU / NSIDES;
			float angle1 = (i+1) * TAU / NSIDES;
			
			// first point is in the centre of the circle
			padVertices[j++] = 0;	// x
			padVertices[j++] = 0;	// y
			
			// second point is at angle0
			padVertices[j++] = (float) Math.cos(angle0);	// x
			padVertices[j++] = (float) Math.sin(angle0);	// y

			// third point is at angle1
			padVertices[j++] = (float) Math.cos(angle1);	// x
			padVertices[j++] = (float) Math.sin(angle1);	// y
		}
		
	    this.padVertexBuffer = shader.createBuffer(this.padVertices);
	    this.HVertexBuffer = shader.createBuffer(this.HVertices);
	}
	
	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", this.padVertexBuffer);	    
		shader.setUniform("u_colour", this.padColour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.padVertices.length / 2);           	

        shader.setAttribute("a_position", this.HVertexBuffer);	    
		shader.setUniform("u_colour", this.HColour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.HVertices.length / 2);
	}
}

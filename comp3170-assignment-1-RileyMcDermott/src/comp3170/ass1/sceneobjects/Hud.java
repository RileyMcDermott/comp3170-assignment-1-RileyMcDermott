package comp3170.ass1.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Hud extends SceneObject{
	
	int NSIDES = 20;
	
	private float[] vertices = new float[NSIDES * 3 * 2];
	
	private int vertexBuffer;
	
	private float[] colour = {0, 0, 0}; //black
	
	public Hud(Shader shader) {		
		int j = 0;
		for (int i = 0; i < NSIDES; i++) {
			
			float angle0 = i * (float)Math.PI / NSIDES;
			float angle1 = (i+1) * (float)Math.PI / NSIDES;
			
			// first point is in the centre of the circle
			vertices[j++] = 0;	// x
			vertices[j++] = 0;	// y
			
			// second point is at angle0
			vertices[j++] = (float) Math.cos(angle0);	// x
			vertices[j++] = (float) Math.sin(angle0);	// y

			// third point is at angle1
			vertices[j++] = (float) Math.cos(angle1);	// x
			vertices[j++] = (float) Math.sin(angle1);	// y
			
	    this.vertexBuffer = shader.createBuffer(this.vertices); 
		}
	}
	
	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", this.vertexBuffer);	    
		shader.setUniform("u_colour", this.colour);	 
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length / 2);      	
	
	}
}

package comp3170.ass1.sceneobjects;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Helicopter extends SceneObject{
	private float[] vertices = new float[] {
			-0.1f, -0.15f,
			0.1f, -0.15f,
			0.1f, 0.15f,
			
			-0.1f, -0.15f,
			-0.1f, 0.15f,
			0.1f, 0.15f,
			
			0.1f, 0.15f,
			-0.1f, 0.15f,
			0, 0.25f,
			
			-0.1f, -0.15f,
			0.1f, -0.15f,
			0, -0.25f
			
	};
;
	private int vertexBuffer;

	private float[] colour = {0.8f, 0.8f, 0.2f}; // yellow
	
	public Helicopter(Shader shader) {		
	    this.vertexBuffer = shader.createBuffer(this.vertices);    
	}
	
	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", this.vertexBuffer);	    
		shader.setUniform("u_colour", this.colour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length / 2);      	
	}
}

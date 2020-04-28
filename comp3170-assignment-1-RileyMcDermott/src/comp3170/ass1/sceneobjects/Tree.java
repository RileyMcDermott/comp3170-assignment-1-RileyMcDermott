package comp3170.ass1.sceneobjects;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Tree extends SceneObject{
	
	private float[] trunkVertices = new float[] {
			0.25f, 0.8f,
			0.25f, -1f,
			-0.25f, -1f,
			
			-0.25f, -1f,
			-0.25f, 0.8f,
			0.25f, 0.8f
	};
	private int trunkVertexBuffer;
	
	private float[] leafVertices = new float[] {
			0f, 1.5f,
			0.75f, -0.5f,
			-0.75f, -0.5f
	};
	private int leafVertexBuffer;
	
	private float[] trunkColour = {0.43f, 0.2f, 0}; // brown
	private float[] leafColour = {0f, 0.43f, 0}; // dark green
	
	public Tree(Shader shader) {
		this.trunkVertexBuffer = shader.createBuffer(this.trunkVertices);
	    this.leafVertexBuffer = shader.createBuffer(this.leafVertices);  
	}
	
	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setAttribute("a_position", this.trunkVertexBuffer);	    
		shader.setUniform("u_colour", this.trunkColour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.trunkVertices.length / 2);           	

		shader.setAttribute("a_position", this.leafVertexBuffer);	    
		shader.setUniform("u_colour", this.leafColour);	    
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.leafVertices.length / 2);           	

	}
}

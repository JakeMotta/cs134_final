package Camera;
import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;

public class Window {
	
	// The previous frame's keyboard state.
    public boolean kbPrevState[] = new boolean[256];

    // The current frame's keyboard state.
    public boolean kbState[] = new boolean[256];
    
    public GL2 gl;
    public GLWindow myWindow;
    
    private int width = 800;
    private int height = 600;
    
    public Window() {
    	GLProfile gl2Profile;
	    
		try {
	        // Make sure we have a recent version of OpenGL
	        gl2Profile = GLProfile.get(GLProfile.GL2);
	    }
	    catch (GLException ex) {
	        System.out.println("OpenGL max supported version is too low.");
	        System.exit(1);
	        return;
	    }
		
		// Create the myWindow and OpenGL context.
	    myWindow = GLWindow.create(new GLCapabilities(gl2Profile));
	    myWindow.setSize(width, height);
	    myWindow.setTitle("I can't charge my phone and listen to music at the same time");
	    myWindow.setVisible(true);
	    myWindow.setDefaultCloseOperation(
	            WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
	    myWindow.addKeyListener(new KeyListener() {
	        @Override
	        public void keyPressed(KeyEvent keyEvent) {
	            if (keyEvent.isAutoRepeat()) {
	                return;
	            }
	            kbState[keyEvent.getKeyCode()] = true;
	        }
	
	        @Override
	        public void keyReleased(KeyEvent keyEvent) {
	            if (keyEvent.isAutoRepeat()) {
	                return;
	            }
	            kbState[keyEvent.getKeyCode()] = false;
	        }
	    });
	    
	    // Setup OpenGL state.
	    myWindow.getContext().makeCurrent();
	    gl = myWindow.getGL().getGL2();
	    gl.glViewport(0, 0, width, height);
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glOrtho(0, width, height, 0, 0, 100);
	    gl.glEnable(GL2.GL_TEXTURE_2D);
	    gl.glEnable(GL2.GL_BLEND);
	    gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public GL2 getGL() { return gl; }

}

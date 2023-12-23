package javafx.driftfx.opengl.canvas.example.rendering;

import java.nio.ByteBuffer;

import org.eclipse.fx.drift.DriftFXSurface;
import org.eclipse.fx.drift.GLRenderer;
import org.eclipse.fx.drift.PresentationMode;
import org.eclipse.fx.drift.RenderTarget;
import org.eclipse.fx.drift.Renderer;
import org.eclipse.fx.drift.StandardTransferTypes;
import org.eclipse.fx.drift.Swapchain;
import org.eclipse.fx.drift.SwapchainConfig;
import org.eclipse.fx.drift.TransferType;
import org.eclipse.fx.drift.Vec2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GLCapabilities;
import javafx.driftfx.opengl.canvas.example.fx.JFXWindow;
import javafx.driftfx.opengl.canvas.example.utils.GLFunctions;

import static org.lwjgl.opengl.GL11.*;

public class RenderLoop extends Thread {

	public static boolean enableFPS = false;
	public static boolean sendToDFX = true;

	private DriftFXSurface surface;
	private Swapchain chain;
	public Renderer hook;

	public int width;
	public int height;

	// For linux.
	public TransferType transfer = StandardTransferTypes.MainMemory;
	// For windows implementation.
	//private TransferType transfer = StandardTransferTypes.NVDXInterop;
	public long contextID = -1;
	public GLCapabilities glCaps;
	private int fb;
	private int depthTex;
	private boolean shouldClose = false;
//	private final MovingAverage FPS = new MovingAverage(30);

	private Framebuffer msaaBuffer;
	private Framebuffer intermediate;
	private int frames = 0;

	private void load() {
		if (hook != null && this.loadFrom(JFXWindow.getRenderPane())) {
//			ctx = org.eclipse.fx.drift.internal.GL.createContext(0, 1, 0);
//			long ctx = org.eclipse.fx.drift.internal.GL.createContext(0, 3, 2);
//
			contextID = org.eclipse.fx.drift.internal.GL.createSharedCompatContext(0);
			org.eclipse.fx.drift.internal.GL.makeContextCurrent(contextID);
			glCaps = GL.createCapabilities();

//			GLFW.glfwInit();
//			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE);
//			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
//			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
//			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
//			//GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 4);
//			//GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
//			contextID = GLFW.glfwCreateWindow(800, 800, "test", 0, 0);
//			if (contextID == 0) {
//				throw new RuntimeException("Failed to create window");
//			}
//			GLFW.glfwHideWindow(contextID);
//			GLFW.glfwMakeContextCurrent(contextID);
//			glCaps = GL.createCapabilities();
//			StatusHandler.postStatus("Renderer initialized.", 10000);
		}
//		else {
//			//not feasible StatusHandler.postStatus("Renderer waiting for DFX surface...", 10000);
//		}
	}

	private boolean loadFrom(DriftFXSurface surf) {
		if (surf == null)
			return false;
		surface = surf;
		return true;
	}

	@Override
	public void run() {
		while (!shouldClose) {
			try {
				long pre = System.currentTimeMillis();
				this.renderLoop();
				long post = System.currentTimeMillis();
				if (enableFPS) {
					long dur = Math.max(1, post-pre);
//					FPS.addValue(1000/dur);
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				shouldClose = true;
			}
		}

		if (chain != null)
			chain.dispose();
		chain = null;
		GLFW.glfwDestroyWindow(contextID);
		GLFW.glfwTerminate();
	}

	private void renderLoop() throws InterruptedException {
		Vec2i size;

		if (surface == null) {
			this.load();
			if (surface == null) {
				return;
			}

			size = hook.getSize();

			while (size.x == 0 || size.y == 0) {
				sleep(300);
				System.out.println("waiting for java fx node size initialization...");
				size = hook.getSize();
//			shouldClose = true;
//			throw new RuntimeException("Render box is size zero!");
			}

			sleep(300);
			// In linux the init sometimes get corrupted memory, make sure javafx had enough time for app init once after drift fx
			// node gets visible for first time & gets size set.
			System.out.println("waiting for java fx gui libs to setup gl and app window stuff...");
//			hook = GLRenderer.getRenderer(surface);
			return;
		}

		size = hook.getSize();

		if (chain == null || size.x != width || size.y != height) {
			System.out.println("Recreating swapchain");
			if (chain != null) {
				chain.dispose();
			}

			chain = hook.createSwapchain(new SwapchainConfig(size, 2, PresentationMode.MAILBOX, transfer));

			width = size.x;
			height = size.y;

			//input = new GLFWInputHandler(width, height, contextID);
			//GLFW.glfwSetCursorPosCallback(contextID, input.mouseCall);
			//GLFW.glfwSetMouseButtonCallback(contextID, input.clickCall);

			msaaBuffer = null;
			intermediate = null;
		}

		GLFunctions.printGLErrors("Main loop");
		if (contextID <= 0)
			return;

		// Supersampling doesnt work with virtualbox hardware acceleration, though works in native env.
//		if (msaaBuffer == null)
//			msaaBuffer = new Framebuffer(width, height, true);

		if (intermediate == null)
			intermediate = new Framebuffer(width, height).setClear(1, 1, 0);

//		GLFunctions.printGLErrors("Pre-render");
//		msaaBuffer.bind(false);
//		GLFunctions.printGLErrors("Framebuffer bind");
//		this.render(width, height);
//		GLFunctions.printGLErrors("Draw");
//		msaaBuffer.unbind();
//		GLFunctions.printGLErrors("Framebuffer unbind");
//		msaaBuffer.sendTo(intermediate);
//		GLFunctions.printGLErrors("Framebuffer copy to intermediate");

		GLFunctions.printGLErrors("Pre-render");
		intermediate.bind(false);
		GLFunctions.printGLErrors("Framebuffer bind");
		this.render(width, height);
		GLFunctions.printGLErrors("Draw");
		intermediate.unbind();
		GLFunctions.printGLErrors("Framebuffer unbind");

		GL11.glFlush();

		if (sendToDFX) {
			// You have to acquire it every frame.
			RenderTarget renderTarget = chain.acquire();

			// Texture is changed every frame, you need to request the current one.
			int	tex = GLRenderer.getGLTextureId(renderTarget);
			// According to docs depth texture is used for culling.
			if (depthTex <= 0) {
				depthTex = GL11.glGenTextures();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTex);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL32.GL_DEPTH_COMPONENT32F, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			}

			if (fb <= 0) {
				fb = GL32.glGenFramebuffers();

				GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, fb);
				GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_COLOR_ATTACHMENT0, tex, 0);
				GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_DEPTH_ATTACHMENT, depthTex, 0);
			}
			else {
				GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, fb);
				GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_COLOR_ATTACHMENT0, tex, 0);
        // GL32.glFramebufferTexture(GL32.GL_FRAMEBUFFER, GL32.GL_DEPTH_ATTACHMENT, depthTex, 0);
			}

			int status = GL32.glCheckFramebufferStatus(GL32.GL_FRAMEBUFFER);
			switch (status) {
				case GL32.GL_FRAMEBUFFER_COMPLETE:
					break;
				case GL32.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
					System.err.println("INCOMPLETE_ATTACHMENT!");
					break;
			}

			GLFunctions.printGLErrors("DFX Framebuffer bind");
			// Some test gl calls.
			glClearColor(frames > 25 ? 0 : 1, 1, 0, 1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			// intermediate.sendTo(fb);
			intermediate.draw();
			GLFunctions.printGLErrors("Framebuffer render to DFX");

			GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, 0);
//			GL32.glDeleteFramebuffers(fb);
//			GL11.glDeleteTextures(depthTex);
			GLFunctions.printGLErrors("DFX Framebuffer unbind");

			// Display updated texture to javafx surface node.
			chain.present(renderTarget);
		}
	}

	private void render(int x, int y) throws InterruptedException {
		// Some dirty gl code to test gl works.
		GL11.glClearColor(1, frames > 25 ? 0 : 1, 0, 1);
//		GL11.glClearColor(1, frames % 22 == 0 ? 0 : 1, frames % 49 == 0 ? 1 : 0, 1);
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glViewport(0, 0, x, y);
//
		frames = frames > 50 ? 0 : ++frames;
		System.out.println("frames: " + frames);
//		glDisable(GL_TEXTURE_2D);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

//		if (frames > 500) return;

		glColor3b((byte) 0, (byte)122, (byte) (frames > 500 ? 0 : 255));
		glPushMatrix();

		glTranslatef(((float) frames) / 50f, 0, 0);
		float gsize = (float) frames / 50f;
		glBegin(GL_QUADS);
//		System.out.println("gsize: " + gsize);
//		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		glVertex2f(0, 0);
		glVertex2f(gsize, 0);
		glVertex2f(gsize, gsize);
		glVertex2f(0,gsize);
		glEnd();
		glPopMatrix();
//		glEnable(GL_TEXTURE_2D);
//		Main.getCalendarRenderer().draw(x, y);
	}

	public void close() {
		shouldClose = true;
	}

	public long getFPS() {
		if (!enableFPS)
			throw new IllegalStateException("FPS is not enabled!");
		return 0;//Math.round(FPS.getAverage());
	}

}

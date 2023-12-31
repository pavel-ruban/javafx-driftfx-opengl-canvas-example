package javafx.driftfx.opengl.canvas.example.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import javafx.driftfx.opengl.canvas.example.utils.GLFunctions;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class Framebuffer {

	public final int width;
	public final int height;
	public final boolean isMultisampled;

	private int bufferID = -1;
	private int textureID = -1;
	private int depthBuffer = -1;

	private float clearRed;
	private float clearGreen;
	private float clearBlue;

	private int[] pboIds = null;

	public Framebuffer(int w, int h) {
		this(w, h, false);
	}

	public Framebuffer(int w, int h, boolean msaa) {
		width = w;
		height = h;
		isMultisampled = msaa;
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		this.createFramebuffer();
		this.checkFramebufferComplete();
		GLFunctions.bindFramebuffer(0);
	}

	public Framebuffer setClear(float red, float green, float blue) {
		clearRed = red;
		clearGreen = green;
		clearBlue = blue;
		return this;
	}

	public void deleteFramebuffer() {
		this.unbindTexture();
		this.unbind();

		if (depthBuffer > -1) {
			GL30.glDeleteRenderbuffers(depthBuffer);
			depthBuffer = -1;
		}

		if (textureID > -1) {
			GL11.glDeleteTextures(textureID);
			textureID = -1;
		}

		if (bufferID > -1) {
			GLFunctions.bindFramebuffer(0);
			GL30.glDeleteFramebuffers(bufferID);
			bufferID = -1;
		}
	}

	private void createFramebuffer() {
		bufferID = GL30.glGenFramebuffers();
		textureID = GL11.glGenTextures();
		depthBuffer = GL30.glGenRenderbuffers();
		GLFunctions.printGLErrors("Framebuffer alloc");

		if (isMultisampled) {
			this.setFilterType(GL11.GL_LINEAR);

			GLFunctions.bindFramebuffer(bufferID);
			// create a multisampled color attachment texture
			GL32.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, textureID);
			GLFunctions.printGLErrors("MSAA Framebuffer tex bind");
			GL32.glTexImage2DMultisample(GL32.GL_TEXTURE_2D_MULTISAMPLE, 4, GL32.GL_RGBA, width, height, true);
			GLFunctions.printGLErrors("MSAA Framebuffer tex alloc");
			GL32.glBindTexture(GL32.GL_TEXTURE_2D_MULTISAMPLE, 0);
			GLFunctions.printGLErrors("MSAA Framebuffer tex unbind");
			GL32.glFramebufferTexture2D(GL32.GL_FRAMEBUFFER, GL32.GL_COLOR_ATTACHMENT0, GL32.GL_TEXTURE_2D_MULTISAMPLE, textureID, 0);
			GLFunctions.printGLErrors("MSAA Framebuffer tex setup");

			// create a (also multisampled) renderbuffer object for depth and stencil attachments
			GLFunctions.bindRenderbuffer(depthBuffer);
			GL32.glRenderbufferStorageMultisample(GL32.GL_RENDERBUFFER, 4, GL32.GL_DEPTH24_STENCIL8, width, height);
			GL32.glBindRenderbuffer(GL32.GL_RENDERBUFFER, 0);
			GL32.glFramebufferRenderbuffer(GL32.GL_FRAMEBUFFER, GL32.GL_DEPTH_STENCIL_ATTACHMENT, GL32.GL_RENDERBUFFER, depthBuffer);
			GLFunctions.printGLErrors("MSAA Framebuffer depth binding");
		}
		else {
			this.setFilterType(GL11.GL_NEAREST);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
			GLFunctions.bindFramebuffer(bufferID);
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textureID, 0);
			GLFunctions.printGLErrors("Framebuffer tex setup");

			GLFunctions.bindRenderbuffer(depthBuffer);
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT24, width, height);
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
			GLFunctions.printGLErrors("Framebuffer depth binding");
		}

		this.clear();
		this.unbindTexture();
		GLFunctions.printGLErrors("Framebuffer completion");
	}

	public void setFilterType(int type) {
		GL11.glBindTexture(isMultisampled ? GL32.GL_TEXTURE_2D_MULTISAMPLE : GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, type);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, type);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glBindTexture(isMultisampled ? GL32.GL_TEXTURE_2D_MULTISAMPLE : GL11.GL_TEXTURE_2D, 0);
		GLFunctions.printGLErrors("Framebuffer filtering");
	}

	public void checkFramebufferComplete() {
		int code = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);

		switch(code) {
			case GL32.GL_FRAMEBUFFER_COMPLETE:
				return;
			case GL32.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
			case GL32.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
			case GL32.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
			case GL32.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
			default:
				throw new RuntimeException("glCheckFramebufferStatus returned unknown status value:" + code);
		}
	}

	private void bindTexture() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
	}

	private void unbindTexture() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void bind(boolean redoView) {
		GLFunctions.bindFramebuffer(bufferID);

		if (redoView)
			GL11.glViewport(0, 0, width, height);
	}

	public void unbind() {
		GLFunctions.bindFramebuffer(0);
	}

	public void draw() {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glColorMask(true, true, true, false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, width, height);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		this.bindTexture();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2d(-1, -1);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2d(-1, 1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2d(1, 1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2d(1, -1);
		GL11.glEnd();
		this.unbindTexture();
		GL11.glPopAttrib();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
	}

	public void clear() {
		this.bind(true);
		GL11.glClearColor(clearRed, clearGreen, clearBlue, 1);
		GL11.glClearDepth(1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		this.unbind();
	}

	public void sendTo(Framebuffer other) {
		if (other.width != width || other.height != height)
			throw new IllegalArgumentException("You cannot plainly send onto a different sized buffer! You need to provide origin X and Y!");
		this.sendTo(other.bufferID);
	}

	public void sendTo(Framebuffer other, int dx, int dy) {
		this.sendTo(other.bufferID, dx, dy);
	}

	public void sendTo(int otherBuffer) {
		this.sendTo(otherBuffer, 0, 0);
	}

	public void sendTo(int otherBuffer, int dx, int dy) {
		GL32.glBindFramebuffer(GL32.GL_DRAW_FRAMEBUFFER, otherBuffer);
		GL32.glBindFramebuffer(GL32.GL_READ_FRAMEBUFFER, bufferID);
		GL32.glBlitFramebuffer(0, 0, width, height, dx, dy, dx+width, dy+height, GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, 0);
	}

	public void sendToColorBit(int otherBuffer) {
		int dx = 0;
		int dy = 0;
		GL32.glBindFramebuffer(GL32.GL_DRAW_FRAMEBUFFER, otherBuffer);
		GL32.glBindFramebuffer(GL32.GL_READ_FRAMEBUFFER, bufferID);
		GL32.glBlitFramebuffer(0, 0, width, height, dx, dy, dx+width, dy+height, GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, 0);
	}

	public void loadImage(BufferedImage img) {
//		if (img.getWidth() != width || img.getHeight() != height)
//			throw new IllegalArgumentException("Tried to load a wrongly-sized image onto a framebuffer");
//		TextureLoader.instance.loadImageOntoTexture(img, textureID, true, isMultisampled);
	}
	/*
	public void loadFXImage(Image img) {
		if (isMultisampled) {
			this.bind(false);
			GLFunctions.printGLErrors("Framebuffer bind");
			GLFunctions.renderJFXImage(img, width, height);
			GLFunctions.printGLErrors("Image Draw");
			this.unbind();
		}
		else {
			?
		}
	}
	 */
	public BufferedImage toImage() {
		return this.toImage(width, height, 0, 0);
	}

	/** For rendering into an image that is of a larger size, autocentered on the canvas. */
	public BufferedImage toImage(int imageWidth, int imageHeight) {
		int ow = (imageWidth-width)/2;
		int oh = (imageHeight-height)/2;
		return this.toImage(imageWidth, imageHeight, ow, oh);
	}

	/** For rendering into an image that is of a larger size. */
	public BufferedImage toImage(int imageWidth, int imageHeight, int offsetX, int offsetY) {
		BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		this.writeIntoImageSlow(img, offsetX, offsetY);
		return img;
	}

	public void writeIntoImageSlow(BufferedImage img, int x, int y) {
		GLFunctions.writeTextureToImage(img, x, y, width, height, textureID);
	}

	public void writeIntoImage(BufferedImage img, int x, int y, boolean flipBuffers) {
		GL11.glFlush();
		/*
		this.bind(false);
		GL30.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
		GLFunctions.printGLErrors("Print to image - bind");
		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL30.glReadPixels(0, 0, width, height, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
		GLFunctions.printGLErrors("Print to image - read");

		for(int i = 0; i < width; i++) {
			for(int k = 0; k < height; k++) {
				int dx = x+i;
				int dy = y+k;
				int idx = (x + (width * y)) * bpp;
				int r = buffer.get(idx) & 0xFF;
				int g = buffer.get(idx + 1) & 0xFF;
				int b = buffer.get(idx + 2) & 0xFF;
				img.setRGB(dx, height - (dy + 1), (r << 16) | (g << 8) | b);
			}
		}

		GL30.glReadBuffer(0);
		this.unbind();
		GLFunctions.printGLErrors("Print to image - unbind");
		 */

		this.writeIntoImageSlow(img, x, y);

		/*
		if (pboIds == null)
			this.loadPBOs();

		GL45.glNamedFramebufferReadBuffer(bufferID, GL30.GL_COLOR_ATTACHMENT0);
		GLFunctions.printGLErrors("Write into image - buffer setup 1a");
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, pboIds[flipBuffers ? 1 : 0]);
		GLFunctions.printGLErrors("Write into image - buffer setup 1b");
		//GL42.glGetFramebufferParamater(bufferID, GL42.GL_IMPLEMENTATION_COLOR_READ_FORMAT);
		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL30.glReadPixels(0, 0, width, height, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, 0);
		GLFunctions.printGLErrors("Write into image - pixel read");
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, pboIds[flipBuffers ? 0 : 1]);
		GLFunctions.printGLErrors("Write into image - buffer setup 2");

		ByteBuffer data = GL30.glMapBuffer(GL30.GL_PIXEL_PACK_BUFFER, GL30.GL_READ_ONLY);
		/*
		byte[] arr0 = new byte[32];
		data.get(arr0);
		data.rewind();
		 *//*
		GLFunctions.printGLErrors("Write into image - buffer map");
		int[] arr = new int[data.limit()/4];
		data.order(ByteOrder.BIG_ENDIAN).asIntBuffer().get(arr);
		//int clear = Colors.RGBtoHex((int)(255*clearRed), (int)(255*clearGreen), (int)(255*clearBlue));
		for (int i = 0; i < width; i++) {
			for (int k = 0; k < height; k++) {
				int idx = k*width+i;
				int src = arr[idx];
				//int color = Colors.mixColors(src | 0xff000000, clear, Colors.getAlpha(src));
				img.setRGB(i+x, k+y, src);
			}
		}

		GL30.glUnmapBuffer(GL30.GL_PIXEL_PACK_BUFFER);
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, 0);
		GLFunctions.printGLErrors("Write into image - end");
		  */
	}

	private void loadPBOs() {
		pboIds = new int[2];
		for (int i = 0; i < pboIds.length; i++)
			pboIds[i] = GL30.glGenBuffers();

		int length = width*height*4;
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, pboIds[0]);
		GL30.glBufferData(GL30.GL_PIXEL_PACK_BUFFER, length, GL30.GL_STREAM_READ);
		GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, pboIds[1]);
		GL30.glBufferData(GL30.GL_PIXEL_PACK_BUFFER, length, GL30.GL_STREAM_READ);

		//GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, 0);

		GLFunctions.printGLErrors("Framebuffer PBO load");
	}

	public String saveAsFile(File f) {
		try {
			f.getParentFile().mkdirs();
			ImageIO.write(this.toImage(), "png", f);
			return f.getAbsolutePath();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

package com.github.hiroshi_cl.jag.sg;

import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class Image {
	public final int width, height;
	public final byte[] image;

	public Image(byte[] bs) throws NotAImageException {
		image = bs;
		try {
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bs));
			width = bi.getWidth();
			height = bi.getHeight();
		} catch (Exception e) {
			throw new NotAImageException();
		}
	}

	public static class NotAImageException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7346196511060446897L;

	}
}

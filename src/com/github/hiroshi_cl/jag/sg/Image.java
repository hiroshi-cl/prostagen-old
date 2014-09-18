package com.github.hiroshi_cl.jag.sg;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class Image {
	public final int width, height;
	public final byte[] image;

	public Image(byte[] bs) throws NotAImageException {
		image = bs;
		try {
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bs));
			width = bi == null ? 1 : bi.getWidth();
			height = bi == null ? 1 : bi.getHeight();
		} catch (Exception e) {
            e.printStackTrace();
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

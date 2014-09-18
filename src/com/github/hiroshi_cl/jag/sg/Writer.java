package com.github.hiroshi_cl.jag.sg;

import java.io.*;
import java.util.*;

public class Writer {
	public static boolean writeString(File path, String text) {
		if (path.getParentFile() != null && !path.getParentFile().exists())
			path.getParentFile().mkdirs();
		try {
			final PrintWriter pw = new PrintWriter(path, "utf-8");
			pw.append(text);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean writeImages(File path, Map<String, Image> map) {
		for (Map.Entry<String, Image> et : map.entrySet())
			try {
				if (!path.exists())
					path.mkdirs();
				final FileOutputStream fos = new FileOutputStream(new File(path, et.getKey()));
				fos.write(et.getValue().image);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				System.err.println(et.getKey());
				e.printStackTrace();
				return false;
			}
		return true;
	}
}

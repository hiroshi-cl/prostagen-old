package com.github.hiroshi_cl.jag.sg;

import com.github.hiroshi_cl.jag.sg.Image.NotAImageException;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
	private final String wikiURL, encode;
	private static final String tagWikiSourceBegin = "<div id=\"body\"><pre id=\"source\">";
	private static final String tagWikiSourceEnd = "</pre></div>";

	public Downloader(String wikiURL, String username, String password, String encode)
			throws UnsupportedEncodingException {
		this.wikiURL = wikiURL;
		this.encode = encode;
		setAuthentication(username, password);
	}

	private void setAuthentication(final String username, final String password) {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password.toCharArray());
			};
		});
	}

	private static String htmlUnescape(final String s) {
		return s.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"").replaceAll("&amp;", "&");
	}

	public String getPage(final String p) {
		try {
			final String pageName = URLEncoder.encode(p, encode);
			final StringBuilder html = new StringBuilder();
			final BufferedReader br = new BufferedReader(new InputStreamReader(new URL(wikiURL + "?cmd=source&page="
					+ pageName).openStream(), encode));
			for (int c = br.read(); c >= 0; c = br.read())
				html.append((char) c);
			br.close();
			final int begin = html.indexOf(tagWikiSourceBegin) + tagWikiSourceBegin.length();
			final int end = html.indexOf(tagWikiSourceEnd);
			return htmlUnescape(html.substring(begin, end));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("Execute in an environment where " + encode + " is available!");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException("Check the URL in the source code!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Wait a minute and try again!");
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new RuntimeException("No such page!");
		}
	}

	public Map<String, Image> getImages(final String p, final String source) {
		final Map<String, Image> map = new HashMap<String, Image>();
		final Matcher m = Pattern.compile("ref\\((?:&quot;)?([^,]+?)(?:,.+)?(?:&quot;)?\\)").matcher(source);
		while (m.find()) {
			final String imgName = m.group(1);
			if (!map.containsKey(imgName))
				try {
					final String pageName = URLEncoder.encode(p, encode);
					final BufferedInputStream bis = new BufferedInputStream(new URL(
							imgName.startsWith("http://") ? imgName : wikiURL + "?plugin=attach&refer=" + pageName
									+ "&openfile=" + imgName).openStream());
					final ByteArrayOutputStream bao = new ByteArrayOutputStream();
					for (int b = bis.read(); b >= 0; b = bis.read())
						bao.write((byte) b);
					bao.flush();

					map.put(imgName, new Image(bao.toByteArray()));
					bis.close();
					bao.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NotAImageException e) {
                    System.err.println(imgName);
					e.printStackTrace();
				}
		}
		return map;
	}
}

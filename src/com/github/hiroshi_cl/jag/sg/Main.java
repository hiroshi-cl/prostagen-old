package com.github.hiroshi_cl.jag.sg;

public class Main {
	private static final String[] names = { "問題/hoge" };

	public static void main(String... args) throws Exception {
		final Downloader d = new Downloader("http://example.com/alumni/", "user", "pass", "euc-jp");
		for (int i = 0; i < 10; i++)
			try {
				System.err.println(i + ": " + names[i]);
				final char c = (char) ('A' + i);
				TeXComposer.compose(d, names[i], "" + c);
				HTMLComposer.compose(d, names[i], "" + (2447 + i));
			} catch (Exception e) {
				e.printStackTrace();
				throw null;
			}
	}

}
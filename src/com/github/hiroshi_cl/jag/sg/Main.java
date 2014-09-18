package com.github.hiroshi_cl.jag.sg;

public class Main {
    private static final String[] names = {
            "問題/hoge"
    };

    public static void main(String... args) throws Exception {
        final byte[] bs = new byte[1024];
        Runtime.getRuntime().exec("pwd").getInputStream().read(bs);
        System.err.println(new String(bs));
        final Downloader d = new Downloader("http://example.com/pukiwiki/",
                "user", "pass", "euc-jp");
        for (int i = 0; i < names.length; i++)
            try {
                System.err.println(i + ": " + names[i]);
                final char c = (char) ('A' + i);
                TeXComposer.compose(d, names[i], "" + c);
                HTMLComposer.compose(d, names[i], "" + c);
            } catch (Exception e) {
                e.printStackTrace();
                //throw null;
            }
    }

}
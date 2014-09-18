package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen;

import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.composers.AtCoderComposer;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.composers.DomesticComposer;
import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Downloader;

public class Main {
    private static final String[] names = {
            "問題/***"
    };

    public static void main(String... args) throws Exception {
//        {
//            final byte[] bs = new byte[1024];
//            Runtime.getRuntime().exec("pwd").getInputStream().read(bs);
//            System.err.println(new String(bs));
//        }
        final Downloader d = new Downloader("http://example.com/pikiwiki/",
                "user", "pass", "utf-8");
        for (int i = 0; i < names.length; i++)
            try {
                System.err.println(i + ": " + names[i]);
                final char c = (char) ('A' + i);
//                TeXComposer.compose(d, names[i], "" + c);
                AtCoderComposer.compose(d, names[i], "" + c);
                DomesticComposer.compose(d, names[i], "" + c);
            } catch (Exception e) {
                e.printStackTrace();
                //throw null;
            }
    }

}
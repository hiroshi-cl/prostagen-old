package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io;

import net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io.Image.NotAImageException;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
    private static final String tagWikiSourceBegin = "<div id=\"body\"><pre id=\"source\">";
    private static final String tagWikiSourceEnd = "</pre></div>";
    private static final Pattern p1 = Pattern.compile("ref\\((?:&quot;)?([^,]+?)(?:,.+)?(?:&quot;)?\\)");
    private final String wikiURL, encode;

    public Downloader(String wikiURL, String username, String password, String encode)
            throws UnsupportedEncodingException {
        this.wikiURL = wikiURL;
        this.encode = encode;
        setAuthentication(username, password);
    }

    private static String htmlUnescape(final String s) {
        return s
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&");
    }

    private void setAuthentication(final String username, final String password) {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
    }

    public String getPage(final String p) {
        try {
            final String pageName = URLEncoder.encode(p, encode);
            final StringBuilder html = new StringBuilder();
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(new URL(
                    wikiURL + "?cmd=source&page=" + pageName).openStream(), encode))) {
                for (int c = br.read(); c >= 0; c = br.read())
                    html.append((char) c);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Check the URL in the source code!", e);
            } catch (IOException e) {
                throw new RuntimeException("Wait a minute and try again!", e);
            }
            final int begin = html.indexOf(tagWikiSourceBegin) + tagWikiSourceBegin.length();
            final int end = html.indexOf(tagWikiSourceEnd);
            return htmlUnescape(html.substring(begin, end));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Execute in an environment where " + encode + " is available!", e);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("No such page!", e);
        }
    }

    public Map<String, Image> getImages(final String p, final String source) {
        final Map<String, Image> map = new HashMap<>();
        final Matcher m = p1.matcher(source);

        try {
            final String pageName = URLEncoder.encode(p, encode);
            while (m.find()) {
                final String imgName = m.group(1);
                if (!map.containsKey(imgName))
                    try (final BufferedInputStream bis = new BufferedInputStream(new URL(
                            imgName.startsWith("http://")
                                    ? imgName
                                    : wikiURL + "?plugin=attach&refer=" + pageName + "&openfile=" + imgName
                    ).openStream());
                         final ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
                        for (int b = bis.read(); b >= 0; b = bis.read())
                            bao.write((byte) b);
                        bao.flush();

                        map.put(imgName, new Image(bao.toByteArray()));
                    } catch (IOException e) {
                        throw new RuntimeException("Wait a minute and try again!", e);
                    } catch (NotAImageException e) {
                        System.err.println(imgName + " is not a image file.");
                    }
            }
            return map;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Execute in an environment where " + encode + " is available!", e);
        }
    }
}
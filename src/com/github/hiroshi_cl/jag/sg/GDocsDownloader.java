package com.github.hiroshi_cl.jag.sg;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipInputStream;

public class GDocsDownloader {
    private static final String encode = "utf-8";

//    public static void main(String... args) {
//        System.out.println(
//                new GDocsDownloader().getPage("1sEstKtKHtjODF-ZJfajIlwuVSJfhlaQuIrOESVxPy6A").substring(0, 100));
//    }

    public String getPage(final String id) {
        final String url = "https://docs.google.com/document/d/" + id + "/export?format=zip&id=" + id;
        try (final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new URL(url).openStream()))) {
            if (zis.getNextEntry() != null) {
                try (final BufferedReader br = new BufferedReader(new InputStreamReader(zis, encode))) {
                    return br.readLine();
                }
            } else {
                throw new RuntimeException("invalid url");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Execute in an environment where " + encode + " is available!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Check the URL in the source code!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Wait a minute and try again!");
        }
    }
}

package net.aitea.acm_icpc.hiroshi_yamaguchi.prostagen.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class Writer {
    public static boolean writeString(File path, String text) {
        if (path.getParentFile() != null && !path.getParentFile().exists())
            path.getParentFile().mkdirs();
        try (final PrintWriter pw = new PrintWriter(path, "utf-8")) {
            pw.append(text);
            pw.flush();
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
                try (final FileOutputStream fos = new FileOutputStream(
                        new File(path, new File(et.getKey()).getName()))) {
                    fos.write(et.getValue().image);
                    fos.flush();
                }
            } catch (IOException e) {
                System.err.println(et.getKey() + " does not exist.");
                e.printStackTrace();
                return false;
            }
        return true;
    }
}

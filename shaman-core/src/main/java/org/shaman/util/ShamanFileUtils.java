package org.shaman.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShamanFileUtils {

    /**
     * writeFile writeFile
     *
     * @param contents
     * @param fileName
     */

    public static void writeFile(String contents, String fileName) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(fileName), true));
            bw.write(contents);
        } catch (IOException e) {
            throw e;
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

}

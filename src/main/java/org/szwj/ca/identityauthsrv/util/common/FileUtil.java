package org.szwj.ca.identityauthsrv.util.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public FileUtil() {

    }

    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        FileOutputStream out = null;

        try {
            out = openOutputStream(file, false);
            out.write(data);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Directory '" + parent + "' could not be created");
            }
        }

        return new FileOutputStream(file, append);
    }
}

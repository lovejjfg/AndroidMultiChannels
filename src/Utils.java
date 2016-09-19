//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utils {
    public Utils() {
    }

    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0L, in.size(), out);
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException var14) {
                var14.printStackTrace();
            }

        }

    }

    public static File ExtBatchRename(File file, String extname) {
        String filename = file.getAbsolutePath();
        if(filename.indexOf(".") >= 0) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }

        File newFile = new File(filename + "." + extname);
        file.renameTo(newFile);
        file.delete();
        return newFile;
    }

    public static void addFileToExistingZip(File zipFile, File file) throws IOException {
        File tempFile = File.createTempFile(zipFile.getName(), (String)null);
        tempFile.delete();
        boolean renameOk = zipFile.renameTo(tempFile);
        if(!renameOk) {
            throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
        } else {
            byte[] buf = new byte[1024];
            ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
            

            for(ZipEntry entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry()) {
                String in = entry.getName();
                if(in.contains(BuildParams.CHANNEL)) {
                    continue;
                }

                out.putNextEntry(new ZipEntry(in));

                int len1;
                while((len1 = zin.read(buf)) > 0) {
                    out.write(buf, 0, len1);
                }
            }

            zin.close();
            FileInputStream in1 = new FileInputStream(file);
            out.putNextEntry(new ZipEntry("META-INF/" + file.getName()));

            int len2;
            while((len2 = in1.read(buf)) > 0) {
                out.write(buf, 0, len2);
            }

            out.closeEntry();
            in1.close();
            out.close();
            tempFile.delete();
        }
    }

    public static void addFilesToExistingZip(File zipFile, File[] files) throws IOException {
        File tempFile = File.createTempFile(zipFile.getName(), (String)null);
        tempFile.delete();
        boolean renameOk = zipFile.renameTo(tempFile);
        if(!renameOk) {
            throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
        } else {
            byte[] buf = new byte[1024];
            ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

            int var16;
            for(ZipEntry entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry()) {
                String i = entry.getName();
                boolean in = true;
                File[] var13 = files;
                int var12 = files.length;

                for(int var11 = 0; var11 < var12; ++var11) {
                    File len = var13[var11];
                    if(len.getName().equals(i)) {
                        in = false;
                        break;
                    }
                }

                if(in) {
                    out.putNextEntry(new ZipEntry(i));

                    while((var16 = zin.read(buf)) > 0) {
                        out.write(buf, 0, var16);
                    }
                }
            }

            zin.close();

            for(int var14 = 0; var14 < files.length; ++var14) {
                FileInputStream var15 = new FileInputStream(files[var14]);
                out.putNextEntry(new ZipEntry(files[var14].getName()));

                while((var16 = var15.read(buf)) > 0) {
                    out.write(buf, 0, var16);
                }

                out.closeEntry();
                var15.close();
            }

            out.close();
            tempFile.delete();
        }
    }
}

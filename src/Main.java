import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
	
 public static void main(String[] args){
	// C:\Users\tt\Desktop\app-hd-debug_360。apk
	 
	 BuildParams buildParams = new BuildParams();
	String[] names =  buildParams.chanelNames;
	String path = buildParams.apkPath;
	if(null == path){
		System.out.println("路径未获取！！");
		return;
		}
	for(String name:names){
		if(null!=name)
		 addUmengChannel(path, name);
	}
	 
	
 }
 
 public static void addUmengChannel(String filepath, String channel) {
     String channel_title = BuildParams.CHANNEL;
     if(filepath.substring(filepath.lastIndexOf(".") + 1).toLowerCase().equals("apk")) {
         String path2 = "";
         if(filepath.lastIndexOf(File.separator) >= 0) {
             path2 = filepath.substring(0, filepath.lastIndexOf(File.separator) + 1);//得到父路径
         }

         if(path2.length() != 0) {
             File s = new File(filepath);//原始的apk
             File t = new File(filepath.substring(0, filepath.lastIndexOf(".")) + "_" + channel + ".apk");//目标apk
             if(!t.exists()) {//不存在就创建
                 try {
                     t.createNewFile();
                 } catch (IOException var12) {
                     var12.printStackTrace();
                 }
             }

             Utils.fileChannelCopy(s, t);//拷贝原始apk到目标apk
             File addFile = new File(path2 + channel_title + channel);//需要添加的渠道文件 
             if(!addFile.exists()) {
                 try {
                     addFile.createNewFile();
                 } catch (IOException var11) {
                     var11.printStackTrace();
                 }
             }

             try {
                 Utils.addFileToExistingZip(t, addFile);//将新加的渠道文件添加到目标apk文件中
                 addFile.delete();
             } catch (IOException var10) {
                 var10.printStackTrace();
             }

         }
     }else{
    	 System.out.println("请输入完整的路径！！！");
     }
 } 
 
 public static void addFileToExistingZip(File zipFile, File file) throws IOException {
     File tempFile = File.createTempFile(zipFile.getName(), (String)null);//创建一个临时的文件
     tempFile.delete();
     boolean renameOk = zipFile.renameTo(tempFile);//移动目标文件到临时文件
     if(!renameOk) {
         throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
     } else {
         byte[] buf = new byte[1024];
         ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

         for(ZipEntry entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry()) {
             String in = entry.getName();
             boolean len = true;
             if(file.getName().equals(in)) {//是渠道文件就别复制了
                 len = false;
             }

             if(len) {
                 out.putNextEntry(new ZipEntry(in));

                 int len1;
                 while((len1 = zin.read(buf)) > 0) {
                     out.write(buf, 0, len1);
                 }
             }
         }

         zin.close();
         FileInputStream in1 = new FileInputStream(file);
         out.putNextEntry(new ZipEntry("META-INF/" + file.getName()));//创建对应的渠道文件

         int len2;
         while((len2 = in1.read(buf)) > 0) {//这个文件在上面已经删除了，估计没有
             out.write(buf, 0, len2);
         }

         out.closeEntry();
         in1.close();
         out.close();
         tempFile.delete();
     }
 }

}

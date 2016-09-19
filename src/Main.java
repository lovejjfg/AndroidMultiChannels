import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
	
 public static void main(String[] args){
	// C:\Users\tt\Desktop\app-hd-debug_360��apk
	 
	 BuildParams buildParams = new BuildParams();
	String[] names =  buildParams.chanelNames;
	String path = buildParams.apkPath;
	if(null == path){
		System.out.println("·��δ��ȡ����");
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
             path2 = filepath.substring(0, filepath.lastIndexOf(File.separator) + 1);//�õ���·��
         }

         if(path2.length() != 0) {
             File s = new File(filepath);//ԭʼ��apk
             File t = new File(filepath.substring(0, filepath.lastIndexOf(".")) + "_" + channel + ".apk");//Ŀ��apk
             if(!t.exists()) {//�����ھʹ���
                 try {
                     t.createNewFile();
                 } catch (IOException var12) {
                     var12.printStackTrace();
                 }
             }

             Utils.fileChannelCopy(s, t);//����ԭʼapk��Ŀ��apk
             File addFile = new File(path2 + channel_title + channel);//��Ҫ��ӵ������ļ� 
             if(!addFile.exists()) {
                 try {
                     addFile.createNewFile();
                 } catch (IOException var11) {
                     var11.printStackTrace();
                 }
             }

             try {
                 Utils.addFileToExistingZip(t, addFile);//���¼ӵ������ļ���ӵ�Ŀ��apk�ļ���
                 addFile.delete();
             } catch (IOException var10) {
                 var10.printStackTrace();
             }

         }
     }else{
    	 System.out.println("������������·��������");
     }
 } 
 
 public static void addFileToExistingZip(File zipFile, File file) throws IOException {
     File tempFile = File.createTempFile(zipFile.getName(), (String)null);//����һ����ʱ���ļ�
     tempFile.delete();
     boolean renameOk = zipFile.renameTo(tempFile);//�ƶ�Ŀ���ļ�����ʱ�ļ�
     if(!renameOk) {
         throw new RuntimeException("could not rename the file " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
     } else {
         byte[] buf = new byte[1024];
         ZipInputStream zin = new ZipInputStream(new FileInputStream(tempFile));
         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

         for(ZipEntry entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry()) {
             String in = entry.getName();
             boolean len = true;
             if(file.getName().equals(in)) {//�������ļ��ͱ�����
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
         out.putNextEntry(new ZipEntry("META-INF/" + file.getName()));//������Ӧ�������ļ�

         int len2;
         while((len2 = in1.read(buf)) > 0) {//����ļ��������Ѿ�ɾ���ˣ�����û��
             out.write(buf, 0, len2);
         }

         out.closeEntry();
         in1.close();
         out.close();
         tempFile.delete();
     }
 }

}

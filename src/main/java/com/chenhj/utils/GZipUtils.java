package com.chenhj.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author tanml
 * 将一串数据按照gzip方式压缩和解压缩  
 */
public class GZipUtils {
	public static final int BUFFER = 1024;
	public static final String EXT = ".gz";
	// 压缩
	public static byte[] compress(byte[] data) throws IOException {
		if (data == null || data.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(data);
		gzip.close();
		return  out.toByteArray();//out.toString("ISO-8859-1");
	}
	
	public static byte[] compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return null;
		}
		return compress(str.getBytes("utf-8"));
	}
	// 解压缩
	public static byte[] uncompress(byte[] data) throws IOException {
		if (data == null || data.length == 0) {
			return data;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		gunzip.close();
		in.close();
		return out.toByteArray();
	}
	
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		byte[] data = uncompress(str.getBytes("utf-8")); // ISO-8859-1
		return new String(data);
	}
	/**
	 * @throws IOException 
     * @Title: unZip 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param unZipfile
     * @param @param destFile 指定读取文件，需要从压缩文件中读取文件内容的文件名
     * @param @return 设定文件 
	 * @return String 返回类型 
     * @throws
 */
	public static String unZip(String unZipfile, String destFile) throws IOException {// unZipfileName需要解压的zip文件名
		InputStream inputStream;
		String inData = null;
		try {
			// 生成一个zip的文件
			File f = new File(unZipfile);
			ZipFile zipFile = new ZipFile(f);
	
			// 遍历zipFile中所有的实体，并把他们解压出来
			ZipEntry entry = zipFile.getEntry(destFile);
			if (!entry.isDirectory()) {
				// 获取出该压缩实体的输入流
				inputStream = zipFile.getInputStream(entry);
	
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] bys = new byte[4096];
				for (int p = -1; (p = inputStream.read(bys)) != -1;) {
					out.write(bys, 0, p);
				}
				inData = out.toString();
				out.close();
				inputStream.close();
			}
			zipFile.close();
		} catch (IOException ioe) {
			throw ioe;
		}
		return inData;
	}
	/**
	 * 文件解压缩
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void decompress(File file) throws Exception {
		decompress(file, true);
	}

	/**
	 * 文件解压缩
	 * 
	 * @param file
	 * @param delete
	 *            是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(File file, boolean delete) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT, ""));
		decompress(fis, fos);
		fis.close();
		fos.flush();
		fos.close();

		if (delete) {
			file.delete();
		}
	}

	/**
	 * 数据解压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os) {
		GZIPInputStream gis = null;
		try {
			gis = new GZIPInputStream(is);

			int count;
			byte data[] = new byte[BUFFER];
			while ((count = gis.read(data, 0, BUFFER)) != -1) {
				os.write(data, 0, count);
			}

			gis.close();
		} catch (Exception e) {
		} finally {

			if (null != gis) {
				try {
					gis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 文件解压缩
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void decompress(String path) throws Exception {
		decompress(path, true);
	}

	/**
	 * 文件解压缩
	 * 
	 * @param path
	 * @param delete
	 *            是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(String path, boolean delete) throws Exception {
		File file = new File(path);
		decompress(file, delete);
	}
//	public static void main(String[] args){
//		String json = "{\"androidSdk\":22,\"androidVer\":\"5.1\",\"cpTime\":1612071603,\"cupABIs\":[\"armeabi-v7a\",\"armeabi\"],\"customId\":\"QT99999\",\"elfFlag\":false,\"id\":\"4a1b644858d83a98\",\"imsi\":\"460015984967892\",\"system\":true,\"systemUser\":true,\"test\":true,\"model\":\"Micromax R610\",\"netType\":0,\"oldVersion\":\"0\",\"pkg\":\"com.adups.fota.sysoper\",\"poll_time\":30,\"time\":1481634113876,\"timeZone\":\"Asia\\/Shanghai\",\"versions\":[{\"type\":\"gatherApks\",\"version\":1},{\"type\":\"kernel\",\"version\":9},{\"type\":\"shell\",\"version\":10},{\"type\":\"silent\",\"version\":4},{\"type\":\"jarUpdate\",\"version\":1},{\"type\":\"serverIps\",\"version\":1}]}";
//		json="ksjdflkjsdflskjdflsdfkjsdf";
//		try {
//			byte[] buf = GZipUtils.compress(json);
//			
//			File fin = new File("D:/temp/test4.txt");
//			FileChannel fcout = new RandomAccessFile(fin, "rws").getChannel();
//			ByteBuffer wBuffer = ByteBuffer.allocateDirect(buf.length);
//			fcout.write(wBuffer.wrap(buf), fcout.size());
//			if (fcout != null) {
//				fcout.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	/**
	 * 数据解压缩
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] data) {

		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = null;

		try {
			bais = new ByteArrayInputStream(data);
			baos = new ByteArrayOutputStream();

			// 解压缩

			decompress(bais, baos);

			data = baos.toByteArray();

			baos.flush();

		} catch (Exception e) {
		} finally {
			try {
				baos.close();

				bais.close();
			} catch (Exception e) {
			}
		}
		return data;
	}

}

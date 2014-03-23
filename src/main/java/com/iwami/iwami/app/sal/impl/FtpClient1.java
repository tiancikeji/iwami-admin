package com.iwami.iwami.app.sal.impl;

import java.io.BufferedInputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.util.Arrays;
import java.util.Properties;  
  


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;  
import org.apache.commons.net.ftp.FTP;  
import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPClientConfig;  
import org.apache.commons.net.ftp.FTPFile;  
import org.apache.commons.net.ftp.FTPReply;  
  
public class FtpClient1 {  
    static String ftpUrl = "proxy.wsfdupload.lxdns.com"; // ftp 服务器路径  
    static int ftpPort = 21; // ftp 服务器端口  
    static String ftpUserName = "iwami"; // ftp 服务器用户名  
    static String ftpPassword = "123abc!@#"; // ftp 服务器密码  
    static String encoding = "UTF-8";  
    static String serverLanguageCode = FTP.DEFAULT_CONTROL_ENCODING;  
    static int timeout = 60000;  
    // 配置文件路径  
    static String configFile =  "ftp.properties";  
  
    static FTPClient ftpClient = null;  
  
    /** 
     * 创建FTPClient 
     *  
     * @throws Exception 
     */  
    private static void createFtpClient() throws Exception {  
        if (ftpClient == null) {  
            ftpClient = new FTPClient();  
            setConnectConfig();  
            connectFtp(ftpClient);  
        }  
    }  
  
    /** 
     * 关闭FTPClient 
     *  
     * @throws Exception 
     */  
    private static void closeFtpClient() throws Exception {  
        if (ftpClient != null) {  
            ftpClient.logout();  
            if (ftpClient.isConnected()) {  
                ftpClient.disconnect();  
            }  
            ftpClient = null;  
        }  
    }  
  
    /** 
     * ftp工具连接 
     *  
     * @param ftpClient 
     * @return 
     * @throws Exception 
     */  
    public static boolean connectFtp(FTPClient ftpClient) throws Exception {  
        // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
        ftpClient.connect(ftpUrl, ftpPort);  
          
        getFtpConfig();  
  
        // 返回登录情况  
        boolean isConnect = ftpClient.login(ftpUserName, ftpPassword);  
  
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
        // ftpClient.enterLocalPassiveMode();  
        ftpClient.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);  
        ftpClient.setControlEncoding(encoding);  
          
        // 设置连接时间  
        ftpClient.setDataTimeout(timeout);  
        // 返回代码  
        int reply = ftpClient.getReplyCode();  
        if (isConnect && FTPReply.isPositiveCompletion(reply)) {  
            return true;  
        }  
        return false;  
    }  
  
    /** 
     * 上传文件 
     *  
     * @param localPath 
     *            本地文件路径 
     * @param filename 
     *            本地文件名 
     * @param remotePath 
     *            服务器文件路径 
     * @return 成功返回true，否则返回false 
     * @throws Exception 
     */  
    public static boolean uploadFile(File file, String remotePath)  
            throws Exception {  
        boolean success = false;  
  
        try {  
            // 创建FTPClient  
            createFtpClient();  
  
            // 判断是文件，还是文件目录  
            if (file.isDirectory()) {  
                success = uploadAll(file, remotePath + File.separator  
                        + file.getName());  
            } else {  
                success = upload(file, remotePath);  
            }  
  
            // 关闭连接  
            closeFtpClient();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        // 返回  
        return success;  
    }  
  
    /** 
     * 上传文件 
     *  
     * @param file 
     * @param remotePath 
     * @return 
     * @throws Exception 
     */  
    private static boolean upload(File file, String remotePath)  
            throws Exception {  
        boolean success;  
        // 文件  
        FileInputStream inputStream = new FileInputStream(file);  
        // 切换ftp工作目录  
        changeWorkingDirectory(remotePath);  
  
        // 上传文件  
        success = ftpClient.storeFile(encodingGBKToISO8859(file.getName()),  
                inputStream);  
  
        inputStream.close();  
        return success;  
    }  
  
    /** 
     * 上传文件 
     *  
     * @param localPath 
     *            本地文件路径 
     * @param filename 
     *            本地文件名 
     * @param remotePath 
     *            服务器文件路径 
     * @return 
     * @throws Exception 
     */  
    public static boolean uploadFile(String localPath, String filename,  
            String remotePath) throws Exception {  
        File file = new File(localPath + File.separator + filename);  
        return uploadFile(file, remotePath);  
    }  
  
    /** 
     * 上传文件目录 
     *  
     * @param file 
     * @param remotePath 
     * @return 
     * @throws Exception 
     */  
    public static boolean uploadDirectory(String localPath, String remotePath)  
            throws Exception {  
        return uploadDirectory(new File(localPath), remotePath);  
    }  
  
    /** 
     * 上传文件目录 
     *  
     * @param file 
     * @param remotePath 
     * @return 
     * @throws Exception 
     */  
    public static boolean uploadDirectory(File file, String remotePath)  
            throws Exception {  
        boolean success = false;  
        // 创建FTPClient  
        createFtpClient();  
  
        success = uploadAll(file, remotePath);  
  
        // 关闭连接  
        closeFtpClient();  
        return success;  
    }  
  
    /** 
     * 上传 
     *  
     * @param file 
     * @param remotePath 
     * @throws Exception 
     * @throws FileNotFoundException 
     * @throws IOException 
     */  
    private static boolean uploadAll(File file, String remotePath)  
            throws Exception {  
        boolean success = false;  
        // 得到文件列表  
        File[] fileList = file.listFiles();  
        for (File uploadFile : fileList) {  
            // 循环子目录  
            if (uploadFile.isDirectory()) {  
                ftpClient.changeWorkingDirectory(remotePath);  
                ftpClient.makeDirectory(uploadFile.getName());  
                success = uploadAll(uploadFile, remotePath + File.separator  
                        + uploadFile.getName());  
            } else {  
                // 上传文件  
                success = upload(uploadFile, remotePath);  
            }  
        }  
        return success;  
    }  
  
    /** 
     * 下载文件目录下所有文件 
     *  
     * @param localPath 
     *            本地文件路径 
     * @param fileName 
     *            本地文件名 
     * @param remotePath 
     *            服务器路径 
     * @return 成功返回true，否则返回false 
     * @throws Exception 
     */  
    public static boolean downloadDirectory(String localPath, String remotePath)  
            throws Exception {  
        boolean success = false;  
  
        try {  
            // 创建FTPClient  
            createFtpClient();  
  
            success = downloadAll(localPath, remotePath);  
  
            // 关闭连接FTPClient  
            closeFtpClient();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return success;  
    }  
  
    /** 
     * 下载文件目录下的所有文件 
     *  
     * @param localPath 
     * @param remotePath 
     * @param success 
     * @return 
     * @throws IOException 
     * @throws Exception 
     * @throws FileNotFoundException 
     */  
    private static boolean downloadAll(String localPath, String remotePath)  
            throws Exception {  
        boolean success = false;  
  
        // 切换ftp工作目录  
        changeWorkingDirectory(remotePath);  
        // 取出下载文件  
        FTPFile[] ftpFiles = ftpClient.listFiles();  
        for (FTPFile ftpFile : ftpFiles) {  
            // 过滤文件名不正确的文件  
            if (ftpFile.getName().equals(".") || ftpFile.getName().equals("..")) {  
                continue;  
            }  
            if (ftpFile.isDirectory()) {  
                // 创建目录  
                File fileDir = new File(localPath + File.separator  
                        + ftpFile.getName());  
                if (!fileDir.exists()) {  
                    fileDir.mkdirs();  
                }  
                // 下载文件  
                success = downloadAll(localPath + File.separator  
                        + ftpFile.getName(), remotePath + File.separator  
                        + ftpFile.getName());  
            } else {  
                // 切换ftp工作目录  
                changeWorkingDirectory(remotePath);  
                // 下载文件  
                success = download(localPath, ftpFile);  
            }  
        }  
        return success;  
    }  
  
    /** 
     * 切换ftp工作目录 
     *  
     * @param remotePath 
     * @throws IOException 
     */  
    private static void changeWorkingDirectory(String remotePath)  
            throws IOException {  
        // 转移到FTP服务器目录  
        ftpClient.changeWorkingDirectory("/");  
        if (StringUtils.isNotBlank(remotePath)) {  
            ftpClient.changeWorkingDirectory(remotePath);  
        }  
    }  
  
    /** 
     * @param localPath 
     * @param ftpFile 
     * @return 
     * @throws Exception 
     */  
    private static boolean download(String localPath, FTPFile ftpFile)  
            throws Exception {  
        boolean success;  
        // 文件目录不存在创建文件  
        File fileDir = new File(localPath);  
        if (!fileDir.exists()) {  
            fileDir.mkdirs();  
        }  
  
        File localFile = new File(localPath + File.separator  
                + ftpFile.getName());  
  
        OutputStream is = new FileOutputStream(localFile);  
        // 下载文件  
        success = ftpClient.retrieveFile(  
                encodingGBKToISO8859(ftpFile.getName()), is);  
        is.close();  
        return success;  
    }  
  
    /** 
     * 下载文件 
     *  
     * @param localPath 
     *            本地文件路径 
     * @param fileName 
     *            本地文件名 
     * @param remotePath 
     *            服务器路径 
     * @return 成功返回true，否则返回false 
     * @throws Exception 
     */  
    public static boolean downloadFile(String localPath, String fileName,  
            String remotePath) throws Exception {  
        boolean success = false;  
  
        try {  
            // 创建FTPClient  
            createFtpClient();  
  
            // 转移到FTP服务器目录  
            if (StringUtils.isNotBlank(remotePath)) {  
                ftpClient.changeWorkingDirectory(remotePath);  
            }  
  
            // 取出下载文件  
            FTPFile[] ftpFiles = ftpClient.listFiles();  
            for (FTPFile ftpFile : ftpFiles) {  
                String ftpFileName = ftpFile.getName();  
                if (ftpFileName.equals(fileName)) {  
                    success = download(localPath, ftpFile);  
                    break;  
                }  
            }  
  
            // 关闭连接FTPClient  
            closeFtpClient();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return success;  
    }  
  
    /** 
     * 删除一个文件 
     *  
     * @throws Exception 
     */  
    public static boolean deleteFile(String filename) throws Exception {  
        boolean flag = true;  
        try {  
            // 创建FTPClient  
            createFtpClient();  
            // 删除文件  
            flag = ftpClient.deleteFile(filename);  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            // 关闭连接FTPClient  
            closeFtpClient();  
        }  
  
        return flag;  
    }  
  
    /** 
     * 删除目录 
     *  
     * @throws Exception 
     */  
    public static void deleteDirectory(String pathname) throws Exception {  
        try {  
            // 创建FTPClient  
            createFtpClient();  
            File file = new File(pathname);  
            if (!file.isDirectory()) {  
                deleteFile(pathname);  
            }  
            ftpClient.removeDirectory(pathname);  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            // 关闭连接FTPClient  
            closeFtpClient();  
        }  
    }  
  
    /** 
     * 设置参数 
     *  
     * @param configFile 
     *            --参数的配置文件 
     */  
    private static void setConnectConfig() {  
        Properties property = new Properties();  
        BufferedInputStream bis = null;  
        try {  
            // 读取配置文件  
            File file = new File(configFile);  
            bis = new BufferedInputStream(new FileInputStream(file));  
            property.load(bis);  
  
            // 设置文件信息  
            ftpUserName = property.getProperty("ftp.userName");  
            ftpPassword = property.getProperty("ftp.password");  
            ftpUrl = property.getProperty("ftp.url");  
            ftpPort = Integer.parseInt(property.getProperty("ftp.port"));  
            encoding = property.getProperty("ftp.encoding");  
            serverLanguageCode = property.getProperty("ftp.serverLanguageCode");  
  
            // 关闭文件  
            if (bis != null){  
                bis.close();  
            }  
        } catch (FileNotFoundException e1) {  
            System.out.println("配置文件 " + configFile + " 不存在！");  
        } catch (IOException e) {  
            System.out.println("配置文件 " + configFile + " 无法读取！");  
        }  
    }  
  
    /** 
     * 重命名文件 
     *  
     * @param oldName 
     *            原文件名 
     * @param newName 
     *            新文件名 
     * @throws Exception 
     */  
    public static void renameFile(String oldName, String newName)  
            throws Exception {  
        try {  
            // 创建FTPClient  
            createFtpClient();  
  
            // 重命名  
            ftpClient.rename(oldName, newName);  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            // 关闭连接FTPClient  
            closeFtpClient();  
        }  
    }  
  
    /** 
     * 设置FTP客服端的配置 
     *  
     * @return ftpConfig 
     */  
    private static FTPClientConfig getFtpConfig() {  
        // FTPClientConfig.SYST_UNIX  
        FTPClientConfig ftpConfig = new FTPClientConfig(FTPClientConfig.SYST_NT);  
        ftpConfig.setServerLanguageCode(serverLanguageCode);  
        return ftpConfig;  
    }  
  
    /** 
     * 将ISO-8859-1编码转为 GBK 
     *  
     * @param obj 
     * @return "" 
     * @throws Exception 
     */  
    private static String encodingToGBK(Object obj) throws Exception {  
        if (obj != null) {  
            return new String(obj.toString().getBytes("iso-8859-1"), encoding);  
        }  
        return null;  
    }  
  
    /** 
     * 将 GBK编码转为ISO-8859-1 
     *  
     * @param obj 
     * @return "" 
     * @throws Exception 
     */  
    private static String encodingGBKToISO8859(Object obj) throws Exception {  
        if (obj != null) {  
            return new String(obj.toString().getBytes(encoding), "iso-8859-1");  
        }  
        return null;  
    }  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        boolean success = false;  
        FileInputStream fis = null;
        try {  
//            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
//          success = FtpUtil.uploadFile("D:\\ftp", "中文的.docx", "temp");  
//          System.out.println(success);  
            //  
//             success = FtpClient.uploadFile(new File("C:\\Users\\scofieldlin\\Desktop\\diamond.apk"),  
//             "apk.iwami.cn");  
            // System.out.println(success);  
  
            // success = FtpUtil.uploadDirectory("D:\\ftp", "temp");  
            // System.out.println(success);  
  
//            System.out.println(dateFormat.format(new java.util.Date()));  
//            success = FtpClient.downloadFile("c://", "test.mp4",  
//                    "apk.iwami.cn");  
//            System.out.println(dateFormat.format(new java.util.Date()));  
//            System.out.println(success);  
  
            // success = FtpUtil.downloadDirectory("d:\\ftp\\a2", "temp");  
            // System.out.println(success);  
        	
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect("proxy.wsfdupload.lxdns.com");
			System.out.println(ftpClient.login("iwami", "123abc!@#"));
			System.out.println(ftpClient.changeWorkingDirectory("/pic.iwami.cn/"));;
			ftpClient.setBufferSize(1024); 
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			fis = new FileInputStream("C:\\Users\\scofieldlin\\Desktop\\2.jpg");
			System.out.println(ftpClient.storeFile("ftp.jpg", fis));; 
			ftpClient.disconnect();
        } catch (Exception e) {  
        	e.printStackTrace();
        }	finally{
        	 IOUtils.closeQuietly(fis); 
        }
  
    }  
  
}  
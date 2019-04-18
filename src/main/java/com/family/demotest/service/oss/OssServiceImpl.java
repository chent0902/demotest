package com.family.demotest.service.oss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.web.util.ZXingCodeUtils;

@Component("oss.service")
public class OssServiceImpl
    implements
    OssService
{

    private static final String ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI5bLJC7UwfnxH";
    private static final String ACCESS_KEY_SECRET = "2iUwlRYY2u9Q7SvFaximkSYcZkyqVd";
    private static final String BACKET_NAME = "demotest";
    private static final String FOLDER = "xcx/";

    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;

    // 阿里云OSS客户端对象
    private OSSClient ossClient = null;

    public OssServiceImpl()
    {
        ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    @Override
    public String createBucketName(String bucketName)
    {
        // 存储空间
        final String bucketNames = bucketName;
        if(!ossClient.doesBucketExist(bucketName))
        {
            // 创建存储空间
            Bucket bucket = ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }

    @Override
    public void deleteBucket(String bucketName)
    {
        ossClient.deleteBucket(bucketName);
        logger.info("删除"+bucketName+"Bucket成功");
    }

    @Override
    public String createFolder(String bucketName, String folder)
    {
        // 文件夹名
        final String keySuffixWithSlash = folder;
        // 判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(bucketName, keySuffixWithSlash))
        {
            // 创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]), null);
            logger.info("创建文件夹成功");
            // 得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir = object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    @Override
    public void deleteFile(String bucketName, String folder, String key)
    {
        ossClient.deleteObject(bucketName, folder+key);
        logger.info("删除"+bucketName+"下的文件"+folder+key+"成功");
    }

    @Override
    public String uploadImg(InputStream is, String fileName)
    {
        return uploadObject2OSS(is, BACKET_NAME, FOLDER, fileName);
    }

    @Override
    public String uploadImg(File file)
    {
        return uploadObject2OSS(file, BACKET_NAME, FOLDER);
    }

    @Override
    public String uploadImg(File file, String newFileName)
    {
        String imgSing = "";
        try
        {
            InputStream is = new FileInputStream(file);
            imgSing = this.uploadImg(is, newFileName);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return imgSing;
    }

    @Override
    public String uploadObject2OSS(InputStream is, String bucketName, String folder, String fileName)
    {
        String resultStr = null;
        try
        {
            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            // 上传的文件的长度
            metadata.setContentLength(is.available());
            // 指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            // 指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            // 指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            // 如果没有扩展名则填默认值application/octet-stream
            // metadata.setContentType(getContentType(fileName));
            metadata.setContentType("image/jpeg");

            // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            // metadata.setContentDisposition("filename/filesize=" + fileName +
            // "/" + fileSize + "Byte.");
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folder+fileName, is, metadata);
            // 解析结果
            resultStr = putResult.getETag();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error(e, "上传阿里云OSS服务器异常."+e.getMessage());

        }
        return resultStr;
    }

    @Override
    public String uploadObject2OSS(File file, String bucketName, String folder)
    {
        String resultStr = null;
        try
        {
            // 以输入流的形式上传文件
            InputStream is = new FileInputStream(file);
            // 文件名
            String fileName = file.getName();
            // 文件大小
            Long fileSize = file.length();
            // 创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            // 上传的文件的长度
            metadata.setContentLength(is.available());
            // 指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            // 指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            // 指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            // 如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize="+fileName+"/"+fileSize+"Byte.");
            // 上传文件 (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folder+fileName, is, metadata);
            // 解析结果
            resultStr = putResult.getETag();

        }
        catch(Exception e)
        {
            logger.error(e, "上传阿里云OSS服务器异常."+e.getMessage());
        }
        return resultStr;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * 
     * @param fileName
     *            文件名
     * @return 文件的contentType
     */
    private String getContentType(String fileName)
    {
        // 文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(".bmp".equalsIgnoreCase(fileExtension))
        {
            return "image/bmp";
        }
        if(".gif".equalsIgnoreCase(fileExtension))
        {
            return "image/gif";
        }
        if(".jpeg".equalsIgnoreCase(fileExtension)||".jpg".equalsIgnoreCase(fileExtension)||".png".equalsIgnoreCase(fileExtension))
        {
            return "image/jpeg";
        }
        if(".html".equalsIgnoreCase(fileExtension))
        {
            return "text/html";
        }
        if(".txt".equalsIgnoreCase(fileExtension))
        {
            return "text/plain";
        }
        if(".vsd".equalsIgnoreCase(fileExtension))
        {
            return "application/vnd.visio";
        }
        if(".ppt".equalsIgnoreCase(fileExtension)||"pptx".equalsIgnoreCase(fileExtension))
        {
            return "application/vnd.ms-powerpoint";
        }
        if(".doc".equalsIgnoreCase(fileExtension)||"docx".equalsIgnoreCase(fileExtension))
        {
            return "application/msword";
        }
        if(".xml".equalsIgnoreCase(fileExtension))
        {
            return "text/xml";
        }
        // 默认返回类型
        return "image/jpeg";
    }

    @Override
    public String createQrcode(String code)
    {

        String qrCodeUrl = "";
        // 图片名称

        InputStream is = ZXingCodeUtils.drawQRCode(code);

        if(is==null)
        {
            return qrCodeUrl;
        }

        String fileName = UUID.randomUUID().toString().replace("-", "")+".png";
        String imgSign = this.uploadImg(is, fileName);

        if(!validator.isBlank(imgSign))
        {
            qrCodeUrl = Constants.OSS_DIR+fileName;
        }

        return qrCodeUrl;

    }

    @Override
    public String uploadImg(String qrcodeUrl, String fileName)
    {

        File file = this.getImagFile(qrcodeUrl);
        if(file==null)
        {
            return "";
        }
        return this.uploadImg(file, fileName);
    }

    /**
     * 远程图片转流处理
     * 
     * @param url
     * @return
     */
    private File getImagFile(String url)
    {

        try
        {
            // 打开链接
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
            // 设置请求方式为"GET"
            conn.setRequestMethod("GET");
            // 超时响应时间为5秒
            conn.setConnectTimeout(5*1000);
            // 通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            // new一个文件对象用来保存图片，默认保存当前工程根目录

            String imgPath = "/var/tomcat/qrcode/demotest";
            String imgName = System.currentTimeMillis()+".png";
            File imageFile = new File(imgPath, imgName);

            // File imageFile = new File("/Users/wujf/Desktop/var/1.jpg");
            // 创建输出流
            FileOutputStream outStream = new FileOutputStream(imageFile);
            // 写入数据
            outStream.write(data);
            // 关闭输出流
            outStream.close();

            // String path = imgPath+"/"+imgName;

            return imageFile;

        }
        catch(Exception e)
        {
            System.out.println("获取网络图片出现异常，图片路径为："+url);
            e.printStackTrace();
        }
        return null;
    }

    private byte[] readInputStream(InputStream inStream)
        throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while((len = inStream.read(buffer))!=-1)
        {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存
        return outStream.toByteArray();
    }

}

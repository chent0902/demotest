package com.family.demotest.service.oss;

import java.io.File;
import java.io.InputStream;

public interface OssService
{

    /**
     * 创建存储空间
     * 
     * @param bucketName
     *            存储空间
     * @return
     */
    public String createBucketName(String bucketName);

    /**
     * 删除存储空间buckName
     * 
     * @param bucketName
     *            存储空间
     */
    public void deleteBucket(String bucketName);

    /**
     * 创建模拟文件夹
     * 
     * @param bucketName
     *            存储空间
     * @param folder
     *            模拟文件夹名如"qj_nanjing/"
     * @return 文件夹名
     */
    public String createFolder(String bucketName, String folder);

    /**
     * 根据key删除OSS服务器上的文件
     * 
     * @param bucketName
     *            存储空间
     * @param folder
     *            模拟文件夹名 如"qj_nanjing/"
     * @param key
     *            Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public void deleteFile(String bucketName, String folder, String key);

    /**
     * 上传图片至OSS
     * 
     * @param is
     *            上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName
     *            存储空间
     * @param folder
     *            模拟文件夹名 如"qj_nanjing/"
     * @return String 返回的唯一MD5数字签名
     * */

    /**
     * 上传图片至OSS
     * 
     * @param is
     *            (图片的InputStream) 图片流
     * @return
     */

    /**
     * 上传图片至OSS
     * 
     * @param is
     *            (图片的InputStream) 图片流
     * @param fileName
     *            图片名称
     * @return
     */
    public String uploadImg(InputStream is, String fileName);

    /**
     * 上传图片至OSS
     * 
     * @param file
     *            上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @return
     */
    public String uploadImg(File file);

    /**
     * 上传图片至OSS
     * 
     * @param file
     *            上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param newFileName
     *            文件新名称
     * @return
     */
    public String uploadImg(File file, String newFileName);

    /**
     * 上传图片至OSS
     * 
     * @param is
     *            上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName
     *            存储空间
     * @param folder
     *            模拟文件夹名 如"qj_nanjing/"
     * @return String 返回的唯一MD5数字签名
     * */
    public String uploadObject2OSS(InputStream is, String bucketName, String folder, String fileName);

    /**
     * 上传图片至OSS
     * 
     * @param file
     *            上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName
     *            存储空间
     * @param folder
     *            模拟文件夹名 如"qj_nanjing/"
     * @return String 返回的唯一MD5数字签名
     * */
    public String uploadObject2OSS(File file, String bucketName, String folder);

    /**
     * 创建核销二维码
     * 
     * @param code
     * @return
     */
    public String createQrcode(String code);

    
    /**
     * 上传远程图片到oss
     * @param qrcodeUrl
     * @param fileName
     * @return
     */
	public String uploadImg(String qrcodeUrl, String fileName);

}

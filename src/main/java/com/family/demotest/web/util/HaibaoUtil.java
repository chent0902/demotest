package com.family.demotest.web.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * java使用阿里云OSS存储对象上传图片
 * 
 * @author wujf
 */
public class HaibaoUtil
{

    private static final String OSSDOMAIN = "http://demotest.oss-cn-hangzhou.aliyuncs.com/";

    // 测试
    // public static void main(String[] args)
    // {
    // // 初始化OSSClient
    //
    // getLotteryHaibao("xcx/h2KjpEn5P4dcBNrDHZFCMT.jpg",
    // "xcx/GSTNNAwGe8xjMtrACM2N2y.png");
    //
    // }

    /**
     * 生成活动海报
     * 
     * @param posterImg
     * @param qrCode
     * @return
     */
    public static InputStream getLotteryHaibao(String posterImg, String qrCode)
    {
        try
        {
            // 1.jpg是你的 主图片的路径
            URL url = new URL(OSSDOMAIN+posterImg);
            BufferedImage buffImg = ImageIO.read(url);

            // 得到画笔对象
            Graphics g = buffImg.getGraphics();

            // 创建你要附加的图象。
            // 小图片的路径
            String headImg = OSSDOMAIN+qrCode;
            URL url1 = new URL(headImg);
            ImageIcon imgIcon = new ImageIcon(url1);
            // 得到Image对象。
            Image img = imgIcon.getImage();

            // 将小图片绘到大图片上。
            // 5,300 .表示你的小图片在大图片上的位置。
            g.drawImage(img, 20, 1074, null);

            g.dispose();

            // ImageIO.write(buffImg, "png", new File("D:/1.png"));

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            os.flush();
            os.close();

            return is;
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 生成带logo的公众号二维码
     * 
     * @param qrCode
     *            带参的公众号二维码
     * @param logo
     *            公众号logo
     * @return
     */
    public static InputStream getQrCodeLogo(String qrCode, String logo)
    {
        try
        {
            // http://papaq.oss-cn-hangzhou.aliyuncs.com/xcx/2KTYGzpJjHs8HhhMWkaEaX.jpg
            // 1.jpg是你的 主图片的路径
            BufferedImage buffImg = ImageIO.read(new File(qrCode));

            // 得到画笔对象
            Graphics g = buffImg.getGraphics();

            g.setColor(Color.WHITE);
            g.fillOval(buffImg.getWidth()/2-98, buffImg.getHeight()/2-98, 196, 196);
            /** 图片 **/
            // String logoPath = OSSDOMAIN+logo;

            String logoUrl = "http://wx.qlogo.cn/mmopen/cjMcyfOzeNuLiauyaW3Jd2xDFacLquZJLSg5W4DhdVMI0ibSmgicbBbNcDpTHZc2TD72sicszMAvFxt6vgfW29gvVlabfVkk8kD5/0";

            BufferedImage logoImg = getUrlByBufferedImage(logoUrl);
            BufferedImage img = convertCircular(logoImg);

            g.drawImage(img, buffImg.getWidth()/2-98, buffImg.getHeight()/2-98, 96, 96, null);

            g.dispose();

            ImageIO.write(buffImg, "png", new File("/Users/wujf/Desktop/QrCode/19.png"));

            // ByteArrayOutputStream os = new ByteArrayOutputStream();
            // ImageIO.write(buffImg, "png", os);
            // InputStream is = new ByteArrayInputStream(os.toByteArray());
            // os.flush();
            // os.close();

            // return is;
            return null;
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 远程读取image转换为Base64字符串
     * 
     * @param imgUrl
     * @return
     */
    public static String img2Base64(String imgUrl)
    {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try
        {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection)url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();

            outStream = new ByteArrayOutputStream();

            // 图片长度
            int count = 0;
            while(count==0)
            {
                count = is.available();
            }
            byte[] buffer = new byte[count];

            // 创建一个Buffer字符串
            // byte[] buffer = new byte[1024];
            // 每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            // 使用一个输入流从buffer里把数据读取出来
            while((len = is.read(buffer))!=-1)
            {
                // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            // return new BASE64Encoder().encode(outStream.toByteArray());

            return Base64.encode(outStream.toByteArray());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(is!=null)
            {
                try
                {
                    is.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            if(outStream!=null)
            {
                try
                {
                    outStream.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            if(httpUrl!=null)
            {
                httpUrl.disconnect();
            }
        }
        return imgUrl;
    }

    /**
     * 通过网络获取图片
     * 
     * @param url
     * @return
     */
    public static BufferedImage getUrlByBufferedImage(String url)
    {
        try
        {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)urlObj.openConnection();
            // 连接超时
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(25000);
            // 读取超时 --服务器响应比较慢,增大时间
            conn.setReadTimeout(25000);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Accept-Language", "zh-cn");
            conn.addRequestProperty("Content-type", "image/jpeg");
            conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
            conn.connect();
            BufferedImage bufImg = ImageIO.read(conn.getInputStream());
            conn.disconnect();
            return bufImg;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
     * 
     * @param url
     *            用户头像地址
     * @return
     * @throws IOException
     */
    public static BufferedImage convertCircular(BufferedImage img)
        throws IOException
    {

        // 透明底的图片
        BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, img.getWidth(), img.getHeight());
        Graphics2D g2 = img2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(img, 0, 0, null);
        // 设置颜色
        g2.setBackground(Color.green);
        g2.dispose();
        return img2;
    }

    /**
     * 远程图片转流处理
     * 
     * @param url
     * @return
     */
    public static InputStream getImageStream(String url)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK)
            {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        }
        catch(IOException e)
        {
            System.out.println("获取网络图片出现异常，图片路径为："+url);
            e.printStackTrace();
        }
        return null;
    }

}

package com.family.demotest.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 订单生产器
 * 
 * @author wujf
 *
 */
public class OrderGenerateUtil
{

    /**
     * 生成订单号
     * 
     * @return
     */
    public static String generateOrderNo()
    {
        String datestr = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        StringBuffer sb = new StringBuffer(datestr);
        int machineId = 1;// 最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV<0)
        {// 有可能是负数
            hashCodeV = -hashCodeV;
        }

        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String hashcodeStr = String.format("%013d", hashCodeV);// 13位

        sb.append(machineId).append(hashcodeStr);
        return sb.toString();

    }

    /**
     * 订单核销码
     * 
     * @return
     */
    public static String generateUseCode()
    {

        String millis = new Date().getTime()+"";
        StringBuffer sb = new StringBuffer().append(millis.substring(millis.length()-3));

        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV<0)
        {// 有可能是负数
            hashCodeV = -hashCodeV;
        }

        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        //String hashcodeStr = String.format("%011d", hashCodeV);// 13位
        sb.append(hashCodeV);
        return sb.toString().substring(0, 8);

    }

    // public static String ctreateOrderNo(){
    // Random random = new Random();
    // int s =random.nextInt(9)+1;
    // try {
    // Thread.sleep(s);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // String millis = new Date().getTime()+"";
    //
    // String datestr = new SimpleDateFormat("yyyyMMddhhmmss").format(new
    // Date());
    // String no = "19"+datestr+millis.substring(millis.length()-4);
    // return no;
    // }
    
    
    public static void main(String[] args) {
    	System.out.println(OrderGenerateUtil.generateUseCode());
	}

}

package com.family.demotest.web.util;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 返回前端接口数据(json)
 * @author wujf
 */
public class ResultCode implements Serializable
{

	private static final long serialVersionUID = 942858696500596315L;
	public static final int SUCCESS = 0;
    public static final int FAIL = -1;
    private int code;//code状态吗,0为成功,其余状态根据业务需求来返回
    private String info;//提示信息
    private String other;//其他说明
    private Object data; //返回数据

    public ResultCode()
    {
        this.code = SUCCESS;
        this.info = "success";
        this.data ="";
        
    }

    public ResultCode(int code, String info)
    {
        this.code = code;
        this.info = info;
    }



    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }


	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 将对象转换成json字符串
	 * @return
	 */
	public String toJSONString(){
		return JSON.toJSONString(this,SerializerFeature.DisableCircularReferenceDetect);
	}

	
    
}

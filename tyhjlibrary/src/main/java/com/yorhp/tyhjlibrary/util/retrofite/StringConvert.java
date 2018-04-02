package com.yorhp.tyhjlibrary.util.retrofite;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Tyhj on 2017/6/14.
 */

public class StringConvert implements Converter<ResponseBody,String>{

    public static final StringConvert INSTANCE=new StringConvert();

    @Override
    public String convert(ResponseBody value) throws IOException {
        String msg = value.string();
        //log("接口数据",msg);
        return msg;
    }
}

package com.yorhp.tyhjlibrary.util.retrofite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Tyhj on 2017/6/12.
 */

public class MLFactory extends Converter.Factory {
    public static final MLFactory INSTANCE = new MLFactory();

    public static MLFactory create() {
        return INSTANCE;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //log("Type",type.toString());
        return StringConvert.INSTANCE;
    }
}

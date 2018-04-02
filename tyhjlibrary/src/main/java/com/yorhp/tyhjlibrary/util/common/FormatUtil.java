package com.yorhp.tyhjlibrary.util.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tyhj on 2017/11/1.
 */

public class FormatUtil {
    //手机号是否正确
    public static boolean isMobile(String mobiles) {
        if(mobiles==null||mobiles.equals("")){
            return false;
        }
        Pattern p = Pattern.compile("^1(3|4|5|7|8)[0-9]\\d{8}$");
        Matcher m = p.matcher(mobiles);
        boolean is = m.matches();
        return is;
    }

    //邮箱是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        boolean is = m.matches();
        return is;
    }

}

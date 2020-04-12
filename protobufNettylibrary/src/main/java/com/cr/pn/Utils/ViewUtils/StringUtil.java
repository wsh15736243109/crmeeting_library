package com.cr.pn.Utils.ViewUtils;

/**
 * Created by zy on 2017/2/14.
 * 字符串相关工具类.
 */
public class StringUtil {

    /**
     * 判断字符串是否为空;
     * @param c
     * @return
     */
    public static boolean isEmpty(String c){
        if (c==null||c.length()<=0||c.equals("null")){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 字符串截取.
     * @param c
     * @param start
     * @param end
     * @return
     */
    public static String subString(String c,int start ,int end){
        return c.substring(start,end);
    }

    /**
     * 字符串截取.
     * @param c
     * @param end
     * @return
     */
    public static String subString(String c ,int end){
        return subString(c,0,end);
    }

    /**
     * 字符串根据特定字符截取成为字符数组.
     * @param c
     * @param s
     * @return
     */
    public static String[] subStringS(String c,String s){
        return c.split(s);
    }

}

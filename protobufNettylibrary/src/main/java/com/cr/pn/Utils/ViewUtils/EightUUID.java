
package com.cr.pn.Utils.ViewUtils;

import java.util.UUID;

/**
 * 随机字符串生成
 * 生成范围为小写a-z,0-9以及大写A-Z
 * @author zy
 *
 */
public class EightUUID {
	
	 public static String[] chars = new String[]
	            {
	                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
	                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
	                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W", "X", "Y", "Z"
	            }; 
	 
    public static String getShortUuid(){ 
        return getShortUuid(8); 
    }
    
    public static String getShortUuid(int length){ 
    	if(length>=28){
    		length = 27;
    	}
        StringBuffer stringBuffer = new StringBuffer(); 
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < length; i++){ 
            String str = uuid.substring(i, i+4); 
            int strInteger  = Integer.parseInt(str, 16); 
            stringBuffer.append(chars[strInteger % 0x3E]); 
        } 
        return stringBuffer.toString();
    }
    
    
}

	
package com.supergao.software.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 检查字符串不为空
     *
     * @param str
     * @return
     */
    public static boolean notNull(String str) {
        if (str != null && !"".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if (str == null || "".equals(str) || "".equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断String是否为空，或者等于指定的option， 否则都返回缺省值
     *
     * @param str
     * @param option
     * @param defaultValue
     * @return
     */
    public static String parseNull(String str, String option,
                                   String defaultValue) {
        if (notNull(str)) {
            if (notNull(option) && str.equals(option)) {
                return defaultValue;
            } else {
                return str;
            }
        } else {
            return defaultValue;
        }
    }

    /**
     * 判断是否在6-16位
     *
     * @param str
     * @return
     */
    public static boolean pswLength(String str) {
        if (str.length() > 5 && str.length() < 17) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是全位相同的数字和字母
     *
     * @param numOrStr
     * @return
     */
    public static boolean equalStr(String numOrStr) {
        boolean flag = true;
        char str = numOrStr.charAt(0);
        for (int i = 0; i < numOrStr.length(); i++) {
            if (str != numOrStr.charAt(i)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断是否连续的字母和数字
     *
     * @param numOrStr
     * @return
     */
    public static boolean isOrderNumeric(String numOrStr) {
        boolean flag = true;// 如果全是连续数字返回true
        boolean isNumeric = true;// 如果全是数字返回true
        char[] chars = numOrStr.toCharArray();
        int[] asciiArray = new int[chars.length];

        for (int i = 0; i < chars.length; i++) {
            asciiArray[i] = (int) (chars[i]);
        }
        if (isNumeric) {// 如果全是数字则执行是否连续数字判断
            for (int i = 0; i < numOrStr.length(); i++) {
                if (i > 0) {// 判断如123456
                    int num = asciiArray[i];
                    int num_ = asciiArray[i - 1] + 1;
                    if (num != num_) {
                        flag = false;
                        break;
                    }
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断是否连续的字母和数字--递减（如：987654、876543）连续数字返回true
     *
     * @param numOrStr
     * @return
     */
    public static boolean isOrderNumeric_(String numOrStr) {
        boolean flag = true;// 如果全是连续数字返回true
        boolean isNumeric = true;// 如果全是数字返回true
        char[] chars = numOrStr.toCharArray();
        int[] asciiArray = new int[chars.length];

        for (int i = 0; i < chars.length; i++) {
            asciiArray[i] = (int) (chars[i]);
        }
        if (isNumeric) {// 如果全是数字则执行是否连续数字判断
            for (int i = 0; i < asciiArray.length; i++) {
                if (i > 0) {// 判断如654321

                    int num = asciiArray[i];
                    int num_ = asciiArray[i - 1] - 1;
                    if (num != num_) {
                        flag = false;
                        break;
                    }
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断密码是否为password弱密码
     *
     * @param str
     * @return
     */
    public static boolean isSimplePsw(String str) {
        Pattern pattern = Pattern.compile("password");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 手机号验证
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileType(String mobile) {

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches(); //判断手机号格式
    }

    /**
     * 邮箱验证
     *
     * @param email
     * @return
     */
    public static boolean isEmailType(String email) {
//		Pattern p =Pattern.compile("w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
//		Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))(a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 是否是字母
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern p = Pattern.compile("[0-9]*");
        return p.matcher(str).matches();
    }
}

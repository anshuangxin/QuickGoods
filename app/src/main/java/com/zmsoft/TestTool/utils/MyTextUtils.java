package com.zmsoft.TestTool.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class MyTextUtils {
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
    public static boolean isName(String str) {
        return str.matches("[\\u4E00-\\u9FA5]{2,4}");
    }


    public static String getSplitByPointText(int a) {
        String b = String.valueOf(a);
        if (b.length() > 3) {
            StringBuilder builder = new StringBuilder(b);
            for (int i = 1; i < b.length() / 3 + 1; i++) {
                builder.insert(b.length() - i * 3, ",");
            }
            return builder.toString();
        } else return b;
    }

    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static int hanzi2Color(String str) {
        String s = hanzi216(str);
        String color = "";
        if (s.length() < 6) {
            String b = "";
            for (int i = 0; i < 6 - s.length(); i++) {
                b += "0";
            }
            color = b + s;
        } else {
            color = s.substring(s.length() - 6, s.length());
        }
        return Color.parseColor("#" + color);
    }

    public static String hanzi216(String str) {
        String hexString = "0123456789ABCDEF";
        String result = "";
        byte[] bytes;
        try {
            bytes = str.getBytes("GBK");// 如果此处不加编码转化，得到的结果就不是理想的结果，中文转码
            StringBuilder sb = new StringBuilder(bytes.length * 2);

            for (int i = 0; i < bytes.length; i++) {
                sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
                sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
                // sb.append("");
            }

            result = sb.toString();
            System.out.println(result);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String splitNum(String u) {
        Pattern p = Pattern.compile("\\d{6,}");// 这个6是指连续数字的最少个数
        Matcher m = p.matcher(u);
        while (m.find()) {
            System.out.println("code:" + m.group());
            return m.group();
        }
        return "";
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; // 文件内容字符串
        // 打开文件
        File file = new File(path);
        // 如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(
                            instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    // 分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();

    }

    /**
     * 实现文本复制功能 add by wangqianzhou
     *
     * @param content
     */

    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(String.valueOf(content.trim()));
    }

    /**
     * 实现粘贴功能 add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }


    public static String ToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十
        {
            if (si.substring(0, 1).equals("1"))
                sd += "十";
            else
                sd += (GetCH(intInput / 10) + "十");
            sd += ToCH(intInput % 10);
        } else if (si.length() == 3)// 百
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2)
                sd += "零";
            sd += ToCH(intInput % 100);
        } else if (si.length() == 4)// 千
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3)
                sd += "零";
            sd += ToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4)
                sd += "零";
            sd += ToCH(intInput % 10000);
        }

        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }

    public static String getDateStr(long millis) {
        if (0 != millis) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(millis);
            @SuppressWarnings("resource")
            Formatter ft = new Formatter(Locale.CHINA);
            return ft.format("%1$tp %1$tT", cal).toString();
        } else {
            return null;
        }
    }

    /**
     * 关键字高亮显�?
     *
     * @param target �?要高亮的关键�?
     * @param text   �?要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效�?
     */
    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.RED);// �?要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    /**
     * 关键字改变style
     * <p/>
     * 要高亮的关键字
     *
     * @param text 要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效
     */
    public static SpannableStringBuilder getDfstylesText(Context context,
                                                         String text, String[] targets, int[] styles) {

        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        int len = targets.length;
        for (int i = 0; i < len; i++) {
            Pattern p = Pattern.compile(targets[i]);
            Matcher m = p.matcher(text);
            TextAppearanceSpan span = null;
            while (m.find()) {
                int a = i;
                if (a > styles.length - 1) {
                    a = styles.length - 1;
                }
                span = new TextAppearanceSpan(context, styles[a]);
                spannable.setSpan(span, m.start(), m.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            }
        }
        return spannable;
    }


    /**
     * 02
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     * 03
     * <p>
     * 04
     *
     * @param str 无逗号的数字
     *            05
     *            <a href="http://home.51cto.com/index.php?s=/space/34010" target="_blank">@return</a> 加上逗号的数字
     *            06
     */


    public static String addComma(String str) {

// 将传进数字反转

        String reverseStr = new StringBuilder(str).reverse().toString();

        String strTemp = "";

        for (int i = 0; i < reverseStr.length(); i++) {

            if (i * 3 + 3 > reverseStr.length()) {

                strTemp += reverseStr.substring(i * 3, reverseStr.length());

                break;
            }

            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";

        }

        // 将[789,456,] 中最后一个[,]去除

        if (strTemp.endsWith(",")) {

            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }

        // 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();

        return resultStr;

    }

}

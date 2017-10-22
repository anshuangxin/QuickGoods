package com.bjyzqs.kuaihuo_yunshouyin.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;


public class Util {

    public final static String LOGIN_COUNT = "login_count";
    public final static String REQUEST_COUNT = "request_count";
    private static String language;

    /**
     * 设置图片背景
     *
     * @param img
     * @param resId
     */
    public static void setBackGround(Context mContext, ImageView img, int resId) {
        img.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(),
                Util.readBitMap(mContext, resId)));
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res
                .getDisplayMetrics());
    }

    public static Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth, int
            reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth && height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * 非空判断
     *
     * @param entity
     * @return true 不为空，false 为空
     */
    public static boolean isNotEmpty(Object entity) {
        boolean empty = false;
        if (entity instanceof Collection) {
            if (null != entity && ((Collection<?>) entity).size() > 0) {
                return true;
            }
        }
        return empty;
    }

    /**
     * BannerBar第一张图片
     */
    public static Drawable bananerBarFiretBG = null;
    /**
     * BannerBar第一张图片路径
     */
    public static String APP_DOWNLOAD_DIR_PNG = Environment.getExternalStorageDirectory()
            + "/ELeadDownload/firsticon.png";

    public static String APP_DOWNLOAD_DIR = Environment.getExternalStorageDirectory()
            + "/ELeadDownload/Banner/";

    public static boolean loadDrawableSuccess = false;

    /**
     * 如果本地url和服务器传过来的URL不一样，删除
     *
     * @param iconurl
     * @return
     */
    public static boolean deleteIcon(Context context, String iconurl, int pos) {
        boolean delete = false;
        String firsticon = SharedPreferencesUtil.getString(context, getAfterPrefix(pos));
        try {
            if (!TextUtils.equals(iconurl, firsticon)) {
                File file = new File(APP_DOWNLOAD_DIR + getAfterPrefix(pos) + ".png");
                if (file.exists()) {
                    file.delete();
                    delete = true;
                }
            }
        } catch (Exception e) {
            Log.d("ELead_Util", e.toString());
        }
        return delete;
    }

    public static String getAfterPrefix(int pos) {
        return "bannericon" + pos;
    }

    /**
     * 获取当前模块语言字符串
     *
     * @return
     */
    public static boolean getCurrentModuleLanguage() {
        String language = getCurrentLanguage();

        return TextUtils.equals("zh", language.toLowerCase()) || TextUtils.equals("cn", language
                .toLowerCase());
    }

    /**
     * 获取系统当前语言
     */
    public static String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }


    /**
     * 创建下载目录
     */
    public static void createDownLoadDirectory() {
        // 判断文件夹是否存在，不存在就创建
        File f = new File(APP_DOWNLOAD_DIR);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * 获取应用程序版本名称
     *
     * @param c
     * @return
     */
    public static int queryVersionCode(Context c) {
        PackageInfo info;
        try {
            info = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            // 当前版本的版本号
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 字符串转换为数字
     *
     * @param value
     * @return
     */
    public static int parseInt(Object value, int defalut) {
        if (value == null) {
            return defalut;
        }
        try {
            return Integer.parseInt(getStringNotNull(value));
        } catch (NumberFormatException e) {
        }
        return defalut;
    }

    /**
     * 获取一个不为空的字符串
     * <p>
     * //@param String
     * s
     *
     * @return String
     */
    public static String getStringNotNull(Object s) {
        if (null == s || "null".equals(s)) {
            return "";
        }
        return s.toString();
    }

    public static Pattern setMatcher() {
        // all domain names
        String[] ext = {
                "top", "com", "net", "org", "edu", "gov", "int", "mil", "cn", "tel", "biz", "cc",
                "tv", "info",
                "name", "hk", "mobi", "asia", "cd", "travel", "pro", "museum", "coop", "aero",
                "ad", "ae", "af",
                "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az",
                "ba", "bb", "bd",
                "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv",
                "bw", "by", "bz",
                "ca", "cc", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cq", "cr",
                "cu", "cv", "cx",
                "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "es",
                "et", "ev", "fi",
                "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gh", "gi", "gl",
                "gm", "gn", "gp",
                "gr", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie",
                "il", "in", "io",
                "iq", "ir", "is", "it", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn",
                "kp", "kr", "kw",
                "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly",
                "ma", "mc", "md",
                "mg", "mh", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mv", "mw",
                "mx", "my", "mz",
                "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nt", "nu", "nz",
                "om", "qa", "pa",
                "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "pt", "pw", "py", "re",
                "ro", "ru", "rw",
                "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn",
                "so", "sr", "st",
                "su", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tm", "tn", "to",
                "tp", "tr", "tt",
                "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "va", "vc", "ve", "vg", "vn",
                "vu", "wf", "ws",
                "ye", "yu", "za", "zm", "zr", "zw"
        };

//	static {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < ext.length; i++) {
            sb.append(ext[i]);
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        // final pattern str
        String pattern = "((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\" +
                ".\\d{1,3}\\.\\d{1,3})|((www\\.|[a-zA-Z\\.]+\\.)?[a-zA-Z0-9\\-]+\\." + sb
                .toString() + "(:[0-9]{1,5})?))((/[a-zA-Z0-9\\./,;\\?'\\+&%\\$#=~_\\-]*)|" +
                "([^\\u4e00-\\u9fa5\\s0-9a-zA-Z\\./,;\\?'\\+&%\\$#=~_\\-]*))";
        // Log.v(TAG, "pattern = " + pattern);
        Pattern WEB_URL = Pattern.compile(pattern);

        //}

        return WEB_URL;
    }






}

package pl.saletrak.wypokplus.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.saletrak.wypokplus.activities.MainActivity;

public class GlobalFunctions {

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String getApiSign(String url, Map<String, String> params) {
        Map<String, String> params_sorted = new TreeMap<String, String>(params);

        String parameters = "";
        List<String> keys = new ArrayList<String>(params_sorted.values());
        for (int i = 0; i < keys.size(); i++) {
            parameters += keys.get(i);
            if(i < keys.size()-1) parameters += ",";
        }

        return MD5(GlobalVariables.sekret+url+parameters);
    }

    public static int dpToPx(int dps) {
        final float scale = MainActivity.getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    public static String getTimeAgo(String formatted) {
        String[] date_splited = formatted.split(" ");
        String[] date = date_splited[0].split("-");
        String[] time = date_splited[1].split(":");
        // todo
        return "";
    }

}

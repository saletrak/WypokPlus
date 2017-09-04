package pl.saletrak.wypokplus.resource;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.saletrak.wypokplus.activities.MainActivity;

public class UserData {

    private static FileData user_data = new FileData(MainActivity.getContext(), "USER_DATA");
    private static FileData user_session = new FileData(MainActivity.getContext(), "USER_SESSION");

    public static final int USER_DATA = 1;
    public static final int USER_SESSION = 2;

    public UserData() {
        if(!user_data.fileExists()) {
            setDefaultValues(USER_DATA);
            setDefaultValues(USER_SESSION);
        }
        else if(!user_session.fileExists()) {
            if(getLogged()) {
                setSession();
            }
            else {
                setDefaultValues(USER_SESSION);
            }
        }
        else {
            try {
                JSONObject user_data_json = new JSONObject(user_data.getContent());
                user_data_json.getBoolean("logged");
            }
            catch (JSONException e) {
                setDefaultValues(USER_DATA);
                setDefaultValues(USER_SESSION);
            }

            try {
                JSONObject user_session_json = new JSONObject(user_session.getContent());
                user_session_json.getLong("time");
            }
            catch (JSONException e) {
                setDefaultValues(USER_SESSION);
            }
        }

        Log.d("dbg_userdata", "constructor");
    }



    @Nullable
    public static JSONObject getUserData() {
        //Log.d("dbg_userdata", user_data.getContent());
        try {
            return new JSONObject(user_data.getContent());
        }
        catch (JSONException e) {
            e.printStackTrace();
            setDefaultValues(USER_DATA);
            try {
                return new JSONObject(user_data.getContent());
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    public static JSONObject getUserSession() {
        try {
            return new JSONObject(user_session.getContent());
        } catch (JSONException e) {
            e.printStackTrace();
            setDefaultValues(USER_SESSION);
            return null;
        }
    }

    public static void setSession() {
        String url = "https://a.wykop.pl/user/login/appkey," + GlobalVariables.appkey + ",format,json";
        Map<String, String> params = new HashMap<String, String>();
        params.put("accountkey", UserData.getToken());
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("apisign", GlobalFunctions.getApiSign(url, params));
        headers.put("User-Agent", "WykopAPI");

        MyRequest myRequest = new MyRequest(MainActivity.getContext(), url, 1, params, headers);
        myRequest.doRequest(new MyRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONObject user_data_json = new JSONObject(user_data.getContent());
                    JSONObject user_session_json = new JSONObject(user_session.getContent());

                    if(user_data_json.has("signup_date")) user_data_json.remove("signup_date");
                    if(user_data_json.has("avatar_q150")) user_data_json.remove("avatar_q150");
                    if(user_data_json.has("sex")) user_data_json.remove("sex");
                    user_data_json.accumulate("signup_date", object.getString("signup_date"));
                    user_data_json.accumulate("avatar_q150", object.getString("avatar_big"));
                    user_data_json.accumulate("sex", object.getString("sex"));
                    user_data.saveFile(user_data_json.toString());

                    if(user_session_json.has("userkey")) user_session_json.remove("userkey");
                    if(user_session_json.has("time")) user_session_json.remove("time");
                    user_session_json.accumulate("userkey", object.getString("userkey"));
                    user_session_json.accumulate("time", System.currentTimeMillis());
                    user_session.saveFile(user_session_json.toString());

                    //Log.d("dbg_session", result);
                    MainActivity.this_activity.navigation.refreshNaviHeader();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private static void setDefaultValues(int data_type) {
        if(data_type == 1) {
            JSONObject data_default = new JSONObject();
            try {
                data_default.accumulate("logged", false);
                user_data.saveFile(data_default.toString());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(data_type == 2) {
            JSONObject session_default = new JSONObject();
            try {
                session_default.accumulate("userkey", "");
                session_default.accumulate("time", 0);
                user_session.saveFile(session_default.toString());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logout() {
        setDefaultValues(USER_DATA);
        setDefaultValues(USER_SESSION);
    }

    public static boolean getLogged() {
        try {
            return getUserData().getBoolean("logged");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    public static String getUserName() {
        try {
            return getUserData().getString("user_name");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String getToken() {
        try {
            return getUserData().getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String getSex() {
        try {
            return getUserData().getString("sex");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

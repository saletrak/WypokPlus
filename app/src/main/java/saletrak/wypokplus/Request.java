package saletrak.wypokplus;

/*public class Request {
    private static String text;

    public Request(String string) {
        text = string;
    }

    public String getText() {
        return text;
    }
}*/

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Request {

    public String odp = "abc";
    public JSONObject odp_json = null;
    private RequestQueue queue;
    private String url = "http://a.wykop.pl/user/login/appkey,i9RDBKxJz4,format,json";

    public Request(Context context) {
        queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    odp = response;
                    Log.d("dbg2", odp);
                }

            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    odp = "ERROR: "+error.toString();
                    Log.d("dbg", odp);
                }
            }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", "kubasal");
                params.put("accountkey", "Jya1fSaNCRjLnrO5VogT");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "WykopAPI");
                params.put("apisign", "9a95cb80726e2049a8b1027e2fbeba19");
                return params;
            }
        };

        queue.add(stringRequest);
        Log.d("dbg", odp);
    }
}

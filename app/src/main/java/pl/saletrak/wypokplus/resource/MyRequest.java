package pl.saletrak.wypokplus.resource;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class MyRequest {

    private Context context;
    private RequestQueue queue;
    private String url;
    private int method;
    private final Map<String, String> params;
    private final Map<String, String> headers;

    public MyRequest(Context context, String url, int method, Map<String, String> params, Map<String, String> headers) {
        this.context = context;
        this.url = url;
        this.queue = Volley.newRequestQueue(this.context);
        this.params = params;
        this.headers = headers;

        if(method == 0) {
            this.method = com.android.volley.Request.Method.GET;
        }
        else if(method == 1) {
            this.method = com.android.volley.Request.Method.POST;
        }

    }

    public interface VolleyCallback {
        void onSuccess(String result);
        void onError(VolleyError error);
    }

    public void doRequest(final VolleyCallback callback) {
        StringRequest stringRequestData = new StringRequest(this.method, this.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        queue.add(stringRequestData);
    }

}

package pl.saletrak.wypokplus.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.saletrak.wypokplus.activities.MainActivity;
import pl.saletrak.wypokplus.resource.MyRequest;
import saletrak.wypokplus.EntryView;
import pl.saletrak.wypokplus.resource.GlobalFunctions;
import pl.saletrak.wypokplus.resource.GlobalVariables;
import saletrak.wypokplus.PostView;
import pl.saletrak.wypokplus.R;

public class UserFragment extends Fragment {

    public UserFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_entries, container, false);

        String url = "https://a.wykop.pl/profile/entries/kubasal/appkey,"+ GlobalVariables.appkey +",page,1,format,json";

        Map<String, String> params = new HashMap<String, String>();
        //params.put("LoginActivity", "kubasal");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("apisign", GlobalFunctions.getApiSign(url, params));
        headers.put("User-Agent", "WykopAPI");

        MyRequest myRequest = new MyRequest(getContext(), url, 1, params, headers);
        myRequest.doRequest(new MyRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray entries_array = new JSONArray(result);
                    LinearLayout entries_wrapper = (LinearLayout) rootView.findViewById(R.id.entries_wrapper);

                    for(int i=0; i < entries_array.length(); i++) {
                        JSONObject entry = entries_array.getJSONObject(i);

                        PostView postView_view = new EntryView(MainActivity.getContext(), entry, false);
                        entries_wrapper.addView(postView_view);

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {
                Log.d("dbg_microblog_error", error.toString());
                Toast.makeText(MainActivity.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}

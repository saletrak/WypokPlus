package pl.saletrak.wypokplus.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.saletrak.wypokplus.activities.MainActivity;
import pl.saletrak.wypokplus.layout_elements.EntryAdapter;
import pl.saletrak.wypokplus.layout_elements.EntryData;
import pl.saletrak.wypokplus.resource.GlobalFunctions;
import pl.saletrak.wypokplus.resource.GlobalVariables;
import pl.saletrak.wypokplus.resource.MyRequest;
import pl.saletrak.wypokplus.R;

public class MicroblogFragment extends Fragment {

    public MicroblogFragment() {
        //
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_entries, container, false);

        String url = "https://a.wykop.pl/stream/index/appkey,"+ GlobalVariables.appkey +",page,1,format,json";

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

                    final ListView listView = (ListView) rootView.findViewById(R.id.entries_list);
                    final ArrayList<EntryData> list = new ArrayList<EntryData>();
                    for(int i=0; i < entries_array.length(); i++) {
                        list.add(new EntryData(entries_array.getJSONObject(i)));
                    }
                    final EntryAdapter entryAdapter = new EntryAdapter(MainActivity.getContext(), list);
                    listView.setAdapter(entryAdapter);
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

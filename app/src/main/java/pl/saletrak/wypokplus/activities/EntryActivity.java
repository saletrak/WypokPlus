package pl.saletrak.wypokplus.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import saletrak.wypokplus.EntryView;
import pl.saletrak.wypokplus.resource.GlobalFunctions;
import pl.saletrak.wypokplus.resource.GlobalVariables;
import pl.saletrak.wypokplus.resource.MyRequest;
import saletrak.wypokplus.PostView;
import pl.saletrak.wypokplus.R;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        String entry_id = intent.getStringExtra("id");
        toolbar.setTitle(entry_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final LinearLayout entry_content = (LinearLayout) findViewById(R.id.entry_content);

        String url = "https://a.wykop.pl/entries/index/"+entry_id+"/appkey,"+ GlobalVariables.appkey +",format,json";

        Map<String, String> params = new HashMap<String, String>();
        //params.put("LoginActivity", "kubasal");

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("apisign", GlobalFunctions.getApiSign(url, params));
        headers.put("User-Agent", "WykopAPI");

        MyRequest myRequest = new MyRequest(getApplicationContext(), url, 1, params, headers);
        myRequest.doRequest(new MyRequest.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject entry = new JSONObject(result);

                    PostView postView_view = new EntryView(MainActivity.getContext(), entry, true);
                    entry_content.addView(postView_view);

                    JSONArray comments = entry.getJSONArray("comments");
                    for(int i=0; i<comments.length(); i++) {
                        PostView comment_view = new EntryView(MainActivity.getContext(), comments.getJSONObject(i), true);
                        entry_content.addView(comment_view);
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}

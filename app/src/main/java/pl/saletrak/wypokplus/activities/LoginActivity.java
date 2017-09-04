package pl.saletrak.wypokplus.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.saletrak.wypokplus.resource.FileData;
import pl.saletrak.wypokplus.resource.GlobalVariables;
import pl.saletrak.wypokplus.resource.UserData;

public class LoginActivity extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(pl.saletrak.wypokplus.R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(pl.saletrak.wypokplus.R.id.toolbar);
        toolbar.setTitle("Zaloguj się");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        }
        else {
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(MainActivity.getContext());
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }

        webview = (WebView) findViewById(pl.saletrak.wypokplus.R.id.webView);
        webview.loadUrl("https://a.wykop.pl/user/connect/appkey,"+ GlobalVariables.appkey);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Pattern pattern = Pattern.compile("^https://a\\.wykop\\.pl/user/ConnectSuccess/");
                Matcher matcher = pattern.matcher(url);
                if(matcher.find()) {
                    String url_splited[] = matcher.replaceFirst("").split("/");
                    JSONObject json_user_data = new JSONObject();
                    try {
                        json_user_data.accumulate("logged", true);
                        for(int i=0; i<url_splited.length; i++) {
                            if(url_splited[i].equals("login")) {
                                json_user_data.accumulate("user_name", url_splited[i+1]);
                            }
                            else if(url_splited[i].equals("token")) {
                                json_user_data.accumulate("token", url_splited[i+1]);
                            }
                        }
                        FileData user_data = new FileData(MainActivity.getContext(), "USER_DATA");
                        user_data.saveFile(json_user_data.toString());

                        UserData.setSession();
                        Log.d("dbg_main", "setting avatar url");

                        MainActivity.this_activity.finish();
                        finish();
                        Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent_main);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.getContext(), "Błąd przetwarzania danych", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // wykonujemy akcje po kliknięciu na element menu
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

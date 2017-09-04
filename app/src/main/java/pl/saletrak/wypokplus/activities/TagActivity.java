package pl.saletrak.wypokplus.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class TagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(pl.saletrak.wypokplus.R.layout.activity_tag);
        Toolbar toolbar = (Toolbar) findViewById(pl.saletrak.wypokplus.R.id.toolbar);
        Intent intent = getIntent();
        String tag = intent.getStringExtra("tag");
        toolbar.setTitle("#"+tag);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tag_name = (TextView) findViewById(pl.saletrak.wypokplus.R.id.tag_name);
        tag_name.setText("#"+tag);
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

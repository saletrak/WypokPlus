package pl.saletrak.wypokplus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.saletrak.wypokplus.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("O aplikacji");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout content = (LinearLayout) findViewById(R.id.content);

        //LinearLayout entry = (LinearLayout) getLayoutInflater().inflate(R.layout.entry, content, false);
        //content.addView(entry);

        /*LinearLayout entry_wrapper = new LinearLayout(MainActivity.getContext());
        TextView entry_author = new TextView(MainActivity.getContext());
        TextView entry_body = new TextView(MainActivity.getContext());

        entry_author.setText("kubasal");
        entry_body.setText("In the XML File LinearLayout already has child view. So there is not need to add them in code.", TextView.BufferType.SPANNABLE);

        Spannable spannable = (Spannable) entry_body.getText();
        StyleSpan spanbold = new StyleSpan(Typeface.BOLD);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(0xFF000000);
        URLSpan urlSpan = new URLSpan("heheszki") {
            @Override
            public void onClick(View widget) {
                Toast.makeText(MainActivity.getContext(), "#"+getURL(), Toast.LENGTH_SHORT).show();
            }
        };
        entry_body.setMovementMethod(LinkMovementMethod.getInstance());
        entry_body.setLinkTextColor(Color.RED);
        spannable.setSpan(spanbold, 38, 50, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(urlSpan, 38, 50, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(backgroundColorSpan, 38, 60, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        entry_wrapper.setOrientation(LinearLayout.VERTICAL);
        entry_wrapper.setLayoutParams(params);
        entry_wrapper.addView(entry_author);
        entry_wrapper.addView(entry_body);
        content.addView(entry_wrapper);*/

        TextView string = new TextView(MainActivity.getContext());
        //SpannableString bodyText = new BodyText("O której #<a href=\"#tacohemingway\">ta<em>cohem</em>ingway</a> na grilowaniu? :D  #<a href=\"#poznan\">poznan</a>").getBody();
        string.setText("ababa");
        content.addView(string);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

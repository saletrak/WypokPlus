package pl.saletrak.wypokplus.layout_elements;

import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.saletrak.wypokplus.activities.MainActivity;

public class SpoilerSpan extends ClickableSpan {

    private String spoiler_text;
    private int spoiler_start;

    public SpoilerSpan() {
        this.spoiler_text = "";
        this.spoiler_start = 0;
    }

    public SpoilerSpan(String spoiler_text, int start) {
        this.spoiler_text = spoiler_text;
        this.spoiler_start = start;
    }

    @Override
    public void onClick(View widget) {
        TextView textView = (TextView) widget;
        Toast.makeText(MainActivity.getContext(), spoiler_text, Toast.LENGTH_SHORT).show();
        Log.d("dbg_spoiler", widget.toString());
        Log.d("dbg_spoiler", textView.getText().toString());
        Spannable spannable = (Spannable) textView.getText();
        SpoilerSpan[] spans = spannable.getSpans(0, textView.getText().length(), SpoilerSpan.class);

        Log.d("dbg_spo", String.valueOf(spoiler_start));
        /*for (SpoilerSpan span : spans) {
            //Log.d("dbg_spo2", String.valueOf(spans[i].spoiler_start));
            if (spoiler_start == span.spoiler_start) {
                //Log.d("dbg_spo2", span.);
            }
        }*/

        spannable.setSpan(new TypefaceSpan("monospace"), spoiler_start, spoiler_start+15, 0);

        // todo
        //spannable.
        //spannable.setSpan(new BackgroundColorSpan(0xFF000000), 1, 10, 0);
        //textView.setText(Html);
        //SpoilerSpan[] spans = ((SpannableStringBuilder) textView.getText()).getSpans(0, textView.getText().length(), SpoilerSpan.class);

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}

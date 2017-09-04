package saletrak.wypokplus;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.saletrak.wypokplus.activities.MainActivity;
import pl.saletrak.wypokplus.activities.TagActivity;
import pl.saletrak.wypokplus.activities.UserActivity;
import pl.saletrak.wypokplus.layout_elements.URLSpanNoUnderline;
import pl.saletrak.wypokplus.resource.Colors;
import pl.saletrak.wypokplus.resource.GlobalFunctions;

public class PostView extends LinearLayout {

    int post_id;

    JSONObject data;
    Context context;

    public PostView(Context context, JSONObject post_data) {
        super(context);
        this.data = post_data;
        this.context = context;

        try {
            this.post_id = data.getInt("id");
            setViews();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PostView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void setViews() {
        this.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params_post = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_post.setMargins(0, GlobalFunctions.dpToPx(10), 0, 0);

        this.setPadding(0, GlobalFunctions.dpToPx(10), 0, 0);
        this.setLayoutParams(params_post);
        this.setBackgroundColor(Colors.getBodyBackgroundColor());

    }

    public static Spannable setLinks(Spannable spannable) {
        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            spannable.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL()) {
                @Override
                public void onClick(View widget) {
                    //Toast.makeText(MainActivity.getContext(), getURL(), Toast.LENGTH_SHORT).show();
                    Context context = MainActivity.getContext();
                    Matcher user = Pattern.compile("^@([a-zA-Z-_0-9]+)").matcher(getURL());
                    Matcher tag = Pattern.compile("^#([a-zA-Z_0-9]+)").matcher(getURL());
                    if(user.find()) {
                        Intent intent = new Intent(context, UserActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("user", user.group(1));
                        context.startActivity(intent);
                    }
                    else if(tag.find()) {
                        Intent intent = new Intent(context, TagActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("tag", tag.group(1));
                        context.startActivity(intent);
                    }
                }
            };
            spannable.setSpan(span, start, end, 0);
        }
        return spannable;
    }

    public abstract class OnClickWithInt implements OnClickListener {

        int param_int;

        public OnClickWithInt(int param_int) {
            this.param_int = param_int;
        }
    }

    public abstract class OnClickWithString implements OnClickListener {

        String param_string;

        public OnClickWithString(String param_string) {
            this.param_string = param_string;
        }
    }

}
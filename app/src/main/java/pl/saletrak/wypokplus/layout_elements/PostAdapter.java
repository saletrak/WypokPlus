package pl.saletrak.wypokplus.layout_elements;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.saletrak.wypokplus.activities.MainActivity;
import pl.saletrak.wypokplus.activities.TagActivity;
import pl.saletrak.wypokplus.activities.UserActivity;


public class PostAdapter<T> extends ArrayAdapter<T> {

    public HashMap<Integer, T> post_data_map = new HashMap<Integer, T>();
    public Context context;
    public int layoutResource;

    public PostAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);

        for(int i=0; i<objects.size(); i++) {
            post_data_map.put(i, objects.get(i));
        }
        this.context = context;
        this.layoutResource = resource;
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

    public abstract class OnClickWithInt implements View.OnClickListener {

        int param_int;

        public OnClickWithInt(int param_int) {
            this.param_int = param_int;
        }
    }

    public abstract class OnClickWithString implements View.OnClickListener {

        String param_string;

        public OnClickWithString(String param_string) {
            this.param_string = param_string;
        }
    }
}

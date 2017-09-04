package pl.saletrak.wypokplus.unused;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.saletrak.wypokplus.activities.MainActivity;
import pl.saletrak.wypokplus.layout_elements.URLSpanNoUnderline;

public class BodyText {

    private String body;
    private String source;
    private final String DBG_TAG = "dbg_bodytext";
    SpannableString spannableString;

    public BodyText(String source) {
        this.source = source;
        analizeContent();
    }

    private void analizeContent() {
        body = source.replaceAll("<br\\u0020*/?>", "");
        spannableString = new SpannableString(body);

        Pattern pattern = Pattern.compile("<([^>]+)>");

        Matcher matcher = pattern.matcher(body);
        List<BodyTextTag> found_tags = new ArrayList<BodyTextTag>();

        int lol = 0;
        while(matcher.find()) {
            //Log.d("dbg_bodytext", String.valueOf(matcher.group(1)));
            found_tags.add(new BodyTextTag(matcher.group(), matcher.start(), matcher.end()));
            //Log.d("dbg_bodytext", "znaleziony tag - ("+lol+") text: "+found_tags.get(lol).text+" start: "+String.valueOf(found_tags.get(lol).start)+" end: "+String.valueOf(found_tags.get(lol).end));
            lol++;
        }

        String[] tag_names = new String[found_tags.size()];

        for(int i=0; i<found_tags.size(); i++) {
            Matcher get_tag_name = Pattern.compile("^<(\\\\?/?[a-zA-Z]+)").matcher(found_tags.get(i).text);
            if(get_tag_name.find()) {
                tag_names[i] = get_tag_name.group(1);

                //Log.d("dbg_bodytext", "nazwa taga - ("+i+")" + tag_names[i]);
            }
        }

        final List<BodyTextTagFull> full_tags = new ArrayList<>();

        for(int i=0; i<tag_names.length; i++) {
            // jesli to jest tag otwierajacy
            if(Pattern.compile("^[a-zA-Z]+").matcher(tag_names[i]).find()) {
                //Log.d("dbg_bodytext", "znajdz koniec tagu ("+i+") "+tag_names[i]+" "+found_tags.get(i).text);

                // jesli tag po nim ma taką samą nazwę i jest zamykający
                if(tag_names[i+1].equals("/"+tag_names[i])) {
                    // utworz obiekt pelny tag
                    full_tags.add(new BodyTextTagFull(found_tags.get(i), found_tags.get(i+1), body.substring(found_tags.get(i).end, found_tags.get(i+1).start)));
                }
                // jesli tag po nim jest otwierajacy lub nie ma takiej samej nazwy
                else {
                    int open_tags_inside = 0;
                    int j = i+1;
                    while(open_tags_inside != -1) {
                        if(open_tags_inside == 0 && tag_names[j].equals("/"+tag_names[i])) {
                            full_tags.add(new BodyTextTagFull(found_tags.get(i), found_tags.get(j), body.substring(found_tags.get(i).end, found_tags.get(j).start)));
                            open_tags_inside = -1;
                        }
                        else {
                            if(Pattern.compile("^/").matcher(tag_names[j]).find()) {
                                open_tags_inside = open_tags_inside - 1;
                            }
                            else {
                                open_tags_inside = open_tags_inside + 1;
                            }
                            j++;
                        }
                    }
                }
            }
        }

        for(int i=0; i<full_tags.size(); i++) {
            Log.d("dbg_bodytext", "full tag - ("+i+")"+full_tags.get(i).tag_open.text+full_tags.get(i).content+full_tags.get(i).tag_close.text);

            int content_start = full_tags.get(i).tag_open.end;
            int content_end = full_tags.get(i).tag_close.start;

            if(Pattern.compile("^<a").matcher(full_tags.get(i).tag_open.text).find()) {
                URLSpanNoUnderline urlSpanNoUnderline = new URLSpanNoUnderline(full_tags.get(i).getParametr("href")) {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(MainActivity.getContext(), getURL(), Toast.LENGTH_SHORT).show();
                    }
                };
                spannableString.setSpan(urlSpanNoUnderline, content_start, content_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(Pattern.compile("^<strong").matcher(full_tags.get(i).tag_open.text).find()) {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(styleSpan, content_start, content_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(Pattern.compile("^<em").matcher(full_tags.get(i).tag_open.text).find()) {
                StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
                spannableString.setSpan(styleSpan, content_start, content_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(Pattern.compile("^<cite").matcher(full_tags.get(i).tag_open.text).find()) {
                StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
                spannableString.setSpan(styleSpan, content_start, content_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(Pattern.compile("^<code>").matcher(full_tags.get(i).tag_open.text).find()) {
                TypefaceSpan typefaceSpan = new TypefaceSpan("monospace");
                spannableString.setSpan(typefaceSpan, content_start, content_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            else if(Pattern.compile("^<code class=\"dnone\"").matcher(full_tags.get(i).tag_open.text).find()) {
                URLSpanNoUnderline urlSpanNoUnderline = new URLSpanNoUnderline(full_tags.get(i).content) {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(MainActivity.getContext(), getURL(), Toast.LENGTH_SHORT).show();
                    }
                };
                spannableString.setSpan(urlSpanNoUnderline, content_start, content_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        spannableString = new SpannableString(Replacer.replace(spannableString, "<([^>]+)>", ""));

    }

    public SpannableString getBody() {
        //return new SpannableString(body);
        return spannableString;
    }

}

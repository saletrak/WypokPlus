package pl.saletrak.wypokplus.unused;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.saletrak.wypokplus.unused.BodyTextTag;

public class BodyTextTagFull {
    protected BodyTextTag tag_open;
    protected BodyTextTag tag_close;
    protected String content;

    public BodyTextTagFull(BodyTextTag tag_open, BodyTextTag tag_close, String content) {
        this.tag_open = tag_open;
        this.tag_close = tag_close;
        this.content = content;
    }

    public String getParametr(String parametr) {
        String tag = tag_open.text;
        Pattern pattern = Pattern.compile("\\u0020"+parametr+"=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(tag);
        if(matcher.find()){
            return matcher.group(1);
        }
        else {
            return "";
        }
    }
}

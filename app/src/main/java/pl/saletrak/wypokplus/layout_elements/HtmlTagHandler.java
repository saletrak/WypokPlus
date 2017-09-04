package pl.saletrak.wypokplus.layout_elements;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.TypefaceSpan;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

import pl.saletrak.wypokplus.layout_elements.SpoilerSpan;

public class HtmlTagHandler implements Html.TagHandler {

    final String DBG_TAG = "dbg_htmltaghan";
    final HashMap<String, String> attributes = new HashMap<String, String>();

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        getAttributes(xmlReader);

        if(tag.equalsIgnoreCase("code")) {
            if(attributes.containsKey("class") && attributes.get("class") != null && attributes.get("class").equalsIgnoreCase("dnone")) {

                if(opening) {
                    output.setSpan(new SpoilerSpan(), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                }
                else {
                    Object obj = getLast(output, SpoilerSpan.class);
                    int where = output.getSpanStart(obj);
                    String text_spoiler = output.toString().substring(where, output.length());
                    output.setSpan(new SpoilerSpan(text_spoiler, where), where, output.length(), 0);
                    output.replace(where, output.length(), "[pokaż spoiler]");
                }
            }
            else {
                if(opening) {
                    output.setSpan(new TypefaceSpan("monospace"), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                }
                else {
                    Object obj = getLast(output, TypefaceSpan.class);
                    int where = output.getSpanStart(obj);
                    output.setSpan(new TypefaceSpan("monospace"), where, output.length(), 0);
                }
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if(objs.length == 0) {
            return null;
        } else {
            for (int i=objs.length; i > 0; i--) {
                if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i-1];
                }
            }
            return null;
        }
    }

    private void getAttributes(XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            if(elementField.get(xmlReader) != null) {
                Object element = elementField.get(xmlReader);
                Field attsField = element.getClass().getDeclaredField("theAtts");
                attsField.setAccessible(true);
                Object atts = attsField.get(element);
                Field dataField = atts.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                String[] data = (String[]) dataField.get(atts);
                Field lengthField = atts.getClass().getDeclaredField("length");
                lengthField.setAccessible(true);
                int len = (Integer) lengthField.get(atts);

                for (int i = 0; i < len; i++)
                    attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
            }
        }
        catch (Exception e) {
            //Log.d(DBG_TAG, "Exception: " + e);
            e.printStackTrace();
        }
    }
}

package saletrak.wypokplus;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import pl.saletrak.wypokplus.activities.EntryActivity;
import pl.saletrak.wypokplus.activities.UserActivity;
import pl.saletrak.wypokplus.layout_elements.HtmlTagHandler;
import pl.saletrak.wypokplus.resource.Colors;
import pl.saletrak.wypokplus.resource.DownloadImageTask;
import pl.saletrak.wypokplus.resource.GlobalFunctions;
import pl.saletrak.wypokplus.R;

public class EntryView extends PostView {

    boolean view_as_single_post;

    RelativeLayout header;
    TextView body;
    LinearLayout embed;
    RelativeLayout footer;

    public EntryView(Context context, JSONObject entry_data, boolean view_as_single_post) {
        super(context, entry_data);
        this.view_as_single_post = view_as_single_post;

        setEntryView();
    }

    public EntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EntryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void setEntryView() {

        header = new RelativeLayout(context);
        body = new TextView(context);
        embed = new LinearLayout(context);
        footer = new RelativeLayout(context);

        this.addView(header);
        this.addView(body);
        this.addView(embed);
        this.addView(footer);

        setHeader();
        setBody();
        setEmbed();
        setFooter();

    }

    private void setHeader() {
        LinearLayout header_user;
        LinearLayout header_user_left;
        LinearLayout header_user_right;

        ImageView avatar;
        ImageView sex;
        TextView author;
        TextView time;
        TextView pluses;

        LinearLayout.LayoutParams params_header = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params_header.setMargins(0,0,0, GlobalFunctions.dpToPx(7));

        RelativeLayout.LayoutParams params_header_user = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_header_user.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params_header_user.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        LinearLayout.LayoutParams params_header_user_left_avatar = new LinearLayout.LayoutParams(GlobalFunctions.dpToPx(40), GlobalFunctions.dpToPx(40));
        params_header_user_left_avatar.setMargins(0, 0, GlobalFunctions.dpToPx(10), 0);

        LinearLayout.LayoutParams params_header_user_left_sex = new LinearLayout.LayoutParams(GlobalFunctions.dpToPx(40), GlobalFunctions.dpToPx(3));

        LinearLayout.LayoutParams params_header_user_right = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        RelativeLayout.LayoutParams params_entry_plus = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_entry_plus.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_entry_plus.addRule(RelativeLayout.ALIGN_PARENT_TOP);


        header.setPadding(GlobalFunctions.dpToPx(10), 0, GlobalFunctions.dpToPx(10), GlobalFunctions.dpToPx(11));
        header.setBackgroundResource(R.drawable.border_bottom);
        header.setLayoutParams(params_header);
        header_user = new LinearLayout(context);
        header_user.setLayoutParams(params_header_user);
        header_user_left = new LinearLayout(context);
        header_user_left.setOrientation(LinearLayout.VERTICAL);
        header_user_right = new LinearLayout(context);
        header_user_right.setOrientation(LinearLayout.VERTICAL);
        header_user_right.setGravity(Gravity.CENTER_VERTICAL);
        header_user_right.setLayoutParams(params_header_user_right);


        pluses = new TextView(context);
        pluses.setLayoutParams(params_entry_plus);
        pluses.setTextColor(0xFFA0A0A0);
        try {
            pluses.setText("+ "+String.valueOf(data.getInt("vote_count")));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        avatar = new ImageView(context);
        avatar.setImageResource(R.drawable.entry_avatar_default);
        avatar.setLayoutParams(params_header_user_left_avatar);
        try {
            new DownloadImageTask(avatar).execute(data.getString("author_avatar"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        sex = new ImageView(context);
        try {
            if(data.getString("author_sex").equals("male")) {
                sex.setImageResource(R.drawable.ic_sex_male);
                sex.setLayoutParams(params_header_user_left_sex);
            }
            else if(data.getString("author_sex").equals("female")) {
                sex.setImageResource(R.drawable.ic_sex_female);
                sex.setLayoutParams(params_header_user_left_sex);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        author = new TextView(context);
        author.setTypeface(null, Typeface.BOLD);
        try {
            author.setTextColor(Colors.getUserNickColor(data.getInt("author_group")));
            author.setText(data.getString("author"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        time = new TextView(context);
        time.setTextColor(0xFFA0A0A0);
        try {
            if(!data.getString("app").equals("null")) {
                time.setText(data.getString("date")+" via "+data.getString("app"));
            }
            else {
                time.setText(data.getString("date"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        try {
            OnClickListener author_activity = new OnClickWithString(data.getString("author")) {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("user", param_string);
                    context.startActivity(intent);
                }
            };
            avatar.setOnClickListener(author_activity);
            author.setOnClickListener(author_activity);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        header.addView(header_user);
        header_user.addView(header_user_left);
        header_user.addView(header_user_right);
        header_user_left.addView(avatar);
        header_user_left.addView(sex);
        header_user_right.addView(author);
        header_user_right.addView(time);
        header.addView(pluses);
    }

    private void setBody() {
        try {
            if(!data.getString("body").equals("")) {
                body.setPadding(GlobalFunctions.dpToPx(10), 0, GlobalFunctions.dpToPx(10), GlobalFunctions.dpToPx(10));
                body.setTextColor(Colors.getTextColor());
                body.setLinkTextColor(0xFF6CB0DD);
                body.setHighlightColor(0x00000000);

                Spannable html = (Spannable) Html.fromHtml(data.getString("body"), null, new HtmlTagHandler());
                Spannable html_links = setLinks(html);
                body.setText(html_links);
                body.setMovementMethod(LinkMovementMethod.getInstance());
            }
            else {
                body.setVisibility(GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setEmbed() {
        try {
            if(!data.get("embed").equals(null)) {
                embed.setPadding(GlobalFunctions.dpToPx(10), 0, GlobalFunctions.dpToPx(10), GlobalFunctions.dpToPx(10));
                JSONObject embed_json = data.getJSONObject("embed");
                String embed_type = embed_json.getString("type");
                boolean embed_plus18 = embed_json.getBoolean("plus18");

                if(embed_type.equals("image")) {
                    ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
                    ImageView embed_imageView = new ImageView(context);
                    LinearLayout.LayoutParams embed_image_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    embed_imageView.setLayoutParams(embed_image_params);
                    embed.addView(embed_imageView);
                    embed.addView(progressBar);
                    embed.setGravity(Gravity.CENTER);
                    embed.setOrientation(LinearLayout.VERTICAL);
                    new DownloadImageTask(embed_imageView, progressBar).execute(embed_json.getString("preview"));
                }
                else if(embed_type.equals("video")) {
                    RelativeLayout embed_video = new RelativeLayout(context);
                    TextView embed_video_text = new TextView(context);
                    embed_video_text.setText("EMBED: video");
                    embed_video_text.setTextColor(0xFFA0A0A0);
                    embed_video.addView(embed_video_text);
                    embed.addView(embed_video);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setFooter() {
        try {
            int plus_count = data.getInt("vote_count");
            int comment_count = data.getInt("comment_count");
            boolean favorite = data.getBoolean("user_favorite");
            final boolean plus = false;


            int[] attrs = new int[] {android.R.attr.selectableItemBackground};
            TypedArray typedArray = context.obtainStyledAttributes(attrs);
            Drawable clickableBackground = typedArray.getDrawable(0);
            Drawable clickableBackground2 = typedArray.getDrawable(0);
            Drawable clickableBackground3 = typedArray.getDrawable(0);
            Drawable clickableBackground4 = typedArray.getDrawable(0);

            LinearLayout.LayoutParams params_footer_icon = new LinearLayout.LayoutParams(GlobalFunctions.dpToPx(20), GlobalFunctions.dpToPx(20));

            // FOOTER

            footer.setBackgroundResource(R.drawable.border_top);
            footer.setPadding(GlobalFunctions.dpToPx(3), 0, GlobalFunctions.dpToPx(10), 0);


            // FOOTER LEFT

            final LinearLayout footer_left = new LinearLayout(context);
            RelativeLayout.LayoutParams params_footer_left = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params_footer_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params_footer_left.addRule(RelativeLayout.CENTER_VERTICAL);
            footer_left.setLayoutParams(params_footer_left);
            footer.addView(footer_left);


            final LinearLayout footer_popup = new LinearLayout(context);
            LinearLayout.LayoutParams params_footer_popup = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            footer_popup.setLayoutParams(params_footer_popup);
            footer_popup.setGravity(Gravity.CENTER_VERTICAL);
            footer_popup.setClickable(true);
            footer_popup.setBackgroundDrawable(clickableBackground4);
            footer_popup.setPadding(GlobalFunctions.dpToPx(5), GlobalFunctions.dpToPx(6), GlobalFunctions.dpToPx(5), GlobalFunctions.dpToPx(5));
            ImageView footer_popup_icon = new ImageView(context);
            footer_popup_icon.setImageResource(R.drawable.entry_footer_popup);
            footer_popup_icon.setColorFilter(Colors.getEntryIconColor());
            footer_popup_icon.setLayoutParams(params_footer_icon);
            footer_popup.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, footer_popup);
                    popupMenu.getMenuInflater().inflate(R.menu.entry_author, popupMenu.getMenu());
                    popupMenu.show();
                }
            });
            footer_popup.addView(footer_popup_icon);


            footer_left.addView(footer_popup);

            // FOOTER RIGHT

            LinearLayout footer_right = new LinearLayout(context);
            RelativeLayout.LayoutParams params_footer_right = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params_footer_right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            footer_right.setLayoutParams(params_footer_right);
            footer.addView(footer_right);


            final LinearLayout footer_pluses = new LinearLayout(context);
            footer_pluses.setGravity(Gravity.CENTER_VERTICAL);
            footer_pluses.setClickable(true);
            footer_pluses.setBackgroundDrawable(clickableBackground);
            footer_pluses.setPadding(GlobalFunctions.dpToPx(5), GlobalFunctions.dpToPx(6), GlobalFunctions.dpToPx(5), GlobalFunctions.dpToPx(5));
            final ImageView footer_pluses_icon = new ImageView(context);
            footer_pluses_icon.setImageResource(R.drawable.entry_footer_plus);
            footer_pluses_icon.setColorFilter(Colors.getEntryIconColor());
            footer_pluses_icon.setLayoutParams(params_footer_icon);
            final TextView footer_pluses_count = new TextView(context);
            footer_pluses_count.setText(String.valueOf(plus_count));
            footer_pluses_count.setTextColor(Colors.getEntryIconColor());
            footer_pluses_count.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            footer_pluses_count.setPadding(0,0,GlobalFunctions.dpToPx(3),0);
            footer_pluses.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(plus) {
                        footer_pluses.setBackgroundColor(0x003B915F);
                        footer_pluses_icon.setColorFilter(Colors.getEntryIconColor());
                        footer_pluses_count.setTextColor(Colors.getEntryIconColor());
                    }
                    else {
                        footer_pluses.setBackgroundColor(0x44A2A2A2);
                        /*footer_pluses_icon.setColorFilter(0xFFFFFFFF);
                        footer_pluses_count.setTextColor(0xFFFFFFFF);*/
                        //plus = true;
                        footer_pluses_icon.setImageResource(R.drawable.entry_footer_plus_bold);
                        footer_pluses_icon.setColorFilter(0xFF3B915F);
                    }
                }
            });
            footer_pluses.addView(footer_pluses_icon);
            footer_pluses.addView(footer_pluses_count);


            LinearLayout.LayoutParams params_footer_comments = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params_footer_comments.setMargins(0,0,GlobalFunctions.dpToPx(2),0);
            LinearLayout footer_comments = new LinearLayout(context);
            footer_comments.setGravity(Gravity.CENTER_VERTICAL);
            footer_comments.setClickable(true);
            footer_comments.setBackgroundDrawable(clickableBackground2);
            footer_comments.setPadding(GlobalFunctions.dpToPx(5), GlobalFunctions.dpToPx(6), GlobalFunctions.dpToPx(5), GlobalFunctions.dpToPx(5));
            footer_comments.setLayoutParams(params_footer_comments);
            ImageView footer_comments_icon = new ImageView(context);
            footer_comments_icon.setImageResource(R.drawable.entry_footer_comment);
            footer_comments_icon.setColorFilter(Colors.getEntryIconColor());
            footer_comments_icon.setLayoutParams(params_footer_icon);
            TextView footer_comments_count = new TextView(context);
            footer_comments_count.setText(String.valueOf(comment_count));
            footer_comments_count.setTextColor(Colors.getEntryIconColor());
            footer_comments_count.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            footer_comments_count.setPadding(GlobalFunctions.dpToPx(3),0,GlobalFunctions.dpToPx(3),0);
            if(view_as_single_post == false) {
                footer_comments.setOnClickListener(new OnClickWithInt(post_id) {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, EntryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id", String.valueOf(param_int));
                        context.startActivity(intent);
                    }
                });
            }
            footer_comments.addView(footer_comments_icon);
            footer_comments.addView(footer_comments_count);


            LinearLayout.LayoutParams params_footer_favorite = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params_footer_favorite.setMargins(0,0,GlobalFunctions.dpToPx(7),0);
            final LinearLayout footer_favorite = new LinearLayout(context);
            footer_favorite.setGravity(Gravity.CENTER_VERTICAL);
            footer_favorite.setClickable(true);
            footer_favorite.setBackgroundDrawable(clickableBackground3);
            footer_favorite.setPadding(GlobalFunctions.dpToPx(8), GlobalFunctions.dpToPx(5), GlobalFunctions.dpToPx(8), GlobalFunctions.dpToPx(5));
            footer_favorite.setLayoutParams(params_footer_favorite);
            final ImageView footer_favorite_icon = new ImageView(context);
            footer_favorite_icon.setImageResource(R.drawable.entry_footer_favorite);
            footer_favorite_icon.setColorFilter(Colors.getEntryIconColor());
            footer_favorite_icon.setLayoutParams(params_footer_icon);
            footer_favorite.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    footer_favorite.setBackgroundColor(0x44A2A2A2);
                    footer_favorite_icon.setImageResource(R.drawable.entry_footer_favorite_added);
                }
            });
            footer_favorite.addView(footer_favorite_icon);



            footer_right.setGravity(Gravity.RIGHT);
            footer_right.addView(footer_favorite);
            footer_right.addView(footer_comments);
            footer_right.addView(footer_pluses);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
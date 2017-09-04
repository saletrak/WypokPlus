package pl.saletrak.wypokplus.layout_elements;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import pl.saletrak.wypokplus.R;
import pl.saletrak.wypokplus.activities.EntryActivity;
import pl.saletrak.wypokplus.activities.MainActivity;
import pl.saletrak.wypokplus.activities.UserActivity;
import pl.saletrak.wypokplus.resource.Colors;
import pl.saletrak.wypokplus.resource.DownloadImageTask;

public class EntryAdapter extends PostAdapter<EntryData> {
    public EntryAdapter(Context context, List<EntryData> objects) {
        super(context, R.layout.entry, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EntryDataHolder holder = null;


        if(row == null) {
            Log.d("dbg_adapter", "getView new");

            LayoutInflater inflater = MainActivity.this_activity.getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);

            holder = new EntryDataHolder();
            holder.pluses = (TextView) row.findViewById(R.id.pluses);
            holder.author = (TextView) row.findViewById(R.id.author);
            holder.time = (TextView) row.findViewById(R.id.time);
            holder.avatar = (ImageView) row.findViewById(R.id.avatar);
            holder.sex = (ImageView) row.findViewById(R.id.sex);

            holder.body = (TextView) row.findViewById(R.id.body);
            holder.embed_progressBar = (ProgressBar) row.findViewById(R.id.embed_progressBar);
            holder.embed_image = (ImageView) row.findViewById(R.id.embed_image);

            holder.footer_popup = (LinearLayout) row.findViewById(R.id.footer_popup);
            holder.footer_popup_icon = (ImageView) row.findViewById(R.id.footer_popup_icon);
            holder.footer_favorite = (LinearLayout) row.findViewById(R.id.footer_favorite);
            holder.footer_favorite_icon = (ImageView) row.findViewById(R.id.footer_favorite_icon);
            holder.footer_comments = (LinearLayout) row.findViewById(R.id.footer_comments);
            holder.footer_comments_icon = (ImageView) row.findViewById(R.id.footer_comments_icon);
            holder.footer_comments_count = (TextView) row.findViewById(R.id.footer_comments_count);
            holder.footer_pluses = (LinearLayout) row.findViewById(R.id.footer_pluses);
            holder.footer_pluses_icon = (ImageView) row.findViewById(R.id.footer_pluses_icon);
            holder.footer_pluses_count = (TextView) row.findViewById(R.id.footer_pluses_count);

            row.setTag(holder);
        }
        else {
            Log.d("dbg_adapter", "getView old");
            holder = (EntryDataHolder) row.getTag();
        }

        final EntryDataHolder holder_final = holder;

        EntryData entryData = post_data_map.get(position);

        // HEADER

        View.OnClickListener author_activity = new OnClickWithString(entryData.author) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("user", param_string);
                context.startActivity(intent);
            }
        };

        holder.pluses.setText("+ "+String.valueOf(entryData.vote_count));

        holder.author.setText(entryData.author);
        holder.author.setTextColor(Colors.getUserNickColor(entryData.author_group));
        holder.author.setOnClickListener(author_activity);

        holder.time.setText(entryData.time);

        new DownloadImageTask(holder.avatar).execute(entryData.avatar);
        holder.avatar.setOnClickListener(author_activity);

        if(!entryData.sex.equals(null)) {
            if(entryData.sex.equals("male")) holder.sex.setImageResource(R.drawable.ic_sex_male);
            else if(entryData.sex.equals("female")) holder.sex.setImageResource(R.drawable.ic_sex_female);
        }
        else holder.sex.setVisibility(View.GONE);


        // BODY

        if(!entryData.body.equals("")) {
            Spannable html = (Spannable) Html.fromHtml(entryData.body, null, new HtmlTagHandler());
            Spannable html_links = setLinks(html);
            holder.body.setText(html_links);
            holder.body.setMovementMethod(LinkMovementMethod.getInstance());
        }
        else holder.body.setVisibility(View.GONE);


        // EMBED

        if(!entryData.equals(null)) {
            new DownloadImageTask(holder.embed_image, holder.embed_progressBar).execute(entryData.embed_preview);
        }


        // FOOTER

        holder.footer_popup_icon.setColorFilter(Colors.getEntryIconColor());
        holder.footer_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder_final.footer_popup);
                popupMenu.getMenuInflater().inflate(R.menu.entry_author, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        holder.footer_favorite_icon.setColorFilter(Colors.getEntryIconColor());
        holder.footer_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder_final.footer_favorite.setBackgroundColor(0x44A2A2A2);
                holder_final.footer_favorite_icon.setImageResource(R.drawable.entry_footer_favorite_added);
            }
        });

        holder.footer_comments_icon.setColorFilter(Colors.getEntryIconColor());
        holder.footer_comments_count.setText(String.valueOf(entryData.comment_count));
        holder.footer_comments.setOnClickListener(new OnClickWithInt(entryData.id) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EntryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", String.valueOf(param_int));
                context.startActivity(intent);
            }
        });

        holder.footer_pluses_icon.setColorFilter(Colors.getEntryIconColor());
        holder.footer_pluses_count.setText(String.valueOf(entryData.vote_count));
        holder.footer_pluses.setOnClickListener(new OnClickWithInt(entryData.id) {
            @Override
            public void onClick(View v) {
                holder_final.footer_pluses.setBackgroundColor(0x44A2A2A2);
                holder_final.footer_pluses_icon.setImageResource(R.drawable.entry_footer_plus_bold);
                holder_final.footer_pluses_icon.setColorFilter(0xFF3B915F);
            }
        });


        return row;
    }

    class EntryDataHolder {
        TextView pluses;
        TextView author;
        TextView time;
        ImageView avatar;
        ImageView sex;

        TextView body;
        ProgressBar embed_progressBar;
        ImageView embed_image;

        LinearLayout footer_popup;
        ImageView footer_popup_icon;
        LinearLayout footer_favorite;
        ImageView footer_favorite_icon;
        LinearLayout footer_comments;
        ImageView footer_comments_icon;
        TextView footer_comments_count;
        LinearLayout footer_pluses;
        ImageView footer_pluses_icon;
        TextView footer_pluses_count;
    }
}

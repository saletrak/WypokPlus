package pl.saletrak.wypokplus.layout_elements;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import pl.saletrak.wypokplus.activities.LoginActivity;
import pl.saletrak.wypokplus.activities.MainActivity;
import pl.saletrak.wypokplus.resource.DownloadImageTask;
import pl.saletrak.wypokplus.resource.UserData;

public class Navigation extends NavigationView {

    public static final int NOTIFY_NOTIFICATION = 1;
    public static final int NOTIFY_TAGS = 2;
    public static final int NOTIFY_MESSAGES = 3;

    public Navigation(Context context) {
        super(context);
    }

    public Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Navigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    View navi_header;

    public void setNaviHeader() {
        if(UserData.getLogged()) {
            this.inflateHeaderView(pl.saletrak.wypokplus.R.layout.navi_header_logged);
            refreshNaviHeader();
        }
        else {
            this.inflateHeaderView(pl.saletrak.wypokplus.R.layout.navi_header_default);
            navi_header = this.getHeaderView(0);
            Button login_button = (Button) navi_header.findViewById(pl.saletrak.wypokplus.R.id.login_button);
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_login = new Intent(MainActivity.getContext(), LoginActivity.class);
                    MainActivity.this_activity.startActivity(intent_login);
                }
            });
        }
    }

    public void refreshNaviHeader() {
        navi_header = this.getHeaderView(0);
        TextView user_name = (TextView) navi_header.findViewById(pl.saletrak.wypokplus.R.id.user_name);
        user_name.setText(UserData.getUserName());
        TextView user_link = (TextView) navi_header.findViewById(pl.saletrak.wypokplus.R.id.user_link);
        user_link.setText("wykop.pl/ludzie/"+UserData.getUserName());

        ImageView avatar = (ImageView) navi_header.findViewById(pl.saletrak.wypokplus.R.id.avatar);
        avatar.setImageResource(pl.saletrak.wypokplus.R.drawable.entry_avatar_default);

        setAvatar(avatar);
        setSex(navi_header);
    }

    private void setAvatar(ImageView imageView) {
        try {
            new DownloadImageTask(imageView).execute(UserData.getUserData().getString("avatar_q150"));
            Log.d("dbg_main", "avatar set");
        }
        catch (JSONException e) {
            Log.d("dbg_avatar", e.getMessage());
        }
    }

    private void setSex(View navi_header) {
        ImageView sex_image = (ImageView) navi_header.findViewById(pl.saletrak.wypokplus.R.id.user_sex);
        try {
            if(UserData.getSex().equals("male")) {
                sex_image.setImageResource(pl.saletrak.wypokplus.R.drawable.ic_sex_male);
            }
            else if(UserData.getSex().equals("famale")) {
                sex_image.setImageResource(pl.saletrak.wypokplus.R.drawable.ic_sex_female);
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setVisibilityNaviItems() {
        Menu nav_menu = this.getMenu();
        if(!UserData.getLogged()) {
            nav_menu.findItem(pl.saletrak.wypokplus.R.id.logout).setVisible(false);
            nav_menu.findItem(pl.saletrak.wypokplus.R.id.user).setVisible(false);
            nav_menu.findItem(pl.saletrak.wypokplus.R.id.moj_wykop).setVisible(false);
        }
        else {
            nav_menu.findItem(pl.saletrak.wypokplus.R.id.login).setVisible(false);
        }
    }

    public void setNotifyCount(int type, int count) {
        TextView notify_count = null;
        if(type == 1) {
            notify_count = (TextView) navi_header.findViewById(pl.saletrak.wypokplus.R.id.notifications_count);
        }
        else if(type == 2) {
            notify_count = (TextView) navi_header.findViewById(pl.saletrak.wypokplus.R.id.tags_count);
        }
        else if(type == 3) {
            notify_count = (TextView) navi_header.findViewById(pl.saletrak.wypokplus.R.id.messages_count);
        }

        if(notify_count != null) {
            notify_count.setVisibility(VISIBLE);
            if(count < 100 && count > 0) {
                notify_count.setText(String.valueOf(count));
            }
            else if(count >= 100) {
                notify_count.setText("+");
            }
            else {
                notify_count.setVisibility(GONE);
                notify_count.setText("0");
            }
        }

    }
}

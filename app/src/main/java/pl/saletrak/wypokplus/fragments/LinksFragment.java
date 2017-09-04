package pl.saletrak.wypokplus.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import pl.saletrak.wypokplus.activities.MainActivity;
import pl.saletrak.wypokplus.resource.DownloadImageTask;
import pl.saletrak.wypokplus.layout_elements.Navigation;
import pl.saletrak.wypokplus.resource.UserData;
import pl.saletrak.wypokplus.R;

public class LinksFragment extends Fragment {

    public LinksFragment() {
        //
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_links, container, false);

        ImageView image_view = (ImageView) rootView.findViewById(R.id.imageView_links);

        try {
            new DownloadImageTask(image_view).execute(UserData.getUserData().getString("avatar_q150"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final TextView json_data_text = (TextView) rootView.findViewById(R.id.json_user_data);
        final TextView json_sesstion_text = (TextView) rootView.findViewById(R.id.json_user_session);
        Button json_user_reload = (Button) rootView.findViewById(R.id.json_user_reload);
        final Animation slide_up = AnimationUtils.loadAnimation(MainActivity.getContext(), R.anim.slide_up);

        json_data_text.setText(UserData.getUserData().toString());
        json_sesstion_text.setText(UserData.getUserSession().toString());
        json_user_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                json_data_text.setText("");
                json_sesstion_text.setText("");
                json_data_text.setText(UserData.getUserData().toString());
                json_sesstion_text.setText(UserData.getUserSession().toString());
                MainActivity.this_activity.navigation.setNotifyCount(Navigation.NOTIFY_MESSAGES, 2);
                json_data_text.startAnimation(slide_up);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

}

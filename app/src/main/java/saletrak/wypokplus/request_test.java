package saletrak.wypokplus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class request_test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test);

        Request request = new Request(this);

        TextView string_view = (TextView) findViewById(R.id.string_view);
        string_view.setText(request.odp);

    }
}

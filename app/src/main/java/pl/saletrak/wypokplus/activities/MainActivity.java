package pl.saletrak.wypokplus.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import pl.saletrak.wypokplus.fragments.LinksFragment;
import pl.saletrak.wypokplus.fragments.MicroblogFragment;
import pl.saletrak.wypokplus.layout_elements.Navigation;
import pl.saletrak.wypokplus.R;
import pl.saletrak.wypokplus.resource.UserData;
import pl.saletrak.wypokplus.fragments.UserFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    private static Context context;
    UserData userData;
    public static MainActivity this_activity;
    //private NavigationView navigationView;
    public Navigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*SharedPreferences preferences = getSharedPreferences("pref_nightTheme", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("pref_nightTheme", false);
        if(useDarkTheme) {
            setTheme(R.style.AppTheme);
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        this_activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userData();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigation = (Navigation) findViewById(R.id.nav_view);
        navigation.getMenu().getItem(0).setChecked(true);
        navigation.setNaviHeader();
        navigation.setVisibilityNaviItems();
        navigation.setNavigationItemSelectedListener(this);
        setContent(new LinksFragment());

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.links) {
            setContent(new LinksFragment());
        }
        else if(id == R.id.microblog) {
            setContent(new MicroblogFragment());
        }
        else if(id == R.id.moj_wykop) {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.fragment_container), "elo", Snackbar.LENGTH_INDEFINITE);
            mySnackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                }
            });
            mySnackbar.show();
        }
        else if(id == R.id.profile) {
            setContent(new UserFragment());
        }
        else if(id == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.about) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Wyloguj się")
                    .setMessage("Czy na pewno chcesz się wylogować?")
                    .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            UserData.logout();
                            finish();
                            startActivity(getIntent());
                        }
                    })
                    .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setCancelable(true)
                    .show();
        }
        else if(id == R.id.login) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Czy na pewno chcesz zamknąć aplikację?")
                    .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("NIE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setCancelable(true)
                    .show();
        }
    }

    public static Context getContext() {
        return context;
    }

    private void setContent(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void userData() {
        userData = new UserData();
        if(UserData.getLogged()) UserData.setSession();

        //Log.d("dbg_main", String.valueOf(userData.getLogged()));
    }




}

package pl.saletrak.wypokplus.resource;

import pl.saletrak.wypokplus.activities.MainActivity;

public class Colors {

    //public static boolean night_theme = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).getBoolean("pref_nightTheme", true);
    public static boolean night_theme = true;

    public static int getTheme() {
        if(night_theme) {
            return MainActivity.getContext().getResources().getIdentifier("AppThemeDark", "style", MainActivity.getContext().getPackageName());
        }
        else {
            return MainActivity.getContext().getResources().getIdentifier("AppTheme", "style", MainActivity.getContext().getPackageName());
        }
    }

    public static int getBodyBackgroundColor() {
        if(night_theme) {
            return 0xFF242424;
        }
        else {
            return 0xFFFFFFFF;
        }
    }

    public static int getUserNickColor(int group) {
        if(group == 0) return 0xFF339933; //zielony
        else if(group == 1) return 0xFFFF5917; //pomarancz
        else if(group == 2) return 0xFFBB0000; //bordo
        else if(group == 5) return 0xFF000000; //admin
        else if(group == 1001) return 0xFF999999; //banned
        else if(group == 1002) return 0xFF525252; //deleted
        else if(group == 2001) return 0xFF3F6FA0; //klient
        return 0;
    }

    public static int getEntryIconColor() {
        if(night_theme) {
            //return 0xFFFFFFFF;
            return 0xFFA0A0A0;
        }
        else {
            return 0xFF54585A;
        }
    }

    public static int getTextColor() {
        if(night_theme) {
            return 0xFFFFFFFF;
        }
        else {
            return 0xEE363636;
        }
    }
}

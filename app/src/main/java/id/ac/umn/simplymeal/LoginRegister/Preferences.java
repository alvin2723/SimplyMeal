package id.ac.umn.simplymeal.LoginRegister;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
//    static final String KEY_USER_TEREGISTER ="usernm",KEY_EMAIL_TEREGISTER = "email", KEY_PASS_TEREGISTER ="pass";
    static final String KEY_USERNAME_SEDANG_LOGIN = "Username_logged_in";
    static final String KEY_EMAIL_SEDANG_LOGIN = "Email_logged_in";
    static final String KEY_STATUS_SEDANG_LOGIN = "Status_logged_in";
    static final String KEY_ADDRESS_SEDANG_LOGIN = "Address_logged_in";
    static final String KEY_NAMA_SEDANG_LOGIN = "Nama_logged_in";
    static final String KEY_MOBILE_SEDANG_LOGIN = "Mobile_logged_in";
    static final String KEY_DOB_SEDANG_LOGIN = "Dob_logged_in";
    static final String KEY_GEN_SEDANG_LOGIN = "Gen_logged_in";
    static final String KEY_IDMENU_SEDANG_LOGIN = "IdMenu_logged_in";
    static final String KEY_TRANS_SEDANG_LOGIN = "Trans_logged_in";
    static final String KEY_IDCART_SEDANG_LOGIN = "Cart_logged_in";

    private static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedInIdCart(Context context,String id_cart){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_IDCART_SEDANG_LOGIN, id_cart);
        editor.apply();
    }
    public static String getLoggedInIdCart(Context context){
        return getSharedPreference(context).getString(KEY_IDCART_SEDANG_LOGIN ,"");
    }

    public static void setLoggedInTrans(Context context,String trans_num){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_TRANS_SEDANG_LOGIN, trans_num);
        editor.apply();
    }

    public static void setLoggedInIdMenu(Context context,String rate){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();

        editor.putString(KEY_IDMENU_SEDANG_LOGIN , rate);
        editor.apply();
    }
    public static String getLoggedInIdMenu(Context context){
        return getSharedPreference(context).getString(KEY_IDMENU_SEDANG_LOGIN ,"");
    }
    public static String getLoggedInTrans(Context context){
        return getSharedPreference(context).getString(KEY_TRANS_SEDANG_LOGIN,"");
    }

    public static void setLoggedInMobileUser(Context context,String mobile){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();

        editor.putString(KEY_MOBILE_SEDANG_LOGIN, mobile);
        editor.apply();
    }
    public static String getLoggedInMobileUser(Context context){
        return getSharedPreference(context).getString(KEY_MOBILE_SEDANG_LOGIN,"");
    }

    public static void setLoggedInDateBirthUser(Context context,String dob){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();

        editor.putString(KEY_DOB_SEDANG_LOGIN, dob);
        editor.apply();
    }
    public static String getLoggedInDateBirthUser(Context context){
        return getSharedPreference(context).getString(KEY_DOB_SEDANG_LOGIN,"");
    }

    public static void setLoggedInGenderUser(Context context,String gender){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();

        editor.putString(KEY_GEN_SEDANG_LOGIN, gender);
        editor.apply();
    }
    public static String getLoggedInGenderUser(Context context){
        return getSharedPreference(context).getString(KEY_GEN_SEDANG_LOGIN,"");
    }

    public static void setLoggedInNamaLengkapUser(Context context,String nama){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();

        editor.putString(KEY_NAMA_SEDANG_LOGIN, nama);
        editor.apply();
    }
    public static String getLoggedInNamaLengkapUser(Context context){
        return getSharedPreference(context).getString(KEY_NAMA_SEDANG_LOGIN,"");
    }

    public static void setLoggedInAddress(Context context,String address){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();

        editor.putString(KEY_ADDRESS_SEDANG_LOGIN, address);
        editor.apply();
    }
    public static String getLoggedInAddress(Context context){
        return getSharedPreference(context).getString(KEY_ADDRESS_SEDANG_LOGIN,"");
    }

    public static void setLoggedInEmail(Context context, String email){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_EMAIL_SEDANG_LOGIN, email);
        editor.apply();
    }

    public static String getLoggedInEmail(Context context){
        return getSharedPreference(context).getString(KEY_EMAIL_SEDANG_LOGIN,"");
    }

    public static void setLoggedInStatus(Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_STATUS_SEDANG_LOGIN,status);
        editor.apply();
    }
    public static boolean getLoggedInStatus(Context context){
        return getSharedPreference(context).getBoolean(KEY_STATUS_SEDANG_LOGIN,false);
    }

    public static void clearLoggedInUser (Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USERNAME_SEDANG_LOGIN);
        editor.remove(KEY_EMAIL_SEDANG_LOGIN);
        editor.remove(KEY_STATUS_SEDANG_LOGIN);
        editor.remove(KEY_ADDRESS_SEDANG_LOGIN);
        editor.remove(KEY_NAMA_SEDANG_LOGIN);
        editor.remove(KEY_MOBILE_SEDANG_LOGIN);
        editor.remove(KEY_DOB_SEDANG_LOGIN);
        editor.remove(KEY_GEN_SEDANG_LOGIN);
        editor.remove(KEY_TRANS_SEDANG_LOGIN);
        editor.remove(KEY_IDMENU_SEDANG_LOGIN);
        editor.apply();
    }
}

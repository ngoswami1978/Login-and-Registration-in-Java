package com.neerajweb.myfirstapp.dao;

import android.content.SharedPreferences;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Admin on 20/08/2015.
 */
public class SettingsService {
    public static final String TAG = "SettingsService";
    private static SharedPreferences preferences;
    private static String PREF_NAME = "MyFirstAppApartment";

    //=====DON'T MODIFY THESE VALUES AFTER PUBLISHING APP! =========================|
    public String strLoginName;
    public String strLoginPassword;
    String seedValue = "This Is MySecure";
    //==============================================================================|

    public static SharedPreferences SettingsService(Context context) {
        preferences=context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getLoginUserName(){
        return strLoginName ;
    }
    public void setLoginUserName(String mUserName)
    {
        this.strLoginName = mUserName;
    }

    public String getLoginPassword(){
        return strLoginPassword ;
    }
    public void setLoginPassword(String mPassword)
    {
        this.strLoginPassword = mPassword;
    }

    // Generic method in Java
    public <T> void AddOrUpdateSetting  (Context context, String key, T value) throws Exception {
        try
        {
            if (key.equals( null ) || key.equals(""))
            {
                throw new IllegalArgumentException (key);
            }

            SharedPreferences.Editor myeditor = preferences.edit();
            String mEncritp = encrypt (seedValue,value.toString());
            myeditor.putString(key, mEncritp);
            myeditor.apply();
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error in SettingService method : " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Generic method in Java
    public <T> T GetSetting (Context context, String key,T value) throws Exception
    {
        String strDecrypt= String.valueOf("");
        try
        {
            if (key.equals( null ) || key.equals(""))
            {
                throw new IllegalArgumentException (key);
            }
            if (!preferences.contains(key))
            {
                if (value != null) {
                    AddOrUpdateSetting(context, key, value);
                    return value;
                }
            }
            strDecrypt = decrypt(seedValue,preferences.getString(key, String.valueOf("")));
        }catch (Exception e)
        {
            Log.d(TAG, "Error in SettingService method : " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //return (T) decrypt(seedValue,preferences.getString(key, String.valueOf("")));
        return (T) strDecrypt;
    }

    public static String encrypt(String seed,String clearText) {
        byte[] encryptedText = null;
        try {
            byte[] keyData = seed.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, ks);
            encryptedText = c.doFinal(clearText.getBytes("UTF-8"));
            return Base64.encodeToString(encryptedText, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt (String seed,String encryptedText) {
        byte[] clearText = null;
        try {
            byte[] keyData = seed.getBytes();
            SecretKey ks = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, ks);
            clearText = c.doFinal(Base64.decode(encryptedText, Base64.DEFAULT));
            return new String(clearText, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }
}

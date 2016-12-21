package com.eusecom.saminveantory;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;



@SuppressWarnings("deprecation")
public class SettingsActivity extends android.preference.PreferenceActivity {
	

	public static final String SERVER_NAME = "servername";	
	public static final String USER_ID = "userid";
	public static final String USER_PSW = "userpsw";
	public static final String USER_NAME = "username";
	public static final String NICK_NAME = "nickname";
	public static final String FIR = "fir";
	public static final String FIRROK = "firrok";

	public static final String FTPSERVER = "ftpserver";
	public static final String FTPUSER = "ftpuser";
	public static final String FTPPSW = "ftppsw";
	public static final String FTPQR = "ftpqr";
	
	public static final String NOSTNUM = "nostnum";
	public static final String NOSTNAME = "nostname";
	public static final String NOSTPRICE = "nostprice";
	public static final String NOSTVOL = "nostvol";
	public static final String NOSTMER = "nostmer";

	public static String getNostnum(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(NOSTNUM, "1");
	}
	
	public static String getNostname(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(NOSTNAME, "item");
	}
	
	public static String getNostprice(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(NOSTPRICE, "0");
	}
	
	public static String getNostvol(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(NOSTVOL, "1");
	}
	
	public static String getNostmer(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(NOSTMER, "ks");
	}
	
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.settings);
	}

	
	public static String getServerName(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(SERVER_NAME, "");
	}

	public static String getNickName(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(NICK_NAME, "");
	}

	public static String getUserId(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_ID, "0");
	}

	public static String getFir(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIR, "0");
	}

	public static String getFirrok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRROK, "0");
	}

	public static String getUserName(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_NAME, "");
	}
	
	public static String getUserPsw(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_PSW, "");
	}
	
	public static String getFtpserver(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FTPSERVER, "82.208.46.20");
	}
	
	public static String getFtpuser(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FTPUSER, "ws453199");
	}

	public static String getFtppsw(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FTPPSW, "heslo");
	}

	public static String getFtpqr(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FTPQR, "");
	}
} 
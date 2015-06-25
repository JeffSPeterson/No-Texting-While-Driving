package com.no_texting_while_driving;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;

public class BackgroundService extends Service {

    //Shared Prefs for storing values in phone
    public static final String PREFS_NAME = "OnOffPrefs";
    public static final String PREFS_REG = "RegistrationPrefs";

    //final Uri CONTENT_URI = Uri.parse("content://sms/sent");
    private Handler smshandler = null;
    private ContentObserver smsObserver = null;
    public boolean monitorStatus = false;
    private ContentResolver contentResolver = null;
    int smsCount = 0;
    public String smsNumber = "";
    private String provider;
    Timer timer;

    SharedPreferences settingPrefs;
    SharedPreferences settingPrefsREG;
    String PhoneNumber;


    private LocationManager locationManager;
    String SpeedMPH;
    int MPH;
    private final double mphConversion = 2.23694;
    private int speed;
    private double latitude;
    private double longitude;
    LocationListener loc_listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "No Text onCreate Service Created", Toast.LENGTH_LONG).show();
        settingPrefs = getSharedPreferences(PREFS_NAME, 0);
        settingPrefsREG = getSharedPreferences(PREFS_REG, 0);
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);

        Toast.makeText(this, "No Text onStart Service Started", Toast.LENGTH_LONG).show();

        PhoneNumber = settingPrefsREG.getString("PHONE_NUMBER", "");
        SpeedMPH = settingPrefs.getString("MPH", "10");
        MPH = java.lang.Integer.parseInt(SpeedMPH);
        contentResolver = getContentResolver();
        smshandler = new SMSHandler();
        smsObserver = new SMSObserver(smshandler);
        Toast.makeText(this, "MPH Set To: " + MPH, Toast.LENGTH_LONG).show();

        loc_listener = new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // called when the provider status changes. Possible status: OUT_OF_SERVICE, TEMPORARILY_UNAVAILABLE or AVAILABLE.
            }

            public void onProviderEnabled(String provider) {
                // called when the provider is enabled by the user
            }

            public void onProviderDisabled(String provider) {
                // called when the provider is disabled by the user, if it's already disabled, it's called immediately after requestLocationUpdates
            }

            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                SpeedMPH = settingPrefs.getString("MPH", "10");
                MPH = java.lang.Integer.parseInt(SpeedMPH);
                // do whatever you want with the coordinates

                //location.getSpeed is in Meters per sec.
                speed = (int) (mphConversion * location.getSpeed());

                if (speed >= MPH) {
                    startSMSMonitoring();
                } else {
                    stopSMSMonitoring();
                }
            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc_listener);

    }//end onStart

    @Override
    public void onDestroy() {
        super.onDestroy();

        locationManager.removeUpdates(loc_listener);
        Toast.makeText(this, "No Text Service Destroyed", Toast.LENGTH_LONG).show();
    }//end onDestroy

    public void startSMSMonitoring() {
        try {
            monitorStatus = false;
            if (!monitorStatus) {
                contentResolver.registerContentObserver(Uri
                        .parse("content://sms"), true, smsObserver);
            }//end if
        } catch (Exception e) {
            Log.w("NO TEXTING", "SMSMonitor :: startSMSMonitoring Exception == " + e.getMessage());
        }//end catch
    }//end StartSMSMonitoring

    public void stopSMSMonitoring() {
        try {
            monitorStatus = false;
            if (!monitorStatus) {
                Log.w("NO TEXTING", "Exception :: Stop monitorStatus" + monitorStatus);
                contentResolver.unregisterContentObserver(smsObserver);
            }//end if
        } catch (Exception e) {
            Log.w("NO TEXTING", "SMSMonitor :: stopSMSMonitoring Exception == " + e.getMessage());
        }//end catch
    }//end stopSMSMonitoring

    class SMSHandler extends Handler {
        public void handleMessage(final Message msg) {
            Log.w("NO TEXTING", "SMSMonitor :: SMS Handler");
        }//end handlemessage
    }//end smshandler

    class SMSObserver extends ContentObserver {
        private Handler sms_handle = null;

        public SMSObserver(final Handler smshandle) {
            super(smshandle);
            sms_handle = smshandle;
            Log.w("NO TEXTING", "SMSMonitor :: SMSObserver");
        }//end smsObserver

        public void onChange(final boolean bSelfChange) {
            timer = new Timer();
            super.onChange(bSelfChange);
            Log.w("NO TEXTING", "SMSMonitor :: OnChange");
            Thread thread = new Thread() {
                public void run() {
                    try {
                        monitorStatus = true;

                        // Send message to Activity
                        Message msg = new Message();
                        sms_handle.sendMessage(msg);
                        Uri uriSMSURI = Uri.parse("content://sms");
                        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, "_id");

                        if (cur.getCount() != smsCount) {
                            smsCount = cur.getCount();

                            if (cur != null && cur.getCount() > 0) {
                                cur.moveToLast();
                                for (int i = 0; i < cur.getColumnCount(); i++) {
                                    //Log("NO TEXTING","SMSMonitor :: incoming Column Name : " +
                                    //cur.getColumnName(i));
                                    //cur.getString(i));
                                }//end for

                                smsNumber = cur.getString(cur.getColumnIndex("address"));
                                if (smsNumber == null || smsNumber.length() <= 0) {
                                    smsNumber = "Unknown";
                                    Log.w("NO TEXTING", "SMSMonitor :: Unknown smsNumber ");
                                }//end if

                                int type = Integer.parseInt(cur.getString(cur.getColumnIndex("type")));
                                String message = cur.getString(cur.getColumnIndex("body"));
                                Log.w("NO TEXTING", "SMSMonitor :: SMS type == " + type);
                                Log.w("NO TEXTING", "SMSMonitor :: Message Txt == " + message);
                                Log.w("NO TEXTING", "SMSMonitor :: Phone Number == " + smsNumber);

                                cur.close();

                                if (type == 1) {
                                    onSMSReceive(message, smsNumber);
                                } else {
                                    onSMSSend(message, smsNumber);
                                }//end else
                            }//end if
                        }//end if

                    } catch (Exception e) {
                        Log.w("NO TEXTING", "SMSMonitor :: onChange Exception == " + e.getMessage());
                    }//end catch
                }//end run

                private void onSMSSend(String message, String smsNumber) {

                    String Message = message;
                    String Number = smsNumber;
                    TelephonyManager mTelephonyMgr;
                    mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    Log.w("NO TEXTING", Number);
                    Log.w("NO TEXTING", Message);

                    if (message.startsWith("**")) {
                        Log.w("NO TEXTING", "**");
                    }//end if
                    else {
                        Log.w("NO TEXTING", Message);

                        SmsManager m = SmsManager.getDefault();
                        String destinationNumber = PhoneNumber;

                        Log.w("NO TEXTING", "Destination Number: " + destinationNumber);

                        String text = "** " + Message;
                        String MessageTONumber = Number;
                        String messageNotification1 = "** This is a Notification that " +
                                mTelephonyMgr.getLine1Number() + " has sent a text going over the set Speed of " +
                                MPH + "mph **";
                        String messageNotification2 = "** Message sent to " + MessageTONumber + " is below the set speed limit. **";

                        m.sendTextMessage(destinationNumber, null, messageNotification1, null, null);
                        // m.sendTextMessage(destinationNumber, null, messageNotification2, null, null);
                        m.sendTextMessage(destinationNumber, null, text, null, null);

                    }//end else

                }//end onSMSsend

                private void onSMSReceive(String message, String smsNumber) {

                    String Message = message;
                    String Number = smsNumber;

                }//end onSMSRecive
            };//end thread

            thread.start();

        }//end OnChange
    }//end smsObserver
}//end bacgroundservice 

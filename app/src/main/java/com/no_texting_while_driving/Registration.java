package com.no_texting_while_driving;

import com.no_texting_while_driving.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends Activity {
	public static final String PREFS_NAME = "RegistrationPrefs";
	
	EditText eTUserName;
	EditText eTPhoneNumber;
	EditText eTPassword;
	
	Button bSave;
	Button bBack;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		bSave = (Button) findViewById(R.id.button_Save);
		bBack = (Button) findViewById(R.id.button_BackToMain);


		eTUserName = (EditText) findViewById(R.id.editText_OwnerName);
		eTPhoneNumber = (EditText) findViewById(R.id.editText_OwnerPhone);
		eTPassword = (EditText) findViewById(R.id.editText_OwnerPassword);
		
		
		final SharedPreferences settingPrefs = getSharedPreferences(PREFS_NAME, 0);

    	
    	eTUserName.setText(settingPrefs.getString("USER_NAME", ""));
    	eTPassword.setText(settingPrefs.getString("PASSWORD", ""));
    	eTPhoneNumber.setText(settingPrefs.getString("PHONE_NUMBER", ""));
    	
    	String phone = settingPrefs.getString("PHONE_NUMBER", "");
    	
    	
    	 bSave.setOnClickListener(new View.OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 		    	SharedPreferences.Editor editor = settingPrefs.edit();
 				
 		    	editor.putString("USER_NAME", eTUserName.getText().toString());              
 		    	editor.putString("PASSWORD", eTPassword.getText().toString());   
 		    	editor.putString("PHONE_NUMBER", eTPhoneNumber.getText().toString()); 
 		    	
 		    	if(settingPrefs.getString("USER_NAME", "").equals("")){
 		    		editor.putBoolean("REGISTERED", false);
 		    	}
 		    	else
 		    	editor.putBoolean("REGISTERED", true);
 		    	
 		    	editor.commit();
 		    	
 		    	

 		       TelephonyManager mTelephonyMgr;
 		       mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
 		       
 		    	// mTelephonyMgr.getLine1Number() +
 		    	// AndroidManifest.xml must have the following permission:
 		    	// <uses-permission android:name="android.permission.SEND_SMS"/>
 		    	//SmsManager m = SmsManager.getDefault();
 		    	String destinationNumber = eTPhoneNumber.getText().toString(); //"0123456789";  
 		    	String text1 = "Hello, " + eTUserName.getText().toString() + ".  This is an automated response from this phone number: "
 		    			+ mTelephonyMgr.getLine1Number() +".";
 		    	String text2 = "This phone has been registered to your phone using the No Texting while Driving Application." 
 		    			+ " User Name: " + eTUserName.getText().toString() + ", Phone Number: " + eTPhoneNumber.getText().toString() 
 		    			+ ", Password: " + eTPassword.getText().toString();  
 		    	//m.sendTextMessage(destinationNumber, null, text1, null, null);
 		    //	m.sendTextMessage(destinationNumber, null, text2, null, null);
 		    	                              
                if (destinationNumber.length()>0 && text1.length()>0){                
                    sendSMS(destinationNumber, text1);
                    sendSMS(destinationNumber, text2);
                   // m.sendTextMessage(destinationNumber, null, text2, null, null);
                }
                else
                    Toast.makeText(getBaseContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();

 			}
 		});
    	
		 bBack.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					finish();
				}
			});
    	

	}
	
    //---sends a SMS message to another device---
    //  @TargetApi(4)
		private void sendSMS(String phoneNumber, String message)
      {      
      	/*
          PendingIntent pi = PendingIntent.getActivity(this, 0,
                  new Intent(this, test.class), 0);                
              SmsManager sms = SmsManager.getDefault();
              sms.sendTextMessage(phoneNumber, null, message, pi, null);        
          */
      	
      	String SENT = "SMS_SENT";
      	String DELIVERED = "SMS_DELIVERED";
      	
          PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
              new Intent(SENT), 0);
          
          PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
              new Intent(DELIVERED), 0);
      	
          //---when the SMS has been sent---
          registerReceiver(new BroadcastReceiver(){
  		@Override
          	public void onReceive(Context arg0, Intent arg1) {
  				switch (getResultCode())
  				{
  				    case Activity.RESULT_OK:
  					    Toast.makeText(getBaseContext(), "SMS sent", 
  					    		Toast.LENGTH_SHORT).show();
  					    break;
  				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
  					    Toast.makeText(getBaseContext(), "Generic failure", 
  					    		Toast.LENGTH_SHORT).show();
  					    break;
  				    case SmsManager.RESULT_ERROR_NO_SERVICE:
  					    Toast.makeText(getBaseContext(), "No service", 
  					    		Toast.LENGTH_SHORT).show();
  					    break;
  				    case SmsManager.RESULT_ERROR_NULL_PDU:
  					    Toast.makeText(getBaseContext(), "Null PDU", 
  					    		Toast.LENGTH_SHORT).show();
  					    break;
  				    case SmsManager.RESULT_ERROR_RADIO_OFF:
  					    Toast.makeText(getBaseContext(), "Radio off", 
  					    		Toast.LENGTH_SHORT).show();
  					    break;
  				}
  			}

          }, new IntentFilter(SENT));
          
          //---when the SMS has been delivered---
          registerReceiver(new BroadcastReceiver(){
  			@Override
  			public void onReceive(Context arg0, Intent arg1) {
  				switch (getResultCode())
  				{
  				    case Activity.RESULT_OK:
  					    Toast.makeText(getBaseContext(), "SMS delivered", 
  					    		Toast.LENGTH_SHORT).show();
  					    break;
  				    case Activity.RESULT_CANCELED:
  					    Toast.makeText(getBaseContext(), "SMS not delivered", 
  					    		Toast.LENGTH_SHORT).show();
  					    break;					    
  				}
  			}
          }, new IntentFilter(DELIVERED));        
      	
          SmsManager sms = SmsManager.getDefault();
          sms.sendTextMessage(phoneNumber, null, message, null, null);               
      }   


}

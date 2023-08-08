package com.schoofi.activitiess;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.activities.LoginScreen;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {

    private EditText oldPass,newPass,rePass;
    private ImageView back;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Change Password");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_change_password);

        oldPass = (EditText) findViewById(R.id.edit_user_old_pass);
        newPass = (EditText) findViewById(R.id.edit_userPass);
        rePass = (EditText) findViewById(R.id.edit_userRePass);
        back = (ImageView) findViewById(R.id.img_back);
        done = (Button) findViewById(R.id.btn_resetDone);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(oldPass.getText().toString().matches("") || newPass.getText().toString().matches("") || rePass.getText().toString().matches(""))
                {
                    Utils.showToast(getApplicationContext(),"Please fill all fields");
                }

                else
                    if (!newPass.getText().toString().matches(rePass.getText().toString()))
                    {
                        Utils.showToast(getApplicationContext(),"Passwords do not match");
                    }


                else
                {
                  postMessage();
                }
            }
        });
    }

    private void postMessage()
    {
        setSupportProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


        final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.CHANGE_PASSWORD+"?u_id="+Preferences.getInstance().userId+"&old_psw="+oldPass.getText().toString()+"&new_psw="+newPass.getText().toString()+"&token="+Preferences.getInstance().token+"&device_id="+Preferences.getInstance().deviceId;

        StringRequest requestObject = new StringRequest(Request.Method.GET,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                try
                {
                    responseObject = new JSONObject(response);
                    System.out.println(response.toString());
                    System.out.println(url1);
                    if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                    {

                        Utils.showToast(getApplicationContext(),"Password Changed Succesfully");
                        //Intent intent = new Intent(PasswordReset.this,LoginScreen.class);

//                        startActivity(intent);
                        finish();

                    }

                    else
                        if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("2"))
                        {
                            Utils.showToast(getApplicationContext(),"Old Password is incorrect");
                        }

                        else
                        if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                        {
                            Utils.showToast(getApplicationContext(),"Session expired please login again!!!");
                        }


                    else

                    {

                        Utils.showToast(getApplicationContext(),"Error Occured");
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                    Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                }
                setSupportProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utils.showToast(getApplicationContext(), "Error submitting alert! Please try after sometime.");
                setSupportProgressBarIndeterminateVisibility(false);
            }
        })
        {
			/*@Override
			protected Map<String,String> getParams(){

				Map<String,String> params = new HashMap<String, String>();

				params.put("mob",userMobile.getText().toString());
				params.put("email",userMobile.getText().toString());
				params.put("u_name", userName.getText().toString());
				params.put("dob", "2015-11-10");
				return params;
			}*/};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(Utils.isNetworkAvailable(this))
            queue.add(requestObject);
        else
        {
            Utils.showToast(this, "Unable to fetch data, kindly enable internet settings!");
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }
}

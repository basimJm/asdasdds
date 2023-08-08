package com.schoofi.activitiess;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.schoofi.adapters.AccountListViewAdapter;
import com.schoofi.adapters.DiaryMultiLevelListAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.DiarySubVO;
import com.schoofi.utils.DiaryVO;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AccountLinkingActivity extends AppCompatActivity {

    ListView accountLinkingListView;
    ImageView back;
    private JSONArray accountListViewArray;
    AccountListViewAdapter accountListViewAdapter;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("Account Linking Activity ");

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        setContentView(R.layout.activity_account_linking);

        back = (ImageView) findViewById(R.id.img_back);

        accountLinkingListView = (ListView) findViewById(R.id.listview_linking);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        callbackManager = CallbackManager.Factory.create();

        postLocalRegistration();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.v("LoginActivity", response.toString());
                                JSONObject data = response.getJSONObject();

                                // Application code
                                try {
                                    final String email;
                                    final String birthday;
                                    final String profilePicUrl;
                                    final String userId;
                                    final String gender;
                                    final String name1;

                                    if(object.getString("email").matches(""))
                                    {
                                        email = "";
                                    }
                                    else
                                    {
                                        email = object.getString("email");
                                    }

                                    if(object.getString("birthday").matches(""))
                                    {
                                        birthday = "";
                                    }
                                    else
                                    {
                                        birthday = object.getString("birthday");
                                    }

                                    if(object.getString("id").matches(""))
                                    {
                                        userId = "";
                                    }

                                    else
                                    {
                                        userId= object.getString("id");
                                    }

                                    if(data.getJSONObject("picture").getJSONObject("data").getString("url").matches(""))
                                    {
                                        profilePicUrl="";
                                    }

                                    else
                                    {
                                        profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    }

                                    if(object.getString("gender").matches(""))
                                    {
                                        gender = "";
                                    }

                                    else
                                    {
                                        gender = object.getString("gender");
                                    }

                                    if(object.getString("name").matches(""))
                                    {
                                        name1 = "";
                                    }

                                    else
                                    {
                                        name1= object.getString("name");
                                    }


                                    Log.d("email",email);
                                    Log.d("birthday",birthday);
                                    Log.d("url",profilePicUrl);
                                    RequestQueue queue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

                                    final ProgressDialog loading = ProgressDialog.show(AccountLinkingActivity.this, "Logging In", "Please wait...", false, false);


                                    final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.LINKING_SOCIAL_ACCOUNTS;

                                    StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            JSONObject responseObject;
                                            System.out.println(response);
                                            //Utils.showToast(getApplicationContext(), ""+response);
                                            // System.out.println(url1);
                                            try
                                            {
                                                responseObject = new JSONObject(response);

                                                if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("0"))
                                                {

                                                    Utils.showToast(AccountLinkingActivity.this,"Error in Linking Account");


                                                }
                                                else
                                                if(responseObject.has("error")&&responseObject.getString("error").equals("0"))
                                                {
                                                    Utils.showToast(AccountLinkingActivity.this, "Session Expired:Please Login Again");
                                                }

                                                else
                                                if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
                                                {

                                                    Utils.showToast(AccountLinkingActivity.this,"Account Linked Successfully");
                                                    finish();
                                                }


                                                else
                                                {
                                                    System.out.println("kkk");
                                                }
                                            }
                                            catch(JSONException e)
                                            {
                                                e.printStackTrace();
                                                loading.dismiss();
                                                //Utils.showToast(TeacherStudentAttendanceDetails.this, "Error submitting alert! Please try after sometime.");
                                                Toast.makeText(getApplicationContext(), "Error in Linking Account", Toast.LENGTH_LONG).show();
                                            }
                                            setProgressBarIndeterminateVisibility(false);

                                        }}, new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            Toast.makeText(getApplicationContext(), "Error in Linking Account", Toast.LENGTH_LONG).show();
                                            setProgressBarIndeterminateVisibility(false);
                                            loading.dismiss();
                                        }
                                    })
                                    {
                                        @Override
                                        protected Map<String,String> getParams(){

                                            Map<String,String> params = new HashMap<String, String>();
                                            Preferences.getInstance().loadPreference(AccountLinkingActivity.this);
                                            params.put("social_user_id",userId);
                                            params.put("social_email_id",email);
                                            params.put("linked_account","1");
                                            params.put("social_picture",profilePicUrl);
                                            params.put("schoofi_user_id",Preferences.getInstance().userId);
                                            params.put("token",Preferences.getInstance().token);
                                            params.put("device_id",Preferences.getInstance().deviceId);



                                            return params;
                                        }};

                                    requestObject.setRetryPolicy(new DefaultRetryPolicy(
                                            25000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                    queue.add(requestObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // Application code
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {



            }

            @Override
            public void onError(FacebookException e) {

            }
        });

        accountLinkingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                try {
                    if(accountListViewArray.getJSONObject(position).getString("social_platform").matches("Facebook"))
                    {
                        loginButton.performClick();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);



            callbackManager.onActivityResult(requestCode, resultCode, data);

    }



    protected void postLocalRegistration()
    {
        setProgressBarIndeterminateVisibility(true);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);


        final String url1 = AppConstants.SERVER_URLS.SERVER_URL+AppConstants.SERVER_URLS.SCHOOL_SPINNER_DIARY_URL;

        StringRequest requestObject = new StringRequest(Request.Method.POST,url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject responseObject;
                System.out.println(response);
                //Utils.showToast(getApplicationContext(), ""+response);
                // System.out.println(url1);


                try {
                    responseObject = new JSONObject(response);
                    if (responseObject.has("Msg") && responseObject.getString("Msg").equals("0"))
                        Utils.showToast(getApplicationContext(), "No Records Found");
                    else if (responseObject.has("error") && responseObject.getString("error").equals("0")) {
                        Utils.showToast(getApplicationContext(), "Session Expired:Please Login Again");
                    } else if (responseObject.has("responseObject")) {
                        loading.dismiss();

                        accountListViewArray = new JSONObject(response).getJSONArray("responseObject");
                        // profileUrl = schoolDiaryArray.getJSONObject(0).getString("school_banner");



                        if(null!=accountListViewArray && accountListViewArray.length()>=0) {
                            Cache.Entry e = new Cache.Entry();
                            e.data = accountListViewArray.toString().getBytes();

                            VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().put(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.SCHOOL_SPINNER_DIARY_URL + "?value="+"linked_account"+"&user_id="+Preferences.getInstance().userId, e);
                            accountLinkingListView.invalidateViews();





                            accountListViewAdapter= new AccountListViewAdapter(getApplicationContext(),accountListViewArray);
                            accountLinkingListView.setAdapter(accountListViewAdapter);
                            accountListViewAdapter.notifyDataSetChanged();
                        }
                    }

                    else
                    {
                        System.out.println("kkk");
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                    setProgressBarIndeterminateVisibility(false);
                    loading.dismiss();
                }


                // mParties = new String[]
                // dynamicToolbarColor();


                setProgressBarIndeterminateVisibility(false);

            }}, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Something went wrong!!", Toast.LENGTH_LONG).show();
                setProgressBarIndeterminateVisibility(false);
                loading.dismiss();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){

                Map<String,String> params = new HashMap<String, String>();

                params.put("user_id",Preferences.getInstance().userId);
                params.put("value","linked_account");





                return params;
            }};

        requestObject.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(requestObject);




    }
}

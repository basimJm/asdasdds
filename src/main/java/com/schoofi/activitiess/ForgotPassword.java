package com.schoofi.activitiess;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.SchoofiApplication;
import com.schoofi.utils.TrackerName;
import com.schoofi.utils.Utils;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {

	private TextView title,userDob;
	private EditText userName,userMobile,userEmail,userVerificationCode;
	private Button done,verify;
	private DatePicker datePicker;
	private Calendar calendar;
	private int year, month, day;
	String date,date1;
	String Month;
	ImageView back;

	private static final String TAG = "PhoneAuthActivity";

	private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

	private static final int STATE_INITIALIZED = 1;
	private static final int STATE_CODE_SENT = 2;
	private static final int STATE_VERIFY_FAILED = 3;
	private static final int STATE_VERIFY_SUCCESS = 4;
	private static final int STATE_SIGNIN_FAILED = 5;
	private static final int STATE_SIGNIN_SUCCESS = 6;

	// [START declare_auth]
	private FirebaseAuth mAuth;
	// [END declare_auth]

	private boolean mVerificationInProgress = false;
	private String mVerificationId;
	private PhoneAuthProvider.ForceResendingToken mResendToken;
	private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		Tracker t = ((SchoofiApplication)getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Set screen name.
		t.setScreenName("Forgot Password");

		// Send a screen view.
		t.send(new HitBuilders.ScreenViewBuilder().build());

		if (savedInstanceState != null) {
			onRestoreInstanceState(savedInstanceState);
		}
		setContentView(R.layout.activity_forgot_password);
		title = (TextView) findViewById(R.id.txt_title);
		userName = (EditText) findViewById(R.id.edit_userName);
		userMobile = (EditText) findViewById(R.id.edit_userMobile);
		userEmail = (EditText) findViewById(R.id.edit_userEmail);
		userDob = (EditText) findViewById(R.id.btn_userDob);
		done = (Button) findViewById(R.id.btn_forgotDone);
		calendar = Calendar.getInstance();
		userDob.setVisibility(View.GONE);
		userEmail.setVisibility(View.GONE);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		userVerificationCode = (EditText) findViewById(R.id.btn_userOTP);
		verify = (Button) findViewById(R.id.btn_forgotOTP);

		userVerificationCode.setVisibility(View.GONE);
		verify.setVisibility(View.GONE);

		userDob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				showDialog(999);
				showDate(year, month, day);

			}
		});

		back = (ImageView) findViewById(R.id.img_back);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});





		done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(userName.getText().toString().matches("") || userMobile.getText().toString().matches(""))
				{
					Utils.showToast(getApplicationContext(),"Plz fill the empty fields");
				}

				else

				{
                   postMessage();
				}

			}
		});

		verify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				verifyPhoneNumberWithCode(mVerificationId,userVerificationCode.getText().toString());
			}
		});


		mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

			@Override
			public void onVerificationCompleted(PhoneAuthCredential credential) {
				// This callback will be invoked in two situations:
				// 1 - Instant verification. In some cases the phone number can be instantly
				//     verified without needing to send or enter a verification code.
				// 2 - Auto-retrieval. On some devices Google Play services can automatically
				//     detect the incoming verification SMS and perform verificaiton without
				//     user action.
				Log.d(TAG, "onVerificationCompleted:" + credential);
				// [START_EXCLUDE silent]
				mVerificationInProgress = false;
				// [END_EXCLUDE]

				// [START_EXCLUDE silent]
				// Update the UI and attempt sign in with the phone credential
				//updateUI(STATE_VERIFY_SUCCESS, credential);
				// [END_EXCLUDE]
				signInWithPhoneAuthCredential(credential);
			}

			@Override
			public void onVerificationFailed(FirebaseException e) {
				// This callback is invoked in an invalid request for verification is made,
				// for instance if the the phone number format is not valid.
				Log.w(TAG, "onVerificationFailed", e);
				// [START_EXCLUDE silent]
				mVerificationInProgress = false;
				// [END_EXCLUDE]

				if (e instanceof FirebaseAuthInvalidCredentialsException) {
					// Invalid request
					// [START_EXCLUDE]
					//mPhoneNumberField.setError("Invalid phone number.");
					// [END_EXCLUDE]
				} else if (e instanceof FirebaseTooManyRequestsException) {
					// The SMS quota for the project has been exceeded
					// [START_EXCLUDE]
					Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
							Snackbar.LENGTH_SHORT).show();
					// [END_EXCLUDE]
				}

				// Show a message and update the UI
				// [START_EXCLUDE]
				//updateUI(STATE_VERIFY_FAILED);
				// [END_EXCLUDE]
			}

			@Override
			public void onCodeSent(String verificationId,
								   PhoneAuthProvider.ForceResendingToken token) {
				// The SMS verification code has been sent to the provided phone number, we
				// now need to ask the user to enter the code and then construct a credential
				// by combining the code with a verification ID.
				Log.d(TAG, "onCodeSent:" + verificationId);

				// Save verification ID and resending token so we can use them later
				mVerificationId = verificationId;
				mResendToken = token;

				// [START_EXCLUDE]
				// Update UI
				//updateUI(STATE_CODE_SENT);
				// [END_EXCLUDE]
			}
		};
	}



	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 999) {
			return new DatePickerDialog(this, myDateListener, year, month, day);
			
			
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2+1, arg3);
		}
		
		public void onCancel(DialogInterface dialog){
            // Send a message to confirm cancel button click
            Toast.makeText(getApplicationContext(),"Date Picker Canceled.", Toast.LENGTH_SHORT).show();
        }
		
	
	
	};
	
	
	
	

	
	private void showDate(int year, int month, int day) {

		

		switch(month)
		{
		case 1: Month = "Jan";
		break;
		case 2: Month = "Feb";
		break;
		case 3: Month = "Mar";
		break;
		case 4: Month = "Apr";
		break;
		case 5: Month = "May";
		break;
		case 6: Month = "Jun";
		break;
		case 7: Month = "Jul";
		break;
		case 8: Month = "Aug";
		break;
		case 9: Month = "Sep";
		break;
		case 10: Month = "Oct";
		break;
		case 11: Month = "Nov";
		break;
		case 12: Month = "Dec";
		break;
		default : System.out.println("llll");

		break;
		}
		
		date = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);

		date1 = String.valueOf(day)+"-"+Month+"-"+String.valueOf(year);
		userDob.setText(date1);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	private void postMessage()
	{
		setSupportProgressBarIndeterminateVisibility(true);
		RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();


		final String url1 = AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.FORGOT_PASSWORD+"?u_name="+userName.getText().toString()+"&phone="+userMobile.getText().toString();

		StringRequest requestObject = new StringRequest(Request.Method.GET,url1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				JSONObject responseObject;
				//System.out.print(response.toString());
				//System.out.print(url1);
				try 
				{
					responseObject = new JSONObject(response);

					if(responseObject.has("Msg")&&responseObject.getString("Msg").equals("1"))
					{







						mAuth = FirebaseAuth.getInstance();

						startPhoneNumberVerification(userMobile.getText().toString());








					}


					else

					{

						Utils.showToast(getApplicationContext(),"Mobile Number or User Name does not match!!");
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

	private boolean validatePhoneNumber() {
		String phoneNumber = userMobile.getText().toString();
		if (TextUtils.isEmpty(phoneNumber)) {
			//mPhoneNumberField.setError("Invalid phone number.");
			Utils.showToast(getApplicationContext(),"Invalid");
			return false;
		}

		return true;
	}

	private void startPhoneNumberVerification(String phoneNumber) {
		// [START start_phone_auth]

		userMobile.setVisibility(View.GONE);
		userDob.setVisibility(View.GONE);
		userName.setVisibility(View.GONE);
		userEmail.setVisibility(View.GONE);
		done.setVisibility(View.GONE);
		userVerificationCode.setVisibility(View.VISIBLE);
		verify.setVisibility(View.VISIBLE);

		PhoneAuthProvider.getInstance().verifyPhoneNumber(
				phoneNumber,        // Phone number to verify
				60,                 // Timeout duration
				TimeUnit.SECONDS,   // Unit of timeout
				this,               // Activity (for callback binding)
				mCallbacks);        // OnVerificationStateChangedCallbacks
		// [END start_phone_auth]

		mVerificationInProgress = true;


	}

	private void verifyPhoneNumberWithCode(String verificationId, String code) {
		// [START verify_with_code]
		PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
		// [END verify_with_code]
		signInWithPhoneAuthCredential(credential);
	}

	// [START resend_verification]
	private void resendVerificationCode(String phoneNumber,
										PhoneAuthProvider.ForceResendingToken token) {
		PhoneAuthProvider.getInstance().verifyPhoneNumber(
				phoneNumber,        // Phone number to verify
				60,                 // Timeout duration
				TimeUnit.SECONDS,   // Unit of timeout
				this,               // Activity (for callback binding)
				mCallbacks,         // OnVerificationStateChangedCallbacks
				token);             // ForceResendingToken from callbacks
	}
	// [END resend_verification]

	// [START sign_in_with_phone]
	private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithCredential:success");

							FirebaseUser user = task.getResult().getUser();
							// [START_EXCLUDE]
							//updateUI(STATE_SIGNIN_SUCCESS, user);
							// [END_EXCLUDE]
							Intent intent = new Intent(ForgotPassword.this,PasswordReset.class);

						    intent.putExtra("userName", userName.getText().toString());


						    startActivity(intent);
						} else {
							// Sign in failed, display a message and update the UI
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
								// The verification code entered was invalid
								// [START_EXCLUDE silent]
								//mVerificationField.setError("Invalid code.");
								// [END_EXCLUDE]
							}

							userMobile.setVisibility(View.VISIBLE);
							userDob.setVisibility(View.VISIBLE);
							userName.setVisibility(View.VISIBLE);
							userEmail.setVisibility(View.VISIBLE);
							done.setVisibility(View.VISIBLE);
							userVerificationCode.setVisibility(View.GONE);
							verify.setVisibility(View.GONE);
							// [START_EXCLUDE silent]
							// Update UI
							//updateUI(STATE_SIGNIN_FAILED);
							// [END_EXCLUDE]
						}
					}
				});
	}
	// [END sign_in_with_phone]


}

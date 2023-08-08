package com.schoofi.testotpappnew;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.schoofi.Utility.AvenuesParams;
import com.schoofi.Utility.Constants;
import com.schoofi.Utility.RSAUtility;
import com.schoofi.Utility.ServiceHandler;
import com.schoofi.Utility.ServiceUtility;
import com.schoofi.activitiess.ParentFeesSuccessScreenCCAvenue;
import com.schoofi.activitiess.R;


public class WebViewActivity extends AppCompatActivity implements  Communicator {

    WebView myBrowser;
    WebSettings webSettings;
    private BroadcastReceiver mIntentReceiver;
    String bankUrl="";
    FragmentManager manager;
    ActionDialog actionDialog= new ActionDialog();
    Timer timer = new Timer();
    TimerTask timerTask;
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    public int loadCounter = 0;

    private ProgressDialog dialog;
    Intent mainIntent;
    String html1, encVal;
    int MyDeviceAPI;

    ArrayList<String> arrayList1;
    ArrayList<String> arrayList2;

    String totalAmount,fee1_type_text,fee_type_text1,fee2_type_text,fee_type_text2,fee3_type_text,fee_type_text3,fee4_type_text,fee_type_text4,fee5_type_text,fee_type_text5,fee_srrvice_tax;

    float feeType11,feeType21,feeType31,feeType41,feeType51,interest,delayCharges,serviceTax1,handlingCharges;

    float handlingCharges11=0;
    float total=0,total1=0,total2=0,finalTotalAmount=0;
    int count;
    String array,array1,array2,array3,array4,array5,array6,array7,array8,array9,array10,gross,serviceTax;
    String orderId;
    Button submit;
    RelativeLayout relativeLayout;
    String phone,value;
    String orderId1,mid1,status1,bankName1,txnAmount1,txnDate1,txnId1,response1,paymentMode1,banktxnId1,currency1,gatewayName1,isChecksumValid1,respMsg1,gross1;
    // String fee_type_text11,fee_type_text21,fee_type_text31,fee_type_text41,fee_type_text51,interest1,delay_charges1,feeServiceTax11,feeHandlingCharges11,totalAmount1,stu_fee_date_id,count,feeStartDate,feeEndDate,days1;


    /**
     * Async task class to get json by making HTTP call
     * */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(WebViewActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                // Creating service handler class instance
                ServiceHandler sh = new ServiceHandler();
                // Making a request to url and getting response
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
                params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

                String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
                System.out.println(vResponse);
                if (!ServiceUtility.chkNull(vResponse).equals("")
                        && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                    StringBuffer vEncVal = new StringBuffer("");
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                    vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                    encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (dialog.isShowing())
                dialog.dismiss();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface
            {
                @JavascriptInterface
                public void processHTML(String html)
                {
                    try {
                        // process the html as needed by the app
                        Document document = Jsoup.parse(html);


                      // html1= Jsoup.parse(html).text();



                        Elements links = document.select("td");

                        arrayList1 = new ArrayList<String>();
                        arrayList2 = new ArrayList<String>();

                        for(int i=0;i<links.size();i+=2)
                        {
                            arrayList1.add(links.get(i).text());
                        }

                        for (int j=1;j<=links.size();j+=2)
                        {
                            arrayList2.add(links.get(j).text());
                        }

                        JSONArray jsonArray = new JSONArray();

                        for(int o=0;o<arrayList1.size();o++)
                        {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(arrayList1.get(o),arrayList2.get(o));
                            jsonArray.put(jsonObject);
                        }

                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("CC-Avenue",jsonArray);





                        //JSONArray jsonArray = new JSONArray(html1);
                        Log.d("html",jsonObject1.toString());

                        Log.v("Logs", "-------------- Process HTML : "+ html);
                        String status = null;
                        if(html.indexOf("Failure")!=-1){
                            status = "Transaction Declined!";
                        }else if(html.indexOf("Success")!=-1){
                            status = "Transaction Successful!";
                        }else if(html.indexOf("Aborted")!=-1){
                            status = "Transaction Cancelled!";
                        }else{
                            status = "Status Not Known!";
                        }
                        //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();



                            Intent intent = new Intent(getApplicationContext(), ParentFeesSuccessScreenCCAvenue.class);
                            intent.putExtra("transStatus", status);
                            intent.putExtra("html",html1);
                        intent.putExtra("array", array);
                        intent.putExtra("array1", array1);
                        intent.putExtra("array2", array2);
                        intent.putExtra("array3", array3);
                        intent.putExtra("array4", array4);
                        intent.putExtra("array5", array5);
                        intent.putExtra("array6", array6);
                        intent.putExtra("array7", array7);
                        intent.putExtra("array8", array8);
                        intent.putExtra("array9", array9);
                        intent.putExtra("array10", array10);
                        intent.putExtra("count", count);
                        intent.putExtra("gross", gross1);
                        intent.putExtra("value",value);
                        intent.putExtra("serviceTax", serviceTax);
                        intent.putExtra("order_id",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(0).getString("order_id"));
                        intent.putExtra("tracking_id",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(1).getString("tracking_id"));
                        intent.putExtra("bank_ref_no",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(2).getString("bank_ref_no"));
                        intent.putExtra("failure_message",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(4).getString("failure_message"));
                        intent.putExtra("payment_mode",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(5).getString("payment_mode"));
                        intent.putExtra("card_name",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(6).getString("card_name"));
                        intent.putExtra("status_code",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(7).getString("status_code"));
                        intent.putExtra("status_message",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(8).getString("status_message"));
                        intent.putExtra("currency",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(9).getString("currency"));
                        intent.putExtra("amount",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(10).getString("amount"));
                        intent.putExtra("billing_name",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(11).getString("billing_name"));
                        intent.putExtra("billing_address",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(12).getString("billing_address"));
                        intent.putExtra("billing_city",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(13).getString("billing_city"));
                        intent.putExtra("billing_state",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(14).getString("billing_state"));
                        intent.putExtra("billing_zip",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(15).getString("billing_zip"));
                        intent.putExtra("billing_country",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(16).getString("billing_country"));
                        intent.putExtra("billing_tel",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(17).getString("billing_tel"));
                        intent.putExtra("billing_email",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(18).getString("billing_email"));
                        intent.putExtra("delivery_name",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(19).getString("delivery_name"));
                        intent.putExtra("delivery_address",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(20).getString("delivery_address"));
                        intent.putExtra("delivery_city",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(21).getString("delivery_city"));
                        intent.putExtra("delivery_state",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(22).getString("delivery_state"));
                        intent.putExtra("delivery_zip",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(23).getString("delivery_zip"));
                        intent.putExtra("delivery_country",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(24).getString("delivery_country"));
                        intent.putExtra("delivery_tel",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(25).getString("delivery_tel"));
                        intent.putExtra("merchant_param1",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(26).getString("merchant_param1"));
                        intent.putExtra("merchant_param2",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(27).getString("merchant_param2"));
                        intent.putExtra("merchant_param3",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(28).getString("merchant_param3"));
                        intent.putExtra("merchant_param4",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(29).getString("merchant_param4"));
                        intent.putExtra("merchant_param5",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(30).getString("merchant_param5"));
                        intent.putExtra("vault",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(31).getString("vault"));
                        intent.putExtra("offer_type",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(32).getString("offer_type"));
                        intent.putExtra("offer_code",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(33).getString("offer_code"));
                        intent.putExtra("discount_value",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(34).getString("discount_value"));
                        intent.putExtra("mer_amount",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(35).getString("mer_amount"));
                        intent.putExtra("eci_value",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(36).getString("eci_value"));
                        intent.putExtra("retry",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(37).getString("retry"));
                        intent.putExtra("response_code",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(38).getString("response_code"));
                        intent.putExtra("billing_notes",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(39).getString("billing_notes"));
                        intent.putExtra("trans_date",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(40).getString("trans_date"));
                        intent.putExtra("bin_country",jsonObject1.getJSONArray("CC-Avenue").getJSONObject(41).getString("bin_country"));
                        intent.putExtra("json",jsonObject1.toString());








                            startActivity(intent);
                            finish();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.v("Logs", "-------------- Error : "+e);
                    }
                }
            }

            //final WebView webview = (WebView) findViewById(R.id.webView);
            //myBrowser.getSettings().setJavaScriptEnabled(true);
            myBrowser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            myBrowser.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                    bankUrl = url;
                    return false;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(myBrowser, url);

                    if(url.indexOf("/ccavresp.php")!=-1){
                        myBrowser.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }

                    // calling load Waiting for otp fragment
                    if(loadCounter < 1){
                        if(MyDeviceAPI >= 19) {
                            loadCitiBankAuthenticateOption(url);
                            loadWaitingFragment(url);
                        }
                    }
                    bankUrl = url;
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

            /* An instance of this class will be registered as a JavaScript interface */



            try {
                StringBuffer params = new StringBuffer();
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));

                String vPostParams = params.substring(0,params.length()-1);

                myBrowser.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                showToast("Exception occured while opening webview.");
            }
        }
    }

    public String stripHtml(String html)
    {
        return Html.fromHtml(html).toString();
    }
    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cc);
        mainIntent = getIntent();
        manager = getFragmentManager();

        array = getIntent().getStringExtra("array");
        array1 = getIntent().getStringExtra("array1");
        array2 = getIntent().getStringExtra("array2");
        array3 = getIntent().getStringExtra("array3");
        array4 = getIntent().getStringExtra("array4");
        array5 = getIntent().getStringExtra("array5");
        array6 = getIntent().getStringExtra("array6");
        array7 = getIntent().getStringExtra("array7");
        array8 = getIntent().getStringExtra("array8");
        array9 = getIntent().getStringExtra("array9");
        array10 = getIntent().getStringExtra("array10");
        count = getIntent().getExtras().getInt("count");
        gross1 = getIntent().getStringExtra("gross");
        serviceTax = getIntent().getStringExtra("serviceTax");

        myBrowser = (WebView) findViewById(R.id.webView);
        webSettings = myBrowser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        MyDeviceAPI = Build.VERSION.SDK_INT;
        // Calling async task to get display content
        new RenderView().execute();
    }

    // Method to start Timer for 30 sec. delay
    public void startTimer() {
        try {
            //set a new Timer
            if(timer == null) {
                timer = new Timer();
            }
            //initialize the TimerTask's job
            initializeTimerTask();

            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer.schedule(timerTask, 30000, 30000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Method to Initialize Task
    public void initializeTimerTask() {
        try {
            timerTask = new TimerTask() {
                public void run() {

                    //use a handler to run a toast that shows the current timestamp
                    handler.post(new Runnable() {
                        public void run() {
                        /*int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getApplicationContext(), "I M Called ..", duration);
                        toast.show();*/
                            loadActionDialog();
                        }
                    });
                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Method to stop timer
    public void stopTimerTask(){
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void loadCitiBankAuthenticateOption(String url){
        if(url.contains("https://www.citibank.co.in/acspage/cap_nsapi.so")){
            CityBankFragment citiFrag = new CityBankFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, citiFrag, "CitiBankAuthFrag");
            transaction.commit();
            loadCounter++;
        }
    }

    public void removeCitiBankAuthOption(){
        CityBankFragment cityFrag = (CityBankFragment) manager.findFragmentByTag("CitiBankAuthFrag");
        FragmentTransaction transaction = manager.beginTransaction();
        if(cityFrag!=null){
            transaction.remove(cityFrag);
            transaction.commit();
        }
    }

    // Method to load Waiting for OTP fragment
    public void loadWaitingFragment(String url){

        // SBI Debit Card
        if(url.contains("https://acs.onlinesbi.com/sbi/")){
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }

        // Kotak Bank Visa Debit card
        else if(url.contains("https://cardsecurity.enstage.com/ACSWeb/")){
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        // For SBI and All its Asscocites Net Banking
        else if(url.contains("https://merchant.onlinesbi.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.onlinesbi.com/merchant/resendsmsotp.htm") || url.contains("https://m.onlinesbi.com/mmerchant/smsenablehighsecurity.htm")
                || url.contains("https://merchant.onlinesbh.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.onlinesbh.com/merchant/resendsmsotp.htm")
                || url.contains("https://merchant.sbbjonline.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.sbbjonline.com/merchant/resendsmsotp.htm")
                || url.contains("https://merchant.onlinesbm.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.onlinesbm.com/merchant/resendsmsotp.htm")
                || url.contains("https://merchant.onlinesbp.com/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.onlinesbp.com/merchant/resendsmsotp.htm")
                || url.contains("https://merchant.sbtonline.in/merchant/smsenablehighsecurity.htm") || url.contains("https://merchant.sbtonline.in/merchant/resendsmsotp.htm")){
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }

        // For ICICI Credit Card
        else if(url.contains("https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/OtpServer")){
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        // City bank Debit card
        else if(url.equals("cityBankAuthPage")){
            removeCitiBankAuthOption();
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        // HDFC Debit Card and Credit Card
        else if(url.contains("https://netsafe.hdfcbank.com/ACSWeb/jsp/dynamicAuth.jsp?transType=payerAuth")){
            //removeCitiBankAuthOption();
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        // For SBI  Visa credit Card
        else if(url.contains("https://secure4.arcot.com/acspage/cap")){
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }

        // For Kotak Bank Visa Credit Card
        else if (url.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer")){
            OtpFragment waitingFragment = new OtpFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.otp_frame, waitingFragment, "OTPWaitingFrag");
            transaction.commit();
            startTimer();
        }
        else{
            removeWaitingFragment();
            removeApprovalFragment();
            stopTimerTask();
        }
    }

    // Method to remove Waiting fragment
    public void removeWaitingFragment(){
        OtpFragment waitingFragment = (OtpFragment) manager.findFragmentByTag("OTPWaitingFrag");
        if(waitingFragment!=null){
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(waitingFragment);
            transaction.commit();
        }
        else{
            // DO nothing
            //Toast.makeText(this," --test-- ",Toast.LENGTH_SHORT).show();
        }
    }

    // Method to load Approve Otp Fragment
    public void loadApproveOTP(String otpText,String senderNo){
        try{
            Integer vTemp = Integer.parseInt(otpText);

            if(bankUrl.contains("https://acs.onlinesbi.com/sbi/") && senderNo.contains("SBI") && (otpText.length() == 6 || otpText.length() == 8)){
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For Kotak bank Debit Card
            else if(bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/") && senderNo.contains("KOTAK") && (otpText.length() == 6 || otpText.length() == 8)){
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // for SBI Net Banking
            else if((((bankUrl.contains("https://merchant.onlinesbi.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbi.com/merchant/resendsmsotp.htm") || bankUrl.contains("https://m.onlinesbi.com/mmerchant/smsenablehighsecurity.htm")) && senderNo.contains("SBI"))
                    || ((bankUrl.contains("https://merchant.onlinesbh.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbh.com/merchant/resendsmsotp.htm")) && senderNo.contains("SBH"))
                    || ((bankUrl.contains("https://merchant.sbbjonline.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.sbbjonline.com/merchant/resendsmsotp.htm")) && senderNo.contains("SBBJ"))
                    || ((bankUrl.contains("https://merchant.onlinesbm.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbm.com/merchant/resendsmsotp.htm")) && senderNo.contains("SBM"))
                    || ((bankUrl.contains("https://merchant.onlinesbp.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbp.com/merchant/resendsmsotp.htm")) && senderNo.contains("SBP"))
                    || ((bankUrl.contains("https://merchant.sbtonline.in/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.sbtonline.in/merchant/resendsmsotp.htm")) && senderNo.contains("SBT"))) && (otpText.length() == 6 || otpText.length() == 8)){
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For ICICI Visa Credit Card
            else if(bankUrl.contains("https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/OtpServer") && senderNo.contains("ICICI")&& (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For ICICI Debit card
            else if(bankUrl.contains("https://acs.icicibank.com/acspage/cap?") && senderNo.contains("ICICI")&& (otpText.length() == 6 || otpText.length() == 8)) {
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For CITI bank Debit card
            else if(bankUrl.contains("https://www.citibank.co.in/acspage/cap_nsapi.so") && senderNo.contains("CITI")&& (otpText.length() == 6 || otpText.length() == 8)){
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For HDFC bank debit card and Credit Card
            else if(bankUrl.contains("https://netsafe.hdfcbank.com/ACSWeb/jsp/dynamicAuth.jsp?transType=payerAuth") && senderNo.contains("HDFC")&& (otpText.length() == 6 || otpText.length() == 8)){
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For HDFC Netbanking
            else if(bankUrl.contains("https://netbanking.hdfcbank.com/netbanking/entry") && senderNo.contains("HDFC")&& (otpText.length() == 6 || otpText.length() == 8)){
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            // For SBI Visa credit Card
            else if(bankUrl.contains("https://secure4.arcot.com/acspage/cap") && senderNo.contains("SBI")&& (otpText.length() == 6 || otpText.length() == 8)){
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            else if(bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer") && senderNo.contains("KOTAK") && (otpText.length() == 6 || otpText.length() == 8)){
                removeWaitingFragment();
                stopTimerTask();
                ApproveOTPFragment approveFragment = new ApproveOTPFragment();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.otp_frame, approveFragment, "OTPApproveFrag");
                transaction.commit();
                approveFragment.setOtpText(otpText);
            }
            else{
                removeApprovalFragment();
                stopTimerTask();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeApprovalFragment(){
        ApproveOTPFragment approveOTPFragment = (ApproveOTPFragment)manager.findFragmentByTag("OTPApproveFrag");
        if(approveOTPFragment !=null){
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(approveOTPFragment);
            transaction.commit();
        }
    }

    public void loadActionDialog(){

        try {
            actionDialog.show(getFragmentManager(), "ActionDialog");
            stopTimerTask();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
        mIntentReceiver = new BroadcastReceiver() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onReceive(Context context, Intent intent) {

                try{
                    //removeWaitingFragment();
                    removeApprovalFragment();
                    ///////////////////////////////////////
                    String msgText = intent.getStringExtra("get_otp");
                    String otp = msgText.split("\\|")[0];
                    String senderNo = msgText.split("\\|")[1];
                    if(MyDeviceAPI >=19) {
                        loadApproveOTP(otp, senderNo);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Exception :"+e,Toast.LENGTH_SHORT).show();
                }
            }
        };
        this.registerReceiver(mIntentReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mIntentReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // On click of Approve button
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void respond(String otpText) {

        String data = otpText;
        try{
            // For SBI and all the associates
            if (bankUrl.contains("https://acs.onlinesbi.com/sbi/")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('otp').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For Kotak Bank Debit card
            else if (bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank")) {
                myBrowser.evaluateJavascript("javascript:document.getElementById('txtOtp').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For SBI Visa credit card
            else if(bankUrl.contains("https://secure4.arcot.com/acspage/cap")){
                myBrowser.evaluateJavascript("javascript:document.getElementsByName('pin1')[0].value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For SBI and associates banks Net Banking
            else if (bankUrl.contains("https://merchant.onlinesbi.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbi.com/merchant/resendsmsotp.htm") || bankUrl.contains("https://m.onlinesbi.com/mmerchant/smsenablehighsecurity.htm")
                    || bankUrl.contains("https://merchant.onlinesbh.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbh.com/merchant/resendsmsotp.htm")
                    || bankUrl.contains("https://merchant.sbbjonline.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.sbbjonline.com/merchant/resendsmsotp.htm")
                    || bankUrl.contains("https://merchant.onlinesbm.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbm.com/merchant/resendsmsotp.htm")
                    || bankUrl.contains("https://merchant.onlinesbp.com/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.onlinesbp.com/merchant/resendsmsotp.htm")
                    || bankUrl.contains("https://merchant.sbtonline.in/merchant/smsenablehighsecurity.htm") || bankUrl.contains("https://merchant.sbtonline.in/merchant/resendsmsotp.htm")) {
                myBrowser.evaluateJavascript("javascript:document.getElementsByName('securityPassword')[0].value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For ICICI credit card
            else if(bankUrl.contains("https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/OtpServer")){
                myBrowser.evaluateJavascript("javascript:document.getElementById('txtAutoOtp').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For ICICI bank Debit card
            else if(bankUrl.contains("https://acs.icicibank.com/acspage/cap?")){
                myBrowser.evaluateJavascript("javascript:document.getElementById('txtAutoOtp').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For Citi Bank debit card
            else if(bankUrl.contains("https://www.citibank.co.in/acspage/cap_nsapi.so")){
                myBrowser.evaluateJavascript("javascript:document.getElementsByName('otp')[0].value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For HDFC Debit card and Credit card
            else if(bankUrl.contains("https://netsafe.hdfcbank.com/ACSWeb/jsp/dynamicAuth.jsp?transType=payerAuth")){
                myBrowser.evaluateJavascript("javascript:document.getElementById('txtOtpPassword').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // HDFC Net Banking
            else if(bankUrl.contains("https://netbanking.hdfcbank.com/netbanking/entry")){
                myBrowser.evaluateJavascript("javascript:document.getElementsByName('fldOtpToken')[0].value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // For Kotak Band visa Credit Card
            else if(bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer")){
                myBrowser.evaluateJavascript("javascript:document.getElementById('otpValue').value = '" + otpText + "'", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            // for CITI Bank Authenticate with option selection
            if(data.equals("password")){
                myBrowser.evaluateJavascript("javascript:document.getElementById('uid_tb_r').click();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
            if(data.equals("smsOtp")){
                myBrowser.evaluateJavascript("javascript:document.getElementById('otp_tb_r').click();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
                loadWaitingFragment("cityBankAuthPage");
            }
            loadCounter++;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void actionSelected(String data) {
        try {
            if (data.equals("ResendOTP")) {
                stopTimerTask();
                removeWaitingFragment();
                if (bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank")) {
                    myBrowser.evaluateJavascript("javascript:reSendOtp();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                // For HDFC Credit and Debit Card
                else if(bankUrl.contains("https://netsafe.hdfcbank.com/ACSWeb/jsp/dynamicAuth.jsp?transType=payerAuth")){
                    myBrowser.evaluateJavascript("javascript:generateOTP();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                // SBI Visa Credit Card
                else if(bankUrl.contains("https://secure4.arcot.com/acspage/cap")){
                    myBrowser.evaluateJavascript("javascript:OnSubmitHandlerResend();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                // For Kotak Visa Credit Card
                else if(bankUrl.contains("https://cardsecurity.enstage.com/ACSWeb/EnrollWeb/KotakBank/server/OtpServer")){
                    myBrowser.evaluateJavascript("javascript:doSendOTP();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                // For ICICI Credit Card
                else if(bankUrl.contains("https://www.3dsecure.icicibank.com/ACSWeb/EnrollWeb/ICICIBank/server/OtpServer")){
                    myBrowser.evaluateJavascript("javascript:resend_otp();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {}
                    });
                }
                else {
                    myBrowser.evaluateJavascript("javascript:resendOTP();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {

                        }
                    });
                }
                //loadCounter=0;
            } else if (data.equals("EnterOTPManually")) {
                stopTimerTask();
                removeWaitingFragment();
            } else if (data.equals("Cancel")) {
                stopTimerTask();
                removeWaitingFragment();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Action not available for this Payment Option !", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}

package com.schoofi.activitiess;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.schoofi.adapters.ParentFeeInvoiceDetailsAdapter;
import com.schoofi.constants.AppConstants;
import com.schoofi.utils.Preferences;
import com.schoofi.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ParentNewFeesHistoryDetails extends AppCompatActivity {

    private ImageView back;
    private TextView studentName,studentClass,studentAdmissionNumber,invoiceNumber,invoiceDate,paymentDate,fine,interest,handlingCharges,serviceTax,totalPaid,paymentType,transactionId,confirmationStatus,transacionStatus,transactionMessage;
    private ListView studentFeeDetailsListview;
    private JSONArray parentStudentFeesHistoryArray;
    private int position;
    private ParentFeeInvoiceDetailsAdapter parentFeeInvoiceDetailsAdapter;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date date1,date2,date3;
    String invoiceDate1,invoiceDate2,paymentDate1,paymentdate2;

    String feeType,feeTypeFees;

    private  TextView invoice;
    String Rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_parent_new_fees_history_details);

        back = (ImageView) findViewById(R.id.img_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        studentName = (TextView) findViewById(R.id.text_student_name);
        studentClass = (TextView) findViewById(R.id.text_student_class_name);
        studentAdmissionNumber = (TextView) findViewById(R.id.text_student_admissionNo);
        invoiceNumber = (TextView) findViewById(R.id.text_student_invoice_number);
        invoiceDate = (TextView) findViewById(R.id.text_student_invoice_date);
        paymentDate = (TextView) findViewById(R.id.text_student_payment_date);
        studentFeeDetailsListview = (ListView) findViewById(R.id.listview_fee);
        fine = (TextView) findViewById(R.id.text_student_payment_fine);
        interest = (TextView) findViewById(R.id.text_student_payment_interest);
        serviceTax = (TextView) findViewById(R.id.text_student_payment_service_tax);
        handlingCharges = (TextView) findViewById(R.id.text_student_payment_handling_charges);
        transactionId = (TextView) findViewById(R.id.text_student_payment_txn_id);
        confirmationStatus = (TextView) findViewById(R.id.text_student_payment_confirmation_status);
        paymentType = (TextView) findViewById(R.id.text_student_payment_payment_type);
        totalPaid = (TextView) findViewById(R.id.text_student_payment_total_paid);
        transacionStatus = (TextView) findViewById(R.id.text_student_payment_status);
        transactionMessage = (TextView) findViewById(R.id.text_student_payment_messsage);
        invoice = (TextView) findViewById(R.id.txt_invoice);



        position = getIntent().getExtras().getInt("position");

        Preferences.getInstance().loadPreference(getApplicationContext());

        Rs = getApplicationContext().getString(R.string.Rs);

        initData();

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    String url ;
                    url = "http://schoofi.com/invoice_pdf.php?inv_no="+parentStudentFeesHistoryArray.getJSONObject(position).getString("inv_no")+"&session="+ Preferences.getInstance().session1;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        });



    }

    private void initData() {


        try {
            Cache.Entry e;
            e = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().get(AppConstants.SERVER_URLS.SERVER_URL + AppConstants.SERVER_URLS.STUDENT_FEES_HISTORY + "?token=" + Preferences.getInstance().token + "&sch_id=" + Preferences.getInstance().schoolId + "&device_id=" + Preferences.getInstance().deviceId + "&stu_id=" + Preferences.getInstance().studentId + "&ins_id=" + Preferences.getInstance().institutionId);
            if (e == null) {
                parentStudentFeesHistoryArray = null;
            } else {
                parentStudentFeesHistoryArray = new JSONArray(new String(e.data));
            }


            if (parentStudentFeesHistoryArray != null) {

                feeType = parentStudentFeesHistoryArray.getJSONObject(position).getString("fee_type_text");
                feeTypeFees = parentStudentFeesHistoryArray.getJSONObject(position).getString("paid_amount_type_wise");
                studentName.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("stu_name"));
                studentClass.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("class_section"));
                studentAdmissionNumber.setText("Admission Number :" + parentStudentFeesHistoryArray.getJSONObject(position).getString("admission_no"));
                invoiceNumber.setText("Invoice Number :" + parentStudentFeesHistoryArray.getJSONObject(position).getString("inv_no"));
                invoiceDate1 = parentStudentFeesHistoryArray.getJSONObject(position).getString("inv_date");
                paymentDate1 = parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_date");
                try {
                    date1 = formatter.parse(invoiceDate1);
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
                    invoiceDate2 = formatter1.format(date1);

                    date2 = formatter4.parse(paymentDate1);

                    paymentdate2= formatter1.format(date2);



                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                invoiceDate.setText("Invoice Date : "+invoiceDate2);
                paymentDate.setText("Payment Date : "+paymentdate2);
                if (parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_chanel").matches("web")) {
                    fine.setText("Fine : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("fine_amount"));
                    interest.setText("Interest : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("interest_amount"));
                    handlingCharges.setText("Handling Charges : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("handling_charges"));
                    totalPaid.setText("Total Amount Paid : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("paid_amount"));
                    paymentType.setText("Payment Type : " +parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_type"));
                    transactionId.setVisibility(View.GONE);
                    confirmationStatus.setVisibility(View.GONE);
                    serviceTax.setVisibility(View.GONE);
                    //handlingCharges.setVisibility(View.GONE);
                } else {
                    transacionStatus.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("confirmation_status"));
                    if(transacionStatus.getText().toString().matches("SUCCESS"))
                    {
                        transactionMessage.setVisibility(View.GONE);
                    }

                    else {
                        transactionMessage.setVisibility(View.VISIBLE);
                        transactionMessage.setText(parentStudentFeesHistoryArray.getJSONObject(position).getString("response_msg"));
                    }
                    fine.setText("Fine : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("fine_amount"));
                    interest.setText("Interest : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("interest_amount"));
                    handlingCharges.setText("Handling Charges : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("pg_charges"));
                    totalPaid.setText("Total Amount Paid : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("paid_amount"));
                    paymentType.setText("Payment Type : " +parentStudentFeesHistoryArray.getJSONObject(position).getString("payment_type"));
                    transactionId.setVisibility(View.VISIBLE);
                    confirmationStatus.setVisibility(View.VISIBLE);
                    serviceTax.setVisibility(View.VISIBLE);
                    //handlingCharges.setVisibility(View.GONE);
                    serviceTax.setVisibility(View.GONE);
                    confirmationStatus.setVisibility(View.GONE);
                    transactionId.setText("Transaction Id : " +parentStudentFeesHistoryArray.getJSONObject(position).getString("transaction_id"));
                    confirmationStatus.setText("Confirmation Status : " +parentStudentFeesHistoryArray.getJSONObject(position).getString("confirmation_status"));
                    serviceTax.setText("Service Tax : " +Rs+ parentStudentFeesHistoryArray.getJSONObject(position).getString("service_tax"));
                }


                ArrayList aList = new ArrayList(Arrays.asList(feeType.split(",")));
                ArrayList aList1 = new ArrayList(Arrays.asList(feeTypeFees.split(",")));

                studentFeeDetailsListview.invalidateViews();

                parentFeeInvoiceDetailsAdapter = new ParentFeeInvoiceDetailsAdapter(getApplicationContext(), aList, aList1);
                studentFeeDetailsListview.setAdapter(parentFeeInvoiceDetailsAdapter);
                parentFeeInvoiceDetailsAdapter.notifyDataSetChanged();

                setListViewHeightBasedOnChildren(studentFeeDetailsListview);



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


        public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    }


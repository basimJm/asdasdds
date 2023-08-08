package com.schoofi.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.Cast;
import com.schoofi.activitiess.ChairmanDashboard;
import com.schoofi.activitiess.R;
import com.schoofi.adapters.AssesmentHomeScreenHorizontalListviewAdapter;
import com.schoofi.adapters.ChairmanDashboardHorizontalListviewAdapter;
import com.schoofi.constants.AppConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import library.Animations.DescriptionAnimation;
import library.SliderAdapter;
import library.SliderLayout;
import library.SliderTypes.BaseSliderView;
import library.SliderTypes.DefaultSliderView;
import library.Tricks.ViewPagerEx;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Adib on 13-Apr-17.
 */

public class FirstFragment extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {

    private OnFragmentInteractionListener listener;
    private SliderLayout mDemoSlider;
    ArrayList<Integer> myList;
    private int position,position1;
    SliderAdapter sliderAdapter;

    AssesmentHomeScreenHorizontalListviewAdapter assesmentHomeScreenHorizontalListviewAdapter;

    int[] COLORS = {
            Color.rgb(102, 155, 76), Color.rgb(10, 173, 162), Color.rgb(248, 88, 129),
            Color.rgb(224, 157, 64), Color.rgb(85, 218, 160), Color.rgb(94, 186, 220), Color.rgb(0, 174, 179),
            Color.rgb(227, 72, 62),  Color.rgb(165, 104, 211), Color.rgb(251, 153, 99), Color.rgb(30,144,255),Color.rgb(46,139,87),Color.rgb(255,228,181),Color.rgb(255,171,64),Color.rgb(141,110,99),Color.rgb(0,96,100),Color.rgb(221, 44, 0),Color.rgb(173, 20, 87),Color.rgb(0, 105, 92),Color.rgb(227, 72, 62),Color.rgb(227, 72, 62)
    };

    int []studentHomeScreenIcon = {R.drawable.attendence,R.drawable.leaverequest,R.drawable.schedule,R.drawable.assignments,R.drawable.announcement,R.drawable.feedback,R.drawable.examschedule,R.drawable.result,R.drawable.events,R.drawable.poll,R.drawable.gps,R.drawable.fees,R.drawable.busattendance,R.drawable.busattendance,R.drawable.diary,R.drawable.yearlyplanner,R.drawable.healthcard,R.drawable.hostel,R.drawable.studyplanner,R.drawable.library,R.drawable.library,R.drawable.library};
    int []studentHomeScreenIcon1 = {R.drawable.attendancetablet,R.drawable.leaverequesttablet,R.drawable.scheduletablet,R.drawable.assignmentstablet,R.drawable.announcementtablet,R.drawable.feedbacktablet,R.drawable.examscheduletablet,R.drawable.resulttablet,R.drawable.eventstablet,R.drawable.polltablet,R.drawable.gpstablet,R.drawable.feestablet,R.drawable.busattendancetablet,R.drawable.busattendancetablet,R.drawable.diarytablet,R.drawable.yearlyplannertablet,R.drawable.healthcardtablet,R.drawable.hosteltablet,R.drawable.studyplannertablet,R.drawable.librarytablet,R.drawable.librarytablet,R.drawable.librarytablet};
    String []studentHomeScreenIconName = {"ATTENDANCE","LEAVE REQUEST","TIME TABLE","ASSIGNMENTS/HOMEWORK","ANNOUNCEMENTS","FEEDBACK","EXAM SCHEDULE","RESULT","EVENTS","POLL","BUS TRACKING","FEES","Group Discussion","BUS ATTENDANCE","DIARY","YEARLY PLANNER","HEALTH CARD","HOSTEL","STUDY PLANNER","LIBRARY","ASSESMENT","KNOWLEDGE RESOURSE"};
    ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
    ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
    ArrayList<Integer> studentHomeScreenIconFinal1 = new ArrayList<Integer>();

    int pos;
    String value,param;

    RecyclerView mRecyclerView,mRecyclerView1,mRecyclerView2;
    RecyclerView.LayoutManager mLayoutManager,mLayoutManager1,mLayoutManager2;
    RecyclerView.Adapter mAdapter,mAdapter1,mAdapter2;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);
        ArrayList<Integer> myList = new ArrayList<>();
        myList.add((R.drawable.aboutschool));
        myList.add((R.drawable.aboutschool));

        for(int i=0;i<studentHomeScreenIcon.length;i++)
        {
            studentHomeScreenIconFinal.add(studentHomeScreenIcon[i]);
            studentHomeScreenIconNamefinal.add(studentHomeScreenIconName[i]);
        }



        sliderAdapter = new SliderAdapter(getActivity().getApplicationContext());

        for(final Integer name : myList)
        {
            final DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity().getApplicationContext());

            defaultSliderView.image(name).setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Log.d("MyActivity", "index selected:" + mDemoSlider.getCurrentPosition());

                    pos = mDemoSlider.getCurrentPosition();


                }


            });

            mDemoSlider.addSlider(defaultSliderView);



            // mDemoSlider.

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.DepthPage);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.stopAutoCycle();
    }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AssesmentHomeScreenHorizontalListviewAdapter(getActivity().getApplicationContext(),studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_view1);
        mRecyclerView1.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView1.setLayoutManager(mLayoutManager1);

        mAdapter1 = new AssesmentHomeScreenHorizontalListviewAdapter(getActivity().getApplicationContext(),studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS);
        mRecyclerView1.setAdapter(mAdapter1);

        mRecyclerView2 = (RecyclerView) view.findViewById(R.id.recycler_view2);
        mRecyclerView2.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager2 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        mAdapter2 = new AssesmentHomeScreenHorizontalListviewAdapter(getActivity().getApplicationContext(),studentHomeScreenIconFinal,studentHomeScreenIconNamefinal,COLORS);
        mRecyclerView2.setAdapter(mAdapter2);





        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }



    public interface OnFragmentInteractionListener {
    }


}

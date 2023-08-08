package com.schoofi.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.schoofi.activitiess.R;
import com.schoofi.utils.CircularTextView;
import com.schoofi.utils.Preferences;

import org.json.JSONException;

import java.util.ArrayList;

public class AssesmentHomeScreenHorizontalListviewAdapter extends RecyclerView.Adapter<AssesmentHomeScreenHorizontalListviewAdapter.ViewHolder> {

    Context context;
    ArrayList<Integer> studentHomeScreenIconFinal = new ArrayList<Integer>();
    int []colorId;
    ArrayList<String> studentHomeScreenIconNamefinal = new ArrayList<String>();
    private static LayoutInflater inflater = null;
    String fontPath = "fonts/Asap-Regular.otf";


    public AssesmentHomeScreenHorizontalListviewAdapter(Context context, ArrayList<Integer> studentHomeScreenIconFinal1, ArrayList<String> studentHomeScreenIconNamefinal2, int []Colors)
    {
        this.context = context;
        this.studentHomeScreenIconFinal = studentHomeScreenIconFinal1;
        this.studentHomeScreenIconNamefinal = studentHomeScreenIconNamefinal2;
        colorId = Colors;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }







    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.assesment_home_screen_horizontal_listview_design, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

            viewHolder.imageView.setImageResource(studentHomeScreenIconFinal.get(i));
            viewHolder.imageText.setText(studentHomeScreenIconNamefinal.get(i));


            viewHolder.linearLayout.setBackgroundColor(colorId[i]);



    }

    @Override
    public int getItemCount() {
        return studentHomeScreenIconFinal.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView imageText;
        CircularTextView circularTextView;
        LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.studentHomeScreenImageView);
            imageText = (TextView) itemView.findViewById(R.id.text_home);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_student_home_grid_layout);

            //holder.imageText.setText("Category");


        }


    }



}





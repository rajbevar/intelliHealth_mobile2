package com.healthcare.bosch.patientapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.activity.DetailedActivity;
import com.healthcare.bosch.patientapp.activity.MainActivity;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;
import com.healthcare.bosch.patientapp.utils.SessionManager.PreferenceManager;
import com.healthcare.bosch.patientapp.utils.model.Observation;

import java.util.List;


public class ObservationListAdapter extends RecyclerView.Adapter<ObservationListAdapter.ViewHolder> {


    private Context context;
    private String patientId, patientName;
    Activity activity;
    private List<Observation> list;

    public ObservationListAdapter(Activity activity, Context context, List<Observation> list, String patientId, String patientName) {
        this.activity = activity;
        this.context = context;
        this.list = list;
        this.patientId = patientId;
        this.patientName = patientName;
    }


    public void updatelistData(List<Observation> list) {
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener,
            View.OnLongClickListener */ {

        TextView txtFPatientName, txtFPatientData, txtFMedicalRecord, txtDate;
        LinearLayout lnrFTop;
        ImageView imgView;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            txtFPatientName = itemView.findViewById(R.id.txtPatientName);
            txtFPatientData = itemView.findViewById(R.id.txtPatientId);
            txtFMedicalRecord = itemView.findViewById(R.id.txtMedicalRecord);
            txtDate = itemView.findViewById(R.id.txtDate);
            lnrFTop = itemView.findViewById(R.id.lnrTop);
            imgView = itemView.findViewById(R.id.imgView);
            cardView = itemView.findViewById(R.id.cardView);

        }

        //a function to set values to view
        void setData(final Observation item) {
            if (item.isViewType().equalsIgnoreCase("vital")) {
                lnrFTop.setBackgroundColor(activity.getResources().getColor(item.getBgColor()));
                txtFPatientName.setText(item.getmName());
                txtFPatientData.setText("See more >");
                txtFMedicalRecord.setText(item.getmValue());
                txtDate.setText(item.getEffectiveDateTime());
                imgView.setImageDrawable(activity.getResources().getDrawable(item.getImgToShow()));
            } else if (item.isViewType().equalsIgnoreCase("medications")) {
                lnrFTop.setBackgroundColor(activity.getResources().getColor(item.getBgColor()));
                if (PreferenceManager.getBooleanValue(context, patientId)) {
                    txtFPatientName.setText(item.getmName());
                } else {
                    txtFPatientName.setText(item.getmName());
                }
                txtFPatientName.setTag("medications");
//                txtFMedicalRecord.setText(item.getmValue());
//                txtDate.setText(item.getEffectiveDateTime());
                txtDate.setVisibility(View.GONE);
                txtFMedicalRecord.setVisibility(View.GONE);

                imgView.setImageDrawable(activity.getResources().getDrawable(item.getImgToShow()));
            } else {
                txtFPatientName.setTag("lab");

                lnrFTop.setBackgroundColor(activity.getResources().getColor(item.getBgColor()));
                txtFPatientName.setText(item.getmName());
                txtFPatientData.setText("View >");
                txtFMedicalRecord.setVisibility(View.GONE);
                imgView.setVisibility(View.GONE);
                txtDate.setVisibility(View.GONE);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("patientId", patientId);
                    bundle.putString("code", item.getCode());
                    bundle.putString("name", item.getmName());
                    bundle.putString("patientName", patientName);
                    if (PreferenceManager.getBooleanValue(context, patientId)) {
                        bundle.putString("from", "notification");
                    }


                    if (item.isViewType().equalsIgnoreCase("vital")) {
                        Utilities.showActivityNotFinished(activity, DetailedActivity.class, bundle);
                    } else if (item.isViewType().equalsIgnoreCase("medications")) {
                        Utilities.showActivityNotFinished(activity, MainActivity.class, bundle);
                        //getting the alarm manager
                       /* Calendar calendar = Calendar.getInstance();
                        AlarmManager am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                        //creating a new intent specifying the broadcast receiver
                        Intent i = new Intent(activity, AlarmReceiver.class);
                        //creating a pending intent using the intent
                        PendingIntent pi = PendingIntent.getBroadcast(activity, 0, i, 0);
                        //setting the   alarm that will be fired
                        calendar.set(Calendar.HOUR_OF_DAY, 16);
                        calendar.set(Calendar.MINUTE, 35);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        am.set(AlarmManager.RTC, calendar.getTimeInMillis(), pi);*/
                    } else {

                    }

                }
            });
        }
    }
}

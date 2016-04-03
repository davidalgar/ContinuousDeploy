package me.algar.cosmos.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.algar.cosmos.R;
import me.algar.cosmos.data.Job;
import timber.log.Timber;

/**
 * Created by david.algar on 3/16/16.
 */
public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder> {
    private final List<Job> jobList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.job_name)
        public TextView jobName;
        @Bind(R.id.job_status)
        public TextView status;
        @Bind(R.id.status_circle)
        public RelativeLayout statusCircle;
        @Bind(R.id.status_circle_error_icon)
        public View statusErrorIcon;
        @Bind(R.id.status_circle_checkmark)
        public View statusCheckmarkIcon;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }

    public JobListAdapter(@NonNull  List<Job> buildList) {
        this.jobList = buildList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        Timber.d("onCreateViewHolder()");
        View listItemRoot = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_job, parent, false);

        return new ViewHolder(listItemRoot);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Job item = jobList.get(position);

        if(item == null){return;}

        holder.jobName.setText(item.name());
        holder.status.setText(item.color());
        if(item.color().equals(Job.FAILURE_COLOR)){
            holder.statusErrorIcon.setVisibility(View.VISIBLE);
            holder.statusCheckmarkIcon.setVisibility(View.GONE);
            holder.statusCircle.setBackground(
                    holder.itemView.getContext().getResources().getDrawable(R.drawable.red_circle));
        } else{
            holder.statusErrorIcon.setVisibility(View.GONE);
            holder.statusCheckmarkIcon.setVisibility(View.VISIBLE);
            holder.statusCircle.setBackground(
                    holder.itemView.getContext().getResources().getDrawable(R.drawable.green_circle));
        }
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    @Nullable
    public Job getItem(int position){
        if(jobList.size() <= position) return null;
        return jobList.get(position);
    }
}


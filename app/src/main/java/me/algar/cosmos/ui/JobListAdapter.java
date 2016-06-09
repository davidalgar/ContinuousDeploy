package me.algar.cosmos.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
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
import me.algar.cosmos.ui.views.StatusIcon;
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
        public StatusIcon statusCircle;

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
        View listItemRoot = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_job, parent, false);

        return new ViewHolder(listItemRoot);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        long curTime = System.nanoTime();
        Job item = jobList.get(position);

        if(item == null){return;}

        holder.jobName.setText(item.name());
        holder.status.setText(item.color());
        if(item.color().equals(Job.FAILURE_COLOR)){
            holder.statusCircle.setStatus(StatusIcon.STATUS_FAILURE);
        } else{
            holder.statusCircle.setStatus(StatusIcon.STATUS_SUCCESS);
        }
        long endTime = System.nanoTime();
        long duration  = (endTime - curTime)/1000;//to ms
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


    public void animateTo(List<Job> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Job> newModels) {
        for (int i = jobList.size() - 1; i >= 0; i--) {
            final Job model = jobList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Job> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Job model = newModels.get(i);
            if (!jobList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Job> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Job model = newModels.get(toPosition);
            final int fromPosition = jobList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Job removeItem(int position) {
        final Job model = jobList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Job model) {
        jobList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Job model = jobList.remove(fromPosition);
        jobList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}


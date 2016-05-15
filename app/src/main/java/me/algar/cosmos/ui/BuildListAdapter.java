package me.algar.cosmos.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.algar.cosmos.R;
import me.algar.cosmos.data.Build;
import me.algar.cosmos.ui.views.StatusIcon;

public class BuildListAdapter extends RecyclerView.Adapter<BuildListAdapter.ViewHolder> {
    private List<Build> buildList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.build_id)
        public TextView buildId;
        @Bind(R.id.build_responsible_name)
        public TextView responsibleName;
        @Bind(R.id.build_status_icon)
        public StatusIcon statusIcon;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }

        public void setOnExpandListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }
    }

    public BuildListAdapter(List<Build> buildList) {
        this.buildList = buildList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View listItemRoot = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_build, parent, false);

        return new ViewHolder(listItemRoot);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Build item = buildList.get(position);

        holder.buildId.setText(item.getBuildNumber());
        holder.responsibleName.setText(item.responsible());

        if(item.result != null && item.result.equals(Build.STATUS_SUCCESS)){
            holder.statusIcon.setStatus(StatusIcon.STATUS_SUCCESS);
        }else {
            holder.statusIcon.setStatus(StatusIcon.STATUS_FAILURE);
        }
    }

    @Override
    public int getItemCount() {
        return buildList.size();
    }
}

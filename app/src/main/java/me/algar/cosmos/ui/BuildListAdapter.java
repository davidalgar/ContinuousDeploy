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
import me.algar.cosmos.api.models.Build;

public class BuildListAdapter extends RecyclerView.Adapter<BuildListAdapter.ViewHolder> {
    private List<Build> buildList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.build_id)
        public TextView buildId;
        @Bind(R.id.build_responsible_name)
        public TextView responsibleName;
        @Bind(R.id.build_status)
        public TextView status;

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
        holder.responsibleName.setText(item.getResponsible());

        if(item.status == Build.STATUS_SUCCESS){
            holder.status.setText(holder.itemView.getContext().getString(R.string.success));
        }else {
            holder.status.setText(holder.itemView.getContext().getString(R.string.failure));
        }
    }

    @Override
    public int getItemCount() {
        return buildList.size();
    }
}

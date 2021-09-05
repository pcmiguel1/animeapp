package com.pcmiguel.anime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {

    private int index = 0;

    private List<Category> categoryList;
    final private ListItemClickListener mOnClickListener;
    public AdapterCategory(List<Category> categoryList, ListItemClickListener onClickListener) {
        this.categoryList = categoryList;
        this.mOnClickListener = onClickListener;

    }


    @NonNull
    @Override
    public AdapterCategory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_design, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategory.ViewHolder holder, final int position) {

        String category = categoryList.get(position).getName();
        int num = categoryList.get(position).getNum();

        holder.setData(category, num);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
            }
        });

        if (index == position) {

            if (holder.linearLayout.getBackgroundTintList() == holder.linearLayout.getContext().getResources().getColorStateList(R.color.colorBlack)) {
                holder.linearLayout.setBackgroundTintList(holder.linearLayout.getContext().getResources().getColorStateList(R.color.colorGray));
            } else {
                holder.linearLayout.setBackgroundTintList(holder.linearLayout.getContext().getResources().getColorStateList(R.color.colorBlack));

                mOnClickListener.onListItemClick(position);

            }
        } else {
            holder.linearLayout.setBackgroundTintList(holder.linearLayout.getContext().getResources().getColorStateList(R.color.colorGray));
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text_category, text_category_num;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_category = itemView.findViewById(R.id.category);
            text_category_num = itemView.findViewById(R.id.category_num);
            linearLayout = itemView.findViewById(R.id.rec_category);

        }

        public void setData(String category, int num) {

            text_category.setText(category);
            text_category_num.setText("("+ num +")");

        }
    }

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

}

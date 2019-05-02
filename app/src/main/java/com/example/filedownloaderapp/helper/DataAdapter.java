package com.example.filedownloaderapp.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filedownloaderapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<ListItems> listItems;
    private Context context;
    private OnItemClickListner mListner;

    public interface OnItemClickListner {
        void onItemClick (int postion);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        mListner = listner;
    }

    public DataAdapter(ArrayList<ListItems> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.getdata_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ListItems items = listItems.get(i);

        String likeCount = items.getLikes();
        String imageURL = items.getImageURL();

        viewHolder.picLike.setText(likeCount);
        Picasso.with(context).load(imageURL).fit().centerInside().into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView picLike;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picLike = itemView.findViewById(R.id.picLike);
            imageView = itemView.findViewById(R.id.pic_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListner != null) {
                        int postion = getAdapterPosition();
                        if (postion != RecyclerView.NO_POSITION) {
                            mListner.onItemClick(postion);
                        }
                    }
                }
            });
        }
    }
}

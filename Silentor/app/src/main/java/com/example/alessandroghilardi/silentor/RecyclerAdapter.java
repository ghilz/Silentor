package com.example.alessandroghilardi.silentor;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TodoItemHolder> {
    private ArrayList<PinItem> mTodoItems;

    public RecyclerAdapter(ArrayList<PinItem> items) {
        mTodoItems = items;
    }

    @Override
    public RecyclerAdapter.TodoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new TodoItemHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.TodoItemHolder holder, int position) {
        PinItem todoItem = mTodoItems.get(position);
        holder.bindTodoItem(todoItem);
    }

    @Override
    public int getItemCount() {
        return mTodoItems.size();
    }

    public static class TodoItemHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {

        private TextView mItemName;
        private TextView mItemDescription;

        public TodoItemHolder(View v) {
            super(v);
            mItemName = (TextView) v.findViewById(R.id.item_name);
            mItemDescription = (TextView) v.findViewById(R.id.item_description);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }


        public void bindTodoItem(PinItem item) {

            double latitude = item.getLatitude();
            double longitude = item.getLongitude();

            String coordinates = "Coordinates: Lat " + latitude + ", Long " + longitude;

            mItemDescription.setText(coordinates);
            mItemName.setText(item.getName());
        }
    }
}

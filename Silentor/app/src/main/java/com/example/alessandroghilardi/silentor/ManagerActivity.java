package com.example.alessandroghilardi.silentor;

import android.app.ListActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

public class ManagerActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ArrayList<PinItem> pinItems;
    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        dbHelper = new DatabaseHelper(this);
        pinItems = new ArrayList<>();

        pinItems.addAll(dbHelper.getAllItems());

        //Toolbar myToolbar = findViewById(R.id.tool_bar);
        //setSupportActionBar(myToolbar);

        // Display icon in the toolbar
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }
        else{
            mGridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
        }

        mAdapter = new RecyclerAdapter(pinItems);
        mRecyclerView.setAdapter(mAdapter);

        // This will attach the ItemTouchListener to the RecyclerView
        setRecyclerViewItemTouchListener();
    }

    private void setRecyclerViewItemTouchListener() {
        // 1 - You create the callback and tell it what events to listen for.
        // It takes two parameters, one for drag directions and one for swipe directions, but
        // you’re only interested in swipe, so you pass 0 to inform the callback not to respond to drag events.
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder viewHolder1)      {
                // 2 - You return false in onMove because you don’t want to perform any special behavior here.
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // 3 - onSwiped is called when you swipe an item in the direction specified in the
                // ItemTouchHelper. Here, you request the viewHolder parameter passed for the
                // position of the item view, then you remove that item from your list of photos.
                // Finally, you inform the RecyclerView adapter that an item has been removed at a
                // specific position.
                int position = viewHolder.getAdapterPosition();
                PinItem item = pinItems.remove(position);
                // delete the item from the DB
                dbHelper.deleteItem(item);
                mRecyclerView.getAdapter().notifyItemRemoved(position);

            }
        };

        // 4 - You initialize the ItemTouchHelper with the callback behavior you defined, and
        // then attach it to the RecyclerView.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

}

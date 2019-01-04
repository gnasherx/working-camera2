package com.mobicule.documentscanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobicule.documentscanner.models.SingleDocument;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<SingleDocument> list = new ArrayList<>();
    CustomAdapter adapter;

    FloatingActionButton fab;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle bundle = getIntent().getExtras();

        title = bundle.getString("title");


        Log.d("ListActivity", "onCreate: title:  " + title);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(title);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.cameraFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, CameraActivity.class));
            }
        });

        recyclerView = findViewById(R.id.recyclerList);
        adapter = new CustomAdapter(ListActivity.this, list);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(ListActivity.this, LinearLayoutManager.VERTICAL, false);
        verticalLayoutManager.setReverseLayout(true);
        verticalLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);

        File f = new File(Environment.getExternalStorageDirectory() + "/" + "DocumentScanner" + "/" + title + "/" + "imageDir");
        File[] files = f.listFiles();
        if (files != null) {
            for (int j = 0; j < files.length; j++) {
                Bitmap bitmap = BitmapFactory.decodeFile(files[j].getPath());
                list.add(new SingleDocument("PAN CARD", bitmap));
                adapter.notifyDataSetChanged();
            }
        }

        list.add(new SingleDocument("Pan card 1"));
        list.add(new SingleDocument("Pan card 2"));
        list.add(new SingleDocument("Pan card 3"));
        list.add(new SingleDocument("Pan card 4"));

        adapter.notifyDataSetChanged();
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        Context context;
        List<SingleDocument> list;

        public CustomAdapter(Context context, List<SingleDocument> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            SingleDocument item = list.get(position);
            holder.tv.setText(item.getName());
            if (item.getBitmap() != null) {
                holder.iv.setImageBitmap(item.getBitmap());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            ImageView iv;

            public CustomViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.textview_name);
                iv = itemView.findViewById(R.id.imageview);
            }
        }
    }

}

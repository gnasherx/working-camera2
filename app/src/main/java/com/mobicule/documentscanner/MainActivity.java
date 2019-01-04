package com.mobicule.documentscanner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobicule.documentscanner.models.Document;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getName();
    private Button mOpenCameraButton;

    ArrayList<Document> list;
    GridView gridView;

    int[] images = {
            R.drawable.bag, R.drawable.pan, R.drawable.ada, R.drawable.chk, R.drawable.dri, R.drawable.vot
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("DocumentScanner");

        gridView = findViewById(R.id.gridview);
        list = new ArrayList<>();
        list.add(new Document("PAN Card"));
        list.add(new Document("Aadhar Card"));
        list.add(new Document("Driving Licence"));
        list.add(new Document("Voting Card"));
        list.add(new Document("Passport"));
        list.add(new Document("Bank Cheque"));

        MainActivity.CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!Utils.isNetworkAvailable(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No internet connectivity!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    String title = list.get(position).getName();
                    //Create the bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v1 = getLayoutInflater().inflate(R.layout.single_element, null);
            TextView name = v1.findViewById(R.id.name);
            ImageView image = v1.findViewById(R.id.image);

            name.setText(list.get(position).getName());
            image.setImageResource(images[position]);

            return v1;
        }
    }


}

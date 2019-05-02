package com.example.filedownloaderapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.filedownloaderapp.helper.DataAdapter;
import com.example.filedownloaderapp.helper.DetailActivity;
import com.example.filedownloaderapp.helper.ListItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataAdapter.OnItemClickListner {

    public static final String EXTRA_URL = "imageURL";
    public static final String EXTRA_LIKES = "likes";

    //endpoint url
    private static final String URL_DATA = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private ArrayList<ListItems> listItems;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.data_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        //function for fetching data from endpoint
        mRequestQueue = Volley.newRequestQueue(this);
        loadRecyclerData();
    }

    private void loadRecyclerData() {
        final ProgressDialog progressDialog = new ProgressDialog(this );
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        //use volley for fetching data
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_DATA,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");
                    for (int i = 0; i<jsonArray.length(); i++ ) {
                        JSONObject hit = jsonArray.getJSONObject(i);

                        String likeCount = hit.getString("likes");
                        String imageURL = hit.getString("webformatURL");

                        listItems.add(new ListItems(imageURL, likeCount));
                    }
                    adapter = new DataAdapter(listItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListner(MainActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int postion) {

        Intent detailIntetn = new Intent(this, DetailActivity.class);
        ListItems clickItem = listItems.get(postion);

        //save data in intent and use in next activity
        detailIntetn.putExtra(EXTRA_URL, clickItem.getImageURL());
        detailIntetn.putExtra(EXTRA_LIKES, clickItem.getLikes());

        startActivity(detailIntetn);
    }
}

package com.example.listproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ListAdapter adapter;
    String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";
    ArrayList<listpojos> list = new ArrayList<listpojos>();
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading Please wait...");
        dialog.show();
        callAPI();
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        callAPI();
    }

    private void callAPI()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                try {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.e("Response :", response + "");
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle(response.getString("title"));
                    JSONArray array = response.getJSONArray("rows");
                    listpojos pojos;
                    for (int i=0; i<array.length(); i++) {
                        pojos=new listpojos();
                        JSONObject obj = array.getJSONObject(i);
                        pojos.title=obj.getString("title");
                        pojos.description=obj.getString("description");
                        pojos.imageHref=obj.getString("imageHref");
                        list.add(pojos);
                    }
                    dialog.dismiss();
                    adapter = new ListAdapter(MainActivity.this, list);
                    listView.setAdapter(adapter);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                swipeRefreshLayout.setRefreshing(false);
                dialog.dismiss();
            }
        });
        queue.add(jsObjRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            dialog.setMessage("Refreshing., Please wait...");
            dialog.show();
            callAPI();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

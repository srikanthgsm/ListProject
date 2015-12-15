package com.telstra.mobile.myproficiency;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//Appcompat Actionbar is used for providing pull down to refresh in current activity
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout pull_down_to_refresh;
    private ListView generic_info_listview;
    private GenericListAdapter generic_info_listadapter;
    String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";
    ArrayList<JSONResponseModel> itemList = new ArrayList<JSONResponseModel>();
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialization
        generic_info_listview = (ListView) findViewById(R.id.generic_info_listview);
        pull_down_to_refresh = (SwipeRefreshLayout) findViewById(R.id.pull_down_to_refresh);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading Please wait...");


        dialog.show();
        invokeAPICall(); // method that handshaking with server for json response

        pull_down_to_refresh.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        invokeAPICall();
    }

    private void invokeAPICall()
    {
        RequestQueue VolleyReqQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    pull_down_to_refresh.setRefreshing(false);
                    Log.e("Response :", response + "");

                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle(response.getString("title"));

                    JSONArray array = response.getJSONArray("rows");
                    JSONResponseModel pojos;
                    for (int i=0; i<array.length(); i++) {
                        pojos=new JSONResponseModel();
                        JSONObject obj = array.getJSONObject(i);
                        pojos.title=obj.getString("title");
                        pojos.description=obj.getString("description");
                        pojos.imageHref=obj.getString("imageHref");
                        itemList.add(pojos);
                    }
                    dialog.dismiss();
                    generic_info_listadapter = new GenericListAdapter(MainActivity.this, itemList);
                    generic_info_listview.setAdapter(generic_info_listadapter);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pull_down_to_refresh.setRefreshing(false);
                dialog.dismiss();
            }
        });
        VolleyReqQueue.add(jsObjRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) { // onclicking the refresh button in menu item
            dialog.setMessage("Refreshing., Please wait...");
            dialog.show();
            invokeAPICall();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

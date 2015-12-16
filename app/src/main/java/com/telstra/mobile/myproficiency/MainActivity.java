package com.telstra.mobile.myproficiency;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.telstra.mobile.adapter.GenericListAdapters;
import com.telstra.mobile.model.JSONResponseModel;
import com.telstra.mobile.utils.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

//Appcompat Actionbar is used for providing pull down to refresh in current activity
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public static ResponseParser responseParser = new ResponseParser();
    String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";
    ArrayList<JSONResponseModel> itemList = new ArrayList<JSONResponseModel>();
    ArrayList<JSONResponseModel.InfoListItem> InfoListItem = new ArrayList<JSONResponseModel.InfoListItem>();
    ProgressDialog dialog;
    private SwipeRefreshLayout pull_down_to_refresh;
    private ListView generic_info_listview;
    private RelativeLayout error_layout;
    private TextView errormsg;
    private GenericListAdapters generic_info_listadapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialization
        error_layout = (RelativeLayout) findViewById(R.id.error_layout);
        errormsg = (TextView) findViewById(R.id.errormsg);
        generic_info_listview = (ListView) findViewById(R.id.generic_info_listview);
        pull_down_to_refresh = (SwipeRefreshLayout) findViewById(R.id.pull_down_to_refresh);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Please wait...");


        dialog.show();
        invokeAPICall(); // method that handshaking with server for json response

        pull_down_to_refresh.setOnRefreshListener(this);
    }


    @Override
    public void onRefresh() {
        invokeAPICall();
    }

    private void invokeAPICall() {
        error_layout.setClickable(false);
        if (Config.isNetworkAvailable(this)) {
            error_layout.setVisibility(View.GONE);
            RequestQueue VolleyReqQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        pull_down_to_refresh.setRefreshing(false);
                        Log.e("Response :", response + "");

                        String splashResult = response.toString();
                        if (splashResult != null && !splashResult.equals("")) {
                            InputStream splashInputStream = new ByteArrayInputStream(
                                    splashResult.getBytes());
                            JSONResponseModel parsedJsonOutputObj = (JSONResponseModel) responseParser
                                    .ParserInputStreamReader(splashInputStream, 1);
                            ActionBar actionBar = getSupportActionBar();
                            actionBar.setTitle(parsedJsonOutputObj.title);
                            InfoListItem = parsedJsonOutputObj.rows;
                        }
                        dialog.dismiss();
                        generic_info_listadapters = new GenericListAdapters(MainActivity.this, InfoListItem);
                        generic_info_listview.setAdapter(generic_info_listadapters);
                    } catch (Exception e) {
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

        }else{
            errormsg.setText(R.string.check_network_connection);
            error_layout.setVisibility(View.VISIBLE);
            error_layout.setOnClickListener(this);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.error_layout:
                dialog.setMessage("Loading., Please wait...");
                dialog.show();
                invokeAPICall();
                break;
        }
    }
}

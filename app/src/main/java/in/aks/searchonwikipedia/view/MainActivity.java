package in.aks.searchonwikipedia.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import in.aks.searchonwikipedia.R;
import in.aks.searchonwikipedia.model.Page;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private SearchRecyclerViewAdapter recyclerViewAdapter;
    private List<Page> pageList;
    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = MainActivity.this;
        pageList = new ArrayList<>();
        httpClient = new AsyncHttpClient();

        /*final DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white_24dp, getApplicationContext().getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        inti();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    void inti() {
        RecyclerView searchResultRecyclerView = findViewById(R.id.search_items);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new SearchRecyclerViewAdapter(mContext, pageList);
        searchResultRecyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.getItem(0);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchItem.expandActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String url = "https://en.wikipedia.org//w/api.php?action=query" +
                        "&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1" +
                        "&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch=" + s + "&gpslimit=10";
                pageList.clear();
                recyclerViewAdapter.clearData();
                httpClient.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
//                            response.getBoolean("batchcomplete");
//                            response.getJSONObject("continue");
                            JSONObject queryObj = response.getJSONObject("query");
//                            queryObj.getJSONArray("redirects");
                            JSONArray pagesArr = queryObj.getJSONArray("pages");
                            for (int i = 0; i < pagesArr.length(); i++) {
                                JSONObject pageObj = pagesArr.getJSONObject(i);
                                Page page = new Page(pageObj);
                                pageList.add(page);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerViewAdapter.notifyDataSetChanged();
                            }
                        }, 200);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("onFailure", "statusCode : " + statusCode);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String url = "https://en.wikipedia.org//w/api.php?action=query" +
                        "&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1" +
                        "&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch=" + s + "&gpslimit=10";
                pageList.clear();
                recyclerViewAdapter.clearData();
                httpClient.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
//                            response.getBoolean("batchcomplete");
//                            response.getJSONObject("continue");
                            JSONObject queryObj = response.getJSONObject("query");
//                            queryObj.getJSONArray("redirects");
                            JSONArray pagesArr = queryObj.getJSONArray("pages");
                            for (int i = 0; i < pagesArr.length(); i++) {
                                JSONObject pageObj = pagesArr.getJSONObject(i);
                                Page page = new Page(pageObj);
                                pageList.add(page);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerViewAdapter.notifyDataSetChanged();
//                                Toast.makeText(MainActivity.this, "pages size : "+pageList.size(), Toast.LENGTH_SHORT).show();
                            }
                        }, 500);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("onFailure", "statusCode : " + statusCode);
                    }
                });
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

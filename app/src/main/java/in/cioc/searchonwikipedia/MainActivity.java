package in.cioc.searchonwikipedia;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    RecyclerView searchResultRecyclerView;
    SearchRecyclerViewAdapter recyclerViewAdapter;
    List<Page> pageList;
    AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = MainActivity.this;
        pageList = new ArrayList<>();
        httpClient = new AsyncHttpClient();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
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
        });

        inti();

    }

    void inti(){
        searchResultRecyclerView = findViewById(R.id.search_items);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter= new SearchRecyclerViewAdapter(pageList);
        searchResultRecyclerView.setAdapter(recyclerViewAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.getItem(0);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true );
        searchItem.expandActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                String url = "https://en.wikipedia.org//w/api.php?action=query" +
                        "&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1" +
                        "&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch="+s+"&gpslimit=10";
                pageList.clear();
                recyclerViewAdapter.clearData();
                httpClient.get(url, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
//                            response.getBoolean("batchcomplete");
//                            response.getJSONObject("continue");
                            JSONObject queryObj = response.getJSONObject("query");
//                            queryObj.getJSONArray("redirects");
                            JSONArray pagesArr = queryObj.getJSONArray("pages");
                            for (int i=0; i<pagesArr.length(); i++){
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
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(MainActivity.this, "onFailure "+statusCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(MainActivity.this, "onFailure obj "+statusCode, Toast.LENGTH_SHORT).show();
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

    public static class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {
        List<Page> pageList;


        public static class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout mLayoutItem;
            TextView title, description;
            ImageView titleImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mLayoutItem = itemView.findViewById(R.id.layout_search_items);
                title = itemView.findViewById(R.id.name);
                description = itemView.findViewById(R.id.description);
                titleImage = itemView.findViewById(R.id.title_image);
            }
        }


        public SearchRecyclerViewAdapter(List<Page> pages){
            this.pageList = pages;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_items, parent, false);
            return new SearchRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Page page = pageList.get(position);

            Glide.with(mContext)
                    .load(page.getSource())
                    .into(holder.titleImage);

            holder.title.setText(page.getTitle());
            holder.description.setText(page.getLitleDescription());

            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("title", page.getTitle()));

                }
            });

        }

        @Override
        public int getItemCount() {
            return pageList.size();
        }

        public void clearData() {
            pageList.clear();
            notifyDataSetChanged();
        }
    }


}

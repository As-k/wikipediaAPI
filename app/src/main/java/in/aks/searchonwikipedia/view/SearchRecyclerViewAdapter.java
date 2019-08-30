package in.aks.searchonwikipedia.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.aks.searchonwikipedia.ImageLoader;
import in.aks.searchonwikipedia.R;
import in.aks.searchonwikipedia.model.Page;

/**
 * Created by Ashish on 30/8/19.
 */

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Page> pageList;

    SearchRecyclerViewAdapter(Context context, List<Page> pages) {
        this.mContext = context;
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

        ImageLoader imageLoader = new ImageLoader(mContext);

        holder.title.setText(page.getTitle());
        if (page.getSource() != null) {
//            Glide.with(mContext)
//                    .load(page.getSource())
//                    .into(holder.titleImage);
            imageLoader.display(page.getSource(), holder.titleImage, R.drawable.ic_search_black_24dp);
        }
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

    void clearData() {
        pageList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayoutItem;
        TextView title, description;
        ImageView titleImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayoutItem = itemView.findViewById(R.id.layout_search_items);
            title = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            titleImage = itemView.findViewById(R.id.title_image);
        }
    }
}


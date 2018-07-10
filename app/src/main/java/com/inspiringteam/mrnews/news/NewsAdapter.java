package com.inspiringteam.mrnews.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiringteam.mrnews.R;
import com.inspiringteam.mrnews.data.models.News;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class NewsAdapter extends BaseAdapter {
    private List<News> mNews = new ArrayList<>();
    private NewsFragment.NewsItemListener mItemListener;
    private Picasso mPicasso;
    private boolean isInternetAccess;

    public NewsAdapter(List<News> news, NewsFragment.NewsItemListener itemListener) {
        setList(news);
        mItemListener = itemListener;
    }

    @Override
    public int getCount() {
        return mNews.size();
    }

    @Override
    public News getItem(int position) {
        return mNews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setImageLoaderService(Picasso picasso) {
        mPicasso = picasso;
    }

    public void clearContent() {
        mNews.clear();
    }

    public void replaceData(List<News> news) {
        setList(news);
        notifyDataSetChanged();
    }

    public void setList(List<News> news) {
        mNews = checkNotNull(news);
    }

    public void setInternetAccessState(boolean isInternetAccess) {
        this.isInternetAccess = isInternetAccess;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.row_news_item, viewGroup, false);
        }
        final News currentNews = getItem(i);

        final ImageView newsImageView = rowView.findViewById(R.id.news_image_id);
        final TextView newsTitleTextView = rowView.findViewById(R.id.news_title_view);
        final TextView sourceTextView = rowView.findViewById(R.id.news_source_view);
        final ImageButton archiveImagebutton = rowView.findViewById(R.id.archive_news_ib);
        archiveImagebutton.setVisibility(View.VISIBLE);
        if (currentNews.isArchived()) {
            archiveImagebutton.setVisibility(View.GONE);
        }

        if (mPicasso != null) {
            if (isInternetAccess) {
                mPicasso.load(currentNews.getUrlToImage()).networkPolicy(NetworkPolicy.NO_CACHE)
                        .fit().into(newsImageView);
                newsTitleTextView.setText(currentNews.getTitle());
            } else {
                mPicasso.load(currentNews.getUrlToImage()).networkPolicy(NetworkPolicy.OFFLINE)
                        .fit().into(newsImageView);
                newsTitleTextView.setText(currentNews.getTitle());
            }
        }


//        sourceTextView.setText(currentNews.getSourceString());

        newsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onNewsClick(currentNews);
            }
        });

        archiveImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onArchiveNewsClick(currentNews);
            }
        });

        return rowView;
    }
}


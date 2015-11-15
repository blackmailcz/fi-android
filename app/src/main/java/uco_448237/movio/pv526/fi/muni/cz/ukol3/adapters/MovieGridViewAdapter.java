package uco_448237.movio.pv526.fi.muni.cz.ukol3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.models.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.models.MovieSection;

/**
 * Created by BlackMail on 18.10.2015.
 */
public class MovieGridViewAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

    private Context mContext;
    private ArrayList<MovieSection> mData;

    public MovieGridViewAdapter (Context context, ArrayList<MovieSection> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        if (mData != null) {
            int size = 0;
            for (MovieSection movieSection : mData) {
                size += movieSection.getMovies().size();
            }
            return size;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(mData != null) {
            int current = 0;
            for (MovieSection movieSection : mData) {
                for (Movie movie : movieSection.getMovies()) {
                    if (current == position) {
                        return movie;
                    }
                    current++;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getCountForHeader(int i) {
        if (mData != null) {
            return mData.get(i).getMovies().size();
        } else {
            return 0;
        }
    }

    @Override
    public int getNumHeaders() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        TextViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.section_header, parent, false);
            holder = new TextViewHolder();
            holder.txt = (TextView) convertView.findViewById(R.id.section_header_text);
            convertView.setTag(holder);
        } else {
            holder = (TextViewHolder) convertView.getTag();
        }
        if (mData != null) {
            holder.txt.setText(mData.get(position).getSectionName());
        }
        return convertView;
    }

    private static class ImageViewHolder {
        ImageView img;
    }

    private static class TextViewHolder {
        TextView txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.movie_grid_view_row,parent,false);
            ImageViewHolder holder = new ImageViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.movie_grid_view_row_image_view);
            convertView.setTag(holder);
        }
        if (mData != null) {
            ImageViewHolder holder = (ImageViewHolder) convertView.getTag();
            Movie currentMovie = (Movie) getItem(position);
            holder.img.setImageResource(currentMovie.getCoverPath());
        }
        return convertView;
    }
}

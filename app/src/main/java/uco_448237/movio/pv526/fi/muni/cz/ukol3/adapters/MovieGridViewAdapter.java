package uco_448237.movio.pv526.fi.muni.cz.ukol3.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.models.Movie;

/**
 * Created by BlackMail on 18.10.2015.
 */
public class MovieGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mData;

    public MovieGridViewAdapter (Context context, List<Movie> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }

    private static class ViewHolder {
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.movie_grid_view_row,parent,false);
            ViewHolder holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.movie_grid_view_row_image_view);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Movie currentMovie = (Movie) getItem(position);
        holder.img.setImageResource(currentMovie.getCoverPath());
        return convertView;
    }
}

package uco_448237.movio.pv526.fi.muni.cz.ukol3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;

import java.util.ArrayList;

import uco_448237.movio.pv526.fi.muni.cz.ukol3.R;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.Movie;
import uco_448237.movio.pv526.fi.muni.cz.ukol3.model.MovieSection;

/**
 * Created by BlackMail on 18.10.2015.
 */
public class MovieGridViewAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

    private Context context;
    private ArrayList<MovieSection> data;

    public MovieGridViewAdapter (Context context, ArrayList<MovieSection> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (this.data != null) {
            int size = 0;
            for (MovieSection movieSection : this.data) {
                size += movieSection.getMovies().size();
            }
            return size;
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(this.data != null) {
            int current = 0;
            for (MovieSection movieSection : this.data) {
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
        if (this.data != null) {
            return this.data.get(i).getMovies().size();
        } else {
            return 0;
        }
    }

    @Override
    public int getNumHeaders() {
        if (this.data != null) {
            return this.data.size();
        } else {
            return 0;
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        TextViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.section_header, parent, false);
            holder = new TextViewHolder();
            holder.txt = (TextView) convertView.findViewById(R.id.section_header_text);
            convertView.setTag(holder);
        } else {
            holder = (TextViewHolder) convertView.getTag();
        }
        if (this.data != null) {
            holder.txt.setText(this.data.get(position).getSectionName());
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
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.movie_grid_view_row,parent,false);
            ImageViewHolder holder = new ImageViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.movie_grid_view_row_image_view);
            convertView.setTag(holder);
        }
        if (this.data != null) {
            ImageViewHolder holder = (ImageViewHolder) convertView.getTag();
            Movie currentMovie = (Movie) getItem(position);
            Picasso.with(context).load(Movie.BASE_URL + currentMovie.getCoverPath()).placeholder(R.drawable.image_not_available).noFade().into(holder.img);

        }
        return convertView;
    }
}

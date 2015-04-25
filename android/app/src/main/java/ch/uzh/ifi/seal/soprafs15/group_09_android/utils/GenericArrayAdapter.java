package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This adapter is set to replace the custom ArrayAdapter. So we are able to display whatever we want
 * like not only Strings bur using whole objects and their methods (like Game.name or User.username
 * etc.
 *
 * @param <T>
 */
public abstract class GenericArrayAdapter<T> extends ArrayAdapter<T> {

    private LayoutInflater mInflater;
    private int resource;
    private int textResourceId;
    private int imageResourceId;

    public GenericArrayAdapter(Context context, int resource, int textResourceId, int imageResourceId, ArrayList<T> objects) {
        super(context, resource, textResourceId, objects);
        this.resource = resource;
        this.textResourceId = textResourceId;
        this.imageResourceId = imageResourceId;
        init(context);
    }

    public abstract void drawText(TextView textView, T object);
    public abstract void setIcon(ImageView imageView, T object);


    private void init(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(resource, parent, false);

            viewHolder.text = (TextView) convertView.findViewById(textResourceId);
            viewHolder.image = (ImageView) convertView.findViewById(imageResourceId);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        drawText(viewHolder.text, getItem(position));
        setIcon(viewHolder.image, getItem(position));

        return convertView;
    }

    private static class ViewHolder {
        TextView text;
        ImageView image;
    }

}
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
 * like not only Strings bur using whole objects and their methods (like GameBean.name or UserBean.username
 * etc.
 *
 * @param <T>
 */
public abstract class GenericArrayAdapter<T> extends ArrayAdapter<T> {

    private LayoutInflater mInflater;
    private int resource;
    private int textResourceId;
    private int textDescriptionResourceId;
    private int imageResourceId;

    public GenericArrayAdapter(Context context, int resource, int textResourceId, int textDescriptionResourceId, int imageResourceId, ArrayList<T> objects) {
        super(context, resource, textResourceId, objects);
        this.resource = resource;
        this.textResourceId = textResourceId;
        this.textDescriptionResourceId = textDescriptionResourceId;
        this.imageResourceId = imageResourceId;
        init(context);
    }

    public abstract void setText(TextView textView, T object);
    public abstract void setTextDescription(TextView textView, T object);
    public abstract void setIcon(ImageView imageView, T object, int index);


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
            viewHolder.textDescription = (TextView) convertView.findViewById(textDescriptionResourceId);
            viewHolder.image = (ImageView) convertView.findViewById(imageResourceId);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setText(viewHolder.text, getItem(position));
        setTextDescription(viewHolder.textDescription, getItem(position));
        setIcon(viewHolder.image, getItem(position), position+1);

        return convertView;
    }

    private static class ViewHolder {
        TextView text;
        TextView textDescription;
        ImageView image;
    }

}
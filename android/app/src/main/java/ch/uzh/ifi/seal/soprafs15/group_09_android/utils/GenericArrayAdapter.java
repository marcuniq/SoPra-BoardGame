package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        // Vars
        private LayoutInflater mInflater;

        public GenericArrayAdapter(Context context, ArrayList<T> objects) {
            super(context, 0, objects);
            init(context);
        }

        // Headers
        public abstract void drawText(TextView textView, T object);

        private void init(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                convertView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            drawText(vh.textView, getItem(position));

            return convertView;
        }

        static class ViewHolder {

            TextView textView;

            private ViewHolder(View rootView) {
                textView = (TextView) rootView.findViewById(android.R.id.text1);
            }
        }
    }
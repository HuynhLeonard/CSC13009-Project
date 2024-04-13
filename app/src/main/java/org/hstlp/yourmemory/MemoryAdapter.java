package org.hstlp.yourmemory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class MemoryAdapter extends ArrayAdapter<ImageView> {
    Context context;
    List<ImageView> myImageView;
    public MemoryAdapter(Context context, int resource, List<ImageView> objects) {
        super(context, resource, objects);

        this.context = context;
        this.myImageView = objects;
    }

    public int getCount() {
        if(myImageView != null)
            return myImageView.size();
        return 0;
    }

    @Override
    public ImageView getItem(int position) {
        if(myImageView != null)
            return myImageView.get(position);
        return null;
    }
    @Override
    public long getItemId(int position) {
        if(myImageView != null)
            return myImageView.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null){
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.memorable_item, null);
            holder.imageView = (ImageView)convertView.findViewById(R.id.memoryImage);
            convertView.setTag(holder);
        } else{
            holder = (Holder)convertView.getTag();
        }

        ImageView image = getItem(position);
        // Picasso.with(context).load(person.getImageUrl()).fit().into(holder.imageView);

        return convertView;
    }

    private class Holder{
        ImageView imageView;
    }
}

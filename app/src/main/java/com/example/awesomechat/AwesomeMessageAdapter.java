package com.example.awesomechat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class AwesomeMessageAdapter extends ArrayAdapter<AwesomeMessage> {

    public AwesomeMessageAdapter(@NonNull Context context, int resource, List<AwesomeMessage> messages) {
        super(context, resource,messages);
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        if(convertView == null){
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.message_item,parent,false);
        }
        ImageView photoImageView = convertView.findViewById(R.id.photoImageView);
        TextView textTextView = convertView.findViewById(R.id.textTextView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);

        AwesomeMessage message = getItem(position);

        boolean isText = message.getImageUrl() == null;

        if(isText){
            textTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            textTextView.setText(message.getText());
        }else{
            textTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getImageUrl())
                    .into(photoImageView);
        }

        nameTextView.setText(message.getName());

        return convertView;
    }
}

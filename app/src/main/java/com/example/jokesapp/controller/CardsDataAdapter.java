package com.example.jokesapp.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.jokesapp.R;
import com.example.jokesapp.model.Joke;

public class CardsDataAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private JokeLikeListener mJokeLikeListener;
    private Joke mJoke;
    private SharedPreferences mSharedPreferences;

    private boolean clicked=true;
    public CardsDataAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext=context;
        mJokeLikeListener=(JokeLikeListener) context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        //supply the layout for your card
        TextView v = (TextView)(contentView.findViewById(R.id.content));
        v.setText(getItem(position));



        ImageButton likeButton=contentView.findViewById(R.id.likeButton);

        if(mSharedPreferences.contains(getItem(position))){
            likeButton.setImageResource(R.drawable.like_filled);
            clicked=false;
        }
        else{
            clicked=true;
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "You liked this joke!", Toast.LENGTH_SHORT).show();
                if(clicked){
                    likeButton.setImageResource(R.drawable.like_filled);

                    clicked=false;
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(likeButton);
                    mJoke=new Joke(getItem(position), true);
                    mJokeLikeListener.jokeIsLiked(mJoke);
                }
                else{
                    likeButton.setImageResource(R.drawable.like_empty);
                    clicked=true;
                    mJoke=new Joke(getItem(position),false);
                    mJokeLikeListener.jokeIsLiked(mJoke);
                }
            }
        });

        ImageButton shareButton= contentView.findViewById(R.id.shareButton);
        String s=v.getText().toString();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating an ACTION_SEND Intent
                Intent intent=new Intent(Intent.ACTION_SEND);

                String shareBody=s;
                intent.setType("text,plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Mama jokes!");
                intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                v.getContext().startActivity(Intent.createChooser(intent,"Share via"));
            }
        });
        return contentView;
    }

}
package com.example.jokesapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import androidx.appcompat.app.ActionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JokeManager {

    private Context mcontext;
    SharedPreferences sharedPreferences;

    public JokeManager(Context context){
        mcontext=context;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mcontext);
    }

    public void saveJokes(Joke joke){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(joke.getJokeText(), joke.isJokeLiked());
        editor.apply();
    }

    public void deleteJoke(Joke joke){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.contains(joke.getJokeText())==true) editor.remove(joke.getJokeText()).commit();

    }

    public List<Joke> retriveJokes(){
        Map<String, ?> getAllData= sharedPreferences.getAll();
        List<Joke> allJokes=new ArrayList<>();
        for(Map.Entry<String, ?> entry: getAllData.entrySet()){
            Joke joke=new Joke(entry.getKey(),(Boolean) entry.getValue());

            if(entry.getKey().matches("variations_seed_native_stored"))
            continue;
            allJokes.add(joke);
        }
        return allJokes;
    }
}

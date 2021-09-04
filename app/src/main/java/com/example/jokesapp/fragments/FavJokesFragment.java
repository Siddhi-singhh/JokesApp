package com.example.jokesapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jokesapp.R;
import com.example.jokesapp.controller.FavJokeListAdapter;
import com.example.jokesapp.model.Joke;
import com.example.jokesapp.model.JokeManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavJokesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavJokesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    RecyclerView mRecyclerView;
    JokeManager mJokeManager;
    FavJokeListAdapter mFavJokesAdapter;
    List<Joke> mJokeList=new ArrayList<>();
    private Joke deletedJoke;

    public FavJokesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FavJokesFragment newInstance() {
        FavJokesFragment fragment = new FavJokesFragment();

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mJokeManager=new JokeManager(context);
        mJokeList.clear();
        if(mJokeManager.retriveJokes().size()>0){
            for(Joke joke: mJokeManager.retriveJokes()){
                mJokeList.add(joke);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fav_jokes, container,false);
        if(view!=null){
            mRecyclerView=view.findViewById(R.id.rv);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mFavJokesAdapter=new FavJokeListAdapter(mJokeList,getContext());
            mRecyclerView.setAdapter(mFavJokesAdapter);

            ItemTouchHelper itemTouchHelper=new ItemTouchHelper(mSimpleCallBack);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);

        }
        return view;
    }

    ItemTouchHelper.SimpleCallback mSimpleCallBack=new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position= viewHolder.getAdapterPosition();
             switch (direction){
                 case ItemTouchHelper.LEFT:
                 case ItemTouchHelper.RIGHT:

                     deletedJoke=mJokeList.get(position);
                     mJokeManager.deleteJoke(mJokeList.get(position));
                     mJokeList.remove(position);
                     mFavJokesAdapter.notifyItemRemoved(position);
                     mFavJokesAdapter.notifyDataSetChanged();

                     Snackbar.make(mRecyclerView,"JOKE IS \" REMOVED\"", Snackbar.LENGTH_SHORT).setAction("undo", new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             mJokeList.add(position, deletedJoke);
                             mJokeManager.saveJokes(deletedJoke);
                             mFavJokesAdapter.notifyItemInserted(position);
                         }
                     }).show();
                     break;

             }
        }
    };
}
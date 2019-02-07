package com.mvvmsample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mvvmsample.databinding.ActivityMainBinding;
import com.mvvmsample.view.PeopleAdapter;
import com.mvvmsample.viewmodel.PeopleViewModel;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private ActivityMainBinding activityMainBinding;
    private PeopleViewModel peopleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
        setSupportActionBar(activityMainBinding.toolbar);
        setupListPeopleView(activityMainBinding.listPeople);
        setupObserver(peopleViewModel);

    }
    public void setupObserver(Observable observable) {
        observable.addObserver(this);
    }

    private void initDataBinding() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        peopleViewModel = new PeopleViewModel(this);
        activityMainBinding.setMainViewModel(peopleViewModel);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof PeopleViewModel) {
            PeopleAdapter peopleAdapter = (PeopleAdapter) activityMainBinding.listPeople.getAdapter();
            PeopleViewModel peopleViewModel = (PeopleViewModel) observable;
            assert peopleAdapter != null;
            peopleAdapter.setPeopleList(peopleViewModel.getPeopleList());
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        peopleViewModel.reset();
    }

    private void setupListPeopleView(RecyclerView listPeople) {
        PeopleAdapter adapter = new PeopleAdapter();
        listPeople.setAdapter(adapter);
        listPeople.setLayoutManager(new LinearLayoutManager(this));
    }
}

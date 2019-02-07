package com.mvvmsample.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.mvvmsample.PeopleApplication;
import com.mvvmsample.R;
import com.mvvmsample.data.PeopleFactory;
import com.mvvmsample.data.PeopleResponse;
import com.mvvmsample.data.PeopleService;
import com.mvvmsample.model.People;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PeopleViewModel extends Observable {


    public ObservableInt peopleProgress;
    public ObservableInt peopleRecycler;
    public ObservableInt peopleLabel;
    public ObservableField<String> messageLabel;

    private List<People> peopleList;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public PeopleViewModel(@NonNull Context context) {

        this.context = context;
        this.peopleList = new ArrayList<>();
        peopleProgress = new ObservableInt(View.GONE);
        peopleRecycler = new ObservableInt(View.GONE);
        peopleLabel = new ObservableInt(View.VISIBLE);
        messageLabel = new ObservableField<>(context.getString(R.string.default_loading_people));
    }


    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }


    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }


    private void changePeopleDataSet(List<People> peoples) {
        peopleList.addAll(peoples);
        setChanged();
        notifyObservers();
    }

    public List<People> getPeopleList() {
        return peopleList;
    }

    private void fetchPeopleList() {

        PeopleApplication peopleApplication = PeopleApplication.create(context);
        PeopleService peopleService = peopleApplication.getPeopleService();

        Disposable disposable = peopleService.fetchPeople(PeopleFactory.RANDOM_USER_URL)
                .subscribeOn(peopleApplication.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PeopleResponse>() {
                    @Override public void accept(PeopleResponse peopleResponse) {
                        changePeopleDataSet(peopleResponse.getPeopleList());
                        peopleProgress.set(View.GONE);
                        peopleLabel.set(View.GONE);
                        peopleRecycler.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) {
                        messageLabel.set(context.getString(R.string.error_loading_people));
                        peopleProgress.set(View.GONE);
                        peopleLabel.set(View.VISIBLE);
                        peopleRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void onClickFabLoad(View view) {
        initializeViews();
        fetchPeopleList();
    }

    public void initializeViews() {
        peopleLabel.set(View.GONE);
        peopleRecycler.set(View.GONE);
        peopleProgress.set(View.VISIBLE);
    }

}

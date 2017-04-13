package com.einsteiny.einsteiny.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.einsteiny.einsteiny.R;
import com.einsteiny.einsteiny.fragments.ExploreFragment;
import com.einsteiny.einsteiny.fragments.ProfileFragment;
import com.einsteiny.einsteiny.fragments.UserCourseFragment;
import com.einsteiny.einsteiny.models.AllCourses;
import com.einsteiny.einsteiny.models.CourseCategory;
import com.einsteiny.einsteiny.network.EinsteinyServerClient;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class EinsteinyActivity extends AppCompatActivity {

    private int tab;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_einsteiny);
        tab = getIntent().getIntExtra("tab", 0);

        Observable<CourseCategory> artCoursesObs = EinsteinyServerClient.getInstance().getArtsCourses();
        Observable<CourseCategory> econCoursesObs = EinsteinyServerClient.getInstance().getEconomicsCourses();
        Observable<CourseCategory> compCoursesObs = EinsteinyServerClient.getInstance().getComputingCourses();
        Observable<CourseCategory> scienceCoursesObs = EinsteinyServerClient.getInstance().getScienceCourses();

        Observable<AllCourses> allCombined = Observable.zip(artCoursesObs, econCoursesObs, compCoursesObs,
                scienceCoursesObs, (art, econ, computing, science) -> new AllCourses(art, econ, computing, science));


        subscription = allCombined.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllCourses>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("DEBUG", "inside on failure");

                    }

                    @Override
                    public void onNext(AllCourses dataAndEvents) {
                        if (dataAndEvents != null) {
                            // After completing http call
                            // will close this activity and lauch main activity
                            setBottomNavigationBar(tab, dataAndEvents);

                        }

                    }
                });

    }

    private void setBottomNavigationBar(int tab, AllCourses courses) {
        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define fragments
        final Fragment explore = ExploreFragment.newInstance(courses);
        final Fragment userCourse = new UserCourseFragment();
        final PreferenceFragmentCompat profile = new ProfileFragment();

        // set passed in tab as default
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (tab) {
            case 0: {
                fragmentTransaction.replace(R.id.flContainer, explore).commit();
                break;
            }
            case 1: {
                fragmentTransaction.replace(R.id.flContainer, userCourse).commit();
                break;
            }
            case 2: {
                fragmentTransaction.replace(R.id.flContainer, profile).commit();
                break;
            }
        }

        // handle navigation selection
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.action_explore:
                                fragmentTransaction.replace(R.id.flContainer, explore).commit();
                                return true;
                            case R.id.action_user_course:
                                fragmentTransaction.replace(R.id.flContainer, userCourse).commit();
                                return true;
                            case R.id.action_profile:
                                fragmentTransaction.replace(R.id.flContainer, profile).commit();
                                return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


}
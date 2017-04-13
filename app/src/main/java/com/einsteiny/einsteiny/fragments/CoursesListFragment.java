package com.einsteiny.einsteiny.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.einsteiny.einsteiny.R;
import com.einsteiny.einsteiny.adapters.ExploreCourseAdapter;
import com.einsteiny.einsteiny.models.Course;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsyang on 4/9/17.
 */

public class CoursesListFragment extends Fragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_COURSES = "courses";

    private ArrayList<Course> courses;
    private ExploreCourseAdapter topicAdapter;

    @BindView(R.id.rvTopics)
    RecyclerView rvTopics;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    public static CoursesListFragment newInstance(String title, ArrayList<Course> courses) {
        CoursesListFragment topicListFragment = new CoursesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putSerializable(ARG_COURSES, courses);
        topicListFragment.setArguments(args);
        return topicListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        courses = (ArrayList<Course>) getArguments().getSerializable(ARG_COURSES);
        topicAdapter = new ExploreCourseAdapter(getContext(), courses);

        rvTopics.setAdapter(topicAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvTopics.setLayoutManager(layoutManager);

        tvTitle.setText(getArguments().getString(ARG_TITLE));
    }


}

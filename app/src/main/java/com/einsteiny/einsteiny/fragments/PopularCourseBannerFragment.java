package com.einsteiny.einsteiny.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.einsteiny.einsteiny.R;
import com.einsteiny.einsteiny.coursesubscribe.CourseSubscribeActivity;
import com.einsteiny.einsteiny.models.Course;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lsyang on 4/21/17.
 */

public class PopularCourseBannerFragment extends Fragment {

    private static final String COURSE_KEY = "course";

    @BindView(R.id.popularImage)
    ImageView ivImage;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    public static PopularCourseBannerFragment newInstance(Course course) {
        PopularCourseBannerFragment popularCourseBannerFragment = new PopularCourseBannerFragment();

        Bundle args = new Bundle();
        args.putParcelable(COURSE_KEY, Parcels.wrap(course));
        popularCourseBannerFragment.setArguments(args);
        return popularCourseBannerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_popular_course_banner, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Course course = Parcels.unwrap(getArguments().getParcelable(COURSE_KEY));
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        Picasso.with(getContext()).load(course.getPhotoUrl()).resize(displayWidth, 0).into(ivImage);
        tvTitle.setText(course.getTitle());

        ivImage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CourseSubscribeActivity.class);
            intent.putExtra(CourseSubscribeActivity.EXTRA_COURSE, Parcels.wrap(course));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), ivImage, "courseImage");
            getContext().startActivity(intent, options.toBundle());
        });
    }
}


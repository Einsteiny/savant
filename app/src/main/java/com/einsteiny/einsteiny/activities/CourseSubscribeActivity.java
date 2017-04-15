package com.einsteiny.einsteiny.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.einsteiny.einsteiny.R;
import com.einsteiny.einsteiny.models.Course;
import com.einsteiny.einsteiny.models.CustomUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseSubscribeActivity extends AppCompatActivity {

    @BindView(R.id.ivCourse)
    ImageView ivCourse;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.btnSubscribe)
    Button btnSubscribe;

    public static final String EXTRA_COURSE = "course";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_subscribe);

        ButterKnife.bind(this);

        final Course course = (Course) getIntent().getSerializableExtra(EXTRA_COURSE);

        tvDescription.setText(course.getDescription());
        tvTitle.setText(course.getTitle());
        supportPostponeEnterTransition();

        String photoUrl = course.getPhotoUrl();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            int displayWidth = getResources().getDisplayMetrics().widthPixels;
            Picasso.with(this).load(photoUrl).resize(displayWidth, 0).into(ivCourse,
                    new Callback() {
                        @Override
                        public void onSuccess() {
                            scheduleStartPostponedTransition(ivCourse);
                        }

                        @Override
                        public void onError() {
                            Log.d("Debug", "onError: error loading course image");
                        }
                    });
        }

        btnSubscribe.setOnClickListener(v -> {
            CustomUser.addSubscribedCourse(course);

            Intent i = new Intent(CourseSubscribeActivity.this, CourseActivity.class);
            i.putExtra(CourseActivity.EXTRA_COURSE, course);
            startActivity(i);

        });
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                });
    }
}

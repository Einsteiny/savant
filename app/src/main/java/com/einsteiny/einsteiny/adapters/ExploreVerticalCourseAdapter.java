package com.einsteiny.einsteiny.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.einsteiny.einsteiny.R;
import com.einsteiny.einsteiny.models.Course;
import com.einsteiny.einsteiny.models.CustomUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExploreVerticalCourseAdapter extends RecyclerView.Adapter<ExploreVerticalCourseAdapter.CourseViewHolder> {

    private OnItemClickListener listener;

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);


    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvSnippet)
        TextView tvDescription;

        @BindView(R.id.tvDuration)
        TextView tvDuration;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.rating)
        RatingBar rating;

        @BindView(R.id.btnLike)
        ImageButton liked;

        @BindView(R.id.tvStatus)
        TextView tvStatus;

        public CourseViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                    }
                }
            });
        }
    }

    private List<Course> courses;
    private Context context;

    public ExploreVerticalCourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View topicView = inflater.inflate(R.layout.item_topic_vertical, parent, false);
        CourseViewHolder viewHolder = new CourseViewHolder(topicView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.tvTitle.setText(course.getTitle());

        holder.tvDuration.setText(context.getResources().getQuantityString(R.plurals.days,
                course.getLessons().size(), course.getLessons().size()));
        holder.tvDescription.setText(course.getDescription());

        if (CustomUser.isCompletedCourse(course.getId())) {
            holder.tvStatus.setText("Completed");
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.title_circle_done));
        } else if (CustomUser.isSubscribedCourse(course.getId())) {
            holder.tvStatus.setText("In progress");
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.title_circle));
        } else {
            holder.tvStatus.setText("Saved");
            holder.tvStatus.setBackground(context.getResources().getDrawable(R.drawable.title_circle_inactive));
        }

        holder.rating.setRating(course.getComplexity());

        if (CustomUser.isLikedCourse(course)) {
            holder.liked.setImageResource(R.drawable.ic_heart_big);
        } else {
            holder.liked.setImageResource(R.drawable.ic_heart_outline_big);
        }

        String photoUrl = course.getPhotoUrl();
        if (photoUrl != null && !photoUrl.isEmpty()) {
//            int displayWidth = context.getResources().getDisplayMetrics().widthPixels;
            Picasso.with(context).load(photoUrl).placeholder(R.drawable.no_data_einstein).into(holder.ivImage);
        }

        holder.liked.setOnClickListener(v -> {
            if (CustomUser.isLikedCourse(course)) {
                holder.liked.setImageResource(R.drawable.ic_heart_outline_big);
                CustomUser.unlikeCourse(course);

                //TODO think if I need this functionality
//                courses.remove(course);
//                notifyDataSetChanged();
            } else {
                updateHeartButton(holder, true);
                holder.liked.setImageResource(R.drawable.ic_heart_big);
                CustomUser.likeCourse(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    private void updateHeartButton(final CourseViewHolder holder, boolean animated) {
        if (animated) {

            AnimatorSet animatorSet = new AnimatorSet();

            ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.liked, "rotation", 0f, 360f);
            rotationAnim.setDuration(300);
            rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.liked, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(300);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.liked, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(300);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    holder.liked.setImageResource(R.drawable.ic_heart_big);
                }
            });

            animatorSet.play(rotationAnim);
            animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //resetLikeAnimationState(holder);
                }
            });

            animatorSet.start();

        }
    }
}

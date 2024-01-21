package com.sunmeat.myapplication;

import static com.sunmeat.myapplication.ImageData.IMAGE_URLS;

import android.graphics.drawable.Drawable;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * АДАПТЕР ДЛЯ ПРОГРУЗКИ КАРТИНОК В СЕТКУ (ГРИД)
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ImageViewHolder> {

    private interface ViewHolderListener {

        void onLoadCompleted(ImageView view, int adapterPosition);

        void onItemClicked(View view, int adapterPosition);
    }

    private final RequestManager requestManager;
    private final ViewHolderListener viewHolderListener;

    public GridAdapter(Fragment fragment) {

        this.requestManager = Glide.with(fragment);
        this.viewHolderListener = new ViewHolderListenerImpl(fragment);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);
        return new ImageViewHolder(view, requestManager, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return IMAGE_URLS.length;
    }

    private static class ViewHolderListenerImpl implements ViewHolderListener {

        private Fragment fragment;
        private AtomicBoolean enterTransitionStarted;

        ViewHolderListenerImpl(Fragment fragment) {
            this.fragment = fragment;
            this.enterTransitionStarted = new AtomicBoolean();
        }

        @Override
        public void onLoadCompleted(ImageView view, int position) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (MainActivity.currentPosition != position) {
                return;
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return;
            }
            fragment.startPostponedEnterTransition();
        }

        @Override
        public void onItemClicked(View view, int position) {
            // Update the position.
            MainActivity.currentPosition = position;

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).
            ((TransitionSet) fragment.getExitTransition()).excludeTarget(view, true);

            ImageView transitioningView = view.findViewById(R.id.card_image);
            fragment.getFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true) // Optimize for shared element transition
                    .addSharedElement(transitioningView, transitioningView.getTransitionName())
                    .replace(R.id.fragment_container, new ImagePagerFragment(), ImagePagerFragment.class
                            .getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private final ImageView image;
        private final RequestManager requestManager;
        private final ViewHolderListener viewHolderListener;

        ImageViewHolder(View itemView, RequestManager requestManager,
                        ViewHolderListener viewHolderListener) {
            super(itemView);
            this.image = itemView.findViewById(R.id.card_image);
            this.requestManager = requestManager;
            this.viewHolderListener = viewHolderListener;
            itemView.findViewById(R.id.card_view).setOnClickListener(this);
        }

        void onBind() {
            int adapterPosition = getAdapterPosition();
            setImage(adapterPosition);
            // Set the string value of the image resource as the unique transition name for the view.
            //image.setTransitionName(String.valueOf(IMAGE_DRAWABLES[adapterPosition]));
            image.setTransitionName(IMAGE_URLS[adapterPosition]);
        }

        void setImage(final int adapterPosition) {
            // Load the image with Glide to prevent OOM error when the image drawables are very large.
            requestManager
                    //.load(IMAGE_DRAWABLES[adapterPosition])
                    .load(IMAGE_URLS[adapterPosition])
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(image, adapterPosition);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                                target, DataSource dataSource, boolean isFirstResource) {
                            viewHolderListener.onLoadCompleted(image, adapterPosition);
                            return false;
                        }
                    })
                    .into(image);
        }

        @Override
        public void onClick(View view) {
            // Let the listener start the ImagePagerFragment.
            viewHolderListener.onItemClicked(view, getAdapterPosition());
        }
    }
}
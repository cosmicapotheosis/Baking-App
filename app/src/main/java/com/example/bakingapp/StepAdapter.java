package com.example.bakingapp;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.model.Step;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StepAdapter extends
        RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private ArrayList<Step> mSteps;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public StepAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param i  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new StepAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new StepAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the step
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param stepAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param i                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull StepAdapterViewHolder stepAdapterViewHolder, int i) {
        Step step = mSteps.get(i);
        String fileExt = Files.getFileExtension(step.getThumbnailUrl());
        // Set TextViews..
        stepAdapterViewHolder.mStepShortDescTextView.setText(step.getShortDescription());
        // Set ImageView
        // Display a placeholder if a thumbnailURL doesn't exist or is the wrong type
        if (Strings.isNullOrEmpty(step.getThumbnailUrl())
            || !fileExt.equals("jpeg")
            || !fileExt.equals("jpg")
            || !fileExt.equals("png")
            || !fileExt.equals("gif")
            || !fileExt.equals("tiff")
            || !fileExt.equals("bmp")) {
            Picasso.get()
                    .load(R.drawable.download)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .fit()
                    .into(stepAdapterViewHolder.mStepThumbnailImageView);
        } else {
            Picasso.get()
                    .load(step.getThumbnailUrl())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .fit()
                    .into(stepAdapterViewHolder.mStepThumbnailImageView);
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our steps list
     */
    @Override
    public int getItemCount() {
        if (null == mSteps) return 0;
        return mSteps.size();
    }

    /**
     * Sets steps
     * @param steps
     */
    public void setmSteps(ArrayList<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public final TextView mStepShortDescTextView;
        public final ImageView mStepThumbnailImageView;

        public StepAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mStepShortDescTextView = itemView.findViewById(R.id.tv_step_short_desc);
            mStepThumbnailImageView = itemView.findViewById(R.id.iv_step_thumbnail);
            itemView.setOnClickListener(this);
        }

        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

package com.example.bakingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private ArrayList<String> mThumbnailUrls;
    private ArrayList<String> mRecipeNames;

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

    public RecipeAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new RecipeAdapterViewHolder that holds the View for each list item
     */
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the recipe
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param recipeAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        // Populate the image view
        String thumbnailUrl = mThumbnailUrls.get(position);

        Picasso.get()
                .load(thumbnailUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .fit()
                .into(recipeAdapterViewHolder.mRecipeThumbnailImageView);

        // Populate the text view
        String recipeName = mRecipeNames.get(position);
        recipeAdapterViewHolder.mRecipeNameTextView.setText(recipeName);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our recipes list
     */
    @Override
    public int getItemCount() {
        if (mRecipeNames == null || mThumbnailUrls == null || mThumbnailUrls.size() != mRecipeNames.size()) return 0;
        return mRecipeNames.size();
    }

    /**
     * Sets the mThumbnailUrls member array list
     * @param thumbnailUrls
     */
    public void setmThumbnailUrls(ArrayList<String> thumbnailUrls) {
        mThumbnailUrls = thumbnailUrls;
        notifyDataSetChanged();
    }

    /**
     * Sets the mRecipeNames member array list
     * @param recipeNames
     */
    public void setmRecipeNames(ArrayList<String> recipeNames) {
        mRecipeNames = recipeNames;
        notifyDataSetChanged();
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
                bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        public final ImageView mRecipeThumbnailImageView;
        public final TextView mRecipeNameTextView;

        public RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeNameTextView = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            mRecipeThumbnailImageView = (ImageView) itemView.findViewById(R.id.iv_recipe_thumbnail);
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

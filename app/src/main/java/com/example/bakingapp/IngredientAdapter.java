package com.example.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private ArrayList<Ingredient> mIngredients;

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param i  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new IngredientAdapterViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new IngredientAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the ingredient
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param ingredientAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param i                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull IngredientAdapterViewHolder ingredientAdapterViewHolder, int i) {
        Ingredient ingredient = mIngredients.get(i);
        // Set text views...
        ingredientAdapterViewHolder.mIngredientNameTextView.setText(ingredient.getIngredient());
        ingredientAdapterViewHolder.mIngredientQtyTextView.setText(ingredient.getQuantity().toString());
        ingredientAdapterViewHolder.mIngredientMeasTextView.setText(ingredient.getMeasure());
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our ingredients list
     */
    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    /**
     * Sets the ingredients list
     * @param ingredients
     */
    public void setmIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mIngredientNameTextView;
        public final TextView mIngredientQtyTextView;
        public final TextView mIngredientMeasTextView;

        public IngredientAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mIngredientMeasTextView = (TextView) itemView.findViewById(R.id.tv_ingredient_meas);
            mIngredientNameTextView = (TextView) itemView.findViewById(R.id.tv_ingredient_name);
            mIngredientQtyTextView = (TextView) itemView.findViewById(R.id.tv_ingredient_qty);
        }
    }
}

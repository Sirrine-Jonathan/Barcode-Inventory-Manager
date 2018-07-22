package byui_cs246.barcodeinventorymanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>
{

    class ItemViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView itemNameView;
        private final TextView quantityView;
        private final ImageView warningImage;
        private final ImageView depletedImage;

        private ItemViewHolder(View itemView)
        {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.name);
            quantityView = itemView.findViewById(R.id.quantity);
            warningImage = itemView.findViewById(R.id.warning_image);
            depletedImage = itemView.findViewById(R.id.depleted_image);
        }
    }

    private final LayoutInflater mInflater;
    private final View.OnClickListener mClickListener;
    private final View.OnLongClickListener mLongClickListener;
    private List<Item> mItems; // Cached copy of items

    ItemListAdapter(Context context, View.OnClickListener clickListener, View.OnLongClickListener longClickListener)
    {
        mInflater = LayoutInflater.from(context);
        mClickListener = clickListener;
        mLongClickListener = longClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = mInflater.inflate(R.layout.list_item, parent, false);
        itemView.setOnClickListener(mClickListener);
        itemView.setOnLongClickListener(mLongClickListener);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position)
    {
        if(mItems != null)
        {
            Item current = mItems.get(position);
            holder.itemNameView.setText(current.getProductName());
            holder.quantityView.setText(String.format("Qty: %s", current.getQuantity()));

            if(current.getQuantity() == 0)
            {
                holder.depletedImage.setVisibility(View.VISIBLE);
                holder.warningImage.setVisibility(View.INVISIBLE);
            }
            else if(current.isLowStockWarningEnabled() && current.getQuantity() <= 1)
            {
                holder.depletedImage.setVisibility(View.INVISIBLE);
                holder.warningImage.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.depletedImage.setVisibility(View.INVISIBLE);
                holder.warningImage.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            // Covers the case of data not being ready yet.
            holder.itemNameView.setText("Empty");
            holder.quantityView.setText("Qty: 0");
        }
    }

    void setItems(List<Item> items)
    {
        mItems = items;
        notifyDataSetChanged();
    }

    Item getItemAtPosition(int position)
    {
        return mItems.get(position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mItems has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount()
    {
        if(mItems != null)
            return mItems.size();
        else return 0;
    }
}
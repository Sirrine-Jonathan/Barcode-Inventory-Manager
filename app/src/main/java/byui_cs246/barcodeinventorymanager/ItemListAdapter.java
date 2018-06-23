package byui_cs246.barcodeinventorymanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>
{

    class ItemViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView itemTextView;

        private ItemViewHolder(View itemView)
        {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.text);
        }
    }

    private final LayoutInflater mInflater;
    private final View.OnClickListener mClickListener;
    private List<Item> mItems; // Cached copy of items

    ItemListAdapter(Context context, View.OnClickListener clickListener)
    {
        mInflater = LayoutInflater.from(context);
        mClickListener = clickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = mInflater.inflate(R.layout.list_item, parent, false);
        itemView.setOnClickListener(mClickListener);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position)
    {
        if(mItems != null)
        {
            Item current = mItems.get(position);
            holder.itemTextView.setText(current.getProductName() + " - " + current.getQuantity());
        }
        else
        {
            // Covers the case of data not being ready yet.
            holder.itemTextView.setText("Empty");
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
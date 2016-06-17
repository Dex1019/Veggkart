package com.example.prince.vegkart;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aniPC on 16-Jun-16.
 */
public class MyListViewAdapter extends ArrayAdapter<Product> {
    private List<Product> item;
    private Context context1;
    private MyViewHolder vhold;
    public static int count = 0;

    public MyListViewAdapter(Context _context, List<Product> _item) {
        super(_context, R.layout.activity_main);
        this.item = _item;
        this.context1 = _context;
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return item.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Product getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public android.view.View getView(int position, View convertView, ViewGroup parent) {
        try {
            View row = convertView;

            if (row == null) {

                row = LayoutInflater.from(context1).inflate(R.layout.listview_row, null, false);
                vhold = new MyViewHolder();
                vhold.initialize(row); //MyViewHolder class function; devDefined
                row.setTag(vhold);

            } else {
                vhold = (MyViewHolder) row.getTag();
            }

            vhold.bind(context1, item, position);//MyViewHolder class function; devDefined

            return row;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    class MyViewHolder extends java.lang.Object {
        //public View view { get; set; }
        public TextView txtPName;
        public TextView txtDescription;
        public TextView txtPrice;
        public EditText quant;

            /*
            //btn to increment value of EditText quant by 1
            public ImageButton btnUP;
            //btn to decrement value of Edittext quant by 1
            public ImageButton btnDOWN;
            */

        public NumberPicker numPick;

        public void initialize(View row) {
            txtPName = (TextView) row.findViewById(R.id.textViewX1);
            txtDescription = (TextView) row.findViewById(R.id.textViewX2);
            txtPrice = (TextView) row.findViewById(R.id.textViewX3);

            numPick = (NumberPicker) row.findViewById(R.id.numPicker);
            numPick.setMaxValue(100);
            numPick.setMinValue(0);
            numPick.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    int pos = (int) (picker).getTag();
                    item.get(pos).quantity = String.valueOf((picker).getValue());
                }
            });

        }


        //this function binds Prduct class variables to viewHolder variables
        public void bind(Context context, List<Product> item, int position) {
            this.txtPName.setText(item.get(position).productName);
            this.txtDescription.setText(item.get(position).description);
            this.txtPrice.setText(item.get(position).price);
            this.numPick.setTag(position);
            this.numPick.setValue(Integer.parseInt(item.get(position).quantity));
        }

    }


}

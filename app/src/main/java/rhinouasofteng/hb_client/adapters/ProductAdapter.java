package rhinouasofteng.hb_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rhinouasofteng.hb_client.R;
import rhinouasofteng.hb_client.models.Product;

/**
 * Created by benwa_000 on 10/31/2015.
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, List<Product> products) {
        super(context, R.layout.item_product, products);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }

        TextView textID = (TextView)convertView.findViewById(R.id.productID);
        TextView textName = (TextView)convertView.findViewById(R.id.productName);
        TextView textCount = (TextView)convertView.findViewById(R.id.productCount);
        TextView textReorder = (TextView)convertView.findViewById(R.id.productReorder);

        textID.setText(product.getID().toString());
        textName.setText(product.getName());
        textCount.setText(product.getCount().toString());
        textReorder.setText(product.getReorder().toString());

        return convertView;
    }
}

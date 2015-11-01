package rhinouasofteng.hb_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import rhinouasofteng.hb_client.adapters.ProductAdapter;
import rhinouasofteng.hb_client.models.Product;

public class InventoryActivity extends AppCompatActivity {
    //TextView IDText;
    ListView ProductListView;
    ProductAdapter productAdapter;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        //IDText = (TextView)this.findViewById(R.id.inventory_id);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, this.productList);
        ProductListView = (ListView)this.findViewById(R.id.product_list_view);
        ProductListView.setAdapter(productAdapter);
        ProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                Product requestedProduct = (Product)ProductListView.getItemAtPosition(position);
                String productID = requestedProduct.getID().toString();

                intent.putExtra("ID", productID);
                startActivity(intent);
            }
        });

        String url = "http://192.168.1.69:8080/hb_server/api0/products";
        new ContactServerTask().execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void BackClick(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    private class ContactServerTask extends AsyncTask<String, Void, Boolean> {

        JSONArray responseArray = null;
        JSONObject responseObject = null;

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                HttpGet httppost = new HttpGet(params[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                StatusLine stat = response.getStatusLine();
                int status = stat.getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    responseArray = new JSONArray(data);
                    //System.out.println(obj);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            try {
                for (int i = 0; i < responseArray.length(); i++) {
                    responseObject = responseArray.getJSONObject(i);
                    Product temp_product = new Product();
                    temp_product.setID(UUID.fromString(responseObject.getString("ID")));
                    temp_product.setName(responseObject.getString("name"));
                    temp_product.setUnit(responseObject.getString("unit"));
                    temp_product.setCost(responseObject.getDouble("cost"));
                    temp_product.setCount(responseObject.getInt("count"));
                    temp_product.setReorder(responseObject.getInt("reorder"));

                    productList.add(temp_product);
                }

                //IDText.setText(productList.get(0).getName());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

package rhinouasofteng.hb_client;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
    ListView ProductListView;
    ProductAdapter productAdapter;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

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

        //String url = "http://54.187.159.168:8080/hb_server/api0/products";
        String url = "http://localhost:8080/hb_server/api0/products";
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

    public void TestClick(View view) {
        final Dialog testDialog = new Dialog(InventoryActivity.this);
        testDialog.setContentView(R.layout.activity_main_dialog);
        testDialog.setTitle("Test Dialog");
        testDialog.setCancelable(true);

        Button DoneButton = (Button)testDialog.findViewById(R.id.buttonDone);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText testText = (EditText)testDialog.findViewById(R.id.editText);
                String temp = testText.getText().toString();
                displayText(Integer.parseInt(temp));
            }
        });
        testDialog.show();
    }

    private void displayText(int input) {
        TextView theMidView = (TextView)this.findViewById(R.id.textViewMid);
        theMidView.setText(input);
    }

    private class ContactServerTask extends AsyncTask<String, Void, Boolean> {

        JSONArray responseArray = null;
        JSONObject responseObject = null;

        @Override
        protected Boolean doInBackground(String... params) {
            HttpGet httppost = new HttpGet(params[0]);
            HttpClient httpclient = new DefaultHttpClient();
            try {
                HttpResponse response = httpclient.execute(httppost);

                StatusLine stat = response.getStatusLine();
                int status = stat.getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    responseArray = new JSONArray(data);
                    System.out.println(responseArray);
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
                }

                productAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                System.out.println(e);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }
    }
}
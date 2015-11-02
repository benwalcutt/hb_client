package rhinouasofteng.hb_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class ProductActivity extends AppCompatActivity {
    TextView product_ID;
    TextView product_Name;
    TextView product_Count;

    public static final int HTTPOK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product_ID = (TextView)this.findViewById(R.id.product_ID);
        product_Name = (TextView)this.findViewById(R.id.product_Name);
        product_Count = (TextView)this.findViewById(R.id.product_Count);
        String incomingProductID = this.getIntent().getStringExtra("ID");

        String url = "http://54.187.159.168:8080/hb_server/api0/products/" + incomingProductID;

        new ContactServerTask().execute(url);
    }

    public void BackClick(View view) {
        Intent intent = new Intent(getApplicationContext(), InventoryActivity.class);
        startActivity(intent);
    }

    private class ContactServerTask extends AsyncTask<String, Void, Boolean> {
        JSONArray responseArray;
        JSONObject responseObject;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                HttpGet method = new HttpGet(params[0]);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(method);

                StatusLine stat = response.getStatusLine();
                int status = stat.getStatusCode();

                if (status == HTTPOK) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    responseArray = new JSONArray(data);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                responseObject = responseArray.getJSONObject(0);

                product_ID.setText(responseObject.getString("ID"));
                product_Name.setText(responseObject.getString("name"));
                product_Count.setText(Integer.toString(responseObject.getInt("count")));

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

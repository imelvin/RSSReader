package im.elvin.rssreader.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import im.elvin.rssreader.R;
import im.elvin.rssreader.constant.Constant;

public class AddFeedActivity extends Activity {

    private EditText feedAddressInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);

        feedAddressInput = (EditText) findViewById(R.id.feed_address_input);

        feedAddressInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String feedAddress = feedAddressInput.getText().toString();
                    if (feedAddress != null && feedAddress.length() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString("feed_address", feedAddress);
                        getIntent().putExtras(bundle);
                        setResult(Constant.RESULT_OK, getIntent());
                        finish();
                    }
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_feed, menu);
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
}

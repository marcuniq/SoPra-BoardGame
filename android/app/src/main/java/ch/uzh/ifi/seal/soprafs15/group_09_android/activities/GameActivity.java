package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /* DEBUG only: create imageViews which means add camels to the Relative Layout.
        *  First: for field1 add a geen, then a blue camel and then do it also for
        *  field2 but vice versa.
        *  There seems to be a problem with the margin and the tob/bottom assignment
        *  need to have a look at it again (TODO)*/
        RelativeLayout field1 = (RelativeLayout) findViewById(R.id.field1);
        field1.addView(addCamel(10, R.drawable.camel_green));
        field1.addView(addCamel(20, R.drawable.camel_blue));

        RelativeLayout field2 = (RelativeLayout) findViewById(R.id.field2);
        field2.addView(addCamel(20, R.drawable.camel_blue));
        field2.addView(addCamel(10, R.drawable.camel_green));
    }

    private ImageView addCamel(int margin, int imageResourceId){

        ImageView image = new ImageView(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        /* TODO: correctly add margin and top/bottom allignment!!*/
        params.setMargins(margin, 0, 0, margin); // ( left, top, right, bottom )
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        image.setImageResource(imageResourceId);

        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

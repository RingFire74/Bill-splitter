package myapplication.vedha.example.com.bill_splitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class contactUs extends AppCompatActivity {

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv = findViewById(R.id.lv);
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<Map<String, String>> list = new ArrayList<>();
        HashMap<String,String> map ;

//        if (c.moveToFirst()) {
//            do {
////                numbers.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
////                contacts.add(c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
//                map = new HashMap<String, String>();
//                map.put("numbers", c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
//                map.put("contacts", c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
//                list.add(map);
//
//            } while (c.moveToNext());
//        }
        map = new HashMap<String, String>();
        map.put("numbers","9500739810");
        map.put("contacts","Gokul");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("numbers","8939206725");
        map.put("contacts","Karthik");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("numbers","9944933857");
        map.put("contacts","Aadithya");
        list.add(map);

        String [] from = {"contacts", "numbers"};
        int [] to = {android.R.id.text1,
                android.R.id.text2};

        SimpleAdapter ad = new SimpleAdapter(this, list,android.R.layout.simple_list_item_2, from,to);
        lv.setAdapter(ad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView phonetv=view.findViewById(android.R.id.text2);
                //Toast.makeText(MainActivity.this, contactstv.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phonetv.getText().toString()));
                startActivity(intent);
            }
        });

    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_btn:
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
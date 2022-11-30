package myapplication.vedha.example.com.bill_splitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contact extends AppCompatActivity implements Connector, TextWatcher {

    ArrayList<String> list3,list4,list5;
    EditText e1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.PhoneLookup._ID};
        String selection = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?";
        String[] selectionArguments = {"a"};
        ListView lv = findViewById(R.id.lv);
        e1 = findViewById(R.id.edit1);
        e1.addTextChangedListener(Contact.this);
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        List<Map<String, String>> list = new ArrayList<>();
        ArrayList<String> list1=new ArrayList<String>(),list2=new ArrayList<String>();
        list3=new ArrayList<String>();
        list4=new ArrayList<String>();
        list5=new ArrayList<String>();
        HashMap<String, String> map;
        int flag1 = 0, flag2 = 0, flag3 = 0;

        if (c.moveToFirst()) {
            do {
//                numbers.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
//                contacts.add(c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
                map = new HashMap<String, String>();
                map.put("numbers", c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                map.put("contacts", c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
                list1.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                list2.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                list.add(map);

            } while (c.moveToNext());
        }
        String[] from = {"contacts", "numbers"};
        int[] to = {android.R.id.text1,
                android.R.id.text2};

        SimpleAdapter ad = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, from, to);
        lv.setAdapter(new ContactAdapter(list2, list1, Contact.this, Contact.this));
//        lv.setAdapter(ad);

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView phonetv = view.findViewById(android.R.id.text2);
//                TextView nametv = view.findViewById(android.R.id.text1);
//                Intent i = new Intent();
//                i.putExtra("msg1", phonetv.getText().toString());
//                i.putExtra("msg", nametv.getText().toString());
//                setResult(1, i);
//                finish();
//            }
//        });
        Button b2 = findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putStringArrayListExtra("msg",list3);
                i.putStringArrayListExtra("msg1",list4);
                setResult(1, i);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_btn:
                Toast.makeText(getApplicationContext(), "Menu", Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home:
                Intent i = new Intent();
                i.putStringArrayListExtra("msg",list5);
                i.putStringArrayListExtra("msg1",list5);
                setResult(1, i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCheckedBox(String s, int pos, String t, String s1) {
        if(t.equals("true")){
            list3.add(s);
            list4.add(s1);
        } else {
            list3.remove(s);
            list4.remove(s1);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        List<Map<String, String>> list = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<String>(), list2 = new ArrayList<String>();
        list3 = new ArrayList<String>();
        list4 = new ArrayList<String>();
        list5 = new ArrayList<String>();
        HashMap<String, String> map;
        int flag1 = 0, flag2 = 0, flag3 = 0;

        if (c.moveToFirst()) {
            do {
//                numbers.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
//                contacts.add(c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
                if (c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))).startsWith(e1.getText().toString())) {
                    map = new HashMap<String, String>();
                    map.put("numbers", c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    map.put("contacts", c.getString((c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))));
                    list1.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    list2.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    list.add(map);
                }

            } while (c.moveToNext());
        }
        String[] from = {"contacts", "numbers"};
        int[] to = {android.R.id.text1,
                android.R.id.text2};

        SimpleAdapter ad = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, from, to);
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(new ContactAdapter(list2, list1, Contact.this, Contact.this));

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
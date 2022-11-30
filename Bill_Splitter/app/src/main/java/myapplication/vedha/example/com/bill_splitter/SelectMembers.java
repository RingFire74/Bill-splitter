package myapplication.vedha.example.com.bill_splitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectMembers extends AppCompatActivity {
    SQLiteDatabase db;
    StringBuffer buffer = new StringBuffer();
    int f = 0;
    String s, category;
    int t;
    ListView listView;
    ArrayAdapter adap1,adap2;
    ArrayList<String> list, list2, cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_members);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Spinner s1 = findViewById(R.id.spinner1);
        final Spinner s2 = findViewById(R.id.spinner2);
        final EditText e1 = findViewById(R.id.edit1);
        list = new ArrayList<String>();
        cat = new ArrayList<String>();
        cat.add("Travel");
        cat.add("Food and Beverages");
        cat.add("Accommodation");
        cat.add("Purchases");
        cat.add("Other");

        listView = findViewById(R.id.list1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectMembers.this);
                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete "+list2.get(i)+" ?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher_background);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
                        list.add(list2.get(i));
                        adap1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_class, list);
                        adap1.setDropDownViewResource(R.layout.spinner_dropdown);
                        s1.setAdapter(adap1);
                        list2.remove(i);
                        listView.setAdapter(new CustomAdapter(list2, list2, SelectMembers.this, SelectMembers.this));
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        // dialog.cancel();
                    }
                });
                // Setting Netural "Cancel" Button
                // Showing Alert Message
                alertDialog.show();
            }
        });

        db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);
        SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();
//                edit.clear();
        String q =sf.getString("Trip_no","0");
        final Integer t_no = Integer.parseInt(q);

        Cursor c = db.rawQuery("SELECT name FROM participants where t_id="+t_no+"", null);
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }


        adap1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_class, list);
        adap1.setDropDownViewResource(R.layout.spinner_dropdown);
        s1.setAdapter(adap1);
        adap2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_class, cat);
        adap2.setDropDownViewResource(R.layout.spinner_dropdown);
        s2.setAdapter(adap2);
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = cat.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        list2 = new ArrayList<String>();
        listView = findViewById(R.id.list1);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button b1 = findViewById(R.id.b1);
        String temp = null;


        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String temp = null;
                int pid = 0;
                int amt = 0;
                if (f == 0) {
                    Cursor c = db.rawQuery("SELECT COUNT(*) FROM event " + "", null);
                    if (c.moveToNext()) {
                        temp = c.getString(0);
                    }
                    t = Integer.parseInt(temp);


                }

                //amt = Integer.parseInt(e2.getText().toString());
                Cursor c = db.rawQuery("SELECT count(*) FROM participants WHERE t_id=" + t_no + "", null);
                if (c.moveToFirst()) {
                    temp = c.getString(0);
                }
                pid = Integer.parseInt(temp);
                db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);
                SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sf.edit();
//                edit.clear();
                String q =sf.getString("Trip_no","0");
                Integer t_no = Integer.parseInt(q);




                list2.add(s);
                list.remove(s);
                listView.setAdapter(new CustomAdapter(list2, list2, SelectMembers.this, SelectMembers.this));
                s1.setAdapter(adap1);

            }
        });

        Button b2 = findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            String temp = null;

            @Override
            public void onClick(View view) {
                if (list2.size() < 1 || e1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter a event name and atleast 1 participant please.", Toast.LENGTH_SHORT).show();
                } else {
                    int t = 0;
                    int flag = 0;
                    int x = 0;
                    String ename[] = null;

                    if (f == 0) {
                        Cursor c = db.rawQuery("SELECT COUNT(*) FROM event WHERE t_id =" + t_no + "", null);
                        if (c.moveToNext()) {
                            temp = c.getString(0);
                        }

                        t = Integer.parseInt(temp);
                        for (int i = 0; i < list2.size(); i++) {
                            Cursor d = db.rawQuery("SELECT id FROM participants WHERE name='" + list2.get(i) + "' AND  t_id =" + t_no + "", null);
                            if (d.moveToNext()) {
                                temp = d.getString(0);
                            }
                            int pid = Integer.parseInt(temp);
                            db.execSQL("INSERT INTO eparticipants VALUES(" + t_no + "," + t + "," + pid + ",'" + list2.get(i) + "');");
                        }

                        Cursor d = db.rawQuery("SELECT e_name FROM event " + "where e_name= '" + e1.getText().toString() + "'", null);
                        if (!d.moveToNext()) {
                            String catS = s2.getSelectedItem().toString();
                            db.execSQL("INSERT INTO event VALUES(" + t_no + "," + t + ",'" + e1.getText().toString() + "','" + catS + "');");
                            Intent i = new Intent(SelectMembers.this, EnterInvesment.class);
//                i.putExtra("eventNo",t);
                            i.putStringArrayListExtra("epart", list2);
                            i.putExtra("ename", e1.getText().toString());
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter unique Event name", Toast.LENGTH_LONG).show();
                        }
                        x = 0;
                    }


                }
            }
        });


    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
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
                buffer.append("1.Choose the category of the event " + "\n");
                buffer.append("2.Enter the event name and choose the event participants" + "\n");
                showMessage("select event and participants", buffer.toString());

                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
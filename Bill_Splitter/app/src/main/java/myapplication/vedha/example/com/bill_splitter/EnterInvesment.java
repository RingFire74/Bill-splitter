package myapplication.vedha.example.com.bill_splitter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import static android.icu.text.MessagePattern.ArgType.SELECT;

public class EnterInvesment extends AppCompatActivity {
    SQLiteDatabase db;
    StringBuffer buffer = new StringBuffer();
    int f = 0;
    String s;
    int t = 0,e_no=0;
    ListView listView;
    ArrayAdapter adap1;
    ArrayList<String> list,list2,list3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_invesment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Spinner s1 = findViewById(R.id.spinner1);
        final EditText e1 = findViewById(R.id.edit1);
        list = new ArrayList<String>();
        db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS payment(e_id INTEGER,id INTEGER,amount INTEGER,FOREIGN KEY(e_id)REFERENCES event(e_id),FOREIGN KEY(id)REFERENCES participants(id));");
        SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();
//                edit.clear();
        String q =sf.getString("Trip_no","0");
        final Integer t_no = Integer.parseInt(q);
        listView = findViewById(R.id.list1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EnterInvesment.this);
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
                        list3.remove(i);
                        listView.setAdapter(new CustomAdapter(list2, list2, EnterInvesment.this, EnterInvesment.this));
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
        Cursor d = db.rawQuery("SELECT COUNT(*) FROM event WHERE t_id ="+t_no+"", null);
        String temp=null;
        if (d.moveToNext())
        {
            temp = d.getString(0);
        }
        e_no = Integer.parseInt(temp);
        e_no-=1;
        Cursor c = db.rawQuery("SELECT name FROM eparticipants WHERE t_id ="+t_no+" AND e_id="+e_no+"", null);
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }

        adap1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_class, list);
        adap1.setDropDownViewResource(R.layout.spinner_dropdown);
        s1.setAdapter(adap1);
        list2 = new ArrayList<String>();
        list3 = new ArrayList<String>();
        listView= findViewById(R.id.list1);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s=list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final EditText e2 = findViewById(R.id.edit2);
        Button b1 =findViewById(R.id.b1);
        e2.setText("");




        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (e2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "PLease enter the payment amount.", Toast.LENGTH_SHORT).show();
                } else {

                    String temp = null;
                    int pid = 0;
                    int amt = 0;
                    if (f == 0) {
                        Cursor c = db.rawQuery("SELECT COUNT(*) FROM event WHERE t_id =" + t_no + "", null);
                        if (c.moveToNext()) {
                            temp = c.getString(0);
                        }
                        t = Integer.parseInt(temp);


                    }

                    amt = Integer.parseInt(e2.getText().toString());
                    Cursor c = db.rawQuery("SELECT id FROM participants WHERE name='" + s + "' AND  t_id =" + t_no + "", null);
                    if (c.moveToFirst()) {
                        temp = c.getString(0);
                    }
                    pid = Integer.parseInt(temp);


                    {
//                    Bundle b=getIntent().getExtras();
//                    e_no=b.getInt("eventNo");

                        db.execSQL("INSERT INTO payment VALUES(" + t_no + "," + e_no + "," + pid + "," + amt + ");");
                    }
                    list2.add(s);
                    list3.add(e2.getText().toString());
                    listView.setAdapter(new MyCustomAdapter(list2, list3, EnterInvesment.this, EnterInvesment.this));
                    list.remove(s);
                    s1.setAdapter(adap1);
                    e2.setText("");

                }
            }
        });

        Button b2 =findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            String temp = null;

            @Override
            public void onClick(View view) {
                if (list2.size() < 1) {
                    Toast.makeText(getApplicationContext(), "Add atleast 1 payment record.", Toast.LENGTH_SHORT).show();
                } else {
//                t = 0;
//                int flag = 0;
//                int x = 0;
//                String ename[] = null;
//
//                if (f == 0) {
//                    Cursor c = db.rawQuery("SELECT COUNT(*) FROM event WHERE t_id ="+t_no+"", null);
//                    if (c.moveToNext()) {
////                        Cursor d = db.rawQuery("SELECT e_name FROM event " + "where e_name= '" + e1.getText().toString() + "' AND WHERE t_id ="+t_no+"", null);
////                        if (!d.moveToNext()) {
////                            t++;
////
////                            db.execSQL("INSERT INTO event VALUES('" + t + "','" + e1.getText().toString() + "');");
////                        } else {
////                            Toast.makeText(getApplicationContext(), "Enter unique Event name", Toast.LENGTH_LONG).show();
////                        }                  temp = c.getString(0);
//                    }
//
//                    t = Integer.parseInt(temp);
//            //
//                    x = 0;
//                }
                    Intent i = new Intent(EnterInvesment.this, AddEvent.class);
                    startActivity(i);

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
                buffer.append("1.Choose the name of the participants through dropdown list " + "\n");
                buffer.append("2.Enter the invesment made by each participants" + "\n");
                showMessage("Enter Invesment", buffer.toString());
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

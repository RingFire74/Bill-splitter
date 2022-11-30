package myapplication.vedha.example.com.bill_splitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import static java.sql.Types.INTEGER;

public class NewTrip extends AppCompatActivity {
    StringBuffer buffer = new StringBuffer();
    SQLiteDatabase db;
    ArrayList<String> list, list1;
    String add;
    EditText edit1;
    ListView listView;
    Integer t_no;
    TextView t1;
    Button b4;
    int np = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_trip);
        Button b1 = (Button) findViewById(R.id.b1);
        Button b2 = (Button) findViewById(R.id.b2);
        Button b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sf.edit();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lonelyplanet.com/search?q=" + add));
                startActivity(i);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        np = 0;
        list = new ArrayList<String>();
        list1 = new ArrayList<String>();
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewTrip.this,MapsActivity.class);
                startActivityForResult(i,99);
            }
        });

        db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);
        edit1 = findViewById(R.id.edit1);
        //db.execSQL("DROP TABLE participants");
        db.execSQL("CREATE TABLE IF NOT EXISTS trips(t_id INTEGER PRIMARY KEY,t_name VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS eparticipants(t_id INTEGER ,e_id INTERGER,id INTERGER ,name VARCHAR,FOREIGN KEY(t_id)REFERENCES trips(t_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS output(t_id INTEGER ,o_id INTEGER,out VARCHAR,FOREIGN KEY(t_id)REFERENCES trips(t_id), PRIMARY KEY(t_id,o_id));");

        db.execSQL("CREATE TABLE IF NOT EXISTS participants(t_id INTEGER ,id INTEGER ,name VARCHAR,number VARCHAR,FOREIGN KEY(t_id)REFERENCES trips(t_id), PRIMARY KEY(t_id,id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS event(t_id INTEGER ,e_id INTERGER ,e_name VARCHAR,category VARCHAR,FOREIGN KEY(t_id)REFERENCES trips(t_id), PRIMARY KEY(t_id,e_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS payment(t_id INTEGER ,e_id INTEGER,id INTEGER,amount INTEGER,FOREIGN KEY(t_id)REFERENCES trips(t_id),FOREIGN KEY(e_id)REFERENCES event(e_id),FOREIGN KEY(id)REFERENCES participants(id));");
        db.execSQL("DELETE FROM participants WHERE id=id ;");
        db.execSQL("DELETE FROM event WHERE e_id=e_id ;");
        db.execSQL("DELETE FROM payment WHERE e_id=e_id ;");
        listView = findViewById(R.id.list);
        SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();
//                edit.clear();
        String q = sf.getString("Trip_no", "0");
        t_no = Integer.parseInt(q) + 1;
        t1=findViewById(R.id.t1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewTrip.this);
                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete "+list.get(i)+"("+list1.get(i)+")"+" ?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_launcher_background);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
                        list.remove(i);
                        np--;
                        list1.remove(i);
                        listView.setAdapter(new CustomAdapter(list, list, NewTrip.this, NewTrip.this));
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
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NewTrip.this, Contact.class);
                startActivityForResult(i, 1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit1.getText().toString().equals("null")||np<2) {
                    Toast.makeText(getApplicationContext(), "Choose a trip destination and atleast 2 members please.", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sf.edit();
//                edit.clear();
                    String q = sf.getString("Trip_no", "0");
                    Integer t_no = Integer.parseInt(q);

                    String name = sf.getString("trip", null);
                    t_no++;
                    db.execSQL("INSERT into trips values(" + t_no + ",'" + name + "');");
                    for (int j = 0; j < np; j++) {
                        int kid = j + 1;
                        db.execSQL("INSERT INTO participants VALUES(" + t_no + ", " + kid + ",'" + list.get(j) + "','" + list1.get(j) + "');");
                    }
                    edit.putString("Trip_no", t_no + "");
                    edit.putString("no", np + "");
                    edit.putString("trip", edit1.getText().toString());
                    edit.commit();
                    String q1 = sf.getString("Trip_no", "0");
                    Integer t1_no = Integer.parseInt(q1);
                    Intent i = new Intent(NewTrip.this, SelectMembers.class);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        String str = data.getStringExtra("msg");
//        String str1 = data.getStringExtra("msg1");
        if(resultCode==1) {
            ArrayList<String> name = data.getStringArrayListExtra("msg"), num = data.getStringArrayListExtra("msg1");
            SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
            for (int x = 0; x < name.size(); x++) {
                np++;
                list.add(name.get(x));
                list1.add(num.get(x));
            }
            SharedPreferences.Editor edit = sf.edit();
            edit.putInt("no", np);
            listView = findViewById(R.id.list);
            listView.setAdapter(new CustomAdapter(list, list, NewTrip.this, NewTrip.this));
        }
        else if(resultCode==2) {
            SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sf.edit();
            edit.putString("locale", data.getStringExtra("locale"));
            edit.commit();
            String[] u = data.getStringExtra("locale").split("_");
            Locale l = new Locale(u[0], u[1]);
                edit1.setText(data.getStringExtra("address"));
            add = data.getStringExtra("address");
                edit1.setVisibility(View.VISIBLE);
            t1.setText("Trip Id: " + t_no + "\n" + "Currency: " + Currency.getInstance(l).getDisplayName());
                t1.setVisibility(View.VISIBLE);
            b4.setVisibility(View.VISIBLE);
        }
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

                buffer.append("1.Enter the name of the trip " + "\n");
                buffer.append("2.Select the participants by clicking ADD participants " + "\n");
                showMessage("ADD NEW TRIP", buffer.toString());
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
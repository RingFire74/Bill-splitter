package myapplication.vedha.example.com.bill_splitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class AddEvent extends AppCompatActivity {
    StringBuffer buffer = new StringBuffer();

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);

        setContentView(R.layout.activity_add_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();
//                edit.clear();
        String q =sf.getString("Trip_no","0");
        final Integer t_no = Integer.parseInt(q);
        ArrayList<String> list = new ArrayList<String>();
        ListView list1 = findViewById(R.id.list1);
        Cursor c = db.rawQuery("SELECT e_name FROM event WHERE t_id ="+t_no+"", null);
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        list1.setAdapter(new CustomAdapter(list, list, AddEvent.this, AddEvent.this));
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //alert
                Cursor e=db.rawQuery("SELECT id FROM eparticipants WHERE t_id ="+t_no+" and e_id ="+i+"",null);

                buffer = new StringBuffer();


                while (e.moveToNext())
                {    int temp=Integer.parseInt(e.getString(0));

                    Cursor c = db.rawQuery("SELECT name FROM participants WHERE t_id ="+t_no+" and id="+temp+"", null);
                    if(c.moveToNext())
                    {
                        buffer.append("" + c.getString(0).toString() + "\t");
                    }


                    Cursor d = db.rawQuery("SELECT * FROM payment WHERE t_id ="+t_no+" AND id=+"+temp+" ANd e_id="+i+"", null);
                    if (d.moveToNext()) {
                        buffer.append("paid: " + d.getString(3) + "\n");
                    }



                }
                // Displaying all records 
                showMessage("Participants Details", buffer.toString());




            }
        });
        FloatingActionButton b2 = findViewById(R.id.b2);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddEvent.this,SelectMembers.class);
                startActivity(i);
            }
        });
        Button b1=findViewById(R.id.b1);
b1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i = new Intent(AddEvent.this,FinalOutput.class);
        startActivity(i);
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
                buffer.append("1.Click + button to add new event " + "\n");
                buffer.append("2.Press Finish button to go the results page" + "\n");
                showMessage("Event list", buffer.toString());
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddEvent.this,MainActivity.class);
        startActivity(i);
    }
}

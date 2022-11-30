package myapplication.vedha.example.com.bill_splitter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String msg=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1, b2, b3;
        TextView t1 = findViewById(R.id.t1);
        db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);
        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
        String p=sf.getString("trip","NA");
        String q = sf.getString("Trip_no", "0");
        Integer t_no = Integer.parseInt(q);
        msg=sf.getString("output","NA");


        t1.setText(p+"("+q+")");
        b1 = (Button)findViewById(R.id.b_start);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(MainActivity.this,AddEvent.class);
                startActivity(i);
            }
        });
        b2 = (Button)findViewById(R.id.b_trip);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(MainActivity.this,NewTrip.class);
                startActivity(i);
            }
        });

        b3 = (Button) findViewById(R.id.b_contact);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddCustomClass a = new AddCustomClass(MainActivity.this,MainActivity.this);
                a.show();


            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_btn:
                Intent i =new Intent(MainActivity.this,About.class);
                startActivity(i);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void go(String id){
        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();
        edit.putString("flag","1");
        edit.putString("history",id);
        edit.commit();
        Intent i = new Intent(MainActivity.this,FinalOutput.class);
        startActivity(i);
    }

}

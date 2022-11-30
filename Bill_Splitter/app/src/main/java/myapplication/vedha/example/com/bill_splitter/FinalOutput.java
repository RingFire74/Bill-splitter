package myapplication.vedha.example.com.bill_splitter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FinalOutput extends AppCompatActivity {
    SQLiteDatabase db;
    ArrayList<String> numberr =new ArrayList<>();
    ArrayList<String> list=new ArrayList<String>();
    String msgcontent=" "+"\n";
    int f = 0;
    int t = 0, p = 0;
    Button b1;
    String temp = null, temp1 = null;
    TextView f1,f2,f3,f4,f5,f6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_output);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intent i = getIntent();
        db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);
        SharedPreferences sf = getSharedPreferences("myfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sf.edit();
//                edit.clear();
        if(sf.getString("flag","0")=="1"){
            String xy=sf.getString("history","0");
            edit.putString("flag","0");
        }
        String xy =sf.getString("Trip_no","0");
        final Integer t_no = Integer.parseInt(xy);
        b1 = findViewById(R.id.b2);
        f1=findViewById(R.id.t1);
        f2=findViewById(R.id.t2);
        f3=findViewById(R.id.t3);
        f4=findViewById(R.id.t4);
        f5=findViewById(R.id.t5);
        Cursor cc = db.rawQuery("SELECT number FROM participants WHERE t_id ="+t_no+"", null);
        while (cc.moveToNext()) {
            numberr.add(cc.getString(0));
        }

        db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);

        if (f == 0) {
            t = 0;
            p = 0;
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM event WHERE t_id ="+t_no+"", null);
            if (c.moveToNext()) {
                temp = c.getString(0);
            }
            t = Integer.parseInt(temp);


            Cursor d = db.rawQuery("SELECT COUNT(id) FROM participants WHERE t_id ="+t_no+"", null);
            if (d.moveToNext()) {
                temp1 = d.getString(0);
            }
            p = Integer.parseInt(temp1);
            p++;

        }
        int calc[][] = new int[p + 1][p + 1];
        int calc1[] = new int[p+1];
        int sum[] = new int[6];
        int ch=0;
        for (int j = 1; j < p; j++)
        {
            for (int k = 1; k < p; k++)
            {
                calc[j][k] = 0;
            }
        }


        for (int j = 0; j < t; j++)
        {
            int k = 1;
            int[] a = new int[p + 1];
            Cursor c = db.rawQuery("SELECT id FROM eparticipants WHERE e_id=" + j + " AND t_id ="+t_no+"", null);
            while (c.moveToNext())
            {
                a[k] = Integer.parseInt(c.getString(0));
                k++;
            }
            Cursor ev = db.rawQuery("SELECT category FROM event WHERE e_id="+j+" AND t_id ="+t_no+"",null);
            if (ev.moveToNext())
            {
                String av=ev.getString(0);
                if(ev.getString(0).equals("Travel")) ch=0;
                else if(ev.getString(0).equals("Food and Beverages"))
                    ch=1;
                else if(ev.getString(0).equals("Accommodation"))
                    ch=2;
                else if(ev.getString(0).equals("Purchases"))
                    ch=3;
                else if(ev.getString(0).equals("Other"))
                    ch=4;
            }
            int t1 = 0;
            for (int x = 1; x < k; x++)
            {
                t1=0;
                Cursor d = db.rawQuery("SELECT amount FROM payment WHERE e_id=" + j + " and id=" + a[x] + " AND t_id ="+t_no+"", null);
                while (d.moveToNext())
                {
                    t1 = Integer.parseInt(d.getString(0));
                }
                sum[ch]+=t1;
                for (int y = 1; y < k; y++)
                {

                    if (k == 1)
                        k = 2;
                    calc[a[x]][a[y]] += (t1 / (k - 1));
                }
            }
        }
        for (int x = 1; x < p; x++)
        {
            for (int y = 1; y < p; y++)
            {
                if (calc[x][y] >= calc[y][x])
                {
                    calc[x][y] -= calc[y][x];
                    calc[y][x] = 0;
                }
            }
        }
        //new calculation code remove for error
        //start
        int checkSum=0;

        for( int x=1;x<p;x++)
        {
            calc1[x]=0;
            for(int y=1;y<p;y++)
            {
                calc1[x]+=calc[x][y];
            }
            checkSum+=calc1[x];
        }
        if(checkSum==0)
        {


        }
        //end
        String inp;
        int num;
        f1.setText("Total Travel expenses "+sum[0]);
        num=0;
        inp="Total Travel expenses "+sum[0];
        db.rawQuery("INSERT INTO output VALUES("+t_no+","+num+",'"+inp+"');",null);
        f2.setText("Total Food and Beverages expenses "+sum[1]);
        num=1;
        inp="Total Food and Beverages expenses "+sum[1];
        db.rawQuery("INSERT INTO output VALUES("+t_no+","+num+",'"+inp+"');",null);
        f3.setText("Total Accommodation expenses "+sum[2]);
        num=2;
        inp="Total Accommodation expenses "+sum[2];
        db.rawQuery("INSERT INTO output VALUES("+t_no+","+num+",'"+inp+"');",null);
        num=3;
        inp="Total Purchases expenses "+sum[3];
        f4.setText("Total Purchases expenses "+sum[3]);
        db.rawQuery("INSERT INTO output VALUES("+t_no+","+num+",'"+inp+"');",null);
        num=4;
        inp="Total Other expenses "+sum[4];
        f5.setText("Total Other expenses "+sum[4]);
        db.rawQuery("INSERT INTO output VALUES("+t_no+","+num+",'"+inp+"');",null);
        String q=sf.getString("no","0"); //if key is not available - it shows NA
        Integer k =Integer.parseInt(q);
        for (int x = 1; x < p; x++) {
            for (int y = 1; y < p; y++) {
                if (calc[x][y] > 0) {
                    String strx = null, stry = null;
                    Cursor c = db.rawQuery("SELECT name FROM participants WHERE id=" + x + " AND t_id ="+t_no+"", null);
                    if (c.moveToNext()) {
                        strx = c.getString(0);
                    }
                    Cursor d = db.rawQuery("SELECT name FROM participants WHERE id=" + y + " AND t_id ="+t_no+"", null);
                    if (d.moveToNext()) {
                        stry = d.getString(0);
                    }

                    list.add(stry + " should give " + calc[x][y] + " to " + strx);
//                    TextView temp = new TextView(getApplicationContext());
//                    temp.setText(stry + " should give " + calc[x][y] + " to " + strx);
//                    msgcontent=msgcontent+"\n"+stry + " should give " + calc[x][y] + " to " + strx;
//                    temp.setTextColor(Color.parseColor("#FFFFFF"));
//                    temp.setTextSize(20);
//                    l1.addView(temp);
                }
            }
        }
        edit.putString("output",msgcontent);
        edit.commit();
        ListView lv= findViewById(R.id.lv);
        lv.setAdapter(new OutputAdapter(list, list, FinalOutput.this, FinalOutput.this));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i =0;i<numberr.size();i++)
                {


//Getting intent and PendingIntent instance


//Get the SmsManager instance and call the sendTextMessage method to send message
                    SmsManager sms = SmsManager.getDefault();


/*if (ContextCompat.checkSelfPermission(this,
       Manifest.permission.SEND_SMS)
       != PackageManager.PERMISSION_GRANTED) {
  // if (ActivityCompat.shouldShowRequestPermissionRationale(this,
    //       Manifest.permission.SEND_SMS)) {
       ActivityCompat.requestPermissions(this,
               new String[]{Manifest.permission.SEND_SMS},
               111);
   } else {
      // Permission already granted
  // }
}
*/

                    sms.sendTextMessage(numberr.get(i), null, msgcontent, null, null);
                    Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                            Toast.LENGTH_LONG).show();}
                Intent i=new Intent(FinalOutput.this,MainActivity.class);
                startActivity(i);
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
                Toast.makeText(getApplicationContext(), "Final output will be sent to participants Via sms on cicking the button", Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

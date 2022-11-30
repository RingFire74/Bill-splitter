package myapplication.vedha.example.com.bill_splitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Vector;

public class AddParticipants extends AppCompatActivity {

    SQLiteDatabase db;
    EditText editText;
    Button b2;
    Integer idgen,id;
    FloatingActionButton b3,b4;
    LinearLayout l1;
    int i=0,j=0;
    ArrayList<Integer> v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        l1 = findViewById(R.id.l1);
        v=new ArrayList<Integer>();
        db = openOrCreateDatabase("BillSplitterDB", Context.MODE_PRIVATE, null);

        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);;
        String q=sf.getString("no","0"); //if key is not available - it shows NA
        Integer k =Integer.parseInt(q);
        for(int j=0;j<k;j++){
            EditText temp = new EditText(getApplicationContext());
            id=View.generateViewId();
            TextInputLayout t1=new TextInputLayout(AddParticipants.this);
            temp.setHint("Enter participant name");
            idgen = View.generateViewId();
            v.add(idgen);
            temp.setId(idgen);
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            int color =R.color.white;
            ColorStateList colorStateList = ColorStateList.valueOf(getColor(color));
            temp.setBackgroundTintList(colorStateList);
            temp.setTextColor(Color.WHITE);
            temp.setHintTextColor(Color.WHITE);
            t1.setDefaultHintTextColor(colorStateList);
            t1.addView(temp);
            i++;
            l1.addView(t1);
        }
        b2=(Button)findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(AddParticipants.this,EnterInvesment.class);
                for(j=0;j<i;j++) {
                    EditText t = (EditText) findViewById(v.get(j));
                    int k = j + 1;
                    db.execSQL("INSERT INTO participants VALUES('" + k + "','" + t.getText() + "');");


                }

                startActivity(in);
            }
        });
    }
}

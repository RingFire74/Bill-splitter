package myapplication.vedha.example.com.bill_splitter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AddCustomClass extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    MainActivity m;
    public String name, rollno, className;
    public EditText ename, roll;
    public Button yes, no;

    public AddCustomClass(Activity a,Context context) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.m=(MainActivity)a;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        ename = findViewById(R.id.edit_name);
        yes.setText("Show");
        no.setText("Cancel");
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:

                m.go(ename.getText().toString());
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}


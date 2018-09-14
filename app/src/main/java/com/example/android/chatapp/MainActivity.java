package com.example.android.chatapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText txt;
    private Button btn;
    private ListView lst;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //.................Move the layout avobe the keyboard........................


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //........................Initialisation...................
        txt = (EditText)findViewById(R.id.txt);
        btn = (Button)findViewById(R.id.btn);
        lst = (ListView)findViewById(R.id.lst);
        ref = FirebaseDatabase.getInstance().getReference().child("User");

        //....................... Sending Message To  Server...........................


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtData = txt.getText().toString();

                if(txtData.trim().matches("")){

                    //.........Check The Invalid String!............

                    Toast.makeText(MainActivity.this,"Invalid String! Data Error",Toast.LENGTH_SHORT).show();

                    //..............Popped out the soft keyboard..........

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    txt.setText(null);

                }
                else {

                    //...............Sends Data To RealTime DataBase..........

                    ref.push().setValue(txtData);


                    //.........Popped out The SOft KeyBoard............

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    txt.setText(null);

                }
            }
        });

        //.............Updating the real-time database and show the list............................

        FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                ref) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView data = (TextView)v.findViewById(android.R.id.text1);
                data.setText(model);
            }
            @Override
            public String getItem(int pos) {
                return super.getItem(getCount() - 1 - pos);
            }
        };

        lst.setAdapter(adapter);

    }
}

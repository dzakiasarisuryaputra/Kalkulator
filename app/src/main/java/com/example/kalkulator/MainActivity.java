package com.example.kalkulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edt1,edt2;
    Button btnInsert,btnDelete;
    RadioGroup radioOperator;
    String operator;
    SharedPreferences sh;

    private ArrayList<History> mylist;
    private RecyclerView myrey;

    HistoryAdapter myAdapter;
    int temp = 1,id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt1 = findViewById(R.id.edittext_line_1);
        edt2 = findViewById(R.id.edittext_line_2);
        btnInsert = findViewById(R.id.button_insert);
        btnDelete = findViewById(R.id.button_delete);
        radioOperator = findViewById(R.id.operasiGroup);
        sh = this.getSharedPreferences("history", Context.MODE_PRIVATE);
        myrey = findViewById(R.id.recyclerview);
        mylist = new ArrayList<>();
        temp = sh.getAll().size()+1;
        myAdapter = new HistoryAdapter(mylist,this,sh);

        Listener();
        showArray();

        if (mylist.size() == 0) {
            id = 1;
        } else {
            id = Integer.parseInt(mylist.get(mylist.size()-1).getId())+1;
        }
    }

    private  Boolean cekValid() {
        if (edt1.getText().toString().equals("") || edt1.getText() == null) {
            return false;
        } else if (edt2.getText().toString().equals("") || edt2.getText() == null) {
            return false;
        } else if (operator == null) {
            return false;
        }
        return true;
    }

    private String value(double value1, double value2) {
        double value = 0;
        String idRwyt = String.valueOf(id);

        if (operator.equals("+")) {
            value = value1 + value2;
            String riwayat = Double.toString(value1)+ " + " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (operator.equals("-")) {
            value = value1 - value2;
            String riwayat = Double.toString(value1)+ " - " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (operator.equals("x")) {
            value = value1 * value2;
            String riwayat = Double.toString(value1)+ " x " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (operator.equals("/")) {
            value = value1 / value2;
            String riwayat = Double.toString(value1)+ " : " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);
        }
        id++;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myrey.setLayoutManager(linearLayoutManager);

        return String.valueOf(value);
    }


    public void showArray() {
        Map<String ,?> entries = sh.getAll();
        for(Map.Entry<String,?> entry: entries.entrySet()){
            getArray(entry.getKey(),entry.getValue().toString());
        }
    }

    public void saveToShared(String id,String hasil){
        try {
            sh.edit().putString(id,hasil).apply();
            String value = sh.getString(id,"");
            getArray(id,value);
            temp++;
            this.id++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getArray(String no,String rwyt){
        try{
            myrey.setAdapter(new HistoryAdapter(mylist, this, sh));
            myrey.setLayoutManager(new LinearLayoutManager(this));
            mylist.add(new History(no,String.valueOf(rwyt)));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("gagal tambah array");
        }
    }

    private void Listener() {
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cekValid()) {
                    double value1 = Double.parseDouble(edt1.getText().toString());
                    double value2 = Double.parseDouble(edt2.getText().toString());
                    value(value1,value2);
                } else {
                    Toast.makeText(getApplicationContext(), "Masukan data dengan benar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

        radioOperator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                operator = radioButton.getText().toString();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mylist.clear();
        myAdapter.notifyDataSetChanged();
    }
}
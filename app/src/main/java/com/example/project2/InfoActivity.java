package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InfoActivity extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    String sql;
    Cursor cursor;

    Button infoUpdate, infoDelete, infoLogout;

    private BottomNavigationView bottomNavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent getId = getIntent(); //전달할 데이터를 받을 Intent
        //text 키값으로 데이터를 받는다. String을 받아야 하므로 getStringExtra()를 사용함
        String id = getId.getStringExtra("text");

        //DataBase연결부분
        helper = new DatabaseOpenHelper(InfoActivity.this, DatabaseOpenHelper.tableName, null, version);
        database = helper.getWritableDatabase();

        sql = "SELECT * FROM "+ helper.tableName + " WHERE id = '" + id + "'";
        cursor = database.rawQuery(sql, null);

        cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
        String pw = cursor.getString(1);
        String name = cursor.getString(2);
        String age = cursor.getString(3);
        String height = cursor.getString(4);
        String weight = cursor.getString(5);

        EditText idEdit = findViewById(R.id.idEditText);
        idEdit.setText(id);
        EditText pwEdit = findViewById(R.id.pwEditText);
        pwEdit.setText(pw);
        EditText nameEdit = findViewById(R.id.nameEditText);
        nameEdit.setText(name);
        EditText ageEdit = findViewById(R.id.ageEditText);
        ageEdit.setText(age);
        EditText heightEdit = findViewById(R.id.heightEditText);
        heightEdit.setText(height);
        EditText weightEdit = findViewById(R.id.weightEditText);
        weightEdit.setText(weight);


        bottomNavi = findViewById(R.id.bottonNavi);
        bottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent intent2 = new Intent(InfoActivity.this, MainActivity.class);
                        intent2.putExtra("text", id);
                        startActivity(intent2);
                        break;
                    case R.id.action_fitness:
                        Intent intent = new Intent(InfoActivity.this, FitnessActivity.class);
                        intent.putExtra("text", id);
                        startActivity(intent);
                        break;
                    case R.id.action_info:

                        break;
                }
                return true;
            }
        });

        //정보수정
        infoUpdate = (Button) findViewById(R.id.btnUpdate);
        infoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int age = Integer.parseInt(ageEdit.getText().toString()); //정수값 가져오기
                int height = Integer.parseInt(heightEdit.getText().toString());
                int weight = Integer.parseInt(weightEdit.getText().toString());
                database.execSQL("UPDATE Users SET " +
                        "age="+age+", height="+height+", weight="+weight+
                        " WHERE id ='" + id + "'");

                Toast toast = Toast.makeText(InfoActivity.this, "수정되었습니다", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //회원탈퇴
        infoDelete = (Button) findViewById(R.id.btnDelete);
        infoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.execSQL("DELETE FROM Users"+
                        " WHERE id ='" + id + "'");
                Toast toast = Toast.makeText(InfoActivity.this, "회원정보가 삭제되었습니다", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //로그아웃
        infoLogout = (Button) findViewById(R.id.btnLogout);
        infoLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(InfoActivity.this, "로그아웃 되었습니다", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
package com.markandreydelacruz.pupsisgrades;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private MaterialBetterSpinner spinnerMonth, spinnerDay, spinnerYear;
    private MaterialEditText editTextStudentNumber, editTextPassword;
    private Button buttonViewGrades;
    private TextView textViewSisSite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        setTitle("PUP SIS GRADES");
        initComp();
    }


    private static final Integer[] MONTH = new Integer[] {
//            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
            1,2,3,4,5,6,7,8,9,10,11,12
    };

    private static final Integer[] DAY = new Integer[] {
            1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31
    };

    private static final Integer[] YEAR = new Integer[] {
            1990,1991,1992,1993,1994,1995,1996,1997,1998,1999,2000,2001,2002,2003,2004,2005,2006
    };

    private void initComp() {
        editTextStudentNumber = (MaterialEditText) findViewById(R.id.editTextStudentNumber);
        editTextPassword = (MaterialEditText) findViewById(R.id.editTextPassword);
        textViewSisSite = (TextView) findViewById(R.id.textViewSisSite);
        spinnerMonth = (MaterialBetterSpinner) findViewById(R.id.spinnerMonth);
        spinnerDay = (MaterialBetterSpinner) findViewById(R.id.spinnerDay);
        spinnerYear = (MaterialBetterSpinner) findViewById(R.id.spinnerYear);
        buttonViewGrades = (Button) findViewById(R.id.buttonViewGrades);

        ArrayAdapter<Integer> spinnerMonthArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_dropdown_item_1line, MONTH);
        spinnerMonth.setAdapter(spinnerMonthArrayAdapter);
        ArrayAdapter<Integer> spinnerDayArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_dropdown_item_1line, DAY);
        spinnerDay.setAdapter(spinnerDayArrayAdapter);
        ArrayAdapter<Integer> spinnerYearArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_dropdown_item_1line, YEAR);
        spinnerYear.setAdapter(spinnerYearArrayAdapter);

        String sisLink = "<a href=\"http://www.sisstudents.pup.edu.ph\">www.sisstudents.pup.edu.ph</a>";
        if (Build.VERSION.SDK_INT >= 24) {
            textViewSisSite.setText( Html.fromHtml(sisLink, Build.VERSION.SDK_INT)); // for 24 api and more
            textViewSisSite.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            textViewSisSite.setText(Html.fromHtml(sisLink)); // or for older api
            textViewSisSite.setMovementMethod(LinkMovementMethod.getInstance());
        }


        buttonViewGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentNumber, password, month, day, year;

                studentNumber = editTextStudentNumber.getText().toString();
                password= editTextPassword.getText().toString();
                month = spinnerMonth.getText().toString();
                day = spinnerDay.getText().toString();
                year = spinnerYear.getText().toString();

                String urlString = "http://bsit3-2018.com/test/getContent.php?sn="+studentNumber+"&p="+password+"&m="+month+"&d="+day+"&y="+year+"";

//                Toast.makeText(getApplicationContext(), urlString, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, Grades.class);
//                intent.putExtra("urlString", "http://192.168.137.1/pupSisGradesWebCrawler/getContent.php")
                intent.putExtra("urlString", urlString);
                startActivity(intent);
                hideSoftKeyboard(MainActivity.this);
            }
        });
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
//        intent.setType("*/*");
        intent.setType("application/vnd.android.package-archive");


        // Append file and send Intent
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent, "Share app via"));
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            shareApplication();
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}

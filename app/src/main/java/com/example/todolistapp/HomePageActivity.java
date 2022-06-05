package com.example.todolistapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.todolistapp.Adds.AddCategory;
import com.example.todolistapp.DrawerThings.FAQActivity;
import com.example.todolistapp.DrawerThings.FavoritesActivity;
import com.example.todolistapp.Fragments._CalendarFragment;
import com.example.todolistapp.Fragments._MineFragment;
import com.example.todolistapp.Fragments._TaskFragment;
import com.example.todolistapp.Log.Log_in_upActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class HomePageActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int SETTINGS_CODE = 234;
    SharedPreferences sharedPreferences;
    BottomNavigationView bottom_navigation;
    NavigationView nav_view;
    FragmentManager fm;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    String item;
    String itemName;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> category_ArrayList;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapterlang;
    ArrayList<String> lang_ArrayList;
    SharedPreferences sharedPreferencesTheme;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_home_page);
        _FindId();
        _Listener();
        GetData();
        DrawerSpinner();
        DrawerSpinnerlang();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_out, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            if (item.getItemId() == R.id.LogOut) {
                confirmDialog();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out ?");
        builder.setMessage("Are you sure yoe want to Log Out ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Toast.makeText(HomePageActivity.this, "Delete", Toast.LENGTH_SHORT).show();
            sharedPreferences = getSharedPreferences("logged_user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_id", "");
            editor.putString("user_email", "");
            editor.putString("user_fullName", "");
            editor.putString("user_phone", "");
            editor.commit();
            Intent intent = new Intent(HomePageActivity.this, Log_in_upActivity.class);
            startActivity(intent);
            finish();

        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    public void _FindId() {
        fm = getSupportFragmentManager();
        firebaseFirestore = FirebaseFirestore.getInstance();
        category_ArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, category_ArrayList);

        lang_ArrayList = new ArrayList<>();
        lang_ArrayList.add("");
        lang_ArrayList.add("Arabic");
        lang_ArrayList.add("English");
        arrayAdapterlang = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, lang_ArrayList);


        bottom_navigation = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_id);
        nav_view = findViewById(R.id.nv);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.nav_open, R.string.nav_close);
    }

    public void DrawerSpinner() {
        AddCategory addCategory = new AddCategory();
        Spinner spinner = (Spinner) nav_view.getMenu().findItem(R.id.category_mine).getActionView();
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                // Toast.makeText(HomePageActivity.this, "postion "+position, Toast.LENGTH_SHORT).show();
                item = String.valueOf(parent.getItemAtPosition(position));
                if (item.equals("Create Category")) {
                    //  Toast.makeText(HomePageActivity.this, "postion "+item, Toast.LENGTH_SHORT).show();
                    addCategory.show(getSupportFragmentManager(), "");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        nav_view.setNavigationItemSelectedListener(this);
    }

    public void DrawerSpinnerlang() {

        Spinner spinner_lang = (Spinner) nav_view.getMenu().findItem(R.id.lang).getActionView();
        spinner_lang.setAdapter(arrayAdapterlang);

        spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                // Toast.makeText(HomePageActivity.this, "postion "+position, Toast.LENGTH_SHORT).show();
                itemName = String.valueOf(parent.getItemAtPosition(position));
                if (itemName.equals("Arabic")) {
                    getNewArabic();
                }
                if (itemName.equals("English")) {
                    getNewENGLISH();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        nav_view.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void _Listener() {
        bottom_navigation.setSelectedItemId(R.id.page_task); // change to whichever id should be default
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mvc, new _TaskFragment()).commit();
        setTitle("Tasks");
        bottom_navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_task:
                    _TaskFragment taskFragment = (_TaskFragment) getSupportFragmentManager().findFragmentById(R.id._task_fragment);


                    FragmentTransaction ft1 = fm.beginTransaction();
                    ft1.replace(R.id.mvc, new _TaskFragment()).commit();
                    setTitle(getString(R.string.tasks));

                    break;
                case R.id.page_Calender:
                    FragmentTransaction ftc = fm.beginTransaction();
                    ftc.replace(R.id.mvc, new _CalendarFragment()).commit();
                    setTitle(getString(R.string.Calender));
                    break;

                case R.id.page_mine:
                    FragmentTransaction ftm = fm.beginTransaction();
                    ftm.replace(R.id.mvc, new _MineFragment()).commit();
                    setTitle(getString(R.string.mine));
                    break;
            }
            return true;
        });
    }

    public void GetData() {
        Query query = firebaseFirestore.collection("categories").orderBy("time", Query.Direction.DESCENDING);

        listenerRegistration = query.addSnapshotListener(this::onEvent);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
        for (DocumentChange documentChange : Objects.requireNonNull(value).getDocumentChanges()) {
            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                String category = documentChange.getDocument().getString("category_name");
                category_ArrayList.add(category);
                arrayAdapter.notifyDataSetChanged();
            }
        }
        category_ArrayList.add("Create Category");
        listenerRegistration.remove();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.StarTasks_mine:
                startActivity(new Intent(this, FavoritesActivity.class));
                setTitle(getString(R.string.favorites));
                break;
            case R.id.category_mine:
                Toast.makeText(this, "category entered", Toast.LENGTH_SHORT).show();

                break;
            case R.id.Theme_mine:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_CODE);
                setTitle(getString(R.string.theme));
                break;
            case R.id.FAQ_mine:
                startActivity(new Intent(this, FAQActivity.class));
                setTitle(getString(R.string.faq));
                break;
        }
        return true;
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void getNewENGLISH() {
        setNewLocale(this, LocaleManager.ENGLISH);
    }

    public void getNewArabic() {
        setNewLocale(this, LocaleManager.Arabic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CODE) {
            this.recreate();
        }
    }

    public void setTheme() {
        sharedPreferencesTheme = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        setTheme(R.style.Theme_ToDoAppTrial);
        if (sharedPreferencesTheme.getString("color_option", "BLUE").equals("blue")) {
            setTheme(R.style.Theme1);
        } else {
            setTheme(R.style.Theme2);
        }
    }
}
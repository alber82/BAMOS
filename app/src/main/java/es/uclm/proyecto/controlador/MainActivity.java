package es.uclm.proyecto.controlador;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Locale;

import es.uclm.proyecto.R;
import es.uclm.proyecto.controlador.fragments.AnimalesFragment;
import es.uclm.proyecto.controlador.fragments.EstudiosFragment;
import es.uclm.proyecto.controlador.fragments.HomeFragment;
import es.uclm.proyecto.controlador.fragments.SettingFragment;


public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        String languageToLoad  = prefs.getString("idioma", Locale.getDefault().getLanguage());

        //Toast.makeText(getApplicationContext(),Locale.getDefault().getLanguage(),Toast.LENGTH_SHORT).show();

        //String languageToLoad  = "es"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

       /* File dir = new File(getApplicationInfo().dataDir + "files/temp/");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
            File storageDir = new File(getApplicationInfo().dataDir + "files/temp/");
            storageDir.mkdir();
*/
        android.support.v4.app.FragmentTransaction ft;
        HomeFragment HomeFragment = new HomeFragment();
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, HomeFragment);
        ft.commit();




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                android.support.v4.app.FragmentTransaction fragmentTransaction;

                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()){
                    case R.id.home:
                        //Toast.makeText(getApplicationContext(),"Inicio seleccionado",Toast.LENGTH_SHORT).show();
                        Log.d(getApplicationContext().getFilesDir().getAbsolutePath(), getApplicationInfo().dataDir);
                        HomeFragment HomeFragment = new HomeFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,HomeFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.animals:
                        AnimalesFragment AnimalFragment = new AnimalesFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,AnimalFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.studies:
                        //Toast.makeText(getApplicationContext(),"Estudios seleccionado",Toast.LENGTH_SHORT).show();
                        EstudiosFragment EstudiosFragment = new EstudiosFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,EstudiosFragment);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.settings:
                        //Toast.makeText(getApplicationContext(),"Configuracion",Toast.LENGTH_SHORT).show();
                        SettingFragment SettingFragment = new SettingFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, SettingFragment);
                        fragmentTransaction.commit();
                        return true;
                                     default:
                        Toast.makeText(getApplicationContext(),"Algo ha ido mal",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();






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
            /*if (id == R.id.action_settings) {

                return true;
            }*/

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Desea salir?")
                .setMessage("¿Esta seguro de que desea salir?")
                .setNegativeButton("No", null)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}

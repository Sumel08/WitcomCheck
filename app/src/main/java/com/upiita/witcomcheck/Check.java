package com.upiita.witcomcheck;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
public class Check extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static String SERVER = "http://192.168.43.167/insert.php";
    public static String primary = "#22232A";
    public static String dark = "#1C1D26";
    public static String blue = "#0F5DBE";
    public static String accent = "#FF9A22";
    public static String textWhite = "#EFEFEF";
    public static String textAccent = "#0F5DB";
    public static String type;
    public static  SimpleDateFormat simpleDateFormat;
    public static String time;
    public static Calendar calander;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(),ChangeServer.class);
            startActivity(intent);
            Toast.makeText(this, "Configurar SERVER", Toast.LENGTH_LONG ).show();
            return true;
        }

        if (id == R.id.action_upload) {
            DataBaseHelper db=new DataBaseHelper(getApplicationContext());
            DataBaseHelper db1=new DataBaseHelper(getApplicationContext());

            Cursor c=db.getReadableDatabase().rawQuery("select * from Registro",null);
            c.moveToFirst();
            do{
                postNewComment(getApplicationContext(),c.getString(1),c.getString(2),c.getString(3),c.getString(4));
            Toast.makeText(this,c.getString(1)+c.getString(2)+c.getString(3)+c.getString(4), Toast.LENGTH_LONG ).show();
                db1.getWritableDatabase().rawQuery("delete from Registro where id= "+c.getString(0)+"",null);
                item.setEnabled(false);

            }while(c.moveToNext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_check, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ImageView imgV = (ImageView)rootView.findViewById(R.id.imageView);


            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                textView.setText(Html.fromHtml("<font color=" + accent + ">{ </font><font color=" + dark + ">Chek in</font><font color=" + accent + "> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.in));

            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==2) {
                textView.setText(Html.fromHtml("<font color="+accent+">{ </font><font color="+dark+">Check out</font><font color="+accent+"> }</font>"));
                imgV.setImageDrawable(getResources().getDrawable(R.drawable.out));

            }
            imgV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mViewPager.getCurrentItem() == 0) {
                        type="check in";
                    }

                    else if(mViewPager.getCurrentItem() == 1) {
                        type="check out";
                    }

                    IntentIntegrator integrator = new IntentIntegrator(getActivity());
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);

                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(true);
                    integrator.initiateScan();
                }
            });
            return rootView;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                calander = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                time = simpleDateFormat.format(calander.getTime());
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
                String date = mdformat.format(calander.getTime());

                Toast.makeText(this, "Scanned: " + result.getContents()+" type "+type+" time "+time+" fecha "+date, Toast.LENGTH_LONG).show();
                //postNewComment(getApplicationContext(),result.getContents());
                insert(result.getContents(),type,date,time);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void postNewComment(final Context context, final String code,final String type,final String fecha,final String hora){
        Log.i("LecturaQR","Enviando al servidor "+ code);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,Check.SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LecturaQR","Se recibió respuesta del servidor "+response);
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context,"Tiempo de espera de respuesta terminado", Toast.LENGTH_LONG).show();
                } else if( error instanceof NoConnectionError){
                    Toast.makeText(context,"Verifica la conexión a internet", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,"Error del servidor", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,"Error de red", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //TODO
                }
                Toast.makeText(context,"Error en el servidor", Toast.LENGTH_LONG).show();
                Log.i("LecturaQR","Error al recibir del servidor"+error.getMessage());

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("codigo",String.valueOf(code));
                params.put("tipo",String.valueOf(type));
                params.put("fecha",String.valueOf(fecha));
                params.put("hora",String.valueOf(hora));

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }
    public void insert(String code,String type,String fecha,String hora){
        ContentValues valor=new ContentValues();
        valor.put("codigo",code);
        valor.put("tipo",type);
        valor.put("fecha",fecha);
        valor.put("hora",hora);
            DataBaseHelper nuevo = new DataBaseHelper(getApplicationContext());
            nuevo.getWritableDatabase().execSQL("insert into Registro (codigo,tipo,fecha,hora) values('"+code+"','"+type+"','"+fecha+"','"+hora+"'"+")");
        //Toast.makeText(this,bd.update("Registro",valor,null,null),Toast.LENGTH_LONG).show();
        //bd.insert("Registro",null,valor);

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Check In";
                case 1:
                    return "Check Out";

            }
            return null;
        }
    }
}

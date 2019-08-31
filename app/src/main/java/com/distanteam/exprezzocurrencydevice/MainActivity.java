package com.distanteam.exprezzocurrencydevice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private InterstitialAd mInterstitialAd;
    private RequestQueue queue;
    private EditText txt1, txt2;
    private TextView view2, view3, viewerror;
    private String div1, div2;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private String[] numeros, numerosnuevos;
    public int i;
    public String remplazo,  uno, dos, datofinal;




    private int[] flags = {


            R.drawable.usd,
            R.drawable.eur,
            R.drawable.jpy,
            R.drawable.cad,
            R.drawable.clp,
            R.drawable.cny,
            R.drawable.nzd,
            R.drawable.aud,
            R.drawable.gbp,
            R.drawable.chf,
            R.drawable.brl,
            R.drawable.rub,
            R.drawable.mxn


    };

    private int[] flags2 = {

            R.drawable.usd,
            R.drawable.eur,
            R.drawable.jpy,
            R.drawable.cad,
            R.drawable.clp,
            R.drawable.cny,
            R.drawable.nzd,
            R.drawable.aud,
            R.drawable.gbp,
            R.drawable.chf,
            R.drawable.brl,
            R.drawable.rub,
            R.drawable.mxn


    };

    String[] countryNames, countryNames2;

    private Spinner spin1, spin2;
    private Button captura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, " Welcome To Exprexzo", Toast.LENGTH_SHORT).show();



        prepareAd();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", " Interstitial not loaded");
                        }
                        prepareAd();
                    }
                });
            }
        }, 30, 30, TimeUnit.SECONDS);




        onWindowFocusChanged(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 1);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        } else {


        }

        //  daynaight = findViewById(R.id.switch1);
        //daynaight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //  @Override
        //public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        //if(isChecked){


        //   getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        //}else{

        //  getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //}
        //}
        //});
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        queue = Volley.newRequestQueue(this);
        this.txt1 = findViewById(R.id.numeros6);
        this.view2 = findViewById(R.id.textView2);
        this.view3 = findViewById(R.id.textView3);
        this.viewerror = findViewById(R.id.textView);



        //  txt1.setEnabled(false);
        txt2 = findViewById(R.id.numeros5);

        captura =(Button) findViewById(R.id.capture);
        captura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                initShareIntent();
            }
        });



        spin1 = findViewById(R.id.spinner6);    // el de abajo


        spin2 = findViewById(R.id.spinner5);   // el de arriba
        spin2.clearFocus();
        countryNames = new String[]{"USD", "EUR", "JPY", "CAD", "CLP", "CNY", "NZD", "AUD", "GBP", "CHF", "BRL", "RUB", "MXN"};


        countryNames2 = new String[]{"USD", "EUR", "JPY", "CAD", "CLP", "CNY", "NZD", "AUD", "GBP", "CHF", "BRL", "RUB", "MXN"};


        Spinner spin1 = (Spinner) findViewById(R.id.spinner5);
        spin1.setOnItemSelectedListener(this);
        spin1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txt2.getWindowToken(), 0);
                return false;
            }
        });


        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), flags, countryNames);
        spin1.setAdapter(customAdapter);

        Spinner spin2 = (Spinner) findViewById(R.id.spinner6);
        spin2.setOnItemSelectedListener(this);
        spin2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txt2.getWindowToken(), 0);
                return false;
            }
        });
        CustomAdapter customAdapter2 = new CustomAdapter(getApplicationContext(), flags2, countryNames2);
        spin2.setAdapter(customAdapter2);
        spin2.setSelection(1);


        actualizardatos();



    }
    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
    File imagePath;
    public void saveBitmap(Bitmap bitmap) {
         imagePath = new File(Environment.getExternalStorageDirectory() + "/MyFiles/screenshotCurrency.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void initShareIntent() {
        try {
            Intent shareIntent = new Intent();

            Bitmap imgBitmap = (Bitmap) BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFiles/screenshotCurrency.png");
            String imgBitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), imgBitmap, "title", null);
            Uri imgBitmapUri = Uri.parse(imgBitmapPath);

            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, (imgBitmapUri));
            startActivity(Intent.createChooser(shareIntent, "Share"));
        }catch (Exception e){

            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();

        }
    }




    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {


    //   Toast.makeText(getApplicationContext(), countryNames[position], Toast.LENGTH_LONG).show();


    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void obtenerdatosscrap(View view) {


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt2.getWindowToken(), 0);
      //AKI HAY QUE OBTENER LSO DATOS DL FICHERO PARA EL OFLINE
        leerdatos();
        String[] number_USD = {numeros[0]+",0",numeros[1],numeros[2],numeros[3],numeros[4],numeros[5],numeros[6],numeros[7],numeros[8],numeros[9],numeros[10],numeros[11],numeros[12] };
        String[] number_EUR = {numeros[13],numeros[14]+",0",numeros[15],numeros[16],numeros[17],numeros[18],numeros[19],numeros[20],numeros[21],numeros[22],numeros[23],numeros[24],numeros[25] };
        String[] number_JPY = {numeros[26],numeros[27],numeros[28]+",0",numeros[29],numeros[30],numeros[31],numeros[32],numeros[33],numeros[34],numeros[35],numeros[36],numeros[37],numeros[38] };
        String[] number_CAD = {numeros[39],numeros[40],numeros[41],numeros[42]+",0",numeros[43],numeros[44],numeros[45],numeros[46],numeros[47],numeros[48],numeros[49],numeros[50],numeros[51] };
        String[] number_CLP = {numeros[52],numeros[53],numeros[54],numeros[55],numeros[56]+",0",numeros[57],numeros[58],numeros[59],numeros[60],numeros[61],numeros[62],numeros[63],numeros[64] };
        String[] number_CNY = {numeros[65],numeros[66],numeros[67],numeros[68],numeros[69],numeros[70]+",0",numeros[71],numeros[72],numeros[73],numeros[74],numeros[75],numeros[76],numeros[77] };
        String[] number_NZD = {numeros[78],numeros[79],numeros[80],numeros[81],numeros[82],numeros[83],numeros[84]+",0",numeros[85],numeros[86],numeros[87],numeros[88],numeros[89],numeros[90] };
        String[] number_AUD = {numeros[91],numeros[92],numeros[93],numeros[94],numeros[95],numeros[96],numeros[97],numeros[98]+",0",numeros[99],numeros[100],numeros[101],numeros[102],numeros[103] };
        String[] number_GBP = {numeros[104],numeros[105],numeros[106],numeros[107],numeros[108],numeros[109],numeros[110],numeros[111],numeros[112]+",0",numeros[113],numeros[114],numeros[115],numeros[116] };
        String[] number_CHF = {numeros[117],numeros[118],numeros[119],numeros[120],numeros[121],numeros[122],numeros[123],numeros[124],numeros[125],numeros[126]+",0",numeros[127],numeros[128],numeros[129] };
        String[] number_BRL = {numeros[130],numeros[131],numeros[132],numeros[133],numeros[134],numeros[135],numeros[136],numeros[137],numeros[138],numeros[139],numeros[140]+",0",numeros[141],numeros[142] };
        String[] number_RUB = {numeros[143],numeros[144],numeros[145],numeros[146],numeros[147],numeros[148],numeros[149],numeros[150],numeros[151],numeros[152],numeros[153],numeros[154]+",0",numeros[155] };
        String[] number_MXN = {numeros[156],numeros[157],numeros[158],numeros[159],numeros[160],numeros[161],numeros[162],numeros[163],numeros[164],numeros[165],numeros[166],numeros[167],numeros[168]+",0" };

        int ddd2 = spin1.getSelectedItemPosition();     //SPINNER5 ES EL DE ABAJO
        int ddd1 = spin2.getSelectedItemPosition();     //SPINNER6 ES EL DE Arriba


        div1 = countryNames[ddd2];          //EL DE ABAJO
        div2 = countryNames[ddd1];             //EL DE ARRIBA
        String esto ="number_"+div2;
        String paiselegido = "";
        float valor1 = 0;
        switch (ddd1){

            case 0:
               paiselegido = number_USD[ddd2];
                break;
            case 1:
                paiselegido = number_EUR[ddd2];
                break;
            case 2:
                paiselegido = number_JPY[ddd2];
                break;
            case 3:
                paiselegido = number_CAD[ddd2];
                break;
            case 4:
                paiselegido = number_CLP[ddd2];
                break;
            case 5:
                paiselegido = number_CNY[ddd2];
                break;
            case 6:
                paiselegido = number_NZD[ddd2];
                break;
            case 7:
                paiselegido = number_AUD[ddd2];
                break;
            case 8:
                paiselegido = number_GBP[ddd2];
                break;
            case 9:
                paiselegido = number_CHF[ddd2];
                break;
            case 10:
                paiselegido = number_BRL[ddd2];
                break;
            case 11:
                paiselegido = number_RUB[ddd2];
                break;
            case 12:
                paiselegido = number_MXN[ddd2];
                break;

            default:
                    break;
        }

        String[] split1;
        split1 = paiselegido.split(",");
        String unidos = split1[0] + "." + split1[1];


        if (txt2.getText().toString().isEmpty()) {

            valor1 = 0;

        } else {
            valor1 = Float.parseFloat(txt2.getText().toString());
        }


        float valor2 = Float.parseFloat(unidos);
        float ultimo = valor1 * valor2;


        txt1.setText(String.valueOf(String.format("%.3f", ultimo)).toString());


        Toast.makeText(this, "Converted!", Toast.LENGTH_SHORT).show();

/*
        new Thread(new Runnable() {
                @Override
                public void run() {
                final StringBuilder builder = new StringBuilder();
                String asd = "";
                float valor1 = 0;
                int count1 = 0;




                    int ddd2 = spin1.getSelectedItemPosition();
                    int ddd1 = spin2.getSelectedItemPosition();

                    div2 = countryNames[ddd1];
                    div1 = countryNames[ddd2];

                    try {

                        Document document = Jsoup.connect("https://themoneyconverter.com/ES/" + div1 + "/" + div2 + "").userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").get();


                        Elements output = document.select("output#res1");


                        for (Element link : output) {

                            asd = link.text();


                        }

                        String[] parts = asd.split(" ");

                        String remplazo = parts[3];
////

                        String[] split1;
                        split1 = remplazo.split(",");
                        String unidos = split1[0] + "." + split1[1];


                        if (txt2.getText().toString().isEmpty()) {

                            valor1 = 0;

                        } else {
                            valor1 = Float.parseFloat(txt2.getText().toString());
                        }


                        float valor2 = Float.parseFloat(unidos);
                        float ultimo = valor1 / valor2;


                        txt1.setText(String.valueOf(ultimo).toString());


                    } catch (IOException e) {

                        builder.append("Error: ").append(e.getMessage()).append("\n");
                        count1++;


                  //      if (count1 == 30) {
                //            errorconexion();
              //          }


                    }

                }

        }).start();
*/






    }

    public void errorconexion() {

        Intent i = new Intent(this, ActivityConexion.class);
        startActivity(i);


    }


    public void copycopy(View v) {
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text;
        text = txt1.getText().toString();

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);

        Toast.makeText(getApplicationContext(), "copy " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == RESULT_OK) {


                //do nothing

            } else {


            }

        }
    }

    public static Bitmap getScreenshot(View view) {

        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;

    }


    /*private void shareBitmap (Bitmap bitmap,String fileName) {
        try {
            File file = new File(getBaseContext().getCacheDir(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFiles/Screenshotcurrency.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(     android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    public void prepareAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    public void clikeoloko(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    private void actualizardatos(){


        new Thread(new Runnable() {
            @Override
            public void run() {

               if (isOnlineNet()){


                   System.out.println("hay internil");
                   actualizar();

               }else {
                   System.out.println("no hay internil");
                   Crearfichero();




               }





            }
        }).start();


    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }
    public String datosguardados;


    private void Crearfichero(){

        String archivos [] = fileList();

        if (ArchivoExiste(archivos, "currency.txt")){

            System.out.println("si existe el archivo pasa por aka");
            if (isOnlineNet()) {
                actualizar();
            }

        }else{
            System.out.println("si NOOOO existe el archivo pasa por aka");
            datosguardados =  "1 0,90159 105,75 1,3285 720,70 7,1616 1,5713 1,4811 0,81378 0,98122 4,1260 66,546 20,009 1,1092 1 117,29 1,4735 799,36 7,9433 1,7428 1,6428 0,90260 1,0883 4,5764 73,810 22,193 0,0094600 0,0085300 1 0,012560 6,8150 0,067730 0,014860 0,014000 0,0077000 0,0092800 0,039020 0,62927 0,18925 0,75269 0,67859 79,595 1 542,44 5,3910 1,1827 1,1827 0,61251 0,73862 3,1059 50,087 15,063 0,0013900 0,0012500 0,14674 0,0018400 1 0,0099400 0,0021800 0,0020500 0,0011300 0,0013600 0,0057300 0,092340 0,027740 0,13962 0,12587 14,770 0,18545 100,62 1 0,21941 0,21941 0,11363 0,13703 0,57613 9,2908 2,7914 0,63636 0,57370 67,314 0,84524 458,59 4,5577 1 0,94222 0,51789 0,62454 2,6258 42,344 12,7225 0,67538 0,60888 71,442 0,89707 486,71 4,8372 1,0613 1 0,54964 0,66284 2,7868 44,941 13,503 13,503 1,1078 129,98 1,6321 885,50 8,8005 1,9309 1,8194 1 1,8194 5,0702 81,764 24,566 1,0189 0,91859 107,78 1,3534 734,28 7,2976 1,6012 1,5087 0,82923 1 4,2044 67,801 20,371 0,24235 0,21848 25,636 0,32190 174,65 1,7357 0,38083 0,35883 0,19723 0,23785 1 16,126 4,8451 0,015030 0,013550 1,5897 0,019960 10,830 0,10763 0,023620 0,0222514 0,012230 0,014750 0,062010 1 0,30045 0,050020 0,045090 5,2910 0,066440 36,046 0,35824 0,078600 0,074060 0,040710 0,049090 0,20639 3,3283 1";

            crearcrearfichero(datosguardados);


    }}

    private void crearcrearfichero(String cadena){

        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("currency.txt", Context.MODE_PRIVATE));

            fout.write(cadena);
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }



    }

    private void actualizar(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                String asd = "";
                float valor1 = 0;
                int count1 = 0;
                int count2 = 0;




                String[] country = new String[]{"USD", "EUR", "JPY", "CAD", "CLP", "CNY", "NZD", "AUD", "GBP", "CHF", "BRL", "RUB", "MXN"};





                for ( i = 0; i<country.length; i++){

                    uno = country[i];
                    System.out.println("contador 1 debe dar 13  " + count1);
                    count1++;
                    for (int j = 0; j<country.length; j++){
                        dos = country[j];



                        try {

                            Document document = Jsoup.connect("https://themoneyconverter.com/ES/" + uno + "/" + dos + "").userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").post();

                            System.out.println("contador 2 debe dar 168  " + count2);
                            Elements output = document.select("output#res1");


                            for (Element link : output) {

                                asd = link.text();


                            }

                            String[] parts = asd.split(" ");

                            remplazo = parts[3];


                                numerosnuevos = new String[169];
                                numerosnuevos[count2] = remplazo;
                                datofinal += remplazo + " ";
                                System.out.println(remplazo);
                                System.out.println(datofinal);


                            //
                            count2++;

////





                        } catch (IOException e) {

                            builder.append("Error: ").append(e.getMessage()).append("\n");



                        }
                    }

                }



               String  datofinal2 = datofinal.replace("null","");
                System.out.println(datofinal2);
                crearcrearfichero(datofinal2);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "\n" + "Updated data!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).start();




    }



    private void leerdatos(){

        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("currency.txt")));


            System.out.println("leee por aki???");


            String textos = fin.readLine();
            numeros= textos.split(" ");

            for(String aaa:numeros){
                System.out.println(aaa);
            }



            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }



    }

    private Boolean ArchivoExiste(String archivos [], String Nombredelarchivo){

        for (int i=0; i<archivos.length; i++)
            if (Nombredelarchivo.equals(archivos[i]))
                return true;
            return false;


    }

    public void numero5(View v){
        if (txt2.getText().toString().equals("1")) {
            txt2.setText("");
        }
    }
}
package kset.ivan.rasus.brucx;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * UPUTE ZA NAPRAVITI DA OVO RADI
 * 1. Otici u metodu onNewIntent() i pogledati kuda vas vodi
 * bravo
 * 2. Otici u metodu deleteTicket() i pogledati sto se to crveni
 * hint! crvene se svi pozivi .setText metode od strane tv
 * tv treba promjeniti u popunime!, to je pravo ime tog polja
 * tko otkrije kako popuniti polje popunime sa request.code() koji nam daje code status koji se onda
 * parsira switched dobije cokoladu
 *
 * Par obzervacija...ovo ne radi jer se poziv metode onResponse zapravo odvija u drugom threadu...
 * iz nekog razloga trenutno request.isSuccessful() je false
 */

public class Sell extends ActionBarActivity {

    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    OkHttpClient client;
    MediaType JSON;
    //AndroidHttpPostGet server = new AndroidHttpPostGet();
    NfcAdapter nfcAdapter;


    // list of NFC technologies detected:
    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

        mTextView = (TextView) findViewById(R.id.id_nfc_status);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }


        if (!nfcAdapter.isEnabled()) {
            AlertDialog alert = new AlertDialog.Builder(Sell.this).create();
            alert.setTitle("NFC checker");
            alert.setMessage("Please enable your NFC in order to continue");
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startNfcSettingsActivity();
                    dialog.dismiss();
                    mTextView.setText("Enabled later");
                }
            });
            alert.show();
            //mTextView.setText("NFC is disabled.");
        } else {
            mTextView.setText("NFC is enabled.");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /*if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            ((TextView)findViewById(R.id.id_tag)).setText(
                    "NFC Tag\n" +
                            ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
        }*/


        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {


            getStudentById(ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));



        }


    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    protected void startNfcSettingsActivity() {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    public void buyTicket(String id) {

        final String login = "cona";
        final String password = "cona123";
        String credential = Credentials.basic(login, password);


        JsonObject json = new JsonObject();
        json.addProperty("type", "test-karta");
        json.addProperty("price", 20);
        String jsonString = json.toString();
        RequestBody body = RequestBody.create(JSON, jsonString);


        final Request request = new Request.Builder()
                .url("http://tomcat.marinpetrunic.com:80/brucx-ws/api/v1/students/" + id + "/tickets")
                .header("Authorization", credential)
                .post(body)
                .build();


        ApiHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("cona", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    //success
                    // Read data on the worker thread
                    final String responseData = response.body().string();

                    // Run view-related code back on the main thread
                    Sell.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(Sell.this, "Karta uspjesno kupljena", Toast.LENGTH_LONG).show();

                        }
                    });
                } else {
                    //failure
                    // Read data on the worker thread
                    final int responseData = response.code();


                    // Run view-related code back on the main thread
                    Sell.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(Sell.this, "Student već ima kupljenu kartu", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
    }

    private void getStudentById(String id) {

        final String nfcid = id;
        final String login = "cona";
        final String password = "cona123";
        String credential = Credentials.basic(login, password);

        final Request request = new Request.Builder()
                .url("http://tomcat.marinpetrunic.com:80/brucx-ws/api/v1/students")
                .header("Authorization", credential)
                .build();

        ApiHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Sell.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Sell.this, "Nisam dobio odgovor", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()) {

                    List<Studenti> studenti = new ArrayList<>();
                    Studenti student1 = null;
                    String jsonResponse = response.body().string();

                    JsonParser parser = new JsonParser();
                    JsonElement jsonElement = parser.parse(jsonResponse);
                    JsonArray students = jsonElement.getAsJsonArray();

                    Gson gson = new Gson();
                    for (JsonElement object : students) {
                        studenti.add(gson.fromJson(object, Studenti.class));
                    }

                    for (int i = 0; i < studenti.size(); i++) {
                        if (studenti.get(i).getNfcId().equals(nfcid))
                            student1 = studenti.get(i);

                    }

                    final Studenti student = student1;

                    Sell.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            TextView name = (TextView) findViewById(R.id.id_name);
                            TextView status = (TextView) findViewById(R.id.id_status);
                            ImageView image = (ImageView) findViewById(R.id.id_image);
                            Button button = (Button) findViewById(R.id.id_buy);

                            // else if(student.getTickets() != null){
                            //for(int i=0; i < student.getTickets().size(); i++){

                            if(student==null){
                                status.setText("Student nije naden");
                                return;
                            }
                            /*if((student.getTickets().size() != 0)){
                                try {
                                    if (student.getTickets().get(0).getTicketType().getName().equals("test-karta") && student.getTickets().get(0).getUsed() == 1) {
                                        status.setText("Student već ima kupljenu kartu");
                                        return;
                                    }
                                } catch (NullPointerException e) {

                                }
                            }*/

                            name.setText(student.getName() + " " + student.getSurname());
                            status.setText("Klikni za kupiti kartu!");
                            //byte[] decodedString = Base64.decode(student.getImage(), Base64.DEFAULT);
                            //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            //image.setImageBitmap(decodedByte);
                            button.setVisibility(View.VISIBLE);
                            button.setClickable(true);

                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    buyTicket(student.getNfcId());
                                    Sell.this.recreate();
                                }
                            });

                            //}
                            //}
                            //} else {
                            //  TextVie
                            // w myTextView = (TextView) findViewById(R.id.id_status);
                            //myTextView.setText("Karta je već kupljena");
                            //}


                        }
                    });
                } else {
                    Sell.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView tv = (TextView) findViewById(R.id.id_status);
                            tv.setText("Student nije naden");
                        }
                    });
                }
            }
        });
    }
}

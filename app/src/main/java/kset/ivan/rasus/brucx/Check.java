package kset.ivan.rasus.brucx;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;

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

public class Check extends ActionBarActivity {

    public static final String TAG = "NfcDemo";

    private TextView mTextView;

    TextView popunime;

    OkHttpClient client;
    MediaType JSON;

    NfcAdapter nfcAdapter;


    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
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
        setContentView(R.layout.activity_check);

        client = new OkHttpClient();
        JSON = MediaType.parse("application/json; charset=utf-8");

        mTextView = (TextView) findViewById(R.id.id_nfc_status);
        popunime = (TextView) findViewById(R.id.id_tag);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!nfcAdapter.isEnabled()) {
            AlertDialog alert = new AlertDialog.Builder(Check.this).create();
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
       /* if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            ((TextView)findViewById(R.id.id_tag)).setText(
                    "NFC Tag\n" +
                            ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
        }*/

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            useTicket(ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
        }

    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
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

    public void useTicket(String id){

        final String login = "cona";
        final String password = "cona123";
        String credential = Credentials.basic(login, password);

        JsonObject json = new JsonObject();
        json.addProperty("used", 1 );
        String jsonString = json.toString();
        RequestBody body = RequestBody.create(JSON, jsonString);


        final Request request = new Request.Builder()
                .url("http://tomcat.marinpetrunic.com:80/brucx-ws/api/v1/students/"+id+"/tickets/test-karta")
                .header("Authorization", credential)
                .put(body)
                .build();

        ApiHelper.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("cona", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //success
                    // Read data on the worker thread
                    final String responseData = response.body().string();

                    // Run view-related code back on the main thread
                    Check.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                                TextView myTextView = (TextView) findViewById(R.id.id_tag);
                                myTextView.setText(responseData);

                        }
                    });
                } else {
                    //failure
                    // Read data on the worker thread
                    //final int responseData = response.code();
                    final String responseData = response.body().string();

                    // Run view-related code back on the main thread
                    Check.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            TextView myTextView = (TextView) findViewById(R.id.id_tag);
                            myTextView.setText(responseData);

                        }
                    });
                }
            }
        });


    }

}

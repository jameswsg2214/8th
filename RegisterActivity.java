package in.arjitha.video;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends BaseActivity implements View.OnClickListener,AppConstant {

    private EditText user_id_et,username_et,password_et;
    private Button mSubmit;

    JSONParser jsonParser = new JSONParser();
    private String add_user_id,add_username,add_password;

    private static final String TAG_STATUS = "status";
    private static final String TAG_CODE = "code";
    private static final String TAG_MESSAGE = "message";

    private  static final String TAG_USER = "user";
    private  static final String TAG_USER_ID = "user_id";
    private  static final String TAG_USERNAME= "username";
    private  static final String TAG_PASSWORD = "password";





    public int code;
    public String message;

    public static int memId;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle loginBg) {
        super.onCreate(loginBg);
        setContentView(R.layout.activity_register);
        StrictMode.enableDefaults();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        memId = getPreferencesMemId();

        user_id_et = (EditText) findViewById(R.id.UserId1);
        username_et = (EditText) findViewById(R.id.Username1);

        password_et = (EditText) findViewById(R.id.Password1);



        //setup buttons
        mSubmit = (Button) findViewById(R.id.SubmitButton);


        //register listeners
        mSubmit.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        add_user_id = user_id_et.getText().toString();
        add_username = username_et.getText().toString();
        add_password = password_et.getText().toString();




        if (!add_user_id.isEmpty()&&!add_username.isEmpty() &&!add_password.isEmpty()) {
            if (jsonParser.isConnected(getApplicationContext())) {
                new adduser().execute();
            }else {
                Toast.makeText(RegisterActivity.this, "Please Check Your Network Connection!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "Your Title or Message is Empty\nPlease Try Again!", Toast.LENGTH_LONG).show();
        }
    }


    class adduser extends AsyncTask<String, String, String> {









        @Override
        protected String doInBackground(String... params) {




            try {
                List<NameValuePair> param = new ArrayList<NameValuePair>(5);
                param.add(new BasicNameValuePair(TAG_USER_ID,add_user_id));


                param.add(new BasicNameValuePair(TAG_USERNAME,add_username));

                param.add(new BasicNameValuePair(TAG_PASSWORD,add_password));




                JSONObject json = jsonParser.makeHttpRequest(ADD_USER_LIST_URL_GET, "POST", param);

                JSONObject jsonStatus = json.getJSONObject(TAG_STATUS);
                code = jsonStatus.getInt(TAG_CODE);
                message = jsonStatus.getString(TAG_MESSAGE);


                if (code == 200) {
                  Intent i = new Intent(RegisterActivity.this, VideoViewActivity.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted

            if (file_url != null)
                Toast.makeText(RegisterActivity.this, "User login submitted successfully", Toast.LENGTH_LONG).show();

        }

    }
}


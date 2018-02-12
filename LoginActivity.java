
package in.arjitha.video;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener, AppConstant {

    private EditText user, pass, type;
    private Button mLogin;
    private TextView mRegister;

    JSONParser jsonParser = new JSONParser();
    private int userId;
    private int roleId;
    public String txtUser, txtPass, loginName, username, password;

    private static final String TAG_STATUS = "status";
    private static final String TAG_CODE = "code";
    private static final String TAG_MESSAGE = "message";
    private  static final String TAG_USER = "user";

    private  static final String TAG_USERNAME= "username";
    private  static final String TAG_PASSWORD = "password";

    public int code;
    public String message;

    private static final String TAG_USER_ID = "user_id";


    Spinner sp;
    String sp_data;

    public static int memId;

    private AVLoadingIndicatorView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        sp = (Spinner) findViewById(R.id.spinner1);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub

                sp_data = sp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //setup input fields
        user = (EditText) findViewById(R.id.TypeUserName);
        pass = (EditText) findViewById(R.id.TypePassword);


        //setup buttons
        mLogin = (Button) findViewById(R.id.LoginSubmit);
        mRegister = (TextView) findViewById(R.id.signUp);

        progressBar = (AVLoadingIndicatorView) findViewById(R.id.loading_bar);
        progressBar.setVisibility(View.GONE);
        //register listeners
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LoginSubmit: {
                txtUser = user.getText().toString().trim();
                txtPass = pass.getText().toString().trim();

                if (!txtUser.isEmpty() && !txtPass.isEmpty()) {
                    if (isOnline()) {
                        new UserLogin().execute();
                    } else
                        Toast.makeText(LoginActivity.this, "Please Check Your Network Connection!", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Username and Password is Empty!", Toast.LENGTH_LONG).show();

            }
            break;
            case R.id.signUp: {
                Intent i = new Intent(LoginActivity.this, VideoViewActivity.class);
                startActivity(i);
            }
            break;

            default:
                break;
        }
    }

    private class UserLogin extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... params) {

            int success;
            username = user.getText().toString();
            password = pass.getText().toString();
            sp_data = sp.getSelectedItem().toString();

            try {
                List<NameValuePair> param = new ArrayList<NameValuePair>(3);
                param.add(new BasicNameValuePair("username", username));
                param.add(new BasicNameValuePair("password", password));
                param.add(new BasicNameValuePair("user_type", sp_data));

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL_POST, "POST", param);

                JSONObject jsonStatus = json.getJSONObject(TAG_STATUS);
                code = jsonStatus.getInt(TAG_CODE);
                message = jsonStatus.getString(TAG_MESSAGE);

                if (code == 200) {

                    JSONArray jsonarray = json.getJSONArray(TAG_USER);
                    if (jsonarray != null) {
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonUser = jsonarray.getJSONObject(i);

                            userId = jsonUser.getInt(TAG_USER_ID);


                            loginName = jsonUser.getString(TAG_USERNAME);

                        }
                    }
                    storePreferencesMemId(userId);
                    storePreferencesName(loginName);
                   Intent admin = new Intent(LoginActivity.this, RegisterActivity.class);
                    finish();
                    startActivity(admin);
                   
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
            if (file_url != null) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onBackPressed() {
        Logout();
    }


}

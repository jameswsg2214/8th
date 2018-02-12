package.in.arjitha.video;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 6/15/2017.
 */

public class UserListAllActivity extends AppCompatActivity implements AppConstant {

    Toolbar toolbar;
    JSONParser jsonParser =new JSONParser();

    private  static final String TAG_STATUS = "status";
    private  static final String TAG_CODE = "code";
    private  static final String TAG_MESSAGE = "message";

    private  static final String TAG_USER = "user";
    private  static final String TAG_USER_ID = "user_id";
    private  static final String TAG_USERNAME= "username";
    private  static final String TAG_PASSWORD = "password";


    public int code;
    public String message;

    public int user_id;
    public String username,password;

    private ListView list;
    private List<User> UserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        StrictMode.enableDefaults();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = (ListView) findViewById(R.id.UserList);
        getData();

    }
    private void getData() {
        if (jsonParser.isConnected(getApplicationContext())) {
            new UserList().execute();
        } else
            Toast.makeText(UserListAllActivity.this, "Please Check Your Network Connection!", Toast.LENGTH_LONG).show();
    }

    public class UserList extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                List<NameValuePair> param = new ArrayList<NameValuePair>(1);


                JSONObject json = jsonParser.makeHttpRequest(USER_LIST_ALL_URL_GET, "POST", param);

                JSONObject jsonStatus = json.getJSONObject(TAG_STATUS);
                code = jsonStatus.getInt(TAG_CODE);
                message = jsonStatus.getString(TAG_MESSAGE);


                if (code == 200) {
                    JSONArray jsonarray = json.getJSONArray(TAG_USER);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonUser = jsonarray.getJSONObject(i);
                        user_id = jsonUser.getInt(TAG_USER_ID);

                        username = jsonUser.getString(TAG_USERNAME);
                        password = jsonUser.getString(TAG_PASSWORD);


                        UserList.add(new User(  user_id,  username,  password));
                    }

                    return message;
                } else {
                    return message;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }



        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            if (file_url != null) {

                populateUserList();
                Toast.makeText(getApplicationContext(), file_url, Toast.LENGTH_LONG).show();
            }
        }

        private void populateUserList() {
            ArrayAdapter<User> adapter = new UserListAdaptor();
            list.setAdapter(adapter);

        }

        private class UserListAdaptor extends ArrayAdapter<User> {
            public UserListAdaptor() {
                super(UserListAllActivity.this, R.layout.user_single_list, UserList);
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View ReferralView = convertView;

                if (ReferralView == null) {
                    ReferralView = getLayoutInflater().inflate(R.layout.user_single_list, parent, false);
                }

                User CurrentReferral = UserList.get(position);


                TextView Feedback_Id = (TextView) ReferralView.findViewById(R.id.UserId);
                Feedback_Id.setText("User Id : " + CurrentReferral.getUser_id());

                TextView Job_Seeker_Id = (TextView) ReferralView.findViewById(R.id.Username);
                Job_Seeker_Id.setText("Username : " + CurrentReferral.getUsername());


                TextView Feedback_Comment  = (TextView) ReferralView.findViewById(R.id.Password);
                Feedback_Comment.setText("Password : " + CurrentReferral.getPassword());





                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.push_up_in);
                animation.setDuration(500);
                ReferralView.startAnimation(animation);

                return ReferralView;
            }


        }


    }

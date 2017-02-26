package baltamon.mx.realmpractice;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import baltamon.mx.realmpractice.models.Friend;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 24/02/2017.
 */

public class FriendDetailActivity extends AppCompatActivity {

    private Realm realm;
    private FriendViewHolder holder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        setUpToolbar();
        startingObjects();
        startViewHolder();

        Bundle bundle = getIntent().getExtras();
        searchFriedn(bundle.getInt("friendID"));
    }

    private void searchFriedn(int friendID) {
        Friend friend = realm.where(Friend.class).equalTo("id",friendID).findFirst();
        holder.friendFirstName.setText(friend.getFirstName());
        holder.friendLastName.setText(friend.getLastName());
        holder.friendEmail.setText(friend.getEmail());
        holder.friendPhone.setText(friend.getPhoneNumber());
    }

    private void startingObjects() {
        Button button = (Button) findViewById(R.id.btnActionButton);
        button.setText("Update friend");
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null){
            actionbar.setTitle("Friend Detail");
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
        }
    }

    private void startViewHolder(){
        holder = new FriendViewHolder();
        holder.friendFirstName = (EditText) findViewById(R.id.etFirstName);
        holder.friendLastName = (EditText) findViewById(R.id.etLastName);
        holder.friendEmail = (EditText) findViewById(R.id.etEmail);
        holder.friendPhone = (EditText) findViewById(R.id.etPhone);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

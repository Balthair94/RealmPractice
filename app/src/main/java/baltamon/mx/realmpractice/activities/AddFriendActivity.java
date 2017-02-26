package baltamon.mx.realmpractice.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;

import baltamon.mx.realmpractice.FriendViewHolder;
import baltamon.mx.realmpractice.MigrationVersion;
import baltamon.mx.realmpractice.R;
import baltamon.mx.realmpractice.models.Friend;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 24/02/2017.
 */

public class AddFriendActivity extends AppCompatActivity {

    private Realm realm;
    private FriendViewHolder holder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        setUpRealm();

        setUpToolbar();
        startingObjects();
        startViewHolder();
    }

    private void setUpRealm(){
        Realm.init(getApplicationContext());

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("database.realm").schemaVersion(1).build();

        realm = Realm.getInstance(configuration);
    }

    private void startingObjects() {
        Button button = (Button) findViewById(R.id.btnActionButton);
        button.setText("add friend");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingFriend();
            }
        });
    }

    private void addingFriend() {
        Friend friend = new Friend();
        friend.setId(getFriendID());
        friend.setFirstName(holder.friendFirstName.getText().toString());
        friend.setLastName(holder.friendLastName.getText().toString());
        friend.setEmail(holder.friendEmail.getText().toString());
        friend.setPhoneNumber(holder.friendPhone.getText().toString());

        callRealmTransaction(friend);
    }

    private int getFriendID(){
        RealmResults<Friend> results = realm.where(Friend.class).findAll();

        if (results.isEmpty())
            return 1;
        else
            return results.max("id").intValue() + 1;

    }

    private void callRealmTransaction(final Friend friend) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Friend saveFriend = bgRealm.copyToRealm(friend);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                onBackPressed();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "Error adding friend", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null){
            actionbar.setTitle("Add friend");
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

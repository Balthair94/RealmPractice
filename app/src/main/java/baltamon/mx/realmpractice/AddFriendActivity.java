package baltamon.mx.realmpractice;

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

import baltamon.mx.realmpractice.models.Friend;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 24/02/2017.
 */

public class AddFriendActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        setUpToolbar();
        startingObjects();
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
        EditText friendFirstName = (EditText) findViewById(R.id.etFirstName);
        EditText friendLastName = (EditText) findViewById(R.id.etLastName);
        EditText friendEmail = (EditText) findViewById(R.id.etEmail);
        EditText friendPhone = (EditText) findViewById(R.id.etPhone);

        Friend friend = new Friend();
        friend.setId(getFriendID());
        friend.setFirstName(friendFirstName.getText().toString());
        friend.setLastName(friendLastName.getText().toString());
        friend.setEmail(friendEmail.getText().toString());
        friend.setPhoneNumber(friendPhone.getText().toString());

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

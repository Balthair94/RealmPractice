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
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Baltazar Rodriguez on 24/02/2017.
 */

public class FriendDetailActivity extends AppCompatActivity {

    private Realm realm;
    private FriendViewHolder holder;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        setUpToolbar();
        startingObjects();
        startViewHolder();

        bundle = getIntent().getExtras();
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFriend();
            }
        });
    }

    private void updateFriend(){
        final Friend friend = new Friend();
        friend.setId(bundle.getInt("friendID"));
        friend.setFirstName(holder.friendFirstName.getText().toString());
        friend.setLastName(holder.friendLastName.getText().toString());
        friend.setEmail(holder.friendEmail.getText().toString());
        friend.setPhoneNumber(holder.friendPhone.getText().toString());

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(friend);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                onBackPressed();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "Error updating friend", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
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

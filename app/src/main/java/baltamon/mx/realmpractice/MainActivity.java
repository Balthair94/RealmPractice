package baltamon.mx.realmpractice;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import baltamon.mx.realmpractice.adapters.FriendListAdapter;
import baltamon.mx.realmpractice.models.Friend;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();

        setUPToolbar();
        setUpFloatingButton();

        if (weHaveFriends())
            fillFrendsList();
        else
            Toast.makeText(this, "You don't have friends", Toast.LENGTH_SHORT).show();

    }

    private void fillFrendsList() {
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new FriendListAdapter(getApplicationContext(), friends);
        listView.setAdapter(adapter);
    }

    public boolean weHaveFriends(){
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();

        if (friends.isEmpty()) {
            return false;
        }

        return true;
    }

    private void setUPToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("My friends");
        }
    }

    private void setUpFloatingButton() {
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddFriendActivity.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

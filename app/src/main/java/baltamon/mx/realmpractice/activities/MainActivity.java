package baltamon.mx.realmpractice.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import baltamon.mx.realmpractice.MigrationVersion;
import baltamon.mx.realmpractice.R;
import baltamon.mx.realmpractice.adapters.FriendListAdapter;
import baltamon.mx.realmpractice.models.Friend;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRealm();

        setUPToolbar();
        setUpFloatingButton();

        if (weHaveFriends())
            fillFrendsList();
        else
            Toast.makeText(this, "You don't have friends", Toast.LENGTH_SHORT).show();

    }

    private void setUpRealm(){
        Realm.init(getApplicationContext());

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("database.realm").schemaVersion(1).build();

        //THE MIGRATION
        try {
            Realm.migrateRealm(configuration, new MigrationVersion());
        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        realm = Realm.getInstance(configuration);
    }

    private void fillFrendsList() {
        RealmResults<Friend> friends = realm.where(Friend.class).findAll();
        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new FriendListAdapter(getApplicationContext(), friends);
        listView.setAdapter(adapter);
        onListViewItemClick(listView);
    }

    private void onListViewItemClick(ListView listView){
        /*
        SEE FRIEND DETAIL
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i) != null){
                    int friendID = adapter.getItem(i).getId();
                    Intent intent = new Intent(MainActivity.this, FriendDetailActivity.class);
                    intent.putExtra("friendID", friendID);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        DELETE FRIEND
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i) != null){
                    final int friendID = adapter.getItem(i).getId();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(Friend.class).equalTo("id", friendID).findFirst().deleteFromRealm();
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), "Goodbye friend", Toast.LENGTH_SHORT).show();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });

                    adapter.notifyDataSetChanged();
                    return true;
                } else
                    return false;
            }
        });
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

        if (adapter == null)
            fillFrendsList();
        else
            adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}

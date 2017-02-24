package baltamon.mx.realmpractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import baltamon.mx.realmpractice.R;
import baltamon.mx.realmpractice.models.Friend;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;


/**
 * Created by Baltazar Rodriguez on 24/02/2017.
 */

public class FriendListAdapter extends RealmBaseAdapter<Friend> implements ListAdapter{

    private static class ViewHolder{
        TextView friendName;
    }

    public FriendListAdapter(Context context, OrderedRealmCollection<Friend>  friends){
        super(context, friends);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (contentView == null){
            contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.friendName = (TextView) contentView.findViewById(R.id.tvFriendName);
            contentView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) contentView.getTag();

        Friend friend = adapterData.get(position);
        viewHolder.friendName.setText(friend.getFirstName() + " " + friend.getLastName());
        return contentView;
    }
}

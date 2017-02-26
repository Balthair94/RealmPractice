package baltamon.mx.realmpractice;

import baltamon.mx.realmpractice.models.Friend;
import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Baltazar Rodriguez on 26/02/2017.
 */

public class MigrationVersion implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema realmSchema =  realm.getSchema();

        /****
         * Version 0 to version 1
         */

        if (oldVersion == 0){
            // Create a new class

            RealmObjectSchema objectSchema = realmSchema.create("Pet")
                    .addField("animal", String.class)
                    .addField("name", String.class);

            realmSchema.get("Friend").addRealmListField("pets", objectSchema);

            oldVersion++;
        }
    }
}

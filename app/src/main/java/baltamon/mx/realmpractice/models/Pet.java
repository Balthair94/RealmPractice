package baltamon.mx.realmpractice.models;

import io.realm.RealmObject;

/**
 * Created by Baltazar Rodriguez on 26/02/2017.
 */

public class Pet extends RealmObject {
    private String animal;
    private String name;

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.healthcare.bosch.patientapp.utils.RealmHelper;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class IntelliHealthRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();

        // Migrate to version 1: Add a new class.
        // Example:
        // public Person extends RealmObject {
        //     private String name;
        //     private int age;
        //     // getters and setters left out for brevity
        // }
        if (oldVersion == 3) {
            RealmObjectSchema sellerSchema = schema.get("Seller");
            sellerSchema.addField("id", int.class, FieldAttribute.PRIMARY_KEY);

            oldVersion++;
        }

        // Migrate to version 2: Add a primary key + object references
        // Example:
        // public Person extends RealmObject {
        //     private String name;
        //     @PrimaryKey
        //     private int age;
        //     private Dog favoriteDog;
        //     private RealmList<Dog> dogs;
        //     // getters and setters left out for brevity
        // }
//        if (oldVersion == 1) {
//            oldVersion++;
//        }
//        if (oldVersion == 2) {
//            // new migrationcode
//            RealmObjectSchema supplySchema = schema.create("Supply").addField("id", String.class).addField("name", String.class);
//            RealmObjectSchema sellerSchema = schema.get("Seller");
//            sellerSchema.addRealmListField("supplies", supplySchema);
//            oldVersion++;
//        }
    }
}
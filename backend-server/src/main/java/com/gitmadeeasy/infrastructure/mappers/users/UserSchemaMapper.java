package com.gitmadeeasy.infrastructure.mappers.users;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.gateways.users.FirebaseUserSchema;
import com.gitmadeeasy.infrastructure.gateways.users.JpaUserSchema;

public class UserSchemaMapper {

    public JpaUserSchema toJpaSchema(User user) {
        return new JpaUserSchema(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmailAddress());
    }

    public User fromJpaSchema(JpaUserSchema schema) {
        return new User(schema.getId(), schema.getFirstName(), schema.getLastName(), schema.getEmailAddress());
    }



    // ----- Firebase-Related Mapping ----- //

    public FirebaseUserSchema toFirebaseSchema(User user) {
        FirebaseUserSchema schema = new FirebaseUserSchema(user.getFirstName(), user.getLastName(), user.getEmailAddress());
        schema.setId(user.getUserId());
        return schema;
    }

    public User fromFirebaseSchema(FirebaseUserSchema schema) {
        return new User(schema.getId(), schema.getFirstName(), schema.getLastName(), schema.getEmailAddress());
    }
}
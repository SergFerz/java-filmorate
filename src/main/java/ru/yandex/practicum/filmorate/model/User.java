package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Value
public class User {

     long id;
     String email;
     String login;
     @NonFinal@Setter String name;
     LocalDate birthday;

     private final Set<Long> friends = new TreeSet<>();

     public void addFriend(long id) {
         friends.add(id);
     }

     public void deleteFriend(long id) {
          friends.remove(id);
     }
}

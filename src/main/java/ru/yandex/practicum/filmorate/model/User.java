package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Value
public class User {

     Long id;
     String email;
     String login;
     @NonFinal@Setter String name;
     LocalDate birthday;

     private final Set<Long> friends = new TreeSet<Long>();

     public void addFriend(Long id) {
         friends.add(id);
     }

     public void deleteFriend(Long id) {
          friends.remove(id);
     }
}

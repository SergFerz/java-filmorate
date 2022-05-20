package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Value
public class User {

     Integer id;
     String email;
     String login;
     @NonFinal@Setter String name;
     LocalDate birthday;

     private final Set<Integer> friends = new TreeSet<Integer>();

     public void addFriend(Integer id) {
         friends.add(id);
     }

     public void deleteFriend(Integer id) {
          friends.remove(id);
     }
}

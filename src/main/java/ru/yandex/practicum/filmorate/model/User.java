package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Value
public class User {

     @NotNull
     @NotBlank
     Long id;
     @NotNull
     @Email
     @NotBlank
     String email;
     @NotNull
     @NotBlank
     String login;
     @NonFinal
     @Setter
     String name;
     @NotNull
     @NotBlank
     LocalDate birthday;

     private final Set<Long> friends = new TreeSet<Long>();

     public void addFriend(Long id) {
         friends.add(id);
     }

     public void deleteFriend(Long id) {
          friends.remove(id);
     }
}

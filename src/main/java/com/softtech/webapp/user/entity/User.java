package com.softtech.webapp.user.entity;

import com.softtech.webapp.general.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class User extends BaseEntity {
    @Id
    @SequenceGenerator(name = "UserGen", sequenceName = "USER_ID_SEQ")
    @GeneratedValue(generator = "UserGen")
    private Long id;
    private String username;
    private String usernameUpper;
    private String password;
    private String name;
    private String surname;
}

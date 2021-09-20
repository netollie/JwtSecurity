package com.netollie.demo.object.bo;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserBO {
    private String username;

    public static UserBO of(DecodedJWT decodedJWT) {
        UserBO user = new UserBO();
        user.setUsername(decodedJWT.getAudience().get(0));
        return user;
    }
}

package com.egs.account.utils.convertor;

import com.egs.account.model.User;
import com.egs.account.model.ajax.JsonUser;
import org.modelmapper.ModelMapper;

public class UserToJsonUserConverter {
    public static JsonUser toJsonUser(User user) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(user, JsonUser.class);
    }
}

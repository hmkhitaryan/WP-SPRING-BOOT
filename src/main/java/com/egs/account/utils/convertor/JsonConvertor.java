package com.egs.account.utils.convertor;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonConvertor {

    public static String toJson(Object o) throws Exception{
	    final ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(o);
    }

}

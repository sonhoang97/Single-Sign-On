package com.example.demooauth2.commons.Converter;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.util.SerializationUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class AuthenticationByteConverter implements AttributeConverter<Authentication, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(Authentication authentication) {
        return SerializationUtils.serialize(authentication);

    }

    @Override
    public Authentication convertToEntityAttribute(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
}

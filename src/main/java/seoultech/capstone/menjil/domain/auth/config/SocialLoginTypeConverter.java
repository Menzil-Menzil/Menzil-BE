package seoultech.capstone.menjil.domain.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import seoultech.capstone.menjil.domain.auth.domain.SocialLoginType;

@Configuration
public class SocialLoginTypeConverter implements Converter<String, SocialLoginType> {
    @Override
    public SocialLoginType convert(String str) {
        return SocialLoginType.valueOf(str.toUpperCase());
    }
}

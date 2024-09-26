package org.ecommerce.ecommerce.configurations;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = Map.of(
                "cloud_name", "dqnwxejgy",
                "api_key", "486184719822545",
                "api_secret", "XOtBNDM_B6EzTK9TUQ9ET1CBDHA");
        return new Cloudinary(config);
    }
}

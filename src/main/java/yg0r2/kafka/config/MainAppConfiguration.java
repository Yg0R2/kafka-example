package yg0r2.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;

//@Configuration
public class MainAppConfiguration {

//    @Bean
    public InstrumentationLoadTimeWeaver loadTimeWeaver() {
        InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        return loadTimeWeaver;
    }

}

package yg0r2.kafka.config;

import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;

//@Configuration
public class MainAppConfiguration {

//    @Bean
    public InstrumentationLoadTimeWeaver loadTimeWeaver() {
        InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        return loadTimeWeaver;
    }

}

package com.param.hl7;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EnableHl7 implements Condition {

    private static final Logger logger = LoggerFactory.getLogger(EnableHl7.class);

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata)  {
        try {
            Resource resource = new ClassPathResource("jdbc.properties");
            InputStreamReader input = new InputStreamReader(resource.getInputStream());
            Properties properties = new Properties();
            properties.load(input);
            Boolean enableHl7 = Boolean.valueOf(properties.getProperty("hl7.service.enabled"));
            System.out.println("enableHl7enableHl7enableHl7enableHl7enableHl7enableHl7enableHl7enableHl7"+enableHl7);
            return enableHl7;
        } catch (IOException e) {
            // Not converted to a thrown exception: this Condition is evaluated during Spring
            // context startup, so throwing here would abort application boot entirely instead
            // of just disabling the HL7 subsystem. Logged instead, see docs/REVIEW_hiswar-exceptions.md.
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}

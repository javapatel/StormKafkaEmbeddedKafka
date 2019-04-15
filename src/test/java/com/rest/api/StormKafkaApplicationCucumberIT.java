package com.rest.api;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@RunWith(Cucumber.class)
//@Profile(value = "test")
@CucumberOptions(features = "src/test/resources/Test.feature", format = {"pretty", "html:target/reports/cucumber/html",
        "json:target/cucumber.json"})
public class StormKafkaApplicationCucumberIT {
    static final String TEST_INPUT_TOPIC = "helloworld.t";


    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(
            2, true, 2, TEST_INPUT_TOPIC, "messages");
}

package com.rest.api;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;

public class KafkaStepsBase extends KafkaApplicationBase {

    @Given("^the bag is empty$")
    public void the_bag_is_empty() throws Exception {
        // Write code here that turns the phrase above into concrete actions
        setup();
    }

    @When("^I put (\\d+) potato in the bag$")
    public void i_put_potato_in_the_bag(String paymentId) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        Map<String, Object> configs = new HashMap(KafkaTestUtils.producerProps(embeddedKafka));
        ProducerRecord producerRecord = new ProducerRecord<String, String>(TEST_INPUT_TOPIC, paymentId);
        Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
        producer.send(producerRecord);

    }

    @Then("^the bag should contain only (\\d+) potato$")
    public void the_bag_should_contain_only_potato(String arg1) throws Exception {
        Thread.sleep(100000);
        Assert.assertEquals(false, paymentStoreService.getPayment(arg1).isEmpty());
    }

}

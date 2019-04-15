package com.rest.api;

import com.storm.ConsumerBolt;
import com.storm.InMemoryPaymentStore;
import com.storm.PaymentStoreService;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.storm.Config;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class StormKafkaApplicationTests {
    static final String TEST_INPUT_TOPIC = "test.input.topic";
    LocalTopologySubmitter stormTopologySubmitter = new LocalTopologySubmitter();
    StormTopologyBuilder stormTopologyBuilder = new StormTopologyBuilder();

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(
            2, true, 2, TEST_INPUT_TOPIC, "messages");

    @Before
    public void setup() throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        submitTopology();
    }

    @Test
    @Ignore
    public void testReceive() throws Exception {

      /*  EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(1, true);
        embeddedKafkaBroker.afterPropertiesSet();
        KafkaServer kafkaServer = embeddedKafkaBroker.getKafkaServer(0);
        BrokerAddress brokerAddress = embeddedKafkaBroker.getBrokerAddress(0);
        kafkaServer.startup();*/

        Map<String, Object> configs = new HashMap(KafkaTestUtils.producerProps(embeddedKafka));
        String payloadMessage = "paymentId";
        ProducerRecord producerRecord = new ProducerRecord<String, String>(TEST_INPUT_TOPIC, payloadMessage);
        Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
        producer.send(producerRecord);


        submitTopology();
        Thread.sleep(10000);
        PaymentStoreService defaultInstance = PaymentStoreService.getDefaultInstance();
        System.out.println(defaultInstance);
        Assert.assertEquals(false, defaultInstance.getPayment(payloadMessage).isEmpty());

    }

    private void submitTopology() throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Config config = new Config();
        config.setDebug(true);
        String bootstrapServers = embeddedKafka.getBrokersAsString();
        System.out.println("Broker -- " + bootstrapServers);
        StormTopology topology = stormTopologyBuilder.build(new ConsumerBolt(new InMemoryPaymentStore()), TEST_INPUT_TOPIC, bootstrapServers);

        stormTopologySubmitter.submitTopology("kafka-storm-kafka", config, topology);
    }
}


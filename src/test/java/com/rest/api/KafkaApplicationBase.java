package com.rest.api;

import com.storm.ConsumerBolt;
import com.storm.InMemoryPaymentStore;
import com.storm.PaymentStoreService;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.storm.Config;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.junit.Before;
import org.junit.ClassRule;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;


public class KafkaApplicationBase extends StormKafkaApplicationCucumberIT{



    protected PaymentStoreService paymentStoreService=PaymentStoreService.getDefaultInstance();


    @Before
    public void setup() throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Config config = new Config();
        config.setDebug(true);
        String bootstrapServers = embeddedKafka.getBrokersAsString();
        System.out.println("Broker -- " + bootstrapServers);
        StormTopology topology = new StormTopologyBuilder().build(new ConsumerBolt(paymentStoreService), TEST_INPUT_TOPIC, bootstrapServers);

        new LocalTopologySubmitter().submitTopology("kafka-storm-kafka", config, topology);
    }


//    public Map<String, Object> producerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//        return props;
//    }
//
//    public ProducerFactory<String, String> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//
//    protected KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());

}


package services.statistics.impl;


import model.Writable;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import play.Play;
import services.statistics.StatService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

public class KafkaStatisticsService<T extends Writable> implements StatService<T> {

    public KafkaStatisticsService() {
        configure();
    }

    private KafkaProducer<String, byte[]> producer;

    private String topic = Play.application().configuration().getString("statistics.kafka.topic", "statistics");

    private void configure() {
//        Properties props = new Properties();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Play.application().configuration().getString("statistics.kafka.servers", "localhost"));
//        props.put(ProducerConfig.RETRIES_CONFIG, Play.application().configuration().getString("statistics.kafka.servers", "localhost"));
//        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, Play.application().configuration().getString("statistics.kafka.compression.type", "none"));
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, Play.application().configuration().getString("statistics.kafka.batch", "200"));
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
//
//        producer = new KafkaProducer<>(props);
    }

    @Override
    public void send(Collection<T> objects) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (T object : objects) {
            try {
                object.writeTo(out);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            producer.send(new ProducerRecord<>(topic, UUID.randomUUID().toString(), out.toByteArray()));
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncSend(Collection<T> objects) {
        send(objects);
    }
}

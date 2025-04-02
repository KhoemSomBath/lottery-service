package com.hacknovation.systemservice.config;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientConnectionStrategyConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author Sombath
 * create at 6/9/22 11:31 AM
 */
@Configuration
public class HazlecastClientConfig {

    @Value("${spring.hazelcast.cluster-name}")
    String clusterName;

    @Value("${spring.hazelcast.member-list}")
    String[] memberIP;

    @Value("${spring.application.name}")
    String instanceName;

    @Bean
    public HazelcastInstance hazelcastInstance(ClientConfig clientConfig){
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    public ClientConfig clientConfig(){
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.setInstanceName(instanceName);
        clientConfig.getConnectionStrategyConfig().setReconnectMode(ClientConnectionStrategyConfig.ReconnectMode.ON);
        ClientNetworkConfig networkConfig = new ClientNetworkConfig();
        networkConfig.setAddresses(Arrays.asList(memberIP));

        clientConfig.setNetworkConfig(networkConfig);

        return clientConfig;
    }
}

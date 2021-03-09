package org.openpaas.paasta.bosh;

import org.openpaas.paasta.bosh.director.BoshDirector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoshApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(BoshApplication.class, args);
        BoshDirector boshDirector = new BoshDirector("admin","e6mlw21gvk9bkbnb64ep","https://localhost:25555","https://localhost:8443");
        System.out.println(boshDirector.getDeploymentTasks("redis-service"));


        String deploy = "addons:\n" +
                "- jobs:\n" +
                "  - name: bpm\n" +
                "    release: bpm\n" +
                "  name: bpm\n" +
                "- include:\n" +
                "    stemcell:\n" +
                "    - os: ubuntu-trusty\n" +
                "    - os: ubuntu-xenial\n" +
                "  jobs:\n" +
                "  - name: metrics_agent\n" +
                "    properties:\n" +
                "      metrics_agent:\n" +
                "        influxdb:\n" +
                "          database: cf_metric_db\n" +
                "          measurement: cf_metrics\n" +
                "          processMeasurement: cf_process_metrics\n" +
                "          url: 192.168.22.80:8089\n" +
                "    release: paasta-monitoring-agent\n" +
                "  name: paasta_metrics_agent\n" +
                "instance_groups:\n" +
                "- azs:\n" +
                "  - z5\n" +
                "  instances: 1\n" +
                "  jobs:\n" +
                "  - name: redis\n" +
                "    release: redis-servicer-release\n" +
                "  name: redis\n" +
                "  networks:\n" +
                "  - name: default\n" +
                "  persistent_disk_type: 1GB\n" +
                "  stemcell: default\n" +
                "  vm_type: medium\n" +
                "name: redis-service\n" +
                "properties:\n" +
                "  password: in-soft\n" +
                "  port: 6379\n" +
                "releases:\n" +
                "- name: bpm\n" +
                "  version: latest\n" +
                "- name: redis-service-release\n" +
                "  version: \"1.0\"\n" +
                "- name: paasta-monitoring-agent\n" +
                "  version: latest\n" +
                "stemcells:\n" +
                "- alias: default\n" +
                "  os: ubuntu-xenial\n" +
                "  version: latest\n" +
                "update:\n" +
                "  canaries: 1\n" +
                "  canary_watch_time: 5000-120000\n" +
                "  max_in_flight: 1\n" +
                "  serial: false\n" +
                "  update_watch_time: 5000-120000";
        //boshDirector.createServiceInstance(deploy);
    }
}

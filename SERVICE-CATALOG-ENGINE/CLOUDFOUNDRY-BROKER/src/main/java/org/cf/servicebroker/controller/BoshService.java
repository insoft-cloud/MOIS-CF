package org.cf.servicebroker.controller;

import com.google.gson.Gson;
import org.cf.servicebroker.common.Constants;
import org.cf.servicebroker.exception.ServiceBrokerException;
import org.cf.servicebroker.model.deployment.DeploymentInstance;
import org.cf.servicebroker.model.deployment.DeploymentLock;
import org.cf.servicebroker.model.deployment.JpaDeployment;
import org.cf.servicebroker.model.plan.JpaServicePlan;
import org.cf.servicebroker.model.serviceinstance.JpaServiceInstn;
import org.cf.servicebroker.repo.JpaDeploymentRepository;
import org.cf.servicebroker.repo.JpaServiceInstanceRepository;
import org.cf.servicebroker.repo.JpaServicePlanRepository;
import org.json.JSONArray;
import org.openpaas.paasta.bosh.director.BoshDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class BoshService {
    private static final Logger logger = LoggerFactory.getLogger(BoshService.class);

    private final BoshDirector boshDirector;
    private final JpaServicePlanRepository jpaServicePlanRepository;
    private final JpaServiceInstanceRepository jpaServiceInstanceRepository;
    private final JpaDeploymentRepository jpaDeploymentRepository;

    public BoshService(BoshDirector boshDirector, JpaServicePlanRepository jpaServicePlanRepository, JpaServiceInstanceRepository jpaServiceInstanceRepository, JpaDeploymentRepository jpaDeploymentRepository) {
        this.boshDirector = boshDirector;
        this.jpaServicePlanRepository = jpaServicePlanRepository;
        this.jpaServiceInstanceRepository = jpaServiceInstanceRepository;
        this.jpaDeploymentRepository = jpaDeploymentRepository;
    }


    public Map getDeployment(String deployment_name) throws Exception {
        return boshDirector.getDeployments(deployment_name);
    }

    public List<DeploymentInstance> getVmInstance(JpaServiceInstn jpaServiceInstn) {
        logger.info("getserviceInstance :::: {} ,  {}", jpaServiceInstn.getDtbNm(), jpaServiceInstn.getService().getSvcNm() );
        List<DeploymentInstance> deploymentInstances = new ArrayList<DeploymentInstance>();
        try {
            String tasks = boshDirector.getListDetailOfInstances(jpaServiceInstn.getDtbNm());
            Thread.sleep(2000);
            List<Map> results = boshDirector.getResultRetrieveTasksLog(tasks);
            logger.info("result ::" + results.toString());
            for (Map map : results) {
                if (map.get("job_name").equals(jpaServiceInstn.getService().getSvcNm())) {
                    DeploymentInstance deploymentInstance = new DeploymentInstance(map);
                    deploymentInstances.add(deploymentInstance);
                }
            }
            return deploymentInstances;
        } catch (Exception e) {
            return deploymentInstances;
        }
    }

    public Map getState(String taskid) throws Exception {
        Map map = new HashMap();

        Map result = boshDirector.getTask(taskid);
        if(result.get("state").equals("error") || result.get("state").equals("cancelled")){
            map.put("result", true);
            map.put("dc", result.get("result").toString());
            return map;
        }
        map.put("result", false);
        return map;
    }

    public boolean getLock(String deployment_name) {
        try {
            String locks = boshDirector.getListLocks();
            JSONArray jsonArray = new JSONArray(locks);
            for (int i = 0; i < jsonArray.length(); i++) {
                String json = jsonArray.get(i).toString();
                DeploymentLock dataJson = new Gson().fromJson(json, DeploymentLock.class);
                if (dataJson.resuource[0].equals(deployment_name)) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return false;
    }

    public void updateInstanceState(String deployment_name, String instance_name, String instance_id, String type) {
        try {
            boshDirector.updateInstanceState(deployment_name, instance_name, instance_id, type);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public Map getListRunningTask(String deployment_name, String task_id) throws Exception {
        List<Map> deployTaskList = boshDirector.getListRunningTasks();
        List<Map> running_deployTask = deployTaskList.stream().filter(r -> r.get("deployment").equals(deployment_name)).collect(Collectors.toList());
        running_deployTask = running_deployTask.stream().filter(r -> r.get("id").toString().equals(task_id)).collect(Collectors.toList());
        return running_deployTask.get(0);
    }

    public boolean runningTask(JpaServiceInstn instance) {
        try {
            List<Map> deployTask = boshDirector.getListRunningTasks();
            List<Map> running_deployTask = deployTask.stream().filter(r -> r.get("deployment").equals(instance.getDtbNm())).collect(Collectors.toList());
            if (running_deployTask.isEmpty()) {
                return true;
            } else {
                if (instance.getTaskId() == null) {
                    instance.setTaskId(running_deployTask.get(0).get("id").toString());
                    jpaServiceInstanceRepository.save(instance);
                }
                running_deployTask = running_deployTask.stream().filter(r -> r.get("id").toString().equals(instance.getTaskId())).collect(Collectors.toList());
                if (running_deployTask.isEmpty()) {
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public String getTaskID(String deployment_name) {
        try {
            List<Map> deployTask = boshDirector.getDeploymentTasks(deployment_name);
            //boshDirector.getListRunningTasks();
            //List<Map> running_deployTask = deployTask.stream().filter(r -> r.get("deployment").equals(deployment_name)).collect(Collectors.toList());
            if (deployTask.isEmpty()) {
                return null;
            }
            return deployTask.get(0).get("id").toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getStartInstanceIPS(String taks_id, String instance_name, String instance_id) {
        try {
            return boshDirector.getStartVMIPS(taks_id, instance_name, instance_id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public String createInstance(JpaServicePlan plan, Map<String, Object> parameter, String deployment_yml) throws ServiceBrokerException {
        try {
            JpaDeployment jpaDeployments = jpaDeploymentRepository.findByName(plan.getJpaService().getSvcNm());
            JpaServicePlan jpaPlan = jpaServicePlanRepository.findById(plan.getId()).get();
            Map parameters = new HashMap();

            parameters.putAll(parameter);
            return boshDirector.createServiceInstance(plan.getJpaService().getSvcNm(),  jpaPlan.getVirtlMchnTy(), deployment_yml, jpaPlan.getCntncDisk(), parameters);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceBrokerException("Unprocessable Entity("+e.getMessage() +")", 422);
        }
    }


    public String updateServiceInstance(String deployment_name, String job_name, String vm_type, String persistent_disk) throws Exception {
        try{
            return boshDirector.updateServiceInstance(deployment_name, job_name, vm_type, persistent_disk);
        } catch (ServiceBrokerException e) {
            return e.getMessage();
        }
    }


    public String getUpdateInstanceIPS(String task_id) {
        try {
            return boshDirector.getUpdateVMIPS(task_id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public String getUpdateVMInstanceID(String task_id, String instance_name) {
        try {
            return boshDirector.getUpdateVMInstance(task_id, instance_name);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public String deleteServiceInstance(String deployment_name) {
        try {
            return boshDirector.deleteDeployment(deployment_name);
        }catch (Exception e) {
            return e.getMessage();
        }

    }

    public Map getIp(String deployment_name, String Instance_name) throws Exception {
         for(Map map : boshDirector.getListInstances(deployment_name)){
             if(map.get("job").toString().equals(Instance_name)){
                 return map;
             }
         }
         return null;
    }
}

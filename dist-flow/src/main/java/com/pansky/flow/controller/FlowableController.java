package com.pansky.flow.controller;

import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.api.FlowableTaskAlreadyClaimedException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * TestFlowable
 *
 * @Author
 * @Date: 2021/10/17 23:35
 * @Version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FlowableController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private org.flowable.engine.TaskService taskService;

    @Autowired
    private org.flowable.engine.IdentityService identityService;

    //流程定义的key , 每个流程唯一
    public static final String PROCESS_DEFINE_KEY = "StudentLeave" ;
    //流程定义的ID , 每个流程唯一
    public static final String PROCESS_DEFINE_ID = "StudentLeave:1:3d266f31-cadf-11ed-a5fe-005056c00008" ;


    /*
     * @Date: 2021/10/17 23:38
     * 第一种：Flowable启动时，会自动部署resource/processes目录下命名为 xx.bpmn20.xml的流程模型
     * 第二种： 压缩到zip形式，直接xml需要配置相对路径，麻烦，暂不用
     */
    @Test
    public void createDeployment() {
        try {
            File zipTemp = new File("F:/software/flowable/leave_approval.bpmn20.zip");
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipTemp));
            Deployment deployment = repositoryService
                    .createDeployment()
                    .addZipInputStream(zipInputStream)
                    .deploy();
            log.info("部署成功:{}", deployment.getId());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

/*    private List<FlowableListener> getTaskListeners() {
        List<FlowableListener> taskListeners =  new ArrayList();
        //监听器开始class
        FlowableListener listener = new FlowableListener();
        listener.setEvent(FlowAbleBpmnConstant.COMPLETE_EVENT_ID);
        listener.setImplementationType(FlowAbleBpmnConstant.LISTENER_TYPE);
        listener.setImplementation(FlowAbleBpmnConstant.TASK_CLASS_NAME_STRING);
        taskListeners.add(listener);
        return taskListeners;
    }*/

    /*
     * @Date: 2021/10/17 23:40
     *  查询部署的流程节点
     */
    @Test
    public void queryFlowNode() {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(PROCESS_DEFINE_ID);
        Process mainProcess = bpmnModel.getMainProcess();
        Collection<FlowElement> flowElements = mainProcess.getFlowElements();
        flowElements.stream().forEach(x->{
            System.out.println("x.getId() = " + x.getId());
            System.out.println("x.getName() = " + x.getName());
        });
    }

    /*
     * @Date: 2021/10/17 23:40
     * Step 2: 查询部署的流程定义
     */
    @Test
    public void searchDeployment() {
        List<Deployment> list1 = repositoryService.createDeploymentQuery().list();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        List<ProcessDefinition> pages = repositoryService.createProcessDefinitionQuery().listPage(1, 10);
        list1.stream().forEach(System.out::println);
        list.stream().forEach(x -> {
            System.out.println("id = " + x.getId());
            System.out.println("key = " + x.getKey());
            System.out.println("deploymentId = " + x.getDeploymentId());
            System.out.println("name = " + x.getName());
        });
//        pages.stream().forEach(System.out::println);
    }

    /*
     * @Date: 2021/10/17 23:40
     * Step 3: 启动流程，创建实例
     */
    @Test
    public void startProcess() {

        String businessKey = "apply001"; //业务代码，根据自己的业务用
        Map<String, Object> varMap = new HashMap<>(); //流程变量，可以自定义扩充
        varMap.put("startFlag","流程启动标识");
        varMap.put("startNode","流程启动节点");
        varMap.put("studentUser","zhang");   // 设置节点的用户参数  assignee="${studentUser}"
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINE_KEY, businessKey, varMap);
        log.info("启动成功:{}", processInstance.getId());
    }


    /*
     * @Date: 2021/10/17 23:40
     * Step 4: 查询指定流程所有启动的实例列表
     * 列表，或 分页 删除
     */
    @Test
    public void queryExecution() {

        List<ProcessInstance> processInstlist = runtimeService.createProcessInstanceQuery().processDefinitionId(PROCESS_DEFINE_ID).list();
        processInstlist.stream().forEach(x -> {
            System.out.println(" inst.actId()  = " + x.getActivityId());
            System.out.println(" inst.describe() = " + x.getDescription());
            System.out.println(" inst.actName() = " + x.getName());
        });

        List<Execution> executions = runtimeService.createExecutionQuery().processDefinitionKey(PROCESS_DEFINE_KEY).list();
        List<Execution> executionPages = runtimeService.createExecutionQuery().processDefinitionKey(PROCESS_DEFINE_KEY).listPage(1, 30);
//        runtimeService.deleteProcessInstance(processInstanceId, deleteReason); //删除实例
        executions.stream().forEach(execution -> {
            System.out.println("execution.actId() = " + execution.getActivityId());
            System.out.println("execution.getName() = " + execution.getName());
        });


        //流程实例查询
        List<ActivityInstance> activeInstlist = runtimeService.createActivityInstanceQuery().processDefinitionId(PROCESS_DEFINE_ID).list();
        activeInstlist.stream().forEach(x -> {
            System.out.println(" inst.actId() = " + x.getActivityId());
            System.out.println(" inst.actType() = " + x.getActivityType());
            System.out.println(" inst.actName() = " + x.getActivityName());
            System.out.println(" inst.assignee() = " + x.getAssignee());

        });

    }

    /*
     * @Date: 2021/10/17 23:40
     * Step 4: 查询指定流程实例的任务信息
     * 列表，或 分页 删除
     */
    @Test
    public void queryTask() {
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(PROCESS_DEFINE_KEY).list();
        taskList.stream().forEach(x -> {
            System.out.println(" task.name = " + x.getName());
            System.out.println(" task.Owner = " + x.getOwner());
            System.out.println(" task.Assignee = " + x.getAssignee());
            System.out.println(" task.nodeKey = " + x.getTaskDefinitionKey());

        });


        // 用户 zhang 的待办任务查询
        List<Task> userTaskList = taskService.createTaskQuery().taskAssignee("zhang").orderByTaskCreateTime().desc().list();

        // candidateUser 值的格式是多个字符串, 指定多个用户，任一用户完成后流程继续
//        List<Task> userTaskList = taskService.createTaskQuery().taskCandidateUser("zhang").orderByTaskCreateTime().desc().list();
        userTaskList.stream().forEach(x -> {
            System.out.println("x.taskId = " + x.getId());
            System.out.println("x.getAssignee = " + x.getAssignee());
            System.out.println("x.nodeKey = " + x.getTaskDefinitionKey());
        });

        // 群组 teacher 的待办任务查询
        List<Task> groupTaskList = taskService.createTaskQuery().taskCandidateGroup("teacher").orderByTaskCreateTime().desc().list();
        groupTaskList.stream().forEach(System.out::println);
    }

    /*
     * @Date: 2021/10/17 23:40
     * Step 5: 待办查询： 按人 assignee 查询
     */
    @Test
    public void stuCompleteAssignTask() throws Exception {
        //候选组 xml文件里面的 flowable: assignee="${studentUser}"，
        // 而studentUser在前面的流程变量设置为zhang了，所以此时需要用 zhang 去完成任务
        String assignee = "zhang";
        // 查询待办任务
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
        for (int i = 0; i < taskList.size(); i++) {
            String taskId = taskList.get(i).getId() ;
            // 完成任务
            taskService.complete(taskId);
            log.info("完成我的第" + (i+1) + "个任务");
        }

    }

    /*
     * @Date: 2021/10/17 23:40
     * Step 5: 待办查询：按组 candidateGroup 查询
     */
    @Test
    public void stuCompleteGroupTask() throws Exception {
        String candidateGroup = "stu_group"; //候选组 xml文件里面的 flowable:candidateGroups="stu_group"
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(candidateGroup).orderByTaskCreateTime().desc().list();
        for (Task task : taskList) {
            // 签收任务 ， 注意需捕获异常
            try {
                // 签收任务 ， 程序中一般赋值给当前用户 userId
                taskService.claim(task.getId(),"userId");
//                taskService.claim(task.getId(), "stu_Li");
            } catch (FlowableTaskAlreadyClaimedException e) {
                throw new Exception("该任务已经被签收");
            }

            // 完成
            taskService.complete(task.getId());
        }
    }

    /*
     * @Date: 2021/10/17 23:40
     * Step 6: 老师按组查询可待签收的任务
     */
    @Test
    public void teaClaimTask() throws Exception {
        String candidateGroupTe = "teacher"; //候选组 xml文件里面的 flowable:candidateGroups="te_group"
        List<Task> claimTaskList = taskService.createTaskQuery().taskCandidateGroup(candidateGroupTe).orderByTaskCreateTime().desc().list();
        log.info("待签收任务数为：{}", claimTaskList.size());
        for (int i = 0; i < claimTaskList.size(); i++) {
            String taskId = claimTaskList.get(i).getId() ;
             // 签收任务
             taskService.claim(taskId,"teacher_Chen");
             log.info("签收第" + (i+1) +"个任务");
        }
    }

    /*
     * @Date: 2021/10/17 23:40
     * Step 6: 老师完成任务   由上一步中claim的第二个参数（签收人：teacher_Chen）来完成任务
     */
    @Test
    public void teaCompleteTask() throws Exception {
        String assignee = "teacher_Chen";
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).orderByTaskCreateTime().desc().list();
         log.info("待处理任务数为：{}", taskList.size());

        for (int i = 0; i < taskList.size(); i++) {
            String taskId = taskList.get(i).getId() ;
            // 参数：审批意见等，用于网关流转
            Map<String, Object> varMap = new HashMap<>();
            varMap.put("outcome", "通过");
            varMap.put("day", 5); //携带变量，用于网关流程的条件判定
            // 签收任务
            taskService.complete(taskId,varMap);
            log.info("完成第" + (i+1) +"个任务");
        }
    }

    /*
     * @Date: 2021/10/17 23:40
     * Step 6: 老师查询可以操作的任务,并完成任务
     */
    @Test
    public void teaCompleteTask2() {
        String candidateGroupTe = "te_group"; //候选组 xml文件里面的 flowable:candidateGroups="te_group"
        List<Task> taskListTe = taskService.createTaskQuery().taskCandidateGroup(candidateGroupTe).orderByTaskCreateTime().desc().list();
        for (Task task : taskListTe) {
            // 申领任务
            taskService.claim(task.getId(), "myte");
            // 完成
            Map<String, Object> variables = new HashMap<>();
            variables.put("command", "agree"); //携带变量，用于网关流程的条件判定，这里的条件是同意
            taskService.complete(task.getId(), variables);
        }
    }

    /*
     * @Date: 2021/10/18 0:17
     * Step 7: 任务转办   设置 act_ru_task表的 Assignee 为另一个人
     */
    @Test
    public void taskTransfer(){
        Task task = taskService.createTaskQuery()
                .processDefinitionId(PROCESS_DEFINE_ID)
                .taskCandidateGroup("principal")
                .singleResult();
        System.out.println("task.getName() = " + task.getName());
        System.out.println("task.node = " + task.getTaskDefinitionKey());
        System.out.println("task.assignee = " + task.getAssignee());

        //任务签收
        taskService.claim(task.getId(),"deng");

        //任务转办   deng交给 wang
        taskService.setAssignee(task.getId(),"wang");

        Task task1 = taskService.createTaskQuery()
                .processDefinitionId(PROCESS_DEFINE_ID)
                .taskCandidateGroup("principal")
                .singleResult();
        System.out.println("task.getName() = " + task1.getName());
        System.out.println("task.node = " + task1.getTaskDefinitionKey());
        System.out.println("task.assignee = " + task1.getAssignee());
    }

    @Test
    public void taskMoveTo(){
        // 查询assign到 wang 的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionId(PROCESS_DEFINE_ID)
                .taskAssignee("wang")
                .singleResult();
        System.out.println("task.getName() = " + task.getName());
        System.out.println("task.node = " + task.getTaskDefinitionKey());
        System.out.println("task.assignee = " + task.getAssignee());



        //任务节点转移
        List<Execution> executions = runtimeService.createExecutionQuery().processDefinitionKey(PROCESS_DEFINE_KEY).list();
        executions.stream().forEach(execution -> {
            System.out.println("execution.actId() = " + execution.getActivityId());
            System.out.println("execution.getName() = " + execution.getName());
        });

/*        List<String> executionIdList = executions.stream().map(execution -> execution.getId()).collect(Collectors.toList());
        executionIdList.stream().forEach(System.out::println);*/
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        System.out.println("execution.id = " + execution.getId());
        System.out.println("execution.actId = " + execution.getActivityId());
        List<String> executionIdList = new ArrayList<>();
        executionIdList.add(execution.getId());
        runtimeService.createChangeActivityStateBuilder().moveExecutionsToSingleActivityId(executionIdList, "apply").changeState();


    }

    /*
     * @Date: 2021/10/18 0:17
     * Step 7: 历史查询，因为一旦流程执行完毕，活动的数据都会被清空，上面查询的接口都查不到数据，但是提供历史查询接口
     */
    @Test
    public void queryHistory() {
        // 历史流程实例
        List<HistoricProcessInstance> historicProcessList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(PROCESS_DEFINE_ID).list();
        // 历史任务
        List<HistoricTaskInstance> historicTaskList = historyService.createHistoricTaskInstanceQuery().processDefinitionKey(PROCESS_DEFINE_KEY).list();

        historicProcessList.stream().forEach(  x->{
            System.out.println("x.getName() = " + x.getName());
            System.out.println("x.getBusinessKey() = " + x.getBusinessKey());
        });

        historicTaskList.stream().forEach(  x->{
            System.out.println("x.getTaskDefinitionKey() = " + x.getName());
            System.out.println("x.getTaskDefinitionKey() = " + x.getTaskDefinitionKey());
        }
        );

        // 实例历史变量 , 任务历史变量
        // historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId);
        // historyService.createHistoricVariableInstanceQuery().taskId(taskId);

        // *****************************************************分隔符********************************************************************
        // *****************************************************分隔符********************************************************************
        // 可能还需要的API
        // 移动任务，人为跳转任务
        // runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId)
        //       .moveActivityIdTo(currentActivityTaskId, newActivityTaskId).changeState();



        // 如果在数据库配置了分组和用户，还会用到
        List<User> users = identityService.createUserQuery().list();    //用户查询，用户id对应xml 里面配置的用户
        List<Group> groups = identityService.createGroupQuery().list(); //分组查询，分组id对应xml 里面配置的分组 如 stu_group，te_group 在表里是id的值

        // 另外，每个查询后面都可以拼条件，内置很多查询，包括模糊查询，大小比较都有
    }



}
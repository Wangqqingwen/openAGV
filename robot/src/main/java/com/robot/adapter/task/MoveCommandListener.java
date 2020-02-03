package com.robot.adapter.task;

import com.robot.adapter.RobotCommAdapter;
import com.robot.mvc.core.telegram.MoveRequest;
import com.robot.mvc.main.DispatchFactory;
import org.opentcs.drivers.vehicle.MovementCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * 车辆移动指令监听器
 * 默认每1秒执行一次
 *
 * @author Laotang
 * @date 2020/1/22.
 */
public class MoveCommandListener implements ActionListener {

    private static final Logger LOG = LoggerFactory.getLogger(MoveCommandListener.class);
    /***/
    private Queue<MovementCommand> commandQueue;
    /**是否需要发送，true时为需要发送*/
    private boolean isNeetSend;
    /**车辆适配器*/
    private RobotCommAdapter adapter;

    /**
     * 构造方法
     *
     * @param adapter 车辆通讯适配器
     */
    public MoveCommandListener(RobotCommAdapter adapter) {
        this.adapter = adapter;
        this.isNeetSend = false;
    }

    /**
     * 定时执行方法，定时器每隔指定的时间执行一次该方法
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // 如果不需要发送则直接退出
        if (!isNeetSend) {
            LOG.debug("{}车辆没有需要发送的移动指令，退出定时发送方法！", adapter.getName());
            return;
        }
        // 如果命令队列为空，则退出
        if ((null == commandQueue || commandQueue.isEmpty())) {
            return;
        }
        // 去重
        Map<String, MovementCommand> commandMap = new LinkedHashMap<>();
        for (MovementCommand command : commandQueue) {
            String key = command.getStep().getSourcePoint().getName();
            commandMap.put(key, command);
        }
        //清空Apadter里的命令队列，再重新添加，以免重复多次添加
        commandQueue.clear();
        List<MovementCommand> commandList = new ArrayList<>(commandMap.size());
        for (Iterator<Map.Entry<String, MovementCommand>> iterator = commandMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, MovementCommand> entry = iterator.next();
            MovementCommand command = entry.getValue();
            // 去重后重新添加
            commandQueue.add(command);
            commandList.add(command);
        }
        // 进行业务处理，定时器每隔指定时间执行一次
        // 将请求发送到业务逻辑处理，自行实现所有的协议内容发送
        MoveRequest moveRequest = new MoveRequest(adapter, commandList);
        DispatchFactory.dispatch(moveRequest);
        // 发送开关，已经发送设置为false，防止重复执行
        isNeetSend = false;

    }

    /**
     * 取得移动命令队列引用
     * @param commandQueue 移动命令队列
     */
    public void quoteCommand(Queue<MovementCommand> commandQueue) {
        this.commandQueue = commandQueue;
        // 接收到新的队列引用后，将变量设置为 true，说明需要发送，等待定时器执行发送
        isNeetSend = true;
    }
}

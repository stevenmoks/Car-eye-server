
/*  car eye 车辆管理平台 
 * 企业网站:www.shenghong-technology.com
 * 车眼管理平台   www.car-eye.cn
 * 车眼开源网址:https://github.com/Car-eye-admin
 * Copyright
 */
package com.dss.commtest.mq;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.TransportListener;
import org.apache.log4j.Logger;

/**    
 *     
 * 项目名称：dsauth    
 * 类名称：JmsSender    
 * 类描述：MQ中写入消息    
 * 创建人：zr    
 * 创建时间：2015-5-11 下午04:25:13    
 * 修改人：zr    
 * 修改时间：2015-5-11 下午04:25:13    
 * 修改备注：    
 * @version 1.0  
 *     
 */
public class JmsSender implements TransportListener{

	private final static Logger logger = Logger.getLogger(JmsSender.class);

	private ConnectionFactory connectionFactory = null;  //ConnectionFactory ：连接工厂，JMS 用它创建连接 
	private Connection connection = null;  // Provider 的连接  
	private Session session = null; //Session： 一个发送或接收消息的线程  
	private Destination destination = null; //// MessageProducer：消息发送者  
	private MessageProducer producer = null; // TextMessage message;  
	protected static JmsSender jmsAuthSender = null;
	private Boolean connected = false;

	public JmsSender(){
		close();
		init();
	}

	/**
	 * 获取唯一实例.
	 * @return
	 */
	public static JmsSender getInstance(){

		if (jmsAuthSender == null) {
			jmsAuthSender = new JmsSender();
		}
		return jmsAuthSender;
	}

	/**
	 * 获取session
	 * @return
	 */
	public Session getSession(){
		return session;
	}

	/**
	 * 初始化链接
	 */
	public void init(){

		try{
			connected = true;
			// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar  
			//202.105.131.18 202.104.149.86   202.104.150.177 139.196.107.48
			connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD,"failover:(tcp://202.105.131.18:61617?tcpNoDelay=true)");

			((ActiveMQConnectionFactory) connectionFactory).setTransportListener(this);

			// 构造从工厂得到连接对象  
			connection = connectionFactory.createConnection();
			//启动
			connection.start();

			// 获取操作连接  
			session = connection.createSession(Boolean.FALSE, 
					Session.AUTO_ACKNOWLEDGE);
			//ds_udp_comm_down_192.168.1.234
			//获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置  
			destination = session.createQueue("ds_dsbusiness_down");

			// 得到消息生成者【发送者】 
			producer = session.createProducer(destination);
			// 设置不持久化，此处学习，实际根据项目决定  
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		}catch(Exception e){
			e.printStackTrace();
			jmsAuthSender = null;
		}
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		try {
			if (producer != null)
				producer.close();
			if (session != null)
				session.close();
			if (connection != null)
				connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
			jmsAuthSender = null;
		}
	}

	/**
	 * · StreamMessage -- Java原始值的数据流
		· MapMessage--一套名称-值对
		· TextMessage--一个字符串对象
		· ObjectMessage--一个序列化的 Java对象
		· BytesMessage--一个未解释字节的数据流
	 */

	/**
	 * 文本消息发送
	 * @param msg
	 */
	public void sendTextMessage(String msg){

		try {
			TextMessage message = session.createTextMessage(msg); 
			if(connected){
				producer.send(message); 
			}
		} catch (JMSException e) {
			e.printStackTrace();
			jmsAuthSender = null;
		}

	}

	/**
	 * 消息发送 MapMessage--一套名称-值对
	 */
	public void sendMapMessage(MapMessage message){
		try {
			if(connected){
				producer.send(message); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			jmsAuthSender = null;
		}
	}

	@Override
	public void onCommand(Object arg0) {
	}

	@Override
	public void onException(IOException arg0) {

		arg0.printStackTrace();
		logger.info("MQ消息链路异常"+arg0.getMessage());
		connected = false;
		jmsAuthSender = null;

	}

	@Override
	public void transportInterupted() {
		connected = false;
		jmsAuthSender = null;
	}

	@Override
	public void transportResumed() {
		connected = true;
	}

}

package com.zzk.dockLock.netty;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zzk.dockLock.pojo.Station;
import com.zzk.dockLock.service.ICabinetService;
import com.zzk.dockLock.service.IStationService;
import com.zzk.dockLock.utils.ConvertCodeUtil;
import com.zzk.dockLock.utils.CrcUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @description:
 * @program: door-lockdemo
 * @author: zzk
 * @created: 2021/12/15 22:04
 */
@Component
public class BusinessHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ICabinetService cabinetService;

    @Autowired
    private IStationService stationService;


    @Autowired
    private MessageHandler messageHandler;



    private static BusinessHandler businessHandler;

    public BusinessHandler() {

    }
    //让service可以被注入
    @PostConstruct
    public void init(){
        businessHandler=this;
        businessHandler.cabinetService=this.cabinetService;
        businessHandler.messageHandler=this.messageHandler;
        businessHandler.stationService=this.stationService;
    }

    //日志
    private static final Logger logger = LoggerFactory.getLogger(BusinessHandler.class);
    //有开门指令，让查询转态指令命令发的慢一点
    public static String sleep=new String ();



    //线程池
    private static final EventExecutorGroup GROUP=new DefaultEventExecutorGroup (100);
    //用来存储ip的hashmap
    private static ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] barray = new byte[buf.readableBytes()];
            buf.readBytes (barray);
            // 将接收到的数据转为字符串，此字符串就是客户端发送的字符串
            String receiveStr = ConvertCodeUtil.receiveHexToString(barray);
            String ip = ((InetSocketAddress)ctx.channel ().remoteAddress ()).getHostString ();
            //保存状态信息
            businessHandler.messageHandler.saveState (ConvertCodeUtil.bytes2Ints (barray),ip);
            logger.info (receiveStr);
        } catch (Exception e) {
            e.printStackTrace ();
        }

        super.channelRead (ctx, msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info ("channel。。。注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info ("channel。。。移除");
        super.channelUnregistered(ctx);
    }

    /**
     * 第一次连接（在连接开始时）
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info ("channel。。。活跃");
        //String ip=ctx.channel ().remoteAddress ().toString ().split ("/")[1];//获取ip和端口号
        String ip = ((InetSocketAddress)ctx.channel ().remoteAddress ()).getHostString ();//获取ip
        //连接上去查询数据库是否有该站
        QueryWrapper<Station> wrapper=new QueryWrapper<> ();
        wrapper.select ("station_ip");
        List<String> IPlist = businessHandler.stationService.list (wrapper).stream ().map (Station::getStationIp).collect (Collectors.toList ());
        //如果有就去修改它的状态（修改为在线状态）
        if (IPlist.contains (ip)){
           LambdaUpdateWrapper<Station> lambdaUpdateWrapper =new LambdaUpdateWrapper<> ();
           lambdaUpdateWrapper.eq (Station::getStationIp,ip).set (Station::getStationStatus,1);
           businessHandler.stationService.update (null,lambdaUpdateWrapper);
        }
        logger.info (ip+"已经连接");
        if (!hashMap.containsKey (ip)) {
            hashMap.put(ip, ctx);

            //新的线程来执行长任务
            GROUP.submit (new Callable<Object> () {
                @Override
                public Object call() throws Exception {
                    //连接上就不断去发送消息
                    sendReadMessage (ip);

                    return null;
                }
            });

        }

        super.channelActive(ctx);
    }

    /**
     * 连接断开的时候
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel。。。不活跃");
        //String ip=ctx.channel ().remoteAddress ().toString ().split ("/")[1];//获取ip+端口号

        String ip = ((InetSocketAddress)ctx.channel ().remoteAddress ()).getHostString ();//获取ip
        logger.info (ip+"已经断开");
        //连接上去查询数据库是否有该站
        QueryWrapper<Station> wrapper=new QueryWrapper<> ();
        wrapper.select ("station_ip");
        List<String> IPlist = businessHandler.stationService.list (wrapper).stream ().map (Station::getStationIp).collect (Collectors.toList ());
        //如果有就去修改它的状态（状态为未在线）
        if (IPlist.contains (ip)){
            LambdaUpdateWrapper<Station> lambdaUpdateWrapper =new LambdaUpdateWrapper<> ();
            lambdaUpdateWrapper.eq (Station::getStationIp,ip).set (Station::getStationStatus,0);
            businessHandler.stationService.update (null,lambdaUpdateWrapper);
        }
        //从hashMap中去除掉线的ip
        if (hashMap.containsKey (ip)) {
            hashMap.remove (ip);
        }
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info ("channeld读取完毕。。。");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       logger.info ("用户事件触发。。。");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info ("channel可写更改");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info ("捕获到异常");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info ("助手类添加");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info ("助手类移除");
        super.handlerRemoved(ctx);
    }

    /**
     * 发出写开门信号
     * @param stationIp 站IP
     * @param deviceId 设备ID
     * @param door 前后门
     * @param off 开关们状态
     * @return
     * @throws InterruptedException
     */
    public static boolean sendWriteMessage(String stationIp,int deviceId,int door,int off ) throws InterruptedException {

        if (hashMap.containsKey(stationIp)) {
            sleep=stationIp;
            byte deviceIdbyte = Integer.valueOf (deviceId).byteValue ();//设备ID由int->byte
            byte doorbyte = Integer.valueOf (door).byteValue ();//柜门由int->byte
            byte offbyte = Integer.valueOf (off).byteValue ();//开关由int->byte
            for (int a=0;a<2;a++) {//重复发两次
                ChannelHandlerContext ctx = (ChannelHandlerContext) hashMap.get(stationIp);
                //初始化消息
                byte[] sendMsgRes={01, 05, 00, 04, 00, 00 };
                sendMsgRes[0]=deviceIdbyte;
                sendMsgRes[3]=doorbyte;
                sendMsgRes[5]=offbyte;
                int [] crc= CrcUtil.getCrc(sendMsgRes);
                //要发送的消息
                byte [] sendMsg=new byte[8];
                for (int i = 0; i < sendMsgRes.length; i++) {
                    sendMsg[i]=sendMsgRes[i];
                }
                //添加crc循环冗余标记位
                sendMsg[6]=(byte) crc[0];
                sendMsg[7]=(byte) crc[1];
                //发送数据
                ByteBuf byteBuf = ctx.alloc().buffer();
                byteBuf.writeBytes (sendMsg);
                ctx.writeAndFlush (byteBuf);
                ctx.flush ();
                Thread.sleep (800);
            }


            return true;
        } else {
            return false;
        }
    }

    public void sendReadMessage(String ip) throws InterruptedException {
        while (hashMap.containsKey (ip)) {
            //连接上有这个ip后就去数据库查询有几个门第一次查询的时候进去
            int cabinetNum = businessHandler.cabinetService.selectCountByIp (ip);
            //如果没有写该站下没有柜门信息减慢发送速度
            if (cabinetNum==0) {
                Thread.sleep (5000);
            }
            ChannelHandlerContext obj = (ChannelHandlerContext) hashMap.get (ip);
            //根据柜门的数量去发送柜门的信息
            for (int sendDeviceIdInt=1;sendDeviceIdInt<=cabinetNum;sendDeviceIdInt++) {
                    byte sendDeviceIdByte=Integer.valueOf (sendDeviceIdInt).byteValue ();
                    byte[] sendMsgRes = {01, 02, 00, 00, 00, 03};
                    sendMsgRes[0]=sendDeviceIdByte;//根据设备id发送消息
                    int[] crc = CrcUtil.getCrc (sendMsgRes);
                    byte[] sendMsg = new byte[8];
                    for (int i = 0; i < sendMsgRes.length; i++) {
                        sendMsg[i] = sendMsgRes[i];
                    }
                    //添加crc循环冗余
                    sendMsg[6] = (byte) crc[0];
                    sendMsg[7] = (byte) crc[1];
                    if (sleep.equals (ip)) {
                        Thread.sleep (4000);//如果有开门关门操作就停一下
                        sleep = "-1";
                    }
                    ByteBuf byteBuf = obj.alloc ().buffer ();
                    byteBuf.writeBytes (sendMsg);
                    obj.writeAndFlush (byteBuf);
                    obj.flush ();
                    Thread.sleep (2000);//每次发送慢下来
                }
            }

    }
}
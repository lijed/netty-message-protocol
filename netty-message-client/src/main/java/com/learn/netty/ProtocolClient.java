/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.learn.netty;

import com.learn.netty.codec.MessageRecordDecode;
import com.learn.netty.codec.MessageRecordEncode;
import com.learn.netty.opcode.OpCode;
import com.learn.netty.protocal.Header;
import com.learn.netty.protocal.MessageRecord;
import com.learn.netty.protocal.User;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/9/12
 **/
public class ProtocolClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(
                                1024 * 1024,
                                9,
                                4,
                                0,
                                0))
                                .addLast(new MessageRecordEncode())
                                .addLast(new MessageRecordDecode())
                                .addLast(new ClientHandler());
                    }
                });

        final ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 8080)).sync();
        Channel channel = future.channel();
        try {
            for (int i = 0; i < 100; i++) {
                MessageRecord record = new MessageRecord();
                Header header = new Header();
                header.setSessionId(100001);
                header.setReqType(OpCode.REQ.code());
                record.setHeader(header);
//                String body = "我是请求数据:" + i;
                User  user = new User();
                user.setAge(12);
                user.setName("Jedli");
                record.setBody(user);
                channel.writeAndFlush(record);
            }

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}

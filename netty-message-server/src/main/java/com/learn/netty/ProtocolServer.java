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
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;


/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/9/12
 **/
@Slf4j
public class ProtocolServer {
    public static void main(String[] args)  {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup(8);

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(
                                        1024 * 1024,
                                        9,
                                        4,
                                        0,
                                        0))
                                .addLast(new MessageRecordEncode())
                                .addLast(new MessageRecordDecode())
                                .addLast(new ServerHandler());
                    }
                });
        final ChannelFuture sync;
        try {
            sync = serverBootstrap.bind(8080).sync();
            log.info("ProtocolServer start success {8080}");
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }
}

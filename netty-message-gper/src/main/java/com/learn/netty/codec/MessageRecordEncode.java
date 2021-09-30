/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.learn.netty.codec;

import com.learn.netty.protocal.Header;
import com.learn.netty.protocal.MessageRecord;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/9/12
 **/

@Slf4j
public class MessageRecordEncode extends MessageToByteEncoder<MessageRecord> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageRecord msg, ByteBuf out) throws Exception {
        log.info("=================开始进行消息的编码=================================");
        Header header = msg.getHeader();
        out.writeLong(header.getSessionId());
        out.writeByte(header.getReqType());
        Object obj = msg.getBody();
        if (obj != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                final byte[] bytes = baos.toByteArray();
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

        } else {
            out.writeInt(0); //表示消息长度为0
        }
    }
}

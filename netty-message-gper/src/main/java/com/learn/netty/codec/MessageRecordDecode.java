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
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/9/11
 **/

@Slf4j
public class MessageRecordDecode extends ByteToMessageDecoder {

    /**
     *
     *   sessionId | reqType | Content-length | Content
     *
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MessageRecord record = new MessageRecord();
        //ByteBuf 表示接收到的消息报文
        Header header = new Header();
        header.setSessionId(in.readLong());
        header.setReqType(in.readByte());
        header.setLength(in.readInt());
        record.setHeader(header);

        // 处理消息
        if (header.getLength() > 0) {
            byte[] contents = new byte[header.getLength()];
            in.readBytes(contents);

            /**
             * Java原生的对象流
             */
            ByteArrayInputStream bais = new ByteArrayInputStream(contents);
            ObjectInputStream ois = new ObjectInputStream(bais);
            record.setBody(ois.readObject());
            log.info("反序列化出来的结果："+record);
            out.add(record);
        } else {
            log.info("消息的内容为空");
        }
    }
}

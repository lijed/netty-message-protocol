/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.learn.netty.codec;

import com.learn.netty.opcode.OpCode;
import com.learn.netty.protocal.Header;
import com.learn.netty.protocal.MessageRecord;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * 仅是为了测试
 *
 * @Author: Administrator
 * Created: 2021/9/12
 **/
public class MainTest {
    public static void main(String[] args) throws Exception {

//        sessionId | reqType | Content-length | Content

        //ChannelHandler单元测试
        EmbeddedChannel channel=new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder
                        (1024*1024,
                                9,  // 8 (sessionid - long) + 1(opcode - byte)
                                4, //
                                0,
                                0),
                new LoggingHandler(),
                new MessageRecordEncode(),
                new MessageRecordDecode()
        );
        //定义消息内容
        Header header=new Header();
        header.setSessionId(1234546);
        header.setReqType(OpCode.REQ.code());
        MessageRecord record=new MessageRecord();
        record.setHeader(header);
        record.setBody("Hello World");

        //序列化的输出结果
//        channel.writeOutbound(record) ;//写出去，编码

       /* ByteBuf buf= ByteBufAllocator.DEFAULT.buffer();
        new MessageRecordEncode().encode(null,record,buf);

        channel.writeInbound(buf); //读取消息内容，进行解码*/

        //反序列化
        //编码得到了一个Bytebuf
        ByteBuf buf= ByteBufAllocator.DEFAULT.buffer();
        new MessageRecordEncode().encode(null,record,buf);

        /**
         * Returns a slice of this buffer's sub-region. Modifying the content of
         * the returned buffer or this buffer affects each other's content while
         * they maintain separate indexes and marks.
         * This method does not modify {@code readerIndex} or {@code writerIndex} of
         * this buffer.
         * <p>
         * Also be aware that this method will NOT call {@link #retain()} and so the
         * reference count will NOT be increased.
         */
//        public abstract ByteBuf slice(int index, int length);

        ByteBuf bb1=buf.slice(0,7); //数据包
        ByteBuf bb2=buf.slice(7,buf.readableBytes()-7); //数据包2

        //新增加一个引用，需要调用retain
        bb1.retain();

        //把字节码反序列化为对象
        channel.writeInbound(bb1);
        channel.writeInbound(bb2);
    }
}

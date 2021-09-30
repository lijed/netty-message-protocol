/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.learn.netty;

import io.netty.buffer.ByteBuf;
import org.apache.jute.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Description:
 *
 * Zookeeper Jute序列化
 *
 * @Author: Administrator
 * Created: 2021/9/12
 **/
public class GpHeader implements Record {

    private Long sessionId;
    private String header;

    public GpHeader() {}

    public GpHeader(Long sessionId, String header) {
        this.sessionId = sessionId;
        this.header = header;
    }

    @Override
    public void serialize(OutputArchive outputArchive, String tag) throws IOException {
        outputArchive.startRecord(this,tag);
        outputArchive.writeLong(sessionId, "sessionid");
        outputArchive.writeString(header, "header");
        outputArchive.endRecord(this, tag);
    }

    @Override
    public void deserialize(InputArchive inputArchive, String s) throws IOException {
        inputArchive.startRecord(s);
        long sessionId = inputArchive.readLong("sessionid");
        this.sessionId = sessionId;
        String  header = inputArchive.readString("header");
        this.header = header;
        inputArchive.endRecord(s);
    }

    public static void main(String[] args) throws IOException {
        String tag = "header";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryOutputArchive boa = BinaryOutputArchive.getArchive(baos);
        new GpHeader(123456L,"GperCreate").serialize(boa,tag);

        ByteBuffer byteBuffer=ByteBuffer.wrap(baos.toByteArray());
        System.out.println(byteBuffer);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        GpHeader gpHeader = new GpHeader();
        final BinaryInputArchive archive = BinaryInputArchive.getArchive(bais);
        gpHeader.deserialize(archive, tag);
        System.out.println(gpHeader.toString());
    }


    @Override
    public String toString() {
        return "GpHeader{" +
                "sessionId=" + sessionId +
                ", header='" + header + '\'' +
                '}';
    }
}

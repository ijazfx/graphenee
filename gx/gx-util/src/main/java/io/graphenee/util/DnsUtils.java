package io.graphenee.util;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.DefaultDnsQuestion;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRawRecord;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.resolver.dns.DnsNameResolver;
import io.netty.resolver.dns.DnsNameResolverBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DnsUtils {

    public static Set<String> dnsTxtRecord(String domainToLookup) {
        Set<String> txtRecords = new HashSet<>();
        List<String> domainList = new ArrayList<>();
        String[] parts = domainToLookup.split("\\.");
        String superDomain = domainToLookup;
        for (String part : parts) {
            domainList.add(superDomain);
            superDomain = superDomain.replaceFirst(part + ".", "");
            if (!superDomain.contains("."))
                break;
        }
        NioEventLoopGroup elg = new NioEventLoopGroup();
        DnsNameResolver resolver = new DnsNameResolverBuilder(elg.next())
                .datagramChannelType(NioDatagramChannel.class)
                .build();
        for (String domain : domainList) {
            DnsQuestion question = new DefaultDnsQuestion(domain, DnsRecordType.TXT);
            try {
                AddressedEnvelope<DnsResponse, InetSocketAddress> envelope = resolver.query(question).get();
                DnsResponse content = envelope.content();
                // Extract TXT records
                int recordCount = content.count(DnsSection.ANSWER);
                for (int i = 0; i < recordCount; i++) {
                    DnsRecord record = content.recordAt(DnsSection.ANSWER, i);
                    if (record instanceof DnsRawRecord) {
                        DnsRawRecord rawRecord = (DnsRawRecord) record;
                        ByteBuf data = rawRecord.content();
                        // TXT records have length-prefixed strings
                        if (data.readableBytes() > 0) {
                            int length = data.readUnsignedByte();
                            String txtValue = data.toString(data.readerIndex(), length, StandardCharsets.UTF_8);
                            txtRecords.add(txtValue);
                        }
                    }
                }
                envelope.release();
            } catch (Exception ex) {
                log.error("Failed to fetch DNS records");
            }
        }
        try {
            elg.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Failed to complete DNS query", e);
        } finally {
            elg.close();
        }
        return txtRecords;

    }

}

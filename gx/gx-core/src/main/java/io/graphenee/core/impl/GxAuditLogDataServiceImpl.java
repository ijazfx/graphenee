package io.graphenee.core.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.common.GxAuthenticatedUser;
import io.graphenee.core.GxAuditLogDataService;
import io.graphenee.core.model.entity.GxAuditLog;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxAuditLogRepository;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.util.JpaSpecificationBuilder;

@Service
@DependsOn({ "flyway", "flywayInitializer" })
@Transactional
public class GxAuditLogDataServiceImpl implements GxAuditLogDataService {

	@Autowired
	GxAuditLogRepository alRepo;

	@Autowired
	GxUserAccountRepository uaRepo;

	@Override
	public GxAuditLog log(String username, String remoteAddress, String auditEvent, String detail) {
		return log(username, remoteAddress, auditEvent, detail, null, null);
	}

	@Override
	public GxAuditLog log(String username, String remoteAddress, String auditEvent, String detail, String auditEntity, Integer oidAuditEntity) {
		GxAuditLog l = new GxAuditLog();
		l.setRemoteAddress(remoteAddress);
		l.setAuditDate(LocalDateTime.now());
		l.setUsername(username);
		l.setAuditEvent(auditEvent);
		l.setDetail(detail);
		l.setAuditEntity(auditEntity);
		l.setOidAuditEntity(oidAuditEntity);
		l = alRepo.save(l);
		return l;
	}

	@Override
	public GxAuditLog log(GxAuthenticatedUser user, String remoteAddress, String auditEvent, String detail) {
		return log(user, remoteAddress, auditEvent, detail, null, null);
	}

	@Override
	public GxAuditLog log(GxAuthenticatedUser user, String remoteAddress, String auditEvent, String detail, String auditEntity, Integer oidAuditEntity) {
		GxAuditLog l = new GxAuditLog();
		l.setRemoteAddress(remoteAddress);
		l.setAuditDate(LocalDateTime.now());
		l.setUsername(user.getUsername());
		l.setAuditEvent(auditEvent);
		l.setDetail(detail);
		l.setAuditEntity(auditEntity);
		l.setOidAuditEntity(oidAuditEntity);
		l = alRepo.save(l);
		return l;
	}

	@Override
	public GxAuditLog log(GxUserAccount user, String remoteAddress, String auditEvent, String detail) {
		return log(user, remoteAddress, auditEvent, detail, null, null);
	}

	@Override
	public GxAuditLog log(GxUserAccount user, String remoteAddress, String auditEvent, String detail, String auditEntity, Integer oidAuditEntity) {
		GxAuditLog l = new GxAuditLog();
		l.setRemoteAddress(remoteAddress);
		l.setAuditDate(LocalDateTime.now());
		l.setUsername(user.getUsername());
		l.setUserAccount(user);
		l.setAuditEvent(auditEvent);
		l.setDetail(detail);
		l.setAuditEntity(auditEntity);
		l.setOidAuditEntity(oidAuditEntity);
		l = alRepo.save(l);
		return l;
	}

	@Override
	public int count(GxAuditLog se) {
		return (int) alRepo.count(makeJpaSpec(se));
	}

	@Override
	public List<GxAuditLog> fetch(int pageNumber, int pageSize, GxAuditLog se, Sort sort) {
		PageRequest pr = PageRequest.of(pageNumber, pageSize, sort);
		return alRepo.findAll(makeJpaSpec(se), pr).getContent();
	}

	private Specification<GxAuditLog> makeJpaSpec(GxAuditLog se) {
		JpaSpecificationBuilder<GxAuditLog> sb = JpaSpecificationBuilder.get();
		sb.like("username", se.getUsername());
		sb.like("detail", se.getDetail());
		sb.like("auditEvent", se.getAuditEvent());
		sb.like("auditEntity", se.getAuditEntity());
		if (se.getOidAuditEntity() != null && se.getOidAuditEntity() != 0) {
			sb.eq("oidAuditEntity", se.getOidAuditEntity());
		}
		if (se.getAuditDate() != null) {
			LocalDateTime start = se.getAuditDate().toLocalDate().atStartOfDay();
			LocalDateTime end = start.toLocalDate().atTime(LocalTime.MAX);
			sb.between("auditDate", start, end);
		}
		return sb.build();
	}

}

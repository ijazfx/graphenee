package io.graphenee.core.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.GxAuditLogDataService;
import io.graphenee.core.model.AbstractDashboardUser;
import io.graphenee.core.model.GxAuthenticatedUser;
import io.graphenee.core.model.bean.GxUserAccountBean;
import io.graphenee.core.model.entity.GxAuditLog;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxAuditLogRepository;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.util.JpaSpecificationBuilder;
import io.graphenee.util.TRCalendarUtil;

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
		l.setAuditDate(TRCalendarUtil.getCurrentTimeStamp());
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
		l.setAuditDate(TRCalendarUtil.getCurrentTimeStamp());
		if (user != null) {
			l.setUsername(user.getUsername());
			if (user instanceof AbstractDashboardUser<?>) {
				AbstractDashboardUser<?> du = (AbstractDashboardUser<?>) user;
				if (du.getUser() instanceof GxUserAccountBean) {
					GxUserAccountBean uab = (GxUserAccountBean) du.getUser();
					GxUserAccount ua = uaRepo.findById(uab.getOid()).orElse(null);
					if (ua != null) {
						l.setUserAccount(ua);
					}
				}
			}
		}
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
			Timestamp start = TRCalendarUtil.startOfDayAsTimestamp(se.getAuditDate());
			Timestamp end = TRCalendarUtil.endOfDayAsTimestamp(start);
			sb.between("auditDate", start, end);
		}
		return sb.build();
	}

}

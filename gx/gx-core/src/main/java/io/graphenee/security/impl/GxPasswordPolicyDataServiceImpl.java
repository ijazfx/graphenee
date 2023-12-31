package io.graphenee.security.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.graphenee.core.GxDataService;
import io.graphenee.core.exception.ChangePasswordFailedException;
import io.graphenee.core.model.entity.GxNamespace;
import io.graphenee.core.model.entity.GxPasswordHistory;
import io.graphenee.core.model.entity.GxPasswordPolicy;
import io.graphenee.core.model.entity.GxUserAccount;
import io.graphenee.core.model.jpa.repository.GxNamespaceRepository;
import io.graphenee.core.model.jpa.repository.GxPasswordHistoryRepository;
import io.graphenee.core.model.jpa.repository.GxPasswordPolicyRepository;
import io.graphenee.core.model.jpa.repository.GxUserAccountRepository;
import io.graphenee.security.GrapheneeSecurityConfiguration;
import io.graphenee.security.GxPasswordPolicyDataService;
import io.graphenee.util.CryptoUtil;
import io.graphenee.util.TRCalendarUtil;

@Service
@ConditionalOnClass(GrapheneeSecurityConfiguration.class)
//@ConditionalOnProperty(prefix = "graphenee", name = "modules.enabled", havingValue = "true")
@Transactional
public class GxPasswordPolicyDataServiceImpl implements GxPasswordPolicyDataService {

	@Autowired
	GxPasswordPolicyRepository passwordPolicyRepo;

	@Autowired
	GxNamespaceRepository namespaceRepo;

	@Autowired
	GxUserAccountRepository userAccountRepo;

	@Autowired
	GxPasswordHistoryRepository passwordHistoryRepo;

	@Autowired
	GxUserAccountRepository userRepo;

	@Autowired
	GxDataService dataService;

	Pattern pattern;
	Matcher matcher;

	private Boolean findPasswordAlreadyUsed(GxNamespace namespace, String username, String password, int maxHistory) {
		if (maxHistory > 0) {
			String passwordHash = CryptoUtil.createPasswordHash(password);
			GxUserAccount userAccount = dataService.findUserAccountByUsernameAndNamespace(username, namespace);
			List<GxPasswordHistory> history = passwordHistoryRepo.findAllByUserAccountOrderByPasswordDateDesc(userAccount);
			for (int i = 0; i < history.size() && i < maxHistory; i++) {
				GxPasswordHistory passwordHistory = history.get(i);
				if (passwordHistory.getHashedPassword().equals(passwordHash))
					return true;
			}
		}
		return false;
	}

	private Boolean findMinLengthExist(String password, int min) {
		return password.length() >= min ? true : false;
	}

	private Boolean findMaxUsernameExist(String username, String password, int max) {
		pattern = Pattern.compile("[^.]{" + ++max + "}");
		matcher = pattern.matcher(password);
		int i = 0;
		while (matcher.find(i++)) {
			if (username.contains(matcher.group()))
				return false;
		}
		return true;
	}

	private Boolean findMinUpperCaseCharExist(String password, int min) {
		if (min == 0)
			return true;
		pattern = Pattern.compile("[A-Z]");
		matcher = pattern.matcher(password);
		while (matcher.find()) {
			min--;
			if (min == 0)
				return true;
		}
		return false;
	}

	private Boolean findMinLowerCaseCharExist(String password, int min) {
		if (min == 0)
			return true;
		pattern = Pattern.compile("[a-z]");
		matcher = pattern.matcher(password);
		while (matcher.find()) {
			min--;
			if (min == 0)
				return true;
		}
		return false;
	}

	private Boolean findMinNumbersExist(String password, int min) {
		if (min == 0)
			return true;
		pattern = Pattern.compile("[\\d]");
		matcher = pattern.matcher(password);
		while (matcher.find()) {
			min--;
			if (min == 0)
				return true;
		}
		return false;
	}

	private Boolean findMinSpecialCharExist(String password, int min) {
		if (min == 0)
			return true;
		pattern = Pattern.compile("[!$#&^|~?%]");
		matcher = pattern.matcher(password);
		while (matcher.find()) {
			min--;
			if (min == 0)
				return true;
		}
		return false;
	}

	@Override
	public Boolean findPasswordIsValid(GxNamespace namespace, String username, String password) {
		try {
			assertPasswordPolicy(namespace, username, password);
			return true;
		} catch (AssertionError e) {
			return false;
		}
	}

	@Override
	public void assertPasswordPolicy(GxNamespace namespace, String username, String password) throws AssertionError {
		GxPasswordPolicy passpol = findPasswordPolicyByNamespace(namespace);
		if (passpol == null || !passpol.getIsActive()) {
			passpol = findPasswordPolicyByNamespace(dataService.systemNamespace());
		}
		assertPasswordPolicy(passpol, username, password);
	}

	@Override
	public void assertPasswordPolicy(GxPasswordPolicy entity, String username, String password) throws AssertionError {
		if (entity == null)
			return;
		if (!entity.getIsActive())
			return;
		if (entity.getIsUserUsernameAllowed() && !findMaxUsernameExist(username, password, entity.getMaxAllowedMatchingUserName()))
			throw new AssertionError("Password must not contain " + entity.getMaxAllowedMatchingUserName() + " or more consecutive characters from username.");
		if (!findMinUpperCaseCharExist(password, entity.getMinUppercase()))
			throw new AssertionError("Password must contain at least " + entity.getMinUppercase() + " upper case letter(s).");
		if (!findMinLowerCaseCharExist(password, entity.getMinLowercase()))
			throw new AssertionError("Password must contain at least " + entity.getMinLowercase() + " lower case letter(s).");
		if (!findMinNumbersExist(password, entity.getMinNumbers()))
			throw new AssertionError("Password must contain at least " + entity.getMinNumbers() + " digit(s).");
		if (!findMinSpecialCharExist(password, entity.getMinSpecialCharacters()))
			throw new AssertionError("Password must contain at least " + entity.getMinUppercase() + " special character(s).");
		if (findPasswordAlreadyUsed(entity.getNamespace(), username, password, entity.getMaxHistory()))
			throw new AssertionError("Password has already been used, set a different password.");
		if (!findMinLengthExist(password, entity.getMinLength()))
			throw new AssertionError("Password must be minimum of " + entity.getMinLength() + " characters.");

	}

	@Override
	public List<GxPasswordPolicy> findAllPasswordPolicyByNamespace(GxNamespace namespace) {
		return passwordPolicyRepo.findAllByNamespace(namespace);
	}

	@Override
	public GxPasswordPolicy findPasswordPolicyByNamespace(GxNamespace namespace) {
		return passwordPolicyRepo.findOneByNamespace(namespace);
	}

	@Override
	public GxPasswordPolicy save(GxPasswordPolicy entity) {
		return passwordPolicyRepo.save(entity);
	}

	@Override
	public void delete(GxPasswordPolicy entity) {
		if (entity.getOid() != null && entity.getIsActive())
			throw new RuntimeException("Password Policy is Active!");
		passwordPolicyRepo.deleteById(entity.getOid());
	}

	@Override
	public void changePassword(GxNamespace namespace, String username, String oldPassword, String newPassword) throws ChangePasswordFailedException {
		GxUserAccount userAccount = dataService.findUserAccountByUsernamePasswordAndNamespace(username, oldPassword, namespace);
		if (userAccount == null)
			throw new ChangePasswordFailedException("Current password did not match.");
		changePassword(namespace, username, newPassword);
	}

	@Override
	public void changePassword(GxNamespace namespace, String username, String newPassword) throws ChangePasswordFailedException {
		GxUserAccount userAccount = dataService.findUserAccountByUsernameAndNamespace(username, namespace);
		if (userAccount == null)
			throw new ChangePasswordFailedException("Current password did not match.");

		// use system policy if no application level policy defined.
		GxPasswordPolicy passwordPolicy = findPasswordPolicyByNamespace(namespace);
		if (passwordPolicy == null) {
			passwordPolicy = findPasswordPolicyByNamespace(dataService.systemNamespace());
		}

		if (passwordPolicy != null && passwordPolicy.getIsActive()) {
			try {
				assertPasswordPolicy(namespace, username, newPassword);
			} catch (AssertionError e) {
				throw new ChangePasswordFailedException(e.getMessage());
			}
			Integer maxHistory = passwordPolicy.getMaxHistory();
			String encryptedPassword = CryptoUtil.createPasswordHash(newPassword);
			userAccount = userAccountRepo.findByUsername(username);
			if (maxHistory > 0) {
				// is password match with current password
				if (userAccount.getPassword().equals(encryptedPassword)) {
					throw new ChangePasswordFailedException("Password has already been used before.");
				}
				if (maxHistory > 1) {
					// is password match with histories passwords
					List<GxPasswordHistory> histories = passwordHistoryRepo.findAllByUserAccountOrderByPasswordDateDesc(userAccount);
					for (GxPasswordHistory history : histories) {
						if (history.getHashedPassword().equals(encryptedPassword))
							throw new ChangePasswordFailedException("Password has already been used before.");
					}
					// password histories update
					if (histories.size() > 0 && histories.size() == maxHistory - 1)
						passwordHistoryRepo.delete(histories.get(histories.size() - 1));
					GxPasswordHistory passwordHistory = new GxPasswordHistory();
					passwordHistory.setUserAccount(userAccount);
					passwordHistory.setHashedPassword(userAccount.getPassword());
					passwordHistory.setPasswordDate(new Timestamp(System.currentTimeMillis()));
					passwordHistoryRepo.save(passwordHistory);
				}
			}
			userAccount.setIsPasswordChangeRequired(false);
			userAccount.setPassword(encryptedPassword);
			userAccountRepo.save(userAccount);
		} else {
			userAccount = userAccountRepo.findByUsernameAndNamespace(username, namespace);
			String encryptedPassword = CryptoUtil.createPasswordHash(newPassword);
			userAccount.setIsPasswordChangeRequired(false);
			userAccount.setPassword(encryptedPassword);
			userAccountRepo.save(userAccount);
		}

	}

	@Override
	public Boolean isPasswordExpired(GxNamespace namespace, GxUserAccount userAccount) {
		// use system policy if no application level policy defined.
		GxPasswordPolicy passwordPolicy = findPasswordPolicyByNamespace(namespace);
		if (passwordPolicy == null) {
			passwordPolicy = findPasswordPolicyByNamespace(dataService.systemNamespace());
		}
		if (passwordPolicy == null)
			return false;

		List<GxPasswordHistory> passwordHistoryList = passwordHistoryRepo.findAllByUserAccountOrderByPasswordDateDesc(userAccount);
		Timestamp currentTime = TRCalendarUtil.getCurrentTimeStamp();
		Long diff = 0L;
		GxPasswordHistory passwordHistory = passwordHistoryList != null && !passwordHistoryList.isEmpty() ? passwordHistoryList.get(0) : null;
		if (passwordHistory != null && passwordHistory.getPasswordDate() != null) {
			diff = TRCalendarUtil.daysBetween(passwordHistory.getPasswordDate(), currentTime);
		} else if (userAccount.getAccountActivationDate() != null) {
			diff = TRCalendarUtil.daysBetween(userAccount.getAccountActivationDate(), currentTime);
		}
		return diff != 0 && diff > passwordPolicy.getMaxAge();

	}

}

package com.clsaa.dop.server.test.manager;

import com.clsaa.dop.server.test.manager.feign.UserInterface;
import com.clsaa.dop.server.test.model.dto.User;
import com.clsaa.dop.server.test.model.po.Po;
import com.clsaa.dop.server.test.util.Services;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * 用户相关操作
 * @author xihao
 * @version 1.0
 * @since 29/03/2019
 */
@Slf4j
public class UserManager {

    public static final String INVALID_USER_PATTERN = "Invalid UserId %d";

    private static final ThreadLocal<Long> userThread = ThreadLocal.withInitial(() -> -1L);

    private static UserInterface userInterface;

    private static LoadingCache<Long,String> userNameCache = CacheBuilder.newBuilder().maximumSize(10000L).build(new CacheLoader<Long, String>() {
        @Override
        public String load(Long key) throws Exception {
            User user = userInterface.getUserById(key);
            if (user == null) {
                log.error("[Get User Info From Feign]: error! invalid user id: {}", key);
                return String.format(INVALID_USER_PATTERN, key);
//                BizAssert.justInvalidParam("[Get User Info From Feign]: error! invalid user id: {}" + key);
            }
            return user.getName();
        }
    });

    static {
        userInterface = Services.of(UserInterface.class);
    }

    public static void setCurrentUserId(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            try {
                Long userIdL = Long.valueOf(userId);
                userThread.set(userIdL);
            } catch (NumberFormatException n) {
                log.error("Invalid user id: {}", userId);
            }
        }
    }

    public static Long getCurrentUserId() {
        return userThread.get();
    }

    public static void setCurrentUserId(Long userId) {
        userThread.set(userId);
    }

    public static void removeThreadUserId() {
        userThread.remove();
    }

    public static User getCurrentUser() {
        return userInterface.getUserById(getCurrentUserId());
    }

    public static <PO extends Po> Function<PO, PO> dateAndUser() {
        return po -> {
            LocalDateTime current = LocalDateTime.now();
            po.setCtime(current);
            po.setMtime(current);
            Long currentUserId = getCurrentUserId();
            po.setCuser(currentUserId);
            po.setMuser(currentUserId);
            return po;
        };
    }

    public static <PO extends Po> Function<PO, PO> updateUserAndTime() {
        return po -> {
            LocalDateTime current = LocalDateTime.now();
            po.setMtime(current);
            Long currentUserId = getCurrentUserId();
            po.setMuser(currentUserId);
            return po;
        };
    }

    public static <PO extends Po> Function<PO, PO> newInfoIfNotExists() {
        return po -> {
            if (po.getId() == null) {
                // new
                dateAndUser().apply(po);
            }else {
                // update
                updateUserAndTime().apply(po);
            }
            return po;
        };
    }

    public static String getUserName(Long userId) {
        try {
            return userNameCache.get(userId);
        } catch (ExecutionException e) {
            log.error("Get user name error! userId: {}", userId, e);
            return String.format(INVALID_USER_PATTERN, userId);
        }
    }
}

package com.viettelpost.api.base;

import com.viettelpost.api.business.models.UserToken;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class BaseController {

    private final String tokenKey = "Token";

    protected void writeError(Exception e) {
        if (e instanceof Exception) {
            getLogger().error(e.getMessage(), e);
        } else {
            getLogger().info(e);
        }
    }

    protected UserToken validateToken(HttpServletRequest req) {
        String token = null;
        try {
            token = req.getHeader(tokenKey);
            UserToken userToken = BaseSecurity.validateToken(token);
            return userToken;
        } catch (Exception e) {
            StringBuilder builder = new StringBuilder("token invalidate: ").append(token);
            getLogger().info(builder);
        }
        return null;
    }

    protected UserToken validateToken(String token) {
        try {
            UserToken userToken = BaseSecurity.validateToken(token);
            return userToken;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            StringBuilder builder = new StringBuilder("token invalidate: ").append(token);
            getLogger().info(builder);
        }
        return null;
    }

    protected boolean isNullToken(HttpServletRequest req) {
        try {
            String token = req.getHeader(tokenKey);
            return Utils.isNullOrEmpty(token);
        } catch (Exception e) {
            writeError(e);
        }
        return true;
    }

    protected boolean isNullToken(String token) {
        try {
            return Utils.isNullOrEmpty(token);
        } catch (Exception e) {
            writeError(e);
        }
        return true;
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null || value.length() == 0)
            return true;

        return false;
    }

    public static boolean isValidData(List ll) {
        if (ll != null) {
            for (Object l : ll) {
                if (Utils.isNullOrEmpty(l)) {
                    return false;
                }
            }
        }
        return true;
    }

    public abstract Logger getLogger();
}

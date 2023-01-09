package com.gtop.uc.common.constant;

/**
 * 全局参数默认值
 * @author hongzw
 */
public interface GlobalPropertiesValue {

    class FirstLoginChangePassword {

        public static final boolean DEFAULT = false;

        public static final boolean TRUE = true;
    }

    class NewPasswordValidateRegex {
        public static final String DEFAULT = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*])[\\da-zA-Z~!@#$%^&*]{8,}$";
    }

    class NeedCaptcha {

        public static final boolean DEFAULT = true;

        public static final boolean FALSE = false;

    }

    class LoginFailLock {

        public static final boolean DEFAULT = false;

    }

    class LoginFailMaxTimes {

        public static final int DEFAULT = 3;

    }

    class LoginFailLockMinutes {

        public static final int DEFAULT = 10;

    }

    class AuthenticationApiUrl {

        public static final String DEFAULT = "http://localhost:9522/oauth2";

    }

    class AuthenticationLoginUrl {

        public static final String DEFAULT = "http://localhost/dev-api/login";

    }

    class PlatformSiteKey {

        public static final String DEFAULT = "SK_platform";

    }

    class PlatformSiteDashboardUrl {

        public static final String DEFAULT = "http://localhost:9527/admin/#/dashboard";

    }

    class Oauth2AccessTokenTimeout {

        /**
         * 12小时
         */
        public static final int DEFAULT = 43200;

    }

    class Oauth2RefreshTokenTimeout {

        /**
         * 30天
         */
        public static final int DEFAULT = 2592000;
    }

    class BizMqType {

        public static final String DEFAULT = "jms";

    }

    class CacheServiceDefaultTimeout {

        public static final long DEFAULT = 1800000;

    }

    class CaptchaTimeout {

        public static final long DEFAULT = 300000;

    }

}

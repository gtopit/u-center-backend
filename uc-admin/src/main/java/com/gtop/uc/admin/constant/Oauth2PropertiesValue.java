package com.gtop.uc.admin.constant;

/**
 * @author hongzw
 */
public interface Oauth2PropertiesValue {

    class UcOauth2ServiceUrl {

        public static final String DEFAULT = "http://localhost:9522/oauth2";

    }

    class UcOauth2ServiceAccessTokenUrl {

        public static final String DEFAULT = UcOauth2ServiceUrl.DEFAULT + "/oauth/token";

    }

    class UcOauth2ServiceUserAuthorizationUrl {

        public static final String DEFAULT = UcOauth2ServiceUrl.DEFAULT + "/oauth/authorize";

    }

    class UcOauth2ServiceUserInfoUrl {

        public static final String DEFAULT = UcOauth2ServiceUrl.DEFAULT + "/user/getInfo";

    }

    class UcOauth2ServiceUserLogoutUrl {

        public static final String DEFAULT = UcOauth2ServiceUrl.DEFAULT + "/logout";

    }

    class UcOauth2ServiceCheckTokenUrl {

        public static final String DEFAULT = UcOauth2ServiceUrl.DEFAULT + "/oauth/check_token";

    }

}

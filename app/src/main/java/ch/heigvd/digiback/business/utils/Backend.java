package ch.heigvd.digiback.business.utils;

public class Backend {
    // Domain names for the backend application
    private static String debugDomainName = "https://api.fleurimont.site/";
    private static String domainName = "https://api.infomaldedos.ch/";

    public static String domain = debugDomainName;

    // URL to fetch initial site info
    public static String postsUrl = "https://infomaldedos.ch/wp-json/wp/v2/posts";
    public static String mediaUrl = "https://infomaldedos.ch/wp-json/wp/v2/media/";
    public static String categoriesURL = "https://infomaldedos.ch/wp-json/wp/v2/categories/";

    public static String getAuthURL() {
        return domain + "auth/";
    }

    public static String getMovementURL() {
        return domain + "movement/user/";
    }

    public static String getActivityURL() {
        return domain + "activity/user/";
    }

    public static String getExerciseURL() {
        return domain + "exercise/";
    }

    public static String getTipURL() {
        return domain + "tip/user/";
    }

    public static String getStatURL() {
        return domain + "stat/user/";
    }
}

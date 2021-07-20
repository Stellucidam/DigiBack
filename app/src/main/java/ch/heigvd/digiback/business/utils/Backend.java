package ch.heigvd.digiback.business.utils;

public class Backend {
    // Domain names for the backend application
    public static String debugDomainName = "https//api.fleurimont.site/";
    public static String domainName = "https//api.infomaldedos.ch/";

    // URL to fetch initial site info
    public static String postsUrl = "https://infomaldedos.ch/wp-json/wp/v2/posts";
    public static String mediaUrl = "https://infomaldedos.ch/wp-json/wp/v2/media/";
    public static String categoriesURL = "https://infomaldedos.ch/wp-json/wp/v2/categories/";

    public static String getMovementURL(String domain) {
        return domain + "movement/user/";
    }

    public static String getActivityURL(String domain) {
        return domain + "activity/user/";
    }

    public static String getAuthURL(String domain) {
        return domain + "auth/";
    }

    public static String getExerciseURL(String domain) {
        return domain + "exercise/user/";
    }
}

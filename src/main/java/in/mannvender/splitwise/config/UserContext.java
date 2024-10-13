package in.mannvender.splitwise.config;

import in.mannvender.splitwise.models.User;

public class UserContext {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
    public static void setUser(User user){
        userThreadLocal.set(user);
    }
    public static User getUser(){
        return userThreadLocal.get();
    }
    public static void clear(){
        userThreadLocal.remove();
    }
}

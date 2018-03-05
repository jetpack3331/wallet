package hut34.wallet.testinfra.matcher;

import com.googlecode.objectify.Ref;
import hut34.wallet.framework.usermanagement.model.User;
import org.assertj.core.api.Condition;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Matchers {

    public static class MatcherException extends RuntimeException {
        public MatcherException(String format, Object... formatArgs) {
            super(String.format(format, formatArgs));
        }
    }

    public static TypeSafeMatcher<Class<?>> hasMethodAnnotation(final String methodName, final Class<? extends Annotation> annotation) {
        return new TypeSafeMatcher<Class<?>>() {
            @Override
            protected boolean matchesSafely(Class<?> clazz) {
                Method method = findMethod(clazz);
                if (method == null) {
                    throw new MatcherException("No such method %s for class %s", methodName, clazz.getSimpleName());
                }
                return method.getAnnotation(annotation) != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("missing required annotation: ").appendValue(annotation);
            }

            private Method findMethod(Class<?> clazz) {
                for (Method method : clazz.getMethods()) {
                    if (method.getName().equals(methodName)) {
                        return method;
                    }
                }
                return null;
            }
        };
    }

    public static TypeSafeMatcher<Class<?>> hasHiddenConstructor() {
        return new TypeSafeMatcher<Class<?>>() {
            @Override
            protected boolean matchesSafely(Class<?> targetClass) {
                Constructor<?>[] cons = targetClass.getDeclaredConstructors();

                if (cons.length > 1 || cons[0].getParameterTypes().length > 1) {
                    return false;
                }

                boolean isConstructorPrivate = Modifier.isPrivate(cons[0].getModifiers());
                // Now call the constructor to make coverage happy
                callDefaultConstructorForTestCodeCoverage(cons[0]);
                return isConstructorPrivate;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("class with a single no-arg private constructor");
            }

            private void callDefaultConstructorForTestCodeCoverage(Constructor<?> con) {
                con.setAccessible(true);
                try {
                    con.newInstance((Object[]) null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        };
    }

    public static <T> Matcher<T> hasFieldWithUserRef(final String fieldName, final User user) {
        return new BaseMatcher<T>() {
            @Override
            public boolean matches(Object o) {
                Ref<User> userRef = getField(o, fieldName);
                if (user == null) {
                    return userRef == null;
                } else {
                    String userId = userRef.getKey().getName();

                    return user.getId().equals(userId);
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("User with username '%s' on field %s", user, fieldName));
            }
        };
    }

    /**
     * Return a hamcrest matcher as a JAssert condition.
     *
     * @param matcher The matcher.
     * @param <T>     Asserted object type.
     * @return Condition.
     */
    public static <T> Condition<T> condition(final Matcher<T> matcher) {
        return new Condition<T>(matcher.toString()) {
            @Override
            public boolean matches(T t) {
                return matcher.matches(t);
            }
        };
    }

    private static String getUsernameFromRef(Object entity, String field) {
        Ref<User> userRef = getField(entity, field);
        return userRef == null ? null : userRef.getKey().getName();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getField(Object entity, String field) {
        return (T) ReflectionTestUtils.getField(entity, field);
    }

}

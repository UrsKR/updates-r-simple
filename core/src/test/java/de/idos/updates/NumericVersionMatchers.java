package de.idos.updates;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class NumericVersionMatchers {
    public static Matcher<Version> isGreaterThan(final Version version) {
        return new TypeSafeMatcher<Version>() {
            @Override
            public boolean matchesSafely(Version item) {
                return item.isGreaterThan(version);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a version greater than ").appendValue(version);
            }
        };
    }

    public static Matcher<NumericVersion> isSmallerThan(final NumericVersion version) {
        return new TypeSafeMatcher<NumericVersion>() {
            @Override
            public boolean matchesSafely(NumericVersion item) {
                return item.compareTo(version) < 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a version less than ").appendValue(version);
            }
        };
    }

    public static Matcher<Version> sameVersionAs(final Version version) {
        return new TypeSafeMatcher<Version>() {
            @Override
            public boolean matchesSafely(Version item) {
                return item.isEqualTo(version);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(version);
            }
        };
    }
}

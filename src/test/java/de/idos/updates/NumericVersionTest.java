package de.idos.updates;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NumericVersionTest {

    NumericVersion currentVersion = new NumericVersion(4, 2, 1);

    @Test
    public void equalsVersionWithSameNumbers() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 2, 1);
        assertThat(currentVersion, is(sameVersionAs(latestVersion)));
    }

    @Test
    public void isSmallerThanVersionWithGreaterMicro() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 2, 2);
        assertThat(currentVersion, is(isSmallerThan(latestVersion)));
    }

    @Test
    public void isSmallerThanVersionWithGreaterMinor() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 3, 1);
        assertThat(currentVersion, is(isSmallerThan(latestVersion)));
    }

    @Test
    public void isSmallerThanVersionWithGreaterMajor() throws Exception {
        NumericVersion latestVersion = new NumericVersion(5, 2, 1);
        assertThat(currentVersion, is(isSmallerThan(latestVersion)));
    }

    @Test
    public void isGreaterThanVersionWithSmallerMajor() throws Exception {
        NumericVersion latestVersion = new NumericVersion(5, 2, 1);
        assertThat(latestVersion, is(isGreaterThan(currentVersion)));
    }

    @Test
    public void isGreaterThanVersionWithSmallerMinor() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 3, 1);
        assertThat(latestVersion, is(isGreaterThan(currentVersion)));
    }

    @Test
    public void isGreaterThanVersionWithSmallerMicro() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 2, 2);
        assertThat(latestVersion, is(isGreaterThan(currentVersion)));
    }

    private Matcher<NumericVersion> isGreaterThan(final NumericVersion version) {
        return new TypeSafeMatcher<NumericVersion>() {
            @Override
            public boolean matchesSafely(NumericVersion item) {
                return item.compareTo(version) > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a version greater than ").appendValue(version);
            }
        };
    }

    private Matcher<NumericVersion> isSmallerThan(final NumericVersion version) {
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

    private Matcher<NumericVersion> sameVersionAs(final NumericVersion version) {
        return new TypeSafeMatcher<NumericVersion>() {
            @Override
            public boolean matchesSafely(NumericVersion item) {
                return item.compareTo(version) == 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(version);
            }
        };
    }
}

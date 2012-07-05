package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NumericVersionTest {

    NumericVersion currentVersion = new NumericVersion(4, 2, 1);

    @Test
    public void equalsVersionWithSameNumbers() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 2, 1);
        assertThat(currentVersion, is(NumericVersionMatchers.sameVersionAs(latestVersion)));
    }

    @Test
    public void isSmallerThanVersionWithGreaterMicro() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 2, 2);
        assertThat(currentVersion, is(NumericVersionMatchers.isSmallerThan(latestVersion)));
    }

    @Test
    public void isSmallerThanVersionWithGreaterMinor() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 3, 1);
        assertThat(currentVersion, is(NumericVersionMatchers.isSmallerThan(latestVersion)));
    }

    @Test
    public void isSmallerThanVersionWithGreaterMajor() throws Exception {
        NumericVersion latestVersion = new NumericVersion(5, 2, 1);
        assertThat(currentVersion, is(NumericVersionMatchers.isSmallerThan(latestVersion)));
    }

    @Test
    public void isGreaterThanVersionWithSmallerMajor() throws Exception {
        NumericVersion latestVersion = new NumericVersion(5, 2, 1);
        assertThat(latestVersion, is(NumericVersionMatchers.isGreaterThan(currentVersion)));
    }

    @Test
    public void isGreaterThanVersionWithSmallerMinor() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 3, 1);
        assertThat(latestVersion, is(NumericVersionMatchers.isGreaterThan(currentVersion)));
    }

    @Test
    public void isGreaterThanVersionWithSmallerMicro() throws Exception {
        NumericVersion latestVersion = new NumericVersion(4, 2, 2);
        assertThat(latestVersion, is(NumericVersionMatchers.isGreaterThan(currentVersion)));
    }

    @Test
    public void hasStringRepresentationWithDots() throws Exception {
        assertThat(currentVersion.asString(), is("4.2.1"));
    }
}
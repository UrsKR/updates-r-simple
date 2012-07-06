package de.idos.updates;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NullVersionTest {

    @Test
    public void anyVersionIsGreater() throws Exception {
        boolean minimalVersionIsSmaller = new NumericVersion(0, 0, 0).isGreaterThan(new NullVersion());
        assertThat(minimalVersionIsSmaller, is(true));
    }

    @Test
    public void isNotGreaterThanAnyVersion() throws Exception {
        boolean minimalVersionIsSmaller = !new NullVersion().isGreaterThan(new NumericVersion(0, 0, 0));
        assertThat(minimalVersionIsSmaller, is(true));
    }

    @Test
    public void isEqualToMinimalVersion() throws Exception {
        boolean minimalVersionIsEqual = new NullVersion().isEqualTo(new NullVersion());
        assertThat(minimalVersionIsEqual, is(true));
    }

    @Test
    public void isNotEqualToAnyOtherVersion() throws Exception {
        boolean minimalVersionIsEqual = new NullVersion().isEqualTo(new NumericVersion(0, 0, 0));
        assertThat(minimalVersionIsEqual, is(false));
    }

    @Test
    public void anyOtherVersionIsNotEqual() throws Exception {
        boolean minimalVersionIsEqual = new NumericVersion(0, 0, 0).isEqualTo(new NullVersion());
        assertThat(minimalVersionIsEqual, is(false));
    }

    @Test
    public void clearlyIdentifiesItself() throws Exception {
        assertThat(new NullVersion().asString(), is("No version or unknown version"));
    }
}
package de.idos.updates;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class HttpRepository implements Repository {
    private URL baseUrl;

    public HttpRepository(String url) {
        try {
            this.baseUrl = new URL(url);
        } catch (IOException e) {
            throw new IllegalArgumentException("Please specify a valid URL as your repository base.", e);
        }
    }

    @Override
    public Version getLatestVersion() {
        try {
            return readVersionsFromRepository();
        } catch (IOException e) {
            return VersionFinder.BASE_VERSION;
        }
    }

    @Override
    public void transferVersionTo(Version version, VersionStore store) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private Version readVersionsFromRepository() throws IOException {
        URL contentUrl = new URL(baseUrl, "updates/availableVersions");
        InputStream input = contentUrl.openStream();
        List<String> strings = IOUtils.readLines(input);
        List<Version> versions = new VersionFactory().createVersionsFromStrings(strings);
        return new VersionFinder().findLatestVersion(versions);
    }
}

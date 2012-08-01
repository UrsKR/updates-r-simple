package de.idos.updates.lookup;

import de.idos.updates.Version;
import de.idos.updates.VersionFactory;
import de.idos.updates.VersionFinder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class HttpLookup implements LookupStrategy {

    private URL baseUrl;

    public HttpLookup(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Version findLatestVersion() throws IOException {
        URL versionList = new URL(baseUrl, "updates/availableVersions");
        InputStream input = versionList.openStream();
        List<String> strings = IOUtils.readLines(input);
        List<Version> versions = new VersionFactory().createVersionsFromStrings(strings);
        return new VersionFinder().findLatestVersion(versions);
    }
}
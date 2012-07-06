package de.idos.updates;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HttpRepository implements Repository {
    private URL baseUrl;

    public HttpRepository(String url) throws MalformedURLException {
        this.baseUrl = new URL(url);
    }

    @Override
    public Version getLatestVersion() {
        try {
            URL contentUrl = new URL(baseUrl, "updates/availableVersions");
            InputStream input = contentUrl.openStream();
            List<String> strings = IOUtils.readLines(input);
            List<Version> versions = new VersionFactory().createVersionsFromStrings(strings);
            return new VersionFinder().findLatestVersion(versions);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException();
        }
    }

    @Override
    public void transferVersionTo(Version version, VersionStore store) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

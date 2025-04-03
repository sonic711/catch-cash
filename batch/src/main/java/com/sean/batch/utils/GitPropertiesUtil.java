package com.sean.batch.utils;

import java.io.InputStream;
import java.util.Properties;

import lombok.SneakyThrows;

public class GitPropertiesUtil {

    private static final String GIT_PROPERTIES_FILE = "/git.properties";
    private final Properties properties;

    public GitPropertiesUtil() {
        properties = loadProperties();
    }

    @SneakyThrows
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream(GIT_PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("Unable to find " + GIT_PROPERTIES_FILE);
                return props;
            }
            props.load(input);
        }
        return props;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getCommitId() {
        return getProperty("git.commit.id");
    }

    public String getCommitAbbrevId() {
        return getProperty("git.commit.id.abbrev");
    }

    public String getCommitTime() {
        return getProperty("git.commit.time");
    }

    public String getBranch() {
        return getProperty("git.branch");
    }

    public String getTags() {
        return getProperty("git.tags");
    }

    public String getBuildVersion() {
        return getProperty("git.build.version");
    }

    @Override
    public String toString() {
        return "INFO:[Branch: " + getBranch()
                + ",CommitId: " + getCommitAbbrevId()
                + ",CommitTime: " + getCommitTime()
                + ",BuildVersion: " + getBuildVersion()
                + ",Tag: " + getTags() + "]";
    }

}

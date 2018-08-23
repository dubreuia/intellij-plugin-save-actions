package com.dubreuia.model.epf;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.dubreuia.model.epf.EpfTestConstants.EXAMPLE_EPF_0;
import static com.dubreuia.model.epf.EpfTestConstants.EXAMPLE_EPF_1;
import static com.dubreuia.model.epf.EpfTestConstants.EXAMPLE_EPF_2;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class EpfKeyTest {

    @Test
    public void should_all_example_file_0_keys_be_present_in_epf_key() throws IOException {
        Properties properties = readProperties(EXAMPLE_EPF_0.toString());
        assertPropertyPresenceInEpf(properties);
    }

    @Test
    public void should_all_example_file_1_keys_be_present_in_epf_key() throws IOException {
        Properties properties = readProperties(EXAMPLE_EPF_1.toString());
        assertPropertyPresenceInEpf(properties);
    }

    @Test
    public void should_all_example_file_2_keys_be_present_in_epf_key() throws IOException {
        Properties properties = readProperties(EXAMPLE_EPF_2.toString());
        assertPropertyPresenceInEpf(properties);
    }

    @Test
    public void should_all_epf_key_be_present_in_example_files_2_to_remove_unused_keys() throws IOException {
        Properties properties = readProperties(EXAMPLE_EPF_2.toString());
        List<String> epfKeyNames = getEpfKeyNames();
        List<String> propertiesKeyNames = getPropertiesKeyNames(properties);
        epfKeyNames.forEach(epfKeyName -> assertThat(propertiesKeyNames).contains(epfKeyName));
    }

    private void assertPropertyPresenceInEpf(Properties properties) {
        List<String> epfKeyNames = getEpfKeyNames();
        List<String> propertiesKeyNames = getPropertiesKeyNames(properties);
        propertiesKeyNames.forEach(propertiesKeyName -> assertThat(epfKeyNames).contains(propertiesKeyName));
    }

    private List<String> getPropertiesKeyNames(Properties properties) {
        return properties.entrySet().stream()
                .map(Map.Entry::getKey)
                .map(Object::toString)
                .filter(key -> EpfKey.getPrefixes().stream().anyMatch(key::startsWith))
                .map(key -> key.substring(key.lastIndexOf(".") == -1 ? 0 : key.lastIndexOf(".") + 1))
                .collect(toList());
    }

    private List<String> getEpfKeyNames() {
        return EpfKey.stream()
                .map(EpfKey::name)
                .collect(toList());
    }

    private Properties readProperties(String configurationPath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(configurationPath)) {
            properties.load(in);
        }
        return properties;
    }

}
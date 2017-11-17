package com.dubreuia.model;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.dubreuia.core.SaveActionManager.LOGGER;
import static com.dubreuia.model.Action.compile;
import static com.dubreuia.model.Action.explicitTypeCanBeDiamond;
import static com.dubreuia.model.Action.fieldCanBeFinal;
import static com.dubreuia.model.Action.finalPrivateMethod;
import static com.dubreuia.model.Action.localCanBeFinal;
import static com.dubreuia.model.Action.missingOverrideAnnotation;
import static com.dubreuia.model.Action.noActionIfCompileErrors;
import static com.dubreuia.model.Action.organizeImports;
import static com.dubreuia.model.Action.rearrange;
import static com.dubreuia.model.Action.rearrangeChangedCode;
import static com.dubreuia.model.Action.reformat;
import static com.dubreuia.model.Action.reformatChangedCode;
import static com.dubreuia.model.Action.suppressAnnotation;
import static com.dubreuia.model.Action.unnecessaryFinalOnLocalVariableOrParameter;
import static com.dubreuia.model.Action.unnecessarySemicolon;
import static com.dubreuia.model.Action.unnecessaryThis;
import static com.dubreuia.model.Action.unqualifiedFieldAccess;
import static com.dubreuia.model.Action.useBlocks;

/**
 * Storage-Implementation for the Workspace-Mechanic-Format. Only the Java language-specific actions are supported.
 *
 * @author markiewb
 */
public enum EpfStorage {

    INSTANCE;

    private static final String EPF_ACTIVATE = "editor_save_participant_org.eclipse.jdt.ui.postsavelistener.cleanup";

    private static final String[] EPF_ORGANIZE_IMPORTS = new String[]{
            "sp_cleanup.organize_imports", "sp_cleanup.remove_unused_imports"};

    private static final String[] EPF_REFORMAT = new String[]{
            "sp_cleanup.format_source_code", "sp_cleanup.format_source_code_changes_only"};

    private static final String EPF_REFORMAT_CHANGED_CODE = "sp_cleanup.format_source_code_changes_only";

    private static final String[] EPF_REARRANGE = new String[]{
            "sp_cleanup.sort_members", "sp_cleanup.sort_members_all"};

    private static final String EPF_FIELD_CAN_BE_FINAL = "sp_cleanup.make_private_fields_final";

    private static final String EPF_LOCAL_CAN_BE_FINAL = "sp_cleanup.make_local_variable_final";

    private static final String EPF_UNQUALIFIED_FIELD_ACCESS = "sp_cleanup.use_this_for_non_static_field_access";

    private static final String[] EPF_MISSING_OVERRIDE_ANNOTATION = new String[]{
            "sp_cleanup.add_missing_override_annotations", "sp_cleanup.add_missing_override_annotations_interface_methods"};

    private static final String EPF_USE_BLOCKS_1 = "sp_cleanup.use_blocks";

    private static final String EPF_USE_BLOCKS_2 = "sp_cleanup.always_use_blocks";

    private static final String EPF_EXPLICIT_TYPE_DIAMOND = "sp_cleanup.remove_redundant_type_arguments";

    /**
     * @return a configuration based on EPF if the path to EPF configuration file is set and valid, or else the default
     * configuration is returned
     */
    public Storage getStorageOrDefault(String configurationPath, Storage defaultStorage) {
        try {
            return getStorageOrDefault0(configurationPath, defaultStorage);
        } catch (IOException e) {
            LOGGER.debug("Error in configuration file " + defaultStorage.getConfigurationPath(), e);
            return defaultStorage;
        }
    }

    private Storage getStorageOrDefault0(String configurationPath, Storage defaultStorage) throws IOException {
        if ("".equals(configurationPath) || configurationPath == null) {
            LOGGER.debug("Using default storage");
            return defaultStorage;
        }

        Storage storage = new Storage();
        // Copy the current values
        storage.setActions(defaultStorage.getActions());
        storage.setInclusions(defaultStorage.getInclusions());
        storage.setExclusions(defaultStorage.getExclusions());

        Properties properties = readProperties(configurationPath);
        // Map all EPF configurations to existing actions

        // do not enable save actions by epf-file settings, else you cannot disable the save action temporarily 
        // storage.setEnabled(activate, isEnabledEPForJava(properties, EPF_ACTIVATE));

        storage.setEnabled(noActionIfCompileErrors, true);

        storage.setEnabled(organizeImports, isEnabledEPForJava(properties, EPF_ORGANIZE_IMPORTS));
        storage.setEnabled(reformat, isEnabledEPForJava(properties, EPF_REFORMAT));
        storage.setEnabled(reformatChangedCode, isEnabledEPForJava(properties, EPF_REFORMAT_CHANGED_CODE));
        storage.setEnabled(rearrange, isEnabledEPForJava(properties, EPF_REARRANGE));
        storage.setEnabled(rearrangeChangedCode, false);

        storage.setEnabled(compile, false);

        storage.setEnabled(fieldCanBeFinal, isEnabledEPForJava(properties, EPF_FIELD_CAN_BE_FINAL));
        storage.setEnabled(localCanBeFinal, isEnabledEPForJava(properties, EPF_LOCAL_CAN_BE_FINAL));
        storage.setEnabled(unqualifiedFieldAccess, isEnabledEPForJava(properties, EPF_UNQUALIFIED_FIELD_ACCESS));
        storage.setEnabled(missingOverrideAnnotation, isEnabledEPForJava(properties, EPF_MISSING_OVERRIDE_ANNOTATION));
        storage.setEnabled(useBlocks, isEnabledEPForJava(properties, EPF_USE_BLOCKS_1, EPF_USE_BLOCKS_2));
        storage.setEnabled(unnecessaryThis, false);
        storage.setEnabled(finalPrivateMethod, false);
        storage.setEnabled(unnecessaryFinalOnLocalVariableOrParameter, false);
        storage.setEnabled(explicitTypeCanBeDiamond, isEnabledEPForJava(properties, EPF_EXPLICIT_TYPE_DIAMOND));
        storage.setEnabled(suppressAnnotation, false);
        storage.setEnabled(unnecessarySemicolon, false);

        LOGGER.debug("Using configuration file from " + defaultStorage.getConfigurationPath());
        return storage;
    }

    private boolean isEnabledEPForJava(Properties properties, String... keys) {
        String prefix = "/instance/org.eclipse.jdt.ui/";
        for (String key : keys) {
            if ("true".equals(properties.getProperty(prefix + key, "false"))) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private Properties readProperties(String configurationPath) throws IOException {
        Properties properties = new Properties();
        FileReader reader = null;
        try {
            reader = new FileReader(configurationPath);
            properties.load(reader);
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

}

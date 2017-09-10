package com.dubreuia.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.dubreuia.model.Action.activate;
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
public class EPFStorage {
    @Nullable
    public Storage getStorage(String configurationPath, Storage defaultStorage) throws IOException {

        if (!"".equals(configurationPath) && configurationPath != null) {

            Storage storage = new Storage();
            //copy the current values
            storage.setActions(defaultStorage.getActions());
            storage.setInclusions(defaultStorage.getInclusions());
            storage.setExclusions(defaultStorage.getExclusions());

            Properties properties = readProperties(configurationPath);
            //map all EPF configurations to existing actions
            storage.setEnabled(activate, isEnabledForJava(properties, "editor_save_participant_org.eclipse.jdt.ui.postsavelistener.cleanup"));
            storage.setEnabled(noActionIfCompileErrors, true /*Use conservative setting. Do not mess the format, if there are typing errors.*/);

            storage.setEnabled(organizeImports, isEnabledForJava(properties, "sp_cleanup.organize_imports", "sp_cleanup.remove_unused_imports"));
            storage.setEnabled(reformat, isEnabledForJava(properties, "sp_cleanup.format_source_code", "sp_cleanup.format_source_code_changes_only"));
            storage.setEnabled(reformatChangedCode, isEnabledForJava(properties, "sp_cleanup.format_source_code_changes_only"));
            storage.setEnabled(rearrange, isEnabledForJava(properties, "sp_cleanup.sort_members", "sp_cleanup.sort_members_all"));
            storage.setEnabled(rearrangeChangedCode, false);

            storage.setEnabled(compile, false);

            storage.setEnabled(fieldCanBeFinal, isEnabledForJava(properties, "sp_cleanup.make_private_fields_final"));
            storage.setEnabled(localCanBeFinal, isEnabledForJava(properties, "sp_cleanup.make_local_variable_final"));
            storage.setEnabled(unqualifiedFieldAccess, isEnabledForJava(properties, "sp_cleanup.use_this_for_non_static_field_access"));
            storage.setEnabled(missingOverrideAnnotation, isEnabledForJava(properties, "sp_cleanup.add_missing_override_annotations", "sp_cleanup.add_missing_override_annotations_interface_methods"));
            storage.setEnabled(useBlocks, isEnabledForJava(properties, "sp_cleanup.use_blocks", "sp_cleanup.always_use_blocks"));
            storage.setEnabled(unnecessaryThis, false);
            storage.setEnabled(finalPrivateMethod, false);
            storage.setEnabled(unnecessaryFinalOnLocalVariableOrParameter, false);
            storage.setEnabled(explicitTypeCanBeDiamond, isEnabledForJava(properties, "sp_cleanup.remove_redundant_type_arguments"));
            storage.setEnabled(suppressAnnotation, false);
            storage.setEnabled(unnecessarySemicolon, false);
            return storage;
        }
        return null;
    }

    private boolean isEnabledForJava(Properties properties, String... keys) {
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

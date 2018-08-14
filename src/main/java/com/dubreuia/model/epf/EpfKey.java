package com.dubreuia.model.epf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public enum EpfKey {

    add_default_serial_version_id,
    add_generated_serial_version_id,
    add_missing_annotations,
    add_missing_deprecated_annotations,
    add_missing_methods,
    add_missing_nls_tags,
    add_missing_override_annotations,
    add_missing_override_annotations_interface_methods,
    add_serial_version_id,
    always_use_blocks,
    always_use_parentheses_in_expressions,
    always_use_this_for_non_static_field_access,
    always_use_this_for_non_static_method_access,
    convert_functional_interfaces,
    convert_to_enhanced_for_loop,
    correct_indentation,
    format_source_code,
    format_source_code_changes_only,
    insert_inferred_type_arguments,
    make_local_variable_final,
    make_parameters_final,
    make_private_fields_final,
    make_type_abstract_if_missing_method,
    make_variable_declarations_final,
    never_use_blocks,
    never_use_parentheses_in_expressions,
    on_save_use_additional_actions,
    organize_imports,
    qualify_static_field_accesses_with_declaring_class,
    qualify_static_member_accesses_through_instances_with_declaring_class,
    qualify_static_member_accesses_through_subtypes_with_declaring_class,
    qualify_static_member_accesses_with_declaring_class,
    qualify_static_method_accesses_with_declaring_class,
    remove_private_constructors,
    remove_redundant_type_arguments,
    remove_trailing_whitespaces,
    remove_trailing_whitespaces_all,
    remove_trailing_whitespaces_ignore_empty,
    remove_unnecessary_casts,
    remove_unnecessary_nls_tags,
    remove_unused_imports,
    remove_unused_local_variables,
    remove_unused_private_fields,
    remove_unused_private_members,
    remove_unused_private_methods,
    remove_unused_private_types,
    sort_members,
    sort_members_all,
    use_anonymous_class_creation,
    use_blocks,
    use_blocks_only_for_return_and_throw,
    use_lambda,
    use_parentheses_in_expressions,
    use_this_for_non_static_field_access,
    use_this_for_non_static_field_access_only_if_necessary,
    use_this_for_non_static_method_access,
    use_this_for_non_static_method_access_only_if_necessary,
    ;

    private static final List<String> PREFIXES = Arrays.asList(
            "sp_cleanup",
            "/instance/org.eclipse.jdt.ui/sp_cleanup"
    );

    public static List<String> getPrefixes() {
        return Collections.unmodifiableList(PREFIXES);
    }

    public static Stream<EpfKey> stream() {
        return Arrays.stream(values());
    }

}

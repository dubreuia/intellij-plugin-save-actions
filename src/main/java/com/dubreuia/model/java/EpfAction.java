/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Alexandre DuBreuil
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.dubreuia.model.java;

import com.dubreuia.model.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;

public enum EpfAction {

    organizeImports(
            Action.organizeImports,
            EpfKey.organize_imports, EpfKey.remove_unused_imports),

    reformat(
            Action.reformat,
            EpfKey.format_source_code),

    reformatChangedCode(
            Action.reformatChangedCode,
            EpfKey.format_source_code_changes_only),

    rearrange(
            Action.rearrange,
            EpfKey.sort_members, EpfKey.sort_members_all),

    fieldCanBeFinal(
            Action.fieldCanBeFinal,
            EpfKey.make_private_fields_final),

    localCanBeFinal(
            Action.localCanBeFinal,
            EpfKey.make_local_variable_final),

    unqualifiedFieldAccess(
            Action.unqualifiedFieldAccess,
            EpfKey.use_this_for_non_static_field_access),

    unqualifiedMethodAccess(
            Action.unqualifiedMethodAccess,
            EpfKey.always_use_this_for_non_static_method_access),

    unqualifiedStaticMemberAccess(
            Action.unqualifiedStaticMemberAccess,
            EpfKey.qualify_static_member_accesses_with_declaring_class),

    missingOverrideAnnotation(
            Action.missingOverrideAnnotation,
            EpfKey.add_missing_override_annotations, EpfKey.add_missing_override_annotations_interface_methods),

    useBlocks(
            Action.useBlocks,
            EpfKey.use_blocks, EpfKey.always_use_blocks),

    generateSerialVersionUID(
            Action.generateSerialVersionUID,
            EpfKey.add_serial_version_id, EpfKey.add_default_serial_version_id, EpfKey.add_generated_serial_version_id),

    explicitTypeCanBeDiamond(
            Action.explicitTypeCanBeDiamond,
            EpfKey.remove_redundant_type_arguments),

    ;

    private final Action action;
    private final List<EpfKey> epfKeys;

    EpfAction(Action action, EpfKey... epfKeys) {
        this.action = action;
        this.epfKeys = Arrays.asList(epfKeys);
    }

    public Action getAction() {
        return action;
    }

    public List<EpfKey> getEpfKeys() {
        return unmodifiableList(epfKeys);
    }

    public static Optional<EpfAction> getEpfActionForAction(Action action) {
        return stream().filter(epfAction -> epfAction.action.equals(action)).findFirst();
    }

    public static Stream<EpfAction> stream() {
        return Arrays.stream(values());
    }

}

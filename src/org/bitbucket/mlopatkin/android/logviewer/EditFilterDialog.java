/*
 * Copyright 2011 Mikhail Lopatkin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitbucket.mlopatkin.android.logviewer;

import org.bitbucket.mlopatkin.android.liblogcat.FilterToText;
import org.bitbucket.mlopatkin.android.liblogcat.LogRecordFilter;

public class EditFilterDialog extends FilterDialog {
    public interface DialogResultReceiver {
        void onDialogResult(EditFilterDialog dialog, LogRecordFilter oldFilter, boolean success);
    }

    private DialogResultReceiver receiver;
    private LogRecordFilter filter;

    protected EditFilterDialog(FilteringMode mode, LogRecordFilter filter,
            DialogResultReceiver resultReceiver) {
        setTitle("Edit filter");
        this.receiver = resultReceiver;
        this.filter = filter;
        getMessageTextField().setText(FilterToText.getMessage(filter));
        getPidTextField().setText(FilterToText.getPids(filter));
        getTagTextField().setText(FilterToText.getTags(filter));
        getLogLevelList().setSelectedItem(FilterToText.getPriority(filter));
        getModePanel().setSelectedMode(mode);
    }

    @Override
    protected void onPositiveResult() {
        assert receiver != null;
        if (!isInputValid()) {
            return;
        }
        receiver.onDialogResult(this, filter, true);
        setVisible(false);
    }

    @Override
    protected void onNegativeResult() {
        assert receiver != null;
        receiver.onDialogResult(this, filter, false);
        setVisible(false);
    }

    public static void startEditFilterDialog(FilteringMode mode, LogRecordFilter filter,
            DialogResultReceiver resultReceiver) {
        if (filter == null) {
            throw new NullPointerException("Filter should be not null");
        }
        if (resultReceiver == null) {
            throw new NullPointerException("resultReceiver should be not null");
        }
        if (mode == null) {
            throw new NullPointerException("mode should be not null");
        }
        EditFilterDialog dialog = new EditFilterDialog(mode, filter, resultReceiver);
        dialog.setVisible(true);
    }
}
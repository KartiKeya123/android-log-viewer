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
package org.bitbucket.mlopatkin.android.liblogcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

public class LogRecordStream {

    private static final Logger logger = Logger.getLogger(LogRecordStream.class);

    private BufferedReader in;

    public LogRecordStream(InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    public LogRecordStream(BufferedReader in) {
        this.in = in;
    }

    public LogRecord next(LogRecord.Kind kind) {
        try {
            String line = in.readLine();
            Matcher result = null;
            while (!isLogEnd(line)) {
                Matcher m = LogRecordParser.parseLogRecordLine(line);
                if (m.matches()) {
                    result = m;
                    break;
                }
                line = in.readLine();
            }
            if (result != null) {
                return LogRecordParser.createThreadtimeRecord(kind, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean isLogEnd(String line) {
        return line == null;
    }
}

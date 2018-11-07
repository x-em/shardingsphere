/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
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
 * </p>
 */

package io.shardingsphere.core.parsing.antlr.extractor.statement.dialect.mysql;

import io.shardingsphere.core.metadata.table.ColumnMetaData;
import io.shardingsphere.core.metadata.table.ShardingTableMetaData;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.AddPrimaryKeyExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.DropPrimaryKeyExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.RenameIndexExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.RuleName;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.dialect.mysql.MySQLAddColumnExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.dialect.mysql.MySQLAddIndexExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.dialect.mysql.MySQLChangeColumnExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.dialect.mysql.MySQLDropIndexExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.phrase.dialect.mysql.MySQLModifyColumnExtractor;
import io.shardingsphere.core.parsing.antlr.extractor.statement.AlterTableExtractor;
import io.shardingsphere.core.parsing.antlr.sql.ddl.AlterTableStatement;
import io.shardingsphere.core.parsing.antlr.sql.ddl.ColumnPosition;
import io.shardingsphere.core.parsing.antlr.sql.ddl.mysql.MySQLAlterTableStatement;
import io.shardingsphere.core.parsing.parser.sql.SQLStatement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * MySQL alter table statement extractor.
 * 
 * @author duhongjun
 */
public final class MySQLAlterTableExtractor extends AlterTableExtractor {
    
    public MySQLAlterTableExtractor() {
        addPhraseExtractor(new MySQLAddColumnExtractor());
        addPhraseExtractor(new MySQLAddIndexExtractor());
        addPhraseExtractor(new MySQLDropIndexExtractor());
        addPhraseExtractor(new RenameIndexExtractor());
        addPhraseExtractor(new AddPrimaryKeyExtractor(RuleName.ADD_CONSTRAINT));
        addPhraseExtractor(new DropPrimaryKeyExtractor());
        addPhraseExtractor(new MySQLChangeColumnExtractor());
        addPhraseExtractor(new MySQLModifyColumnExtractor());
    }
    
    @Override
    protected SQLStatement createStatement(final ShardingTableMetaData shardingTableMetaData) {
        AlterTableStatement result = new MySQLAlterTableStatement();
        result.setTableMetaDataMap(shardingTableMetaData);
        return result;
    }
    
    @Override
    protected void adjustColumn(final AlterTableStatement alterStatement, final List<ColumnMetaData> newColumnMeta) {
        MySQLAlterTableStatement mysqlAlter = (MySQLAlterTableStatement) alterStatement;
        if (mysqlAlter.getPositionChangedColumns().isEmpty()) {
            return;
        }
        if (mysqlAlter.getPositionChangedColumns().size() > 1) {
            Collections.sort(mysqlAlter.getPositionChangedColumns());
        }
        for (ColumnPosition each : mysqlAlter.getPositionChangedColumns()) {
            if (null != each.getFirstColumn()) {
                adjustFirst(newColumnMeta, each.getFirstColumn());
            } else {
                adjustAfter(newColumnMeta, each);
            }
        }
    }
    
    private void adjustFirst(final List<ColumnMetaData> newColumnMeta, final String columnName) {
        ColumnMetaData firstMeta = null;
        Iterator<ColumnMetaData> it = newColumnMeta.iterator();
        while (it.hasNext()) {
            ColumnMetaData each = it.next();
            if (each.getColumnName().equals(columnName)) {
                firstMeta = each;
                it.remove();
                break;
            }
        }
        if (null != firstMeta) {
            newColumnMeta.add(0, firstMeta);
        }
    }
    
    private void adjustAfter(final List<ColumnMetaData> newColumnMeta, final ColumnPosition columnPosition) {
        int afterIndex = -1;
        int adjustColumnIndex = -1;
        for (int i = 0; i < newColumnMeta.size(); i++) {
            if (newColumnMeta.get(i).getColumnName().equals(columnPosition.getColumnName())) {
                adjustColumnIndex = i;
            }
            if (newColumnMeta.get(i).getColumnName().equals(columnPosition.getAfterColumn())) {
                afterIndex = i;
            }
            if (adjustColumnIndex >= 0 && afterIndex >= 0) {
                break;
            }
        }
        if (adjustColumnIndex >= 0 && afterIndex >= 0 && adjustColumnIndex != afterIndex + 1) {
            ColumnMetaData adjustColumn = newColumnMeta.remove(adjustColumnIndex);
            if (afterIndex < adjustColumnIndex) {
                afterIndex = afterIndex + 1;
            }
            newColumnMeta.add(afterIndex, adjustColumn);
        }
    }
}

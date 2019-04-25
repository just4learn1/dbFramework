package com.mzc.db.mysql.data;

import com.mzc.utils.SqlUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mzc.db.mysql.MysqlEntityManager.CLASSNAME_FIELD;

public class EntityData<T> extends WeakReference<T> {
    private long id;
    private EntityField entityField;
    public Class clazz;
    public AtomicBoolean needInsert = new AtomicBoolean(false);     //标志此对象是需要insert还是update
    public AtomicBoolean changed = new AtomicBoolean(false);
    LinkedList<Field> changedFields = new LinkedList<>();     //需求顺序，在添加的时候需要排重

    public EntityData(long id, Class clazz, EntityField entityField, T referent) {
        super(referent);
        this.entityField = entityField;
        this.id = id;
        this.clazz = clazz;
    }

    public EntityData(long id, Class clazz, EntityField entityField, T t, boolean needInsert) {
        super(t);
        this.id = id;
        this.entityField = entityField;
        this.needInsert.compareAndSet(false, needInsert);
        this.changed.compareAndSet(false, needInsert);
        this.clazz = clazz;
        if (needInsert) {
            this.setAllFieldsChanged();
        }
    }

    public long getId() {
        return id;
    }

    public boolean compareAndSetChanged(boolean expect, boolean update) {
        return needInsert.compareAndSet(expect, update);
    }

    public boolean compareAndSetNeedInsert(boolean expect, boolean update) {
        return changed.compareAndSet(expect, update);
    }

    /**
     * 添加所有字段到changedFields中
     */
    public void setAllFieldsChanged() {
        changedFields.add(entityField.getIdField());
        changedFields.addAll(entityField.getOtherFieldMap().values());
    }

    public void notifyChanged(Field f) {
        if (!changedFields.contains(f)) {
            changedFields.add(f);
        }
    }

    public void appendInsertStr(StringBuffer sb) {
        int cnt = changedFields.size() + 1;
        changedFields.forEach(f->{
            sb.append(f.getName()).append(",");
        });
        sb.append(CLASSNAME_FIELD).append(") VALUES(");
        for (int i = 0; i < cnt; i++) {
            sb.append("?");
            if (i < cnt -1) {
                sb.append(",");
            }
        }
        sb.append(")");
    }

    public void fullFillParamater(PreparedStatement pstat) throws Exception {
        int index = 1;
        T t = this.get();
        System.out.println(t);
        index = SqlUtil.fullFillParamater(index, t, pstat, changedFields);
        pstat.setString(index++, clazz.getSimpleName());
        System.out.println(index);
        System.out.println(t);
    }

}

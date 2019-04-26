package com.mzc.db.mysql.data;

import com.mzc.utils.SqlUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mzc.db.mysql.MysqlEntityManager.CLASSNAME_FIELD;

public class EntityData<T> extends WeakReference<T> {
    private long id;
    private EntityField entityField;
    public Class clazz;
    public AtomicBoolean needInsert = new AtomicBoolean(false);     //标志此对象是需要insert还是update
    public AtomicBoolean changed = new AtomicBoolean(false);
    CopyOnWriteArraySet<Field> changedFields = new CopyOnWriteArraySet<>();
    public long saveTime = System.currentTimeMillis();
    private long period = 5_000L;


    public EntityData(long id, Class clazz, EntityField entityField, T referent) {
        super(referent);
        this.entityField = entityField;
        this.id = id;
        this.clazz = clazz;
    }

    public void notifyInsert() {
        this.compareAndSetNeedInsert(false, true);
        this.compareAndSetChanged(false, true);
        this.saveTime = System.currentTimeMillis() + period;
        this.setAllFieldsChanged();
    }

    public void notifyChanged() {
        this.compareAndSetChanged(false, true);
        long now = System.currentTimeMillis();
        if (saveTime < now) {
            this.saveTime = System.currentTimeMillis() + period;
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
            this.notifyChanged();
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
        pstat.setString(index++, clazz.getName());
        System.out.println(index);
        System.out.println(t);
    }

}

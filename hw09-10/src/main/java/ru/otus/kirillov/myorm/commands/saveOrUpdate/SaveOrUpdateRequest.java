package ru.otus.kirillov.myorm.commands.saveOrUpdate;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.myorm.commands.Request;
import ru.otus.kirillov.myorm.schema.elements.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.schema.elements.EntityDescriptor;
import ru.otus.kirillov.utils.CommonUtils;

import java.util.List;

/**
 * Created by Александр on 01.02.2018.
 */
public class SaveOrUpdateRequest extends Request {

    private EntityDescriptor descriptor;

    private List<Pair<AbstractFieldDescriptor, Object>> fieldValueByDescriptor;

    private boolean needsToCommit;


    public SaveOrUpdateRequest(EntityDescriptor descriptor, List<Pair<AbstractFieldDescriptor, Object>> fieldValueByDescriptor) {
        super(Type.SAVE_OR_UPDATE);
        this.descriptor = CommonUtils.retunIfNotNull(descriptor);
        this.fieldValueByDescriptor = CommonUtils.retunIfNotNull(fieldValueByDescriptor);
    }

    public SaveOrUpdateRequest(EntityDescriptor descriptor, List<Pair<AbstractFieldDescriptor, Object>> fieldValueByDescriptor,
                               boolean needsToCommit) {
        this(descriptor, fieldValueByDescriptor);
        this.needsToCommit = needsToCommit;
    }

    public EntityDescriptor getDescriptor() {
        return descriptor;
    }

    public List<Pair<AbstractFieldDescriptor, Object>> getFieldValueByDescriptor() {
        return fieldValueByDescriptor;
    }

    public boolean isNeedsToCommit() {
        return needsToCommit;
    }
}
